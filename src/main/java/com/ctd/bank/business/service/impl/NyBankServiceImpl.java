package com.ctd.bank.business.service.impl;

import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctd.bank.business.model.*;

import com.ctd.bank.business.service.NyBankService;
import com.ctd.bank.business.util.KeyUtil;
import com.ctd.bank.common.bean.model.Result;

import com.ctd.bank.common.dao.IBaseDAO;
import com.ctd.bank.common.exception.BusinessException;
import com.ctd.bank.common.util.HttpUtil;
import com.ctd.bank.config.BankConfig;

import org.springframework.stereotype.Service;

@Service
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class NyBankServiceImpl implements NyBankService {
    @Resource
    private IBaseDAO baseDAO;
    @Resource
    private BankConfig bankCongig;

    @Override
    public Result openAccount(String applyId) throws Exception {
        Result result = null;
        String url = bankCongig.getNyBankNyIP();
        JsonRootBean jsonRootBean = this.getBody(applyId);
        // JsonRootBean jsonRootBeane    =this.getBodyecs(applyId);    // 测试数据方法
        String jsonstr = HttpUtil.httpPostWithjson(url, JSON.toJSONString(jsonRootBean));
        System.out.println("jsonstr========" + jsonstr);
        JSONObject map = JSON.parseObject(jsonstr);
        String resMessage = map.getString("resMessage");
        String flowNum = map.getString("flowNum");
        String uniqueId = map.getString("uniqueId");
        String responseId = map.getString("responseId");
        String resCode = map.getString("resCode");
        if (resMessage.equals("操作成功") && flowNum.equals("1")) {
            NyBankOpeNacCount nyBankOpeNacCount = new NyBankOpeNacCount();
            nyBankOpeNacCount.setResponseId(responseId);
            nyBankOpeNacCount.setUniqueId(uniqueId);
            nyBankOpeNacCount.setResCode(resCode);
            nyBankOpeNacCount.setBIZ_FLOW_NO(applyId);
            baseDAO.update("com.businesss.updateBusiness", nyBankOpeNacCount);
        }
        return Result.ok(jsonstr);
    }

    /*
     * @return
     * @throws Exception
     */
    public JsonRootBean getBody(String applyId) throws Exception {

        ApplyInfoModel applyInfoModel = new ApplyInfoModel();
        applyInfoModel.setId(applyId);
        //得到单位信息
        ApplyInfoModel applyInfo = (ApplyInfoModel) baseDAO.selectOne("com.company.apply.selectById", applyInfoModel);
        //获取单位证件号码
        String usid = applyInfo.getUSID();//获取统一社会信用代码
        String CorpName = applyInfo.getCorpName();//获取单位名称
        String Operaddr = applyInfo.getOperaddr();//获取注册地址
        // 得到网点信息
        DevnetworkInfo devnetworkInfo = (DevnetworkInfo) baseDAO.selectOne("com.network.queryBankBydevId", applyInfo.getDevId());


        String Byzd1 = devnetworkInfo.getByzd1(); // 获取开户网点编号
        String Byzd2 = Byzd1.substring(2);//  //获取网点机构号
        // 得到经办人信息
        PeopleDutyModel jbr = (PeopleDutyModel) baseDAO.selectOne("com.duty.selectByDutyAndFlowNo", new PeopleDutyModel("06", applyInfo.getId()));
        String IdcardNo = jbr.getIdcardNo();         // 获取经办人证件号码
        String Name = jbr.getName();                // 获取经办人姓名

        //*-----------查询公司法人信息---------*//*
        PeopleDutyModel legalInfo = getFr(applyId);
        //获取法定代表人或负责人证件类型
        String FrIdcardNo = legalInfo.getIdcardNo();  //获取法定代表人或负责人证件号码
        String FrName = legalInfo.getName(); //获取法定代表人或负责人姓名
        String FrPhone = legalInfo.getPhone();   //获取法人移动电话号码
        //
        //*-----------查询营业收入---------*//*
        NyBankOpeNacCount nyBankOpeNacCount = (NyBankOpeNacCount) baseDAO.selectOne("com.businesss.selectBusiness", applyInfo.getId());
        String GSINCOME = nyBankOpeNacCount.getGSINCOME();         // 获取营业收入

        String serialNumber = KeyUtil.SerialNumber.getId();//流水号
        CtrlInfo ctrlInfo = new CtrlInfo();
        //investorName	投资人姓名
        // investorType	投资人类型
        // investorCertType	投资人证件类型
        //investorCertId	投资人证件号码
        //investorScale	投资人投资比例
        ctrlInfo.setNumSeq(" "); //顺序号
        ctrlInfo.setCodRln(" "); //关联关系标识
        ctrlInfo.setCodSholCtrl(" "); //股东或控股人标识
        ctrlInfo.setCodTypCtrl(""); //关联企业或个人类型
        ctrlInfo.setNumCodOrg(" "); //组织机构代码
        ctrlInfo.setCodCrdOrg(" "); //机构信用代码
        ctrlInfo.setCodPidReg(" "); //登记或注册证件类型
        ctrlInfo.setNumPidReg(" "); //登记或注册证件号码
        ctrlInfo.setDatePidReg(" "); //登记或注册证件有效日期
        ctrlInfo.setCodCtry(" "); //国家或地区
        ctrlInfo.setCodRgn(" "); //行政区划
        ctrlInfo.setTxtAddr(" "); //详细地址
        ctrlInfo.setNamCtr(" "); //股东或实际控制人名称或姓名
        ctrlInfo.setCodCcyCctrb(" "); //出资币种
        ctrlInfo.setAmtCtrb(" "); //出资金额
        ctrlInfo.setCodTpyCtrb(" "); //出资方式
        ctrlInfo.setVluStockHoldPct(" "); //持股比例
        ctrlInfo.setDateStockStock(" "); //股东结构对应日期
        ctrlInfo.setDateInfoMdf(" "); //更新信息日期
        ctrlInfo.setCodPidLp(" "); //法定代表人证件类型
        ctrlInfo.setNumSeq(" "); //法定代表人证件号码
        ctrlInfo.setDatePidLp(" "); //法定代表人证件有效期
        ctrlInfo.setNamLp(" "); //法定代表人姓名
        ctrlInfo.setCodRlnLp(" "); //法定代表人关系标识

        CtrlInfoList ctrlInfoList = new CtrlInfoList();
        ctrlInfoList.setCtrlInfo(ctrlInfo);
        List<CtrlInfoList> clists = new ArrayList<>();
        clists.add(ctrlInfoList);

        Common common = new Common();
        common.setNumBrch(Byzd2);    //机构号           配置这个参数，不同网点的不一样
        common.setReqSeqNo(serialNumber);  //请求流水
        common.setToken("571be90a2853c60c000125f1953cb308");    //渠道令牌

        Extension extension = new Extension();
        extension.setExtAdd("DAOS_0110001");

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setCodPro("44");  //开户省市
        accountInfo.setNumOrg(Byzd1);  //开户网点
        accountInfo.setNumUrMagra(" ");  //客户经理A
        accountInfo.setCodAcc("01");   //账户性质
        accountInfo.setNumAcc(" ");  // 账号或者预占账号
        accountInfo.setCodNloc(""); //  异地开户标志  0:本地  1:异地
        accountInfo.setCodRsnNloc(" "); // 异地开户原因
        accountInfo.setNumPidCorp(usid);  //单位证件号码   统一社会信用代码
        accountInfo.setCodPidCorp("611009");     //单位证件类型  统一社会信用代码
        accountInfo.setNamCustCorp(CorpName);      //单位名称
        accountInfo.setNamEnCorp("");               //单位英文名称
        accountInfo.setCodPidOper("110001");            //经办人证件类型
        accountInfo.setNumPidOper(IdcardNo);                     //经办人证件号码
        accountInfo.setDatePidOper("");                         //经办人证件有效期
        accountInfo.setNamOper(Name);                     //经办人姓名
        accountInfo.setNumMobOper("");                              //经办人手机号
        //accountInfo.setoprPstn    //经办人职位
        //经办人国籍
        //经办人联系地址
        accountInfo.setCodCcs("");                   //CCS风险等级码
        accountInfo.setIndC3("");                    //C3客户
        accountInfo.setTxtQrcode("");                    //二维码
        accountInfo.setCodDptr("");                    //存款人类别
        accountInfo.setCodPidLp("110001");                  //法定代表人或负责人证件类型
        accountInfo.setNumPidLp(FrIdcardNo);       //法定代表人或负责人证件号码
        accountInfo.setDatePidLp("");                       //法定代表人或负责人证件有效期
        accountInfo.setNamLp(FrName);                     //法定代表人或负责人姓名
        //  lgpNtn  法定代表人国籍
        //     legalEmail    法定代表人邮箱
        accountInfo.setCodRlnLp("");                    //关系标志
        accountInfo.setNumCodOrg(usid);                    //组织机构代码证号
        accountInfo.setDateCodOrg(" ");                    //组织机构代码证件有效期
        accountInfo.setNamOrg("");                    //组织机构名称
        accountInfo.setNamAcc("");                   //账户名称
        accountInfo.setTxtAddr("");                    //详细地址
        accountInfo.setNumItnlPrfx("");                    //固定电话国际区号
        accountInfo.setNumTnlPrfx("");                    //国内区号
        accountInfo.setNumPho(" ");                   //电话号码
        accountInfo.setNumSubPho("");                    //分机号码
        accountInfo.setNumItnlPrfxMob("");                    //移动电话国际区号
        accountInfo.setNumMob(FrPhone);                    //法人移动电话号码
        accountInfo.setNumZipBus("");                    //注册地址邮政编码
        accountInfo.setCodCustPub("");                    //人民银行客户分类
        accountInfo.setCodCityReg("");                    //注册地国别
        accountInfo.setCodAreaReg("");                    //注册地地区（行政区划）
        accountInfo.setCodCCyReg("");                    //注册资金币种
        accountInfo.setTxtAddrReg(Operaddr);      //注册地址
        accountInfo.setAmtReg("");                    //注册资金金额
        accountInfo.setTxtBusReg("");                    //经营范围描述
        accountInfo.setCodEmpeCorp("");                    //从业人数
        accountInfo.setCodAest("");                    //资产总额
        accountInfo.setCodIncmBus(GSINCOME);                    //营业收入
        accountInfo.setCodIndsTpy("");                    //产业分类代码
        accountInfo.setCodOcpHuge("");                    //行业门类代码
        accountInfo.setCodOcpTyp("");                    //行业类型码
        accountInfo.setTxtOcpTyp("");                    //行业类型描述
        accountInfo.setCodCityBus("");                    //银联地区/注册地区代码
        accountInfo.setTxtAddrBus("");                    //生产经营地址
        accountInfo.setIndRegBus("");                    //经营地址行政区划
        accountInfo.setNumZipBus("");                    //经营地址邮政编码
        accountInfo.setIndRegBus("");                    //经营地址与注册地是否一致
        ///accountInfo.setValidEnd("");                    //经营期限至("");                    //经营期限自
        //accountInfo.setValidEnd("");                    //经营期限至
        // accountInfo.setBusinessState("");                    //经营状态
        accountInfo.setIndRstRmot("");                    //远程视频审核结果
        accountInfo.setNamIpTax("");                    //客户名称（税务登记证记载名)
        accountInfo.setIndTaxPay("");                    //目前纳税状况
        accountInfo.setNumTax("");                    //税务登记证号
        accountInfo.setDateValdTax("");                    //一般纳税人生效日期
        accountInfo.setNamContTax("");                    //税务联系人
        accountInfo.setTxtAddrTax("");                    //生产经营地国籍
        accountInfo.setNumPhoTax("");                    //税务联系电话（固）
        accountInfo.setNumMobTax(" ");                    //税务联系电话（移动）
        accountInfo.setNamFnEntTax("");                    //纳税账户开户银行
        accountInfo.setNumAccTax("");                    //纳税账户银行账号
        accountInfo.setIndCustMsk("");                    //非居民涉税信息尽职调查标识
        accountInfo.setTxtSigdList("");                   //签约信息
        accountInfo.setDateFeeBgn("");                    //费用有效期起
        accountInfo.setDateFeeEnd("");                    //费用有效期止
        accountInfo.setVluPctFee("");                    //浮动比率
        accountInfo.setCodDataSrce("");                    //数据来源
        accountInfo.setDatePidStartOper("");                    //经办人证件有效期起始
        accountInfo.setDatePidStartLp("");                    //法人证件有效期起始
        accountInfo.setCodIntlFstCls("");                    //行内条线一级分类
        accountInfo.setCodIntlSecCls("");                    //行内条线二级分类
        accountInfo.setNumCodOrgSupe("");                    //上级组织机构代码
        accountInfo.setNumLcnOpen("");                    //基本户开户许可证
        accountInfo.setNumLcnOpen("");                    //上级组织名称
        accountInfo.setNamOrgSupe("");                    //上级组织关系标识
        accountInfo.setCodPidSupe("");                    //上级组织法人或负责人证件类型
        accountInfo.setNumPidSupe("");                    //上级组织法人或负责人证件号码
        accountInfo.setDatePidSupe("");                    //上级组织法人或负责人证件有效期
        accountInfo.setNamSupe("");                    //上级组织法人或负责人姓名
        accountInfo.setIndTrustAcc("");                    //托管账户标识
        accountInfo.setLicenseNum("");                    //基本存款账户编号
        accountInfo.setBasicAccount("");                    //基本存款账户账号
        accountInfo.setBasicBank("");                    //基本存款账户开户行
        accountInfo.setChkDat("");                    //基本存款账户核准日期
        accountInfo.setCodBusLcn("");                    //注册证件信息登记或注册证件类型
        accountInfo.setDateInvlBusLcn("");                    //注册证件信息证件有效期
        accountInfo.setNumCust("");                    //客户号
        accountInfo.setProInfo("");                    //产品信息
        accountInfo.setIndAcc("");                    //账户标识
        accountInfo.setIndAccSpe("");                    //特殊账号标识
        accountInfo.setCodCcyCctrb("");                    //币种
        accountInfo.setIndCshRmt("");                    //钞汇标识
        accountInfo.setIndSelNum("");                    //选号标识
        accountInfo.setGenRange("");                    //通存范围
        accountInfo.setExchRange("");                    //通兑范围
        accountInfo.setControlTyp("");                    //支控方式
        accountInfo.setPagOutCyc("");                    //账页输出周期
        accountInfo.setInterIdent("");                        //计息标识
        accountInfo.setRateCode("");                    //利率代码
        accountInfo.setPayCycle("");                    //付息周期
        accountInfo.setCodCtryExec("");                    //国家/地区
        accountInfo.setSettleTyp("");                    //结算方式
        accountInfo.setEnblDat("");                    //启用日期
        accountInfo.setEndDat("");                    //终止日期
        accountInfo.setCodOrg("");                       //机构类别
        accountInfo.setCodTaxDclrOrg("");                    //居民标识
        accountInfo.setInfoDec("");                    //声明人信息
        //支付联系人姓名
        //支付联系人国籍
        //支付联系人证件类型
        //支付联系人证件编号
        //  支付联系人证件有效期
        //支付联系人电话
        Request res = new Request();
        res.setCodPlt("1008");    //平台编码        到时候要申请
        res.setCodBus(" ");// 业务种类编码
        res.setOrgNo(Byzd1); //开户网点
        res.setAccOrgNo(Byzd1);//办理开户业务网点
        res.setCtrlInfoList(clists);
        res.setAccountInfo(accountInfo);

        //*    execInfoList   ctrlInfo.setNumSeq(" "); //顺序号
        ctrlInfo.setNumSeq(" "); //高管类型
        ctrlInfo.setNumSeq(" "); //高管证件类型
        ctrlInfo.setNumSeq(" "); //高管证件号码
        ctrlInfo.setNumSeq(" "); //高管证件有效期
        ctrlInfo.setNumSeq(" "); //高管姓名
        ctrlInfo.setNumSeq(" "); //国家或地区
        ctrlInfo.setNumSeq(" "); //邮政编码
        ctrlInfo.setNumSeq(" "); //行政区划
        ctrlInfo.setNumSeq(" "); //详细地址
        ctrlInfo.setNumSeq(" "); //任职状态
        ctrlInfo.setNumSeq(" "); //任职日期
        ctrlInfo.setNumSeq(" "); //离职日期
        ctrlInfo.setNumSeq(" "); //性别
        ctrlInfo.setNumSeq(" "); //职务
        ctrlInfo.setNumSeq(" "); //学历
        ctrlInfo.setNumSeq(" "); //固定电话
        ctrlInfo.setNumSeq(" "); //移动电话
        ctrlInfo.setNumSeq(" "); //电子邮件

        // codCustDoc	证明材料类型
        //numCustDoc	证明材料号码
        //dateIsueDoc	签发日期
        //dateEndDoc	到期失效日期
        //  dateDocChk	年检日期
        //namOgIsueDoc	签发机构

        Data data = new Data();
        data.setCommon(common);
        data.setRequest(res);
        JsonRootBean jsonRootBean = new JsonRootBean();
        jsonRootBean.setExtension(extension);
        jsonRootBean.setData(data);
        jsonRootBean.setCityCode("00");
        jsonRootBean.setChannelCode("10");
        jsonRootBean.setMsgType("01");
        return jsonRootBean;
    }

//测试方法
  /*  public  JsonRootBean  getBodyecs(String applyId) throws Exception {

        ApplyInfoModel applyInfoModel = new ApplyInfoModel();
        applyInfoModel.setId(applyId);
        ApplyInfoModel applyInfo = (ApplyInfoModel) baseDAO.selectOne("com.company.apply.selectById", applyInfoModel);

        NyBankOpeNacCount nyBankOpeNacCount = (NyBankOpeNacCount) baseDAO.selectOne("com.businesss.selectBusiness", applyInfo.getId());
        String   GSINCOME= nyBankOpeNacCount.getGSINCOME();         // 获取营业收入
        System.out.println(GSINCOME);
//*
        String serialNumber = KeyUtil.SerialNumber.getId();//流水号
        CtrlInfo ctrlInfo = new CtrlInfo();
        //investorName	投资人姓名
        // investorType	投资人类型
        // investorCertType	投资人证件类型
        //investorCertId	投资人证件号码
        //investorScale	投资人投资比例
        ctrlInfo.setNumSeq("1"); //顺序号
        ctrlInfo.setCodRln("1"); //关联关系标识
        ctrlInfo.setCodSholCtrl("0"); //股东或控股人标识
        ctrlInfo.setCodTypCtrl("1"); //关联企业或个人类型
        ctrlInfo.setNumCodOrg(" "); //组织机构代码
        ctrlInfo.setCodCrdOrg(" "); //机构信用代码
        ctrlInfo.setCodPidReg(" "); //登记或注册证件类型
        ctrlInfo.setNumPidReg(" "); //登记或注册证件号码
        ctrlInfo.setDatePidReg(" "); //登记或注册证件有效日期
        ctrlInfo.setCodCtry(" "); //国家或地区
        ctrlInfo.setCodRgn(" "); //行政区划
        ctrlInfo.setTxtAddr(" "); //详细地址
        ctrlInfo.setNamCtr("李四"); //股东或实际控制人名称或姓名
        ctrlInfo.setCodCcyCctrb(" "); //出资币种
        ctrlInfo.setAmtCtrb(" "); //出资金额
        ctrlInfo.setCodTpyCtrb(" "); //出资方式
        ctrlInfo.setVluStockHoldPct(" "); //持股比例
        ctrlInfo.setDateStockStock(" "); //股东结构对应日期
        ctrlInfo.setDateInfoMdf(" "); //更新信息日期
        ctrlInfo.setCodPidLp(" "); //法定代表人证件类型
        ctrlInfo.setNumSeq(" "); //法定代表人证件号码
        ctrlInfo.setDatePidLp(" "); //法定代表人证件有效期
        ctrlInfo.setNamLp(" "); //法定代表人姓名
        ctrlInfo.setCodRlnLp(" "); //法定代表人关系标识

        CtrlInfoList ctrlInfoList=new CtrlInfoList();
        ctrlInfoList.setCtrlInfo(ctrlInfo);
        List<CtrlInfoList> clists=new ArrayList<>();
        clists.add(ctrlInfoList);


        Common common = new Common();
        common.setNumBrch("0569");    //机构号                                 配置这个参数，不同网点的不一样
      //common.setReqSeqNo(serialNumber);  //请求流水
        common.setToken("571be90a2853c60c000125f1953cb308");    //渠道令牌

        System.out.println("common===="+JSON.toJSONString(common));

        Extension extension =new Extension();
        extension.setExtAdd("DAOS_0110001");

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setCodPro("44");  //开户省市
        accountInfo.setNumOrg("440569");  //开户网点
        accountInfo.setNumUrMagra(" ");  //客户经理A
        accountInfo.setCodAcc(" ");   //账户性质
        accountInfo.setNumAcc(" ");  // 账号或者预占账号
        accountInfo.setCodNloc(""); //  异地开户标志  0:本地  1:异地
        accountInfo.setCodRsnNloc(" "); // 异地开户原因
        accountInfo.setNumPidCorp("617490150");  //单位证件号码   统一社会信用代码
        accountInfo.setCodPidCorp("611009");     //单位证件类型  统一社会信用代码
        accountInfo.setNamCustCorp("珠海永科制药有限公司");      //单位名称
        accountInfo.setNamEnCorp("");               //单位英文名称
        accountInfo.setCodPidOper("110001");            //经办人证件类型
        accountInfo.setNumPidOper("44030119620522131X");                     //经办人证件号码
        accountInfo.setDatePidOper("");                         //经办人证件有效期
        accountInfo.setNamOper("欧阳峰");                     //经办人姓名
        accountInfo.setNumMobOper("");                              //经办人手机号
        //accountInfo.setoprPstn    //经办人职位
        //经办人国籍
        //经办人联系地址
        accountInfo.setCodCcs("");                   //CCS风险等级码
        accountInfo.setIndC3("");                    //C3客户
        accountInfo.setTxtQrcode("");                    //二维码
        accountInfo.setCodDptr("");                    //存款人类别
        accountInfo.setCodPidLp("110001");                  //法定代表人或负责人证件类型
        accountInfo.setNumPidLp("44030119620522131X");                    //法定代表人或负责人证件号码
        accountInfo.setDatePidLp("");                   //法定代表人或负责人证件有效期
        accountInfo.setNamLp("欧阳峰");                     //法定代表人或负责人姓名
        //  lgpNtn  法定代表人国籍
        //     legalEmail    法定代表人邮箱
        accountInfo.setCodRlnLp("");                    //关系标志
        accountInfo.setNumCodOrg("617490150");                    //组织机构代码证号
        accountInfo.setDateCodOrg(" ");                    //组织机构代码证件有效期
        accountInfo.setNamOrg("");                    //组织机构名称
        accountInfo.setNamAcc("");                   //账户名称
        accountInfo.setTxtAddr("");                    //详细地址
        accountInfo.setNumItnlPrfx("");                    //固定电话国际区号
        accountInfo.setNumTnlPrfx("");                    //国内区号
        accountInfo.setNumPho(" ");                   //电话号码
        accountInfo.setNumSubPho("");                    //分机号码
        accountInfo.setNumItnlPrfxMob("");                    //移动电话国际区号
        accountInfo.setNumMob("13702312316");                    //法人移动电话号码
        accountInfo.setNumZipBus("");                    //注册地址邮政编码
        accountInfo.setCodCustPub("");                    //人民银行客户分类
        accountInfo.setCodCityReg("");                    //注册地国别
        accountInfo.setCodAreaReg("");                    //注册地地区（行政区划）
        accountInfo.setCodCCyReg("");                    //注册资金币种
        accountInfo.setTxtAddrReg("珠海市联港工业区双林片区创业北路9号");                    //注册地址
        accountInfo.setAmtReg("");                    //注册资金金额
        accountInfo.setTxtBusReg("");                    //经营范围描述
        accountInfo.setCodEmpeCorp("");                    //从业人数
        accountInfo.setCodAest("");                    //资产总额
        accountInfo.setCodIncmBus("02");                    //营业收入
        accountInfo.setCodIndsTpy("");                    //产业分类代码
        accountInfo.setCodOcpHuge("");                    //行业门类代码
        accountInfo.setCodOcpTyp("");                    //行业类型码
        accountInfo.setTxtOcpTyp("");                    //行业类型描述
        accountInfo.setCodCityBus("");                    //银联地区/注册地区代码
        accountInfo.setTxtAddrBus("");                    //生产经营地址
        accountInfo.setIndRegBus("");                    //经营地址行政区划
        accountInfo.setNumZipBus("");                    //经营地址邮政编码
        accountInfo.setIndRegBus("");                    //经营地址与注册地是否一致
        ///accountInfo.setValidEnd("");                    //经营期限至("");                    //经营期限自
        //accountInfo.setValidEnd("");                    //经营期限至
        // accountInfo.setBusinessState("");                    //经营状态
        accountInfo.setIndRstRmot("");                    //远程视频审核结果
        accountInfo.setNamIpTax("");                    //客户名称（税务登记证记载名)
        accountInfo.setIndTaxPay("");                    //目前纳税状况
        accountInfo.setNumTax("");                    //税务登记证号
        accountInfo.setDateValdTax("");                    //一般纳税人生效日期
        accountInfo.setNamContTax("");                    //税务联系人
        accountInfo.setTxtAddrTax("");                    //生产经营地国籍
        accountInfo.setNumPhoTax("");                    //税务联系电话（固）
        accountInfo.setNumMobTax(" ");                    //税务联系电话（移动）
        accountInfo.setNamFnEntTax("");                    //纳税账户开户银行
        accountInfo.setNumAccTax("");                    //纳税账户银行账号
        accountInfo.setIndCustMsk("");                    //非居民涉税信息尽职调查标识
        accountInfo.setTxtSigdList("");                   //签约信息
        accountInfo.setDateFeeBgn("");                    //费用有效期起
        accountInfo.setDateFeeEnd("");                    //费用有效期止
        accountInfo.setVluPctFee("");                    //浮动比率
        accountInfo.setCodDataSrce("");                    //数据来源
        accountInfo.setDatePidStartOper("");                    //经办人证件有效期起始
        accountInfo.setDatePidStartLp("");                    //法人证件有效期起始
        accountInfo.setCodIntlFstCls("");                    //行内条线一级分类
        accountInfo.setCodIntlSecCls("");                    //行内条线二级分类
        accountInfo.setNumCodOrgSupe("");                    //上级组织机构代码
        accountInfo.setNumLcnOpen("");                    //基本户开户许可证
        accountInfo.setNumLcnOpen("");                    //上级组织名称
        accountInfo.setNamOrgSupe("");                    //上级组织关系标识
        accountInfo.setCodPidSupe("");                    //上级组织法人或负责人证件类型
        accountInfo.setNumPidSupe("");                    //上级组织法人或负责人证件号码
        accountInfo.setDatePidSupe("");                    //上级组织法人或负责人证件有效期
        accountInfo.setNamSupe("");                    //上级组织法人或负责人姓名
        accountInfo.setIndTrustAcc("");                    //托管账户标识
        accountInfo.setLicenseNum("");                    //基本存款账户编号
        accountInfo.setBasicAccount("");                    //基本存款账户账号
        accountInfo.setBasicBank("");                    //基本存款账户开户行
        accountInfo.setChkDat("");                    //基本存款账户核准日期
        accountInfo.setCodBusLcn("");                    //注册证件信息登记或注册证件类型
        accountInfo.setDateInvlBusLcn("");                    //注册证件信息证件有效期
        accountInfo.setNumCust("");                    //客户号
        accountInfo.setProInfo("");                    //产品信息
        accountInfo.setIndAcc("");                    //账户标识
        accountInfo.setIndAccSpe("");                    //特殊账号标识
        accountInfo.setCodCcyCctrb("");                    //币种
        accountInfo.setIndCshRmt("");                    //钞汇标识
        accountInfo.setIndSelNum("");                    //选号标识
        accountInfo.setGenRange("");                    //通存范围
        accountInfo.setExchRange("");                    //通兑范围
        accountInfo.setControlTyp("");                    //支控方式
        accountInfo.setPagOutCyc("");                    //账页输出周期
        accountInfo.setInterIdent("");                        //计息标识
        accountInfo.setRateCode("");                    //利率代码
        accountInfo.setPayCycle("");                    //付息周期
        accountInfo.setCodCtryExec("");                    //国家/地区
        accountInfo.setSettleTyp("");                    //结算方式
        accountInfo.setEnblDat("");                    //启用日期
        accountInfo.setEndDat("");                    //终止日期
        accountInfo.setCodOrg("");                       //机构类别
        accountInfo.setCodTaxDclrOrg("");                    //居民标识
        accountInfo.setInfoDec("");                    //声明人信息
        //支付联系人姓名
        //支付联系人国籍
        //支付联系人证件类型
        //支付联系人证件编号
        //  支付联系人证件有效期
        //支付联系人电话
        Request res = new Request();
        res.setCodPlt("1008");    //平台编码        到时候要申请
        res.setCodBus(" ");// 业务种类编码
        res.setOrgNo("440569"); //开户网点
        res.setAccOrgNo("440569");//办理开户业务网点
        res.setCtrlInfoList(clists);
        res.setAccountInfo(accountInfo);




        //*    execInfoList   ctrlInfo.setNumSeq(" "); //顺序号
        ctrlInfo.setNumSeq(" "); //高管类型
        ctrlInfo.setNumSeq(" "); //高管证件类型
        ctrlInfo.setNumSeq(" "); //高管证件号码
        ctrlInfo.setNumSeq(" "); //高管证件有效期
        ctrlInfo.setNumSeq(" "); //高管姓名
        ctrlInfo.setNumSeq(" "); //国家或地区
        ctrlInfo.setNumSeq(" "); //邮政编码
        ctrlInfo.setNumSeq(" "); //行政区划
        ctrlInfo.setNumSeq(" "); //详细地址
        ctrlInfo.setNumSeq(" "); //任职状态
        ctrlInfo.setNumSeq(" "); //任职日期
        ctrlInfo.setNumSeq(" "); //离职日期
        ctrlInfo.setNumSeq(" "); //性别
        ctrlInfo.setNumSeq(" "); //职务
        ctrlInfo.setNumSeq(" "); //学历
        ctrlInfo.setNumSeq(" "); //固定电话
        ctrlInfo.setNumSeq(" "); //移动电话
        ctrlInfo.setNumSeq(" "); //电子邮件

       // codCustDoc	证明材料类型
       //numCustDoc	证明材料号码
      //dateIsueDoc	签发日期
     //dateEndDoc	到期失效日期
    //  dateDocChk	年检日期
   //namOgIsueDoc	签发机构


        //CtrlInfoList    ctrlInfoList = new CtrlInfoList();
        Data data=new Data();
        data.setCommon(common);
        data.setRequest(res);
        JsonRootBean  jsonRootBean =new JsonRootBean();
        jsonRootBean.setExtension(extension);
        jsonRootBean.setData(data);
        jsonRootBean.setCityCode("00");
        jsonRootBean.setChannelCode("10");
        jsonRootBean.setMsgType("01");


        return jsonRootBean;


    }*/


    /*-----------1、查询公司法人信息---------*/

    public PeopleDutyModel getFr(String applyId) throws Exception {

        ApplyInfoModel applyInfo = (ApplyInfoModel) baseDAO.selectOne("com.company.apply.selectById", new ApplyInfoModel(applyId));
        if (applyInfo == null) {
            throw new BusinessException("id有误");
        }
        PeopleDutyModel fr = null;
        // 9911 个体户 只有经营者
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
