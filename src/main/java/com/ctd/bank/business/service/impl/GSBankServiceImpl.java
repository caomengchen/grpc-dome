package com.ctd.bank.business.service.impl;


import com.ctd.bank.business.model.ApplyInfoModel;
import com.ctd.bank.business.model.PeopleDutyModel;
import com.ctd.bank.business.service.GSBankService;
import com.ctd.bank.common.bean.model.Result;
import com.ctd.bank.common.dao.IBaseDAO;
import com.ctd.bank.common.exception.BusinessException;
import com.ctd.bank.config.BankConfig;


import com.icbc.api.DefaultIcbcClient;
import com.icbc.api.IcbcApiException;
import com.icbc.api.IcbcConstants;
import com.icbc.api.request.ApplyCurrentAccountRequestV2;
import com.icbc.api.response.ApplyCurrentAccountResponseV2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class GSBankServiceImpl implements GSBankService {

    @Resource
    private IBaseDAO baseDAO;
    @Resource
    private BankConfig bankCongig;

    @Override
    public Result gsBankOpenAccount(String applyId) throws Exception {
        Result result = null;
        Map<String, Object> resultMap = new HashMap<>();
        String APP_ID = bankCongig.getGsAppId();    //InitServlet.getConfig().get("APP_ID");
        String MY_PRIVATE_KEY = bankCongig.getGsMyPrivateKey();//   InitServlet.getConfig().get("MY_PRIVATE_KEY");
        String APIGW_PUBLIC_KEY = bankCongig.getApigwPublicKey();//  InitServlet.getConfig().get("APIGW_PUBLIC_KEY");
        String GS_ACCOUNT_URL = bankCongig.getGsAccountUrl();//    InitServlet.getConfig().get("GS_ACCOUNT_URL");
        //签名类型为RSA2时，需传入appid，私钥和网关公钥，签名类型使用定值IcbcConstants.SIGN_TYPE_RSA2，其他参数使用缺省值
        DefaultIcbcClient client = new DefaultIcbcClient(APP_ID, IcbcConstants.SIGN_TYPE_RSA2, MY_PRIVATE_KEY, APIGW_PUBLIC_KEY);
        ApplyCurrentAccountRequestV2 request = new ApplyCurrentAccountRequestV2();
        request.setServiceUrl(GS_ACCOUNT_URL);

        String trxDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String trxTime = new SimpleDateFormat("kk:mm:ss").format(new Date());
        System.out.println(trxDate + trxTime);
        ApplyInfoModel applyInfoModel = new ApplyInfoModel();
        applyInfoModel.setId(applyId);  //applyId   前端传参
        /*-----------查询公司信息---------------*/
        ApplyInfoModel applyInfo = (ApplyInfoModel) baseDAO.selectOne("com.company.apply.selectById", applyInfoModel);
        String accPername = applyInfo.getCorpName();  //客户名称
        String busLicense = applyInfo.getUSID();//营业执照(统一社会信用代码)


        /*-----------查询公司法人信息---------*/
        PeopleDutyModel legalInfo = getFr(applyId);
        String corpName = legalInfo.getName();//法定代表人姓名



        /*-----------查询公司经办人信息---------*/
        PeopleDutyModel operator = queryDutyInfo(applyId, "06", "", "", "").get(0);
        String preApplTel = operator.getPhone();//预约人手机号码


        /*---------将查询出来的值填入-----------*/
        gstzMethod(request, trxDate, trxTime, accPername, busLicense, corpName,
                preApplTel);

        ApplyCurrentAccountResponseV2 response = new ApplyCurrentAccountResponseV2();
        try {
            //由于工商那边接口不稳定需要连续调用才会成功
            boolean flag = true;
            int i = 0;
            while (flag) {
                Thread.sleep(2000);
                response = client.execute(request, generateMsgId());//msgId消息通讯唯一编号，要求每次调用独立生成，APP级唯一
                i++;
                if (response.isSuccess() || i == 10) {
                    flag = false;
                }
                System.out.println("i----------------------" + i);

            }

            if (response.isSuccess()) {
                //6、业务成功处理，请根据接口文档用response.getxxx()获取同步返回的业务数据
                resultMap.put("returnCode", response.getReturnCode());
                resultMap.put("returnMsg", response.getReturnMsg());
                resultMap.put("msgId", response.getMsgId());
                resultMap.put("applno", response.getApplno());
                resultMap.put("accno", response.getAccno());
                ApplyInfoModel applyIn = new ApplyInfoModel();
                applyIn.setId(applyId);
                applyIn.setBankAccountNo(response.getAccno());
                baseDAO.update("com.company.apply.saveBankAccountNo", applyIn);

            } else {
                //失败getMsgId
                System.out.println("ReturnCode:" + response.getReturnCode());
                System.out.println("ReturnMsg:" + response.getReturnMsg());
                resultMap.put("returnCode", response.getReturnCode());
                resultMap.put("returnMsg", response.getReturnMsg());
            }
        } catch (IcbcApiException e) {
            e.printStackTrace();
        }

        return result.ok(resultMap);
    }


    private void gstzMethod(ApplyCurrentAccountRequestV2 request,
                            String trxDate, String trxTime, String accPername,
                            String busLicense, String corpName, String preApplTel) {
        //请对照接口文档用bizContent.setxxx()方法对业务上送数据进行赋值，非必输字段可不上送
        ApplyCurrentAccountRequestV2.ApplyCurrentAccountRequestV2Biz bizContent = new ApplyCurrentAccountRequestV2.ApplyCurrentAccountRequestV2Biz();
        bizContent.setTrxDate(trxDate);//渠道交易日期
        bizContent.setTrxTime(trxTime);//渠道交易时间
        bizContent.setAccname(accPername);//户名
        bizContent.setCurrtype("1");//币种
        bizContent.setCis("");//集团CIS号
        bizContent.setZoneno("2010");//开户地区号
        bizContent.setBrno("0213");//开户网点号
        bizContent.setAccatrbt("2");//账户性质
        bizContent.setAcctype("");//账户类型
        bizContent.setAccpertype("");//账户类别
        bizContent.setAccpername(accPername);//存款人名称/客户名称
        bizContent.setCisPhone("");//单位电话
        bizContent.setCisPost("");//单位邮政编码
        bizContent.setRegCurr("");//注册资金币种
        bizContent.setRegCapital("");//注册资金
        bizContent.setIndustryForPrimary("");//经营范围
        bizContent.setBusEnddate("");//营业执照有效期
        bizContent.setBusLicense(busLicense);//营业执照号码
        bizContent.setTaxHno("");//税务登记证信息－国税税务登记证号码
        bizContent.setTaxHenddate("");//税务登记证信息－国税税务登记证有效日期
        bizContent.setTaxLno("");//税务登记证信息－地税税务登记证号码
        bizContent.setTaxLenddate("");//税务登记证信息－地税税务登记证有效日期
        bizContent.setTaxOtherCer("");//税务登记证信息－无须办理的证明
        bizContent.setCorpFlag("1");//法定代表人或单位负责人
        bizContent.setCorpName(corpName);//法定代表人证件或单位负责人证件-姓名
        bizContent.setCorpType("");//法定代表人证件或单位负责人证件-身份证件种类
        bizContent.setCorpNo("");//法定代表人证件或单位负责人证件-身份证件号码
        bizContent.setCorpEnddate("");//法定代表人证件或单位负责人证件-身份证件有效期
        bizContent.setCorpPhone("");//法定代表人证件或单位负责人证件-固定电话
        bizContent.setCorpMvphone("");//法定代表人证件或单位负责人证件-手机
        bizContent.setAuthName("");//授权代理人证件-姓名
        bizContent.setAuthType("");//授权代理人证件-身份证件种类
        bizContent.setAuthNo("");//授权代理人证件-身份证件号码
        bizContent.setAuthEnddate("");//授权代理人证件-身份证件有效期
        bizContent.setAuthPhone("");//授权代理人证件-固定电话
        bizContent.setAuthMvphone("");//授权代理人证件-手机
        bizContent.setParentCorpFlag("");//上级单位-法定代表人或单位负责人
        bizContent.setParentCorpName("");//上级单位-法定代表人证件或单位负责人证件-姓名
        bizContent.setParentCorpType("");//上级单位-法定代表人证件或单位负责人证件-身份证件种类
        bizContent.setParentCorpNo("");//上级单位-法定代表人证件或单位负责人证件-身份证件号码
        bizContent.setParentCorpEnddate("");//上级单位-法定代表人证件或单位负责人证件-身份证件有效期
        bizContent.setParentCorpPhone("");//上级单位-法定代表人证件或单位负责人证件-固定电话
        bizContent.setParentCorpMvphone("");//上级单位-法定代表人证件或单位负责人证件-手机
        bizContent.setParentName("");//上级单位名称
        bizContent.setParentOrgexno("");//上级单位基本存款账户许可证号
        bizContent.setParentOrgno("");//上级单位组织机构代码证号码
        bizContent.setFinaPhone("");//财务主管人电话
        bizContent.setFinaMvphone("");//财务主管人手机
        bizContent.setHolderPhone("");//控股股东电话
        bizContent.setHolderMvphone("");//控股股东手机
        bizContent.setRholderPhone("");//实际控制人电话
        bizContent.setRholderMvphone("");//实际控制人手机
        bizContent.setWtUnitName("");//委托单位名称
        bizContent.setAuthExpDate("");//授权到期日
        bizContent.setAuthHandle("");//授权办理人职务
        bizContent.setRegAddArea("");//注册地址-国家
        bizContent.setRegAddPro("");//注册地址-省（直辖市、自治区）
        bizContent.setRegAddCity("");//注册地址-市
        bizContent.setRegAddDis("");//注册地址-县、区
        bizContent.setRegAddress("");//注册地址-地址
        bizContent.setRegAddPhoarno("");//注册地址-国际电话国家代码
        bizContent.setRegAddPhocino("");//注册地址-地区代码
        bizContent.setRegAddPhonum("");//注册地址-号码主体
        bizContent.setRegAddPhobodyNum("");//注册地址-分机号码
        bizContent.setRegAddPost("");//注册地址-单位邮政编码
        bizContent.setFinaExecName("");//财务主管人证件-姓名
        bizContent.setFinaExecType("");//财务主管人证件-身份证件种类
        bizContent.setFinaExecTypeno("");//财务主管人证件-身份证件号码
        bizContent.setFinaExecEnddate("");//财务主管人证件-身份证件有效期
        bizContent.setFinaExecPhone("");//财务主管人证件-固定电话
        bizContent.setFinaExecMvphone("");//财务主管人证件-手机
        bizContent.setPreApplTel(preApplTel);//预约人手机
        bizContent.setWorkAddArea("");//办公地址-国家
        bizContent.setWorkAddPro("");//办公地址-省（直辖市、自治区）
        bizContent.setWorkAddCity("");//办公地址-市
        bizContent.setWorkAddDis("");//办公地址-县、区
        bizContent.setWorkAddress("");//办公地址-地址
        bizContent.setWorkAddPhonum("");//办公地址-电话号码
        bizContent.setWorkAddPhobodyNum("");//办公地址-分机号码
        bizContent.setWorkAddPost("");//办公地址-单位邮政编码
        bizContent.setLpPhobodyNum("");//法定代表人证件或单位负责人证件-分机号码
        bizContent.setFePhobodyNum("");//财务主管人证件-分机号码
        bizContent.setHolderName("");//控股股东信息-姓名
        bizContent.setHolderPhobodyNum("");//控股股东信息-分机号码
        bizContent.setAcPhobodyNum("");//授权代理人证件-分机号码
        bizContent.setNoitfyFlag("");//消息通知标志 0-不通知 1-通知
        request.setBizContent(bizContent);
    }


    public PeopleDutyModel getFr(String applyId) throws Exception {

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

    public static String generateMsgId() {
        String str = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
        System.out.println(str);
        return str;
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
        List<PeopleDutyModel> peopleDutyModels = baseDAO.selectList("com.bank.queryByDutyAndFlowNo", peopleDutyModel);
        return peopleDutyModels;
    }
}
