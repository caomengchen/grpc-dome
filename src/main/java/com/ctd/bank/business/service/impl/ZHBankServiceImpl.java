package com.ctd.bank.business.service.impl;

import com.ctd.bank.business.model.*;
import com.ctd.bank.business.service.ZHBankService;
import com.ctd.bank.business.util.SocketParamsUtil;
import com.ctd.bank.business.util.socket.Client;
import com.ctd.bank.common.bean.model.Result;
import com.ctd.bank.common.constant.BaseConstant;
import com.ctd.bank.common.dao.IBaseDAO;
import com.ctd.bank.common.exception.BusinessException;

import com.ctd.bank.config.BankConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Slf4j
public class ZHBankServiceImpl  implements ZHBankService {

    @Resource
    private IBaseDAO baseDAO;

    @Autowired
    private BankConfig bankConfig;

    @Override
    @Transactional
    public Result sendChinaBankOne(Map<String, String> paramMap) throws Exception {
        String applyId = paramMap.get("applyId");
        ApplyInfoModel applyInfo = (ApplyInfoModel) baseDAO.selectOne("com.company.apply.selectById", new ApplyInfoModel(applyId));
        String devId = applyInfo.getDevId();
        paramMap.put("biz_flow_no", applyId);
        //按照最后打印的设备来更新银行机构号    开户设备是当前操作的。
        selectNetworkBydevid(applyId, devId);

        Map map = baseInfo(applyId);
        Map mapsq = (Map) baseDAO.selectOne("com.bank.chinaBankOpenApply",paramMap);
        if(mapsq==null){
            throw new BusinessException("没有填写银行开户信息");
        }
        paramMap.put("openaccountid", mapsq.get("ID")+"");
        List list = baseDAO.selectList("com.bank.chinaBankOpenSq",paramMap);
        map.put("jybh", "AP1001|");
        map.put("sqyw", list);
        map.put("openaccount", mapsq);

        String parmStr = SocketParamsUtil.chinaBankZs(map);
        String str = "";
        //银行接口不稳定请求十次 失败不再请求
        int i=1;
        while(i <= 10){
            log.info("获取配置文件{}", bankConfig);
            str = Client.getSocketCon(parmStr, BaseConstant.INTERFACE_30, bankConfig);

            if(StringUtils.isNotBlank(str) && "0000".equals(str.substring(4,8))&&"交易成功".equals(str.substring(8,12))){
                str = str.substring(str.length()-22,str.indexOf("AP1001"));
                map.put("bankcano", str);
                map.put("biz_flow_no", applyId);
                baseDAO.update("com.bank.updateOpenAccount",map);

                // 更新执照库存
                updateLicense(applyId);
                break;
            }else{
                if(i == 10){
                    throw new BusinessException("银行预开户接口请求失败");
                }
            }
            i++;
        }

        return Result.ok(str);
    }


