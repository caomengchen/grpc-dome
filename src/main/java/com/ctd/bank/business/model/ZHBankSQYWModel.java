package com.ctd.bank.business.model;

import lombok.Data;

@Data
public class ZHBankSQYWModel {
   
	private String id;
	
	private String personId;   //人员ID
	
	private String A;          //A-办理单位银行结算账户
	
	private String B;          //B-签署综合服务协议
	
	private String C;          //C-预留签章样式
	
	private String D;          //D-变更预留签章样式
	
	private String E;          //E-办理日常结算及购买空白支付凭证业务
	
	private String F;          //F-取消日常结算及购买空白支付凭证业务
	
	private String G;          //G-作为我单位大额交易有权确认人员
	
	private String H;          //H-取消我单位大额交易有权确认人员
	
	private String I;          //I-其他
	
	private String hz;         //汇总
	
	private String sum;               //选中个数
	
	private String openAccountid;     //开户id
	
	private String wydzr;             //网银对账人
	
	private String yjdzr;             //邮寄对账人
	
	private String ck;                //持卡
	
	private String dxjsr;             //短信接受人
	
	private String dzhdx;             //电子回单箱
	
	private String wyjbr;             //网银经办人
	
	private String wysqr;             //网银授权人
	
	private String createTime;        //创建时间
	
	private String duty;              //职位
	
	private String sqrlx;             //授权人类型
    
	
	private String bzFlowNo;          //公司ID
	
	private String name;              //姓名
	
	private String idcardNo;          //身份证号
	
	private String idcardType;        //证件类型
	
	private String phone;             //手机号
	
}
