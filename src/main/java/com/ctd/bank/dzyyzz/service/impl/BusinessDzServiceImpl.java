package com.ctd.bank.dzyyzz.service.impl;

import com.alibaba.fastjson.JSON;
import com.ctd.bank.business.model.ApplyInfoModel;
import com.ctd.bank.business.model.DevnetworkInfo;
import com.ctd.bank.business.model.PeopleDutyModel;
import com.ctd.bank.common.bean.model.Result;
import com.ctd.bank.common.dao.impl.BaseDAO;
import com.ctd.bank.common.exception.BusinessException;
import com.ctd.bank.common.util.BaseUtils;
import com.ctd.bank.common.webservice.WebServiceCXFFunction;
import com.ctd.bank.config.DzkConfig;
import com.ctd.bank.dzyyzz.service.BusinessDzService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.endpoint.Client;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@SuppressWarnings({"unchecked","rawtypes"})
public class BusinessDzServiceImpl implements BusinessDzService {

	@Resource
    private BaseDAO baseDAO;

	@Resource
    private DzkConfig dzkConfig;

	@Override
	public Result upload(String applyId) throws Exception {

		ApplyInfoModel applyInfo = (ApplyInfoModel) baseDAO.selectOne("com.company.apply.selectById", new ApplyInfoModel(applyId));
		if ("9911".equals(applyInfo.getEntitytypeid())){
			return gthDzk(applyInfo);
		}else{
			return gsDzk(applyInfo);
		}
	}
    
    public Result gsDzk(ApplyInfoModel applyInfo) throws Exception {

		Map<String, String> resultMap = new HashMap<>();
        /*-----------查询网点信息---------------*/
        DevnetworkInfo devnetworkInfo =  (DevnetworkInfo)baseDAO.selectOne("com.network.queryBankBydevId", applyInfo.getDevId());
        
        /*-----------1、查询公司法人信息---------*/
        String legalType = applyInfo.getLegalperson(); //法人代表职位 1、董事长 2、执行董事 3、经理
        PeopleDutyModel legalInfo;
        if("1".equals(legalType)){
            legalInfo = queryDutyInfo(applyInfo.getId() ,"01", "1", "", "").get(0); //董事 -- 董事长
        }else if("2".equals(legalType)){
            legalInfo = queryDutyInfo(applyInfo.getId(),"01", "", "", "").get(0); //执行董事
        }else{
            legalInfo = queryDutyInfo(applyInfo.getId(),"03", "", "", "").get(0); //经理
        }
        String yearLast = new SimpleDateFormat("yy",Locale.CHINESE).format(Calendar.getInstance().getTime());
		String appId ="4419002"+yearLast+getTime();//业务流水号
		String moneys = BaseUtils.NumberToCN.number2CNMontrayUnit((new BigDecimal( applyInfo.getRegcapital()).multiply(new BigDecimal("10000")).setScale(0,BigDecimal.ROUND_HALF_DOWN).toString()));
		String money = moneys+"人民币";
		String content = "http://www.gsxt.gov.cn/index.html?uniscid=" + applyInfo.getUSID();
		byte[] bytes = BaseUtils.getNormalQRCode(content, 100, 100);
		String infoXML = "<?xml version='1.0' encoding='utf-8'?><licence><firm>"
				+ "<type>1</type>"
				+ "<operType>1</operType>"
				+ "<licenceType>0</licenceType>"
				+ "<attribute904>"+ appId+ "</attribute904>"
				+ "<attribute905>1</attribute905>"
				+ "<attribute908>" + devnetworkInfo.getBankName() + "</attribute908>"//载体来源，银行中文名称   如果这边发的是联名卡，那么908是必传的 .如果908为空的话，我们会认为那个工商的标准卡
				+ "<attribute909>" + devnetworkInfo.getByzd2() + "</attribute909>"//银行机构代码，可以传该网点的统一社会信用代码，数字+字母。如果没有可以不传。
				+ "<attribute910>" + devnetworkInfo.getNetworkName() +"</attribute910>"//银行网点机构中文名称
				+ "<attribute500>广东省</attribute500>"
				+ "<attribute501>珠海市</attribute501>"
				+ "<attribute504>441900</attribute504>"
				+ "<attribute13>" + applyInfo.getUSID() + "</attribute13>"
				+ "<attribute16>" + legalInfo.getIdcardNo()+ "</attribute16>"
				+ "<attribute18>" + applyInfo.getCorpName()+ "</attribute18>"
				+ "<attribute19>" + applyInfo.getOrgForm()+ "</attribute19>"
				+ "<attribute20>" + legalInfo.getName()+ "</attribute20>"
				+ "<attribute25>" + money+ "</attribute25>"
				+ "<attribute29>" + applyInfo.getOperaddr() + "</attribute29>"
				+ "<attribute33>" + getDate(applyInfo.getCreateTime()) + "</attribute33>"
				+ "<attribute35>长期</attribute35>"
				+ "<attribute38>长期</attribute38>"
				+ "<attribute39>" + applyInfo.getBusinessscope()+ "</attribute39>"
				+ "<attribute41>" + Base64.encodeBase64String(bytes)+"</attribute41>"
				+ "<attribute42>珠海市市场监督管理局</attribute42>"
				+ "<attribute43>" + getDate(applyInfo.getCreateTime()) + "</attribute43>"
				+ "<attribute44>http://www.gsxt.gov.cn</attribute44>"
				+ "<attribute52>" + legalInfo.getPhone() + "</attribute52>"
				+ "<attribute56>" + legalInfo.getIdcardNo()+"</attribute56>" //法人身份证号码
				+ "</firm></licence>";
		String code = send(dzkConfig, infoXML);
		if (!"0".equals(code)) {
			throw new BusinessException("电子营业执照数据上传失败！");
		}
		resultMap.put("resultValue", code);
		resultMap.put("appId", appId);
		if ("0".equals(resultMap.get("resultValue"))) {
			updateAppId(applyInfo.getId(), appId);
		}
		return Result.ok(resultMap);
	}


