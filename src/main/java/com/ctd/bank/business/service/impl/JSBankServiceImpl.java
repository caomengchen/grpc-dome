package com.ctd.bank.business.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import com.ctd.bank.business.model.ApplyInfoModel;
import com.ctd.bank.business.model.PeopleDutyModel;
import com.ctd.bank.business.service.JSBankService;
import com.ctd.bank.business.util.JHSocketUtil;
import com.ctd.bank.common.bean.model.Result;
import com.ctd.bank.common.constant.BaseConstant;
import com.ctd.bank.common.dao.IBaseDAO;
import com.ctd.bank.common.exception.BusinessException;
import com.ctd.bank.config.BankConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

@Service
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class JSBankServiceImpl implements JSBankService {

    @Resource
    private IBaseDAO baseDAO;
    @Resource
    private BankConfig bankCongig;

    /**
     * 1.统一信用代码和手机号还有营业执照号三个不同就行
     * Unn_Soc_Cr_Cd,BsnLcns_No,CtcPsn_MblPh_No
     * 2.报文长度六位需要加上传送参数的最前面
     *
     * @author Administrator
     */
    @Override
    public Result jsBankOpenAccount(String applyId) throws Exception {
        Result result = null;
        String account_ip = bankCongig.getJsAccountIp();
        Integer account_port = Integer.parseInt(bankCongig.getJSAccountPortTest());
        String trxDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        ApplyInfoModel applyInfoModel = new ApplyInfoModel();
        applyInfoModel.setId(applyId);  //applyId   前端传参
        /*-----------查询公司信息---------------*/
        ApplyInfoModel applyInfo = (ApplyInfoModel) baseDAO.selectOne("com.company.apply.selectById", applyInfoModel);
        String accPername = applyInfo.getCorpName();  //客户名称
        String busLicense = applyInfo.getUSID();//统一社会信用代码
        String bsnlNo = applyInfo.getZbarCode();    //营业执照号码
        /*-----------查询公司法人信息---------*/
        PeopleDutyModel legalInfo = getFr(applyId);

        /*-----------查询公司经办人信息---------*/
        PeopleDutyModel operator = queryDutyInfo(applyId, "06", "", "", "").get(0);
        String preApplTel = operator.getPhone();//预约人手机号码
        String name = operator.getName();//经办人姓名

        String xml = "<?xml version=\"1.0\" encoding=\"GB2312\" ?>" +
                "<root>" +
                "<TranCode>A01014101</TranCode>" +
                "<EntNm>" + accPername + "</EntNm>" +
                "<Unn_Soc_Cr_Cd>" + busLicense + "</Unn_Soc_Cr_Cd>" +
                "<BsnLcns_No>" + bsnlNo + "</BsnLcns_No>" +
                "<Rsrvtn_InsID>440270001</Rsrvtn_InsID>" +
                "<CtcPsn_MblPh_No>" + preApplTel + "</CtcPsn_MblPh_No>" +
                "<Cst_CtcPsn_Nm>" + name + "</Cst_CtcPsn_Nm>" +
                "<Rsrvtn_Pcsg_Dt>" + trxDate + "</Rsrvtn_Pcsg_Dt>" +
                "</root>";
        String resultXml = JHSocketUtil.getJhSocketCon(account_ip, account_port, xml, BaseConstant.INTERFACE_49);
        if (StringUtils.isBlank(resultXml)) {
            throw new BusinessException("接口返回为空");
        }
        XMLSerializer xmlSerializer = new XMLSerializer();
        String resutStr = xmlSerializer.read(resultXml).toString();

        JSONObject jsonObject = JSONObject.fromObject(resutStr);
        if ("00000".equals(jsonObject.getString("RetCode")) && "交易成功".equals(jsonObject.getString("RetMsg"))) {
            ApplyInfoModel applyIn = new ApplyInfoModel();
            applyIn.setId(applyId);
            // 预留账户账号
            applyIn.setBankAccountNo(jsonObject.getString("Rsrv_Acc_AccNo"));
            // 预约编号
            applyIn.setYybh(jsonObject.getString("Rsrvtn_ID"));
            baseDAO.update("com.company.apply.saveBankCardNo", applyIn);

            result = Result.ok();
        } else {
            result = Result.build(-1, jsonObject.getString("RetMsg"));
        }
        return result;
    }

    public PeopleDutyModel getFr(String applyId) throws Exception {

        {

            ApplyInfoModel applyInfo = (ApplyInfoModel) baseDAO.selectOne("com.company.apply.selectById", new ApplyInfoModel(applyId));
            if (applyInfo == null) {
                throw new BusinessException("id有误");
            }
            PeopleDutyModel fr = null;
            // 9910 个体户 只有经营者
            if ("9911".equals(applyInfo.getEntitytypeid())) {
                fr = (PeopleDutyModel) baseDAO.selectOne("com.duty.selectByDutyAndFlowNo", new PeopleDutyModel("08", applyInfo.getId()));//经营者
                return fr;
            }
            String legalperson = applyInfo.getLegalperson();
            if ("3".equals(legalperson)) {
                fr = (PeopleDutyModel) baseDAO.selectOne("com.duty.selectByDutyAndFlowNo", new PeopleDutyModel("03", applyInfo.getId()));//经理
            } else if ("2".equals(legalperson)) {
                fr = (PeopleDutyModel) baseDAO.selectOne("com.duty.selectByDutyAndFlowNo", new PeopleDutyModel("01", applyInfo.getId()));//执行董事
            } else if ("1".equals(legalperson)) {
                PeopleDutyModel peopleDutyModel = new PeopleDutyModel();
                peopleDutyModel.setBizFlowNo(applyInfo.getId());
                peopleDutyModel.setDuty("01");
                peopleDutyModel.setDutyType("1");
                fr = (PeopleDutyModel) baseDAO.selectList("com.bank.queryByDutyAndFlowNo", peopleDutyModel).get(0);
            }
            return fr;
        }
    }


    /**
     * 查询公司职位人员
     *
     * @param applyId       Id
     * @param duty          职位信息：01-董事 02-监事 03-经理 04-财务负责人 05-公司联系人 06-公司经办人 07-投资人
     * @param dutyType      职位类型：1-董事长 2-副董事 3-不任职董事 4-监事主席 5-不任职监事
     * @param appointedType 委任方式：1-董事选举 2-执行董事任命 3-股东委派
     * @param dutySource    职位来源：1-股东代表 2-职工代表
     * @return
     * @throws Exception
     */
    public List<PeopleDutyModel> queryDutyInfo(String applyId, String duty, String dutyType, String appointedType, String dutySource) throws Exception {
        PeopleDutyModel peopleDutyModel = new PeopleDutyModel();
        peopleDutyModel.setBizFlowNo(applyId);
        peopleDutyModel.setDuty(duty);
        peopleDutyModel.setDutyType(dutyType);
        peopleDutyModel.setAppointedType(appointedType);
        peopleDutyModel.setDutySource(dutySource);
        List<PeopleDutyModel> peopleDutyModels = baseDAO.selectList("oracle.bank.queryByDutyAndFlowNo", peopleDutyModel);
        return peopleDutyModels;
    }

}