    /**
     * 设备编号查询网点 同时更新银行机构号
     * @param applyId
     * @param devId
     * @throws Exception
     */
    public void selectNetworkBydevid(String applyId, String devId) throws Exception {
        try{
            // 得到网点信息
            DevnetworkInfo devnetworkInfo = (DevnetworkInfo) baseDAO.selectOne("com.network.queryBankBydevId", devId);
            if(null == devnetworkInfo){
                throw new BusinessException("查询银行机构失败");
            }
            Map<String, String> mapjq = new HashMap<>();
            mapjq.put("biz_flow_no", applyId);
            mapjq.put("lkhyhjgh", devnetworkInfo.getByzd1());
            baseDAO.update("com.bank.updateChinaBankJg",mapjq);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Map<String,Map> baseInfo(String applyId) throws Exception{

        Map<String,Map> conpInfos = new HashMap<String,Map>();

        Map conpInfo = new HashMap();
        Map<String, Integer> conpDutyNum = new HashMap<String, Integer>();
        ApplyInfoModel applyInfoModel = new ApplyInfoModel();
        applyInfoModel.setId(applyId);

        /*-----------查询公司信息---------------*/
        ApplyInfoModel applyInfo = (ApplyInfoModel) baseDAO.selectOne("com.company.apply.selectById", applyInfoModel);
        conpInfo.put("applyInfo", applyInfo);


        /*-----------7、查询公司经办人信息---------*/
        PeopleDutyModel operator = queryDutyInfo(applyId,"06","","","").get(0);
        conpInfo.put("operator",operator);
        conpDutyNum.put("operator", 1);

        Map tips = new HashMap<String, String>();
        tips.put("tips","是否填写税务信息，并完成办税申请。");// "即将进入【税务登记】信息填写页面,请确认是否继续?"
        // 个体户和内资提示消息不同
        if ("9911".equals(applyInfo.getEntitytypeid())){
            tips.put("tips","是否填写税务信息，并完成办税申请。");
        }

        conpInfos.put("tips",tips);

        if("9911".equals(applyInfo.getEntitytypeid())){

            /*-----------9、 个体户经营者查询---------*/
            PeopleDutyModel individual = queryDutyInfo(applyId,"08","","","").get(0);
            conpInfo.put("individual",individual);
            conpDutyNum.put("individual",1);

            conpInfos.put("conpanyInfo",conpInfo);
            conpInfos.put("conpDutyNum",conpDutyNum);
            return conpInfos;
        }
        /**
         * 职位人员信息查询
         */
        /*-----------1、查询公司法人信息---------*/
        String legalType = applyInfo.getLegalperson(); //法人代表职位 1、董事长 2、执行董事 3、经理
        PeopleDutyModel legalInfo;
        if("1".equals(legalType)){
            legalInfo = queryDutyInfo(applyId ,"01", "1", "", "").get(0); //董事 -- 董事长
        }else if("2".equals(legalType)){
            legalInfo = queryDutyInfo(applyId,"01", "", "", "").get(0); //执行董事
        }else{
            legalInfo = queryDutyInfo(applyId,"03", "", "", "").get(0); //经理
        }
        conpInfo.put("legal", legalInfo);
        conpDutyNum.put("legal",1);
        /*-----------2、查询公司董事信息---------*/
        PeopleDutyModel director1 = new PeopleDutyModel();   //1、董事长
        PeopleDutyModel director2 = new PeopleDutyModel();   //2、执行董事
        List<PeopleDutyModel> director3 = new ArrayList<PeopleDutyModel>();   //3、副董事及其他董事成员
        List<PeopleDutyModel> directors = queryDutyInfo(applyId,"01","","",""); //董事成员
        if(directors.size() == 1){
            director2 = directors.get(0);
        }else{
            director1 = queryDutyInfo(applyId,"01","1","","").get(0);   //董事长
            List<PeopleDutyModel> director32 = queryDutyInfo(applyId,"01","2","",""); //副董事
            for(PeopleDutyModel dir: director32){
                director3.add(dir);
            }
            List<PeopleDutyModel> director33 = queryDutyInfo(applyId,"01","3","",""); //不担任职位董事
            for(PeopleDutyModel dir: director33){
                director3.add(dir);
            }
        }
        conpInfo.put("directors", directors);  //董事信息
        conpInfo.put("director1", director1);  //董事长信息
        conpInfo.put("director2", director2);  //执行董事信息
        conpInfo.put("director3", director3);  //董事会其他成员信息
        if(directors.size() == 1 ){ //执行董事
            conpDutyNum.put("directors", 1);    //董事数量
            conpDutyNum.put("director1", 0);    //董事长数量
            conpDutyNum.put("director2", 1);    //执行董事数量
            conpDutyNum.put("director3", 0);    //董事会其他成员数量
        }else{  //董事会
            conpDutyNum.put("directors", directors.size());
            conpDutyNum.put("director1", 1);
            conpDutyNum.put("director2", 0);
            conpDutyNum.put("director3", director3.size());
        }
        /*-----------3、查询公司监事信息---------*/
        PeopleDutyModel supervisor1 = new PeopleDutyModel(); //监事会主席
        List<PeopleDutyModel> supervisor2 = new ArrayList<PeopleDutyModel>(); //监事会其他成员
        List<PeopleDutyModel> supervisors =  queryDutyInfo(applyId,"02","","",""); //监事成员
        if(supervisors.size() >2){  //监事会
            supervisor1 = queryDutyInfo(applyId,"02","4","","").get(0); //监事会主席
            supervisor2 = queryDutyInfo(applyId,"02","5","",""); //监事会其他成员
            conpInfo.put("supervisor1",supervisor1);
            conpInfo.put("supervisor2",supervisor2);
            conpInfo.put("supervisors",supervisors);
            conpDutyNum.put("supervisor1", 1);
            conpDutyNum.put("supervisor2", supervisor2.size());
            conpDutyNum.put("supervisors", supervisors.size());
        }else{  //不设监事会
            conpInfo.put("supervisor1",supervisor1);
            conpInfo.put("supervisor2",supervisor2);
            conpInfo.put("supervisors",supervisors);
            conpDutyNum.put("supervisor1", 0);
            conpDutyNum.put("supervisor2", 0);
            conpDutyNum.put("supervisors", supervisors.size());
        }

        /*-----------4、查询公司经理信息---------*/
        PeopleDutyModel manager = queryDutyInfo(applyId,"03","","","").get(0);
        conpInfo.put("manager",manager);
        conpDutyNum.put("manager", 1);
        /*-----------5、查询公司财务信息---------*/
        PeopleDutyModel chief = queryDutyInfo(applyId,"04","","","").get(0);
        conpInfo.put("chief",chief);
        conpDutyNum.put("chief", 1);
        /*-----------6、查询公司联系人信息---------*/
        PeopleDutyModel link = queryDutyInfo(applyId,"10","","","").get(0);
        conpInfo.put("secretary",link);
        conpDutyNum.put("secretary", 1);

        /*-----------8、查询公司股东信息---------*/
        List<PeopleDutyModel> shareholders = queryDutyInfo(applyId,"07","","","");

        conpInfo.put("shareholders",shareholders);
        conpDutyNum.put("shareholders", shareholders.size());

        conpInfos.put("conpanyInfo",conpInfo);
        conpInfos.put("conpDutyNum",conpDutyNum);
        return conpInfos;

    }


    /**
     * 查询公司职位人员
     * @param applyId  Id
     * @param duty 职位信息：01-董事 02-监事 03-经理 04-财务负责人 05-公司联系人 06-公司经办人 07-投资人
     * @param dutyType 职位类型：1-董事长 2-副董事 3-不任职董事 4-监事主席 5-不任职监事
     * @param appointedType 委任方式：1-董事选举 2-执行董事任命 3-股东委派
     * @param dutySource 职位来源：1-股东代表 2-职工代表
     * @return
     * @throws Exception
     */
    public List<PeopleDutyModel> queryDutyInfo(String applyId,String duty, String dutyType, String appointedType, String dutySource) throws Exception{
        PeopleDutyModel peopleDutyModel =new PeopleDutyModel();
        peopleDutyModel.setBizFlowNo(applyId);
        peopleDutyModel.setDuty(duty);
        peopleDutyModel.setDutyType(dutyType);
        peopleDutyModel.setAppointedType(appointedType);
        peopleDutyModel.setDutySource(dutySource);
        List<PeopleDutyModel> peopleDutyModels = baseDAO.selectList("com.bank.queryByDutyAndFlowNo",peopleDutyModel);
        return peopleDutyModels;
    }



    /**
     * 更新营业执照库存
     *
     * @param applyId
     */
    public Result updateLicense(String applyId) {
        ApplyInfoModel applyInfoModel = new ApplyInfoModel();
        applyInfoModel.setId(applyId);
        ApplyInfoModel applyInfo ;
        LicenseInfoModel licenseInfoModel = new LicenseInfoModel();
        try {
            applyInfo = (ApplyInfoModel) baseDAO.selectOne("com.company.apply.selectById", applyInfoModel);
            licenseInfoModel.setUsid(applyInfo.getUSID());
            licenseInfoModel.setBankCardNo(applyInfo.getBankAccountNo());
            licenseInfoModel.setBankAccountNo(applyInfo.getBankCard());
            baseDAO.update("com.license.updateLicense", licenseInfoModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @Override
    public Result sendOpenAccApply(HttpServletRequest request) throws Exception {

        String type = request.getParameter("type");
        String zh = request.getParameter("zh");
        if(StringUtils.isBlank(type)){
            throw new BusinessException("type为空");
        }
        if("4".equals(type) && StringUtils.isBlank(zh)){
            throw new BusinessException("账号为空");
        }
        //中行uuid
        if("1".equals(type)){
            return Result.ok(Client.getSocketCon(SocketParamsUtil.chinaBankUUID(), bankConfig));
        }

        //中行
        if ("2".equals(type)){
            return Result.ok(Client.getSocketCon(bankConfig));
        }
        return Result.ok("没有执行请求");
    }




}