	public Result gthDzk(ApplyInfoModel applyInfo) throws Exception {

        /*-----------查询网点信息---------------*/
        DevnetworkInfo  devnetworkInfo =  (DevnetworkInfo)baseDAO.selectOne("com.network.queryBankBydevId", applyInfo.getDevId());
        /*-----------查询个体户经营者信息---------*/
        PeopleDutyModel SelfEmployed= queryDutyInfo(applyInfo.getId(),"08","","","").get(0);
     
        String yearLast = new SimpleDateFormat("yy",Locale.CHINESE).format(Calendar.getInstance().getTime());
		String appId ="4419002"+yearLast+getTime();//业务流水号
		String content = "http://www.gsxt.gov.cn/index.html?uniscid=" + applyInfo.getUSID();
		byte[] bytes = BaseUtils.getNormalQRCode(content, 100, 100);
		String infor = "<?xml version='1.0' encoding='utf-8'?><licence><firm><type>8</type><operType>1</operType><licenceType>0</licenceType>"
			       +"<attribute904>"+appId+"</attribute904>"									//工商业务流水号，此字段和attribute900不能同时使用；只能有一个有效
			       +"<attribute905>1</attribute905>"											//电子营业执照申请数量；与attribute904同时使用有效
                   + "<attribute908>" + devnetworkInfo.getBankName() + "</attribute908>"		//载体来源，银行中文名称   如果这边发的是联名卡，那么908是必传的 .如果908为空的话，我们会认为那个工商的标准卡
                   + "<attribute909>" + devnetworkInfo.getByzd2() + "</attribute909>"			//银行机构代码，可以传该网点的统一社会信用代码，数字+字母。如果没有可以不传。
                   + "<attribute910>" + devnetworkInfo.getNetworkName() +"</attribute910>"		//银行网点机构中文名称
			       +"<attribute500>广东省</attribute500>"											//注册信息所属省名称
			       +"<attribute501>珠海市</attribute501>"                             			//注册信息所属市名称
			       +"<attribute504>441900</attribute504>"                           			//登记机关代码
			       +"<attribute13>" + applyInfo.getUSID()+"</attribute13>"            			//统一社会信用代码
			       +"<attribute16>" + SelfEmployed.getIdcardNo()+"</attribute16>"          		//法人身份证号码
			       +"<attribute18>" + applyInfo.getCorpName()+"</attribute18>"        			//公司名称
			       +"<attribute19>个体工商</attribute19>"                                         //执照类型，根据执照类型文字提供
			       +"<attribute23>" + SelfEmployed.getName()+"</attribute23>"                   //法人姓名
			       +"<attribute28>个人经营</attribute28>"								            //组成形式
			       +"<attribute31>" + applyInfo.getOperaddr()+"</attribute31>"					//经营场所
			       +"<attribute33>" + getDate(applyInfo.getCreateTime())+"</attribute33>"		//成立日期
			       +"<attribute34>" + getDate(applyInfo.getCreateTime())+"</attribute34>"		//开业日期
			       +"<attribute39>" + applyInfo.getBusinessscope()+"</attribute39>"				//经营范围
			   	   + "<attribute41>" + Base64.encodeBase64String(bytes)+"</attribute41>"
			       +"<attribute42>珠海市市场监督管理局</attribute42>"						        //登记机关
			       +"<attribute43>" + getDate(applyInfo.getCreateTime())+"</attribute43>"		//登记日期
			       +"<attribute52>" + SelfEmployed.getPhone()+"</attribute52>"					//领照人电话号码（法人电话号码）
				   + "<attribute56>" + SelfEmployed.getIdcardNo()+"</attribute56>"              //法人身份证号码
			       +"</firm>"
			       +"</licence>";
		
		String code = send(dzkConfig, infor);
		if (!"0".equals(code)) {
			throw new BusinessException("电子营业执照获取失败！");
		}
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("resultValue",code);
		resultMap.put("appId", appId);
		if ("0".equals(resultMap.get("resultValue"))) {
			updateAppId(applyInfo.getId(), appId);
		}
		return  Result.ok(resultMap);
	}

	public String send(DzkConfig dzkConfig, String xml) throws Exception{
		log.info("电子卡上传的xml:{}", xml);
		Client client = WebServiceCXFFunction.createClient(dzkConfig.getWebserviceUrl());
		Object[] obj;
		Object[] parameters = new Object[] { xml, dzkConfig.getSysCode() };
		try {
			obj = client.invoke("businessLicenceRegister", parameters);
			log.info("上传电子营业执照返回:{}",JSON.toJSONString(obj));
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());

		}
		return obj[0].toString();
	}


	public void updateAppId(String applyId,String appId) throws Exception{
		ApplyInfoModel applyInfoModel = new ApplyInfoModel();
		applyInfoModel.setAppId(appId);
		applyInfoModel.setId(applyId);
		baseDAO.update("com.company.apply.saveAppId", applyInfoModel);
	}
	
	/**
     * 查询公司职位人员
     * @param applyId  Id
     * @param duty 职位信息：01-董事 02-监事 03-经理 04-财务负责人 05-公司联系人 06-公司经办人 07-投资人 08-个体户经营者
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
        List<PeopleDutyModel> peopleDutyModels = baseDAO.selectList("com.company.apply.queryByDutyAndFlowNo",peopleDutyModel);
        return peopleDutyModels;
    }

    public static String getTime() throws Exception {
   		SimpleDateFormat df = new SimpleDateFormat("ddHHmmss");// 设置日期格式
   		String date = df.format(new Date());
   		return date;
   	}


	public static String getDate(String date) throws Exception {
		Date dates = new SimpleDateFormat("yyyy-MM-dd").parse(date);
	   String foundingDate = new SimpleDateFormat("yyyy年MM月dd日").format(dates);
	   return foundingDate;
   }

}
