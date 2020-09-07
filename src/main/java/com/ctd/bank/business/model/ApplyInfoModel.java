package com.ctd.bank.business.model;

import lombok.Data;

import java.util.Date;

@Data
public class ApplyInfoModel {
	
	private String id;

	/**
	 * 名称自主申报信息
	 */
	private String appDate; 		//名称申报时间（2019-04-17 00:25:30）
	private String nameDistrict; 	//名称申报地区名称
	private String nameCity;		//名称申报地区编码
	private String localDistrict;	//名称申报区域编号
	private String entTra;			//名称申报字号（隆欣）
	private String industryCo;		//名称申报所属行业代码（5219）
	private String orgForm;			//名称申报公司类型（有限公司）
	private String employeeNum;		//从业人数
	private String jyfs;		    //经营方式
	private String cyname;		    //产业名称
	private String jyfw;		    //经营范围
	private String isji;		    //是否集群注册
	private String contractNo;		//合同编号    集群注册需要
	private String jqUSID;		    //集群注册公司统一信用代码
	private String jqCorpName;		//集群注册公司名称
	private String yybh;		    //预约编号
	private Date submitTime;		//提交时间
	private Date gsshTime;		    //给税务提交工商信息时间
	private Date yhxxTime;		    //给税务提交银行信息时间 
	private String gsshStatus;		    //给税务提交工商信息状态
    private String yhxxStatus ;         //给税务提交银行信息状态
    private String taxStatus ;          //确定填写税务信息
    private String printLicensetime ;   //打照日期
    private String writeCardTime ;      //打照日期


    /**
	 * 第一步 
	 */
	
	private String acceptId;//业务号
	
	private String corpName;//企业名称
	
	private String certid;
	
	private String regcapitalcoin; //注册币种
	
	private String regcapital;//注册资本     ------ 第二步可修改
	
	private String businessscope;//业务范围     -------第三部可修改
	
	private String nameseg3;//
	
	/**
	 * 第三步 企业基本信息
	 */
	
	private String copynum;//副本数量
	
	private String entitytypeid;//公司类型

	private String entitytypetext;//公司类型名称
	
	private String isselectyear;//经营期限  0,1
	
	/**
	 * 第四步  住所信息
	 */
	private String propertyowne;//产权人
	
	private String operationarea;//住房面积
	
	private String town;//镇
	private String townCode;//镇代码
	
	private String village;//村
	
	private String street;//街
	
	private String buildingno;//门牌号
	
	private String buildingname;//建筑名称
	
	private String informationnote;//房屋情况说明   1 - 同一地址登记多个市场主体，与已登记的市场主体有投资关系  2 - 同一地址已登记其他市场主体，原市场主体已不在该地址经营  3 - 以上两种都不是
	
	private String domicileuse;//住所用途   1 - 经营性用房（办公用途）  2 - 城镇农村居民自建房     3 - 经营性用房（非办公用途）  
	
	private String permissiontype;//房屋使用权
	
	private String leaseterm;//租赁 填写年数
	
	private String operaddr; //企业地址  
	
	private String faxno;//标准地址带回的
	
	/**
	 * 第六步
	 */
	private String legalperson;//2 执行董事  1董事  3 经理
	
	
	
	private String phone;//公司电话号码  可不填写
	
	private String postcode;//邮编   默认东莞

	
	
	private String historyinfoid;//业务历史流水id
	
	private String createUser;//业务创建人

	private String step;
	
	private String createTime;
	
	private String status;
	
	private String zbarCode;

	private String fbarCode;
	
	private String USID;// 信用代码
	
	private String bankCode; //银行编号
	
	private String bankAccountNo; // 银行账号

	private String bankCard;  // 银行卡号
	
	private String appId;
	
	private String money;


	private String aicAcceptId; //业务系统的AcceptId
	
	private String asyncBizState;//异步业务状态：01、申请书分发失败 02、申请书分发成功 03、申请书签署失败 04、申请书签署成功 05、申请书提交失败 06、申请书提交成功 07、申请审核失败 08、申请审核成功 21、领照表分发失败 22、领照表分发成功 23、领照表签署失败 24领照表签署成功 25、领照表提交失败 26领照表提交成功
	
	private String asyncBizComm; //异步业务备注
	
	private String devId;//设备编号

	private Date asyncBizTime; //异步业务更新时间


	private String city;//市

	private String province; //省

	private String area; //区


	private String wcsyzm; // 无偿使用证明

	private String isfqszm; // 是否具有房屋权属证明

	private String wccjr; // 无偿使用证明出具人

	private String danwei; // 出具房屋权属情况说明的单位

	private String fwqszm; // 房屋权属证号

	private String czf; // 出租方

	private String chzf; // 承租方

	private String lxdh; // 联系电话

	private String community; // 社区

	private String signBase64; // 打照后签名base64

	private String fbarCodeCertId; // 副本执照id 获取打照数据时保存  回传打照接口要传

	private String zbarCodeCertId; // 正本执照id 获取打照数据时保存 ZBARCODECERTID

	private String apprdate; // 核准日期 xxxx年xx月xx日

	private String estdate; // 成立日期	xxxx年xx月xx日

	private String licenseReceiveStatus; // 0成功 回传状态

	private String licenseReceiveTime; // 回传时间

	private String mzzh; // 名称组合模式

	private String mzzhCode; // 名称组合模式代码 前端区别赋值

	private String applySource; // 数据来源

	public ApplyInfoModel(String id) {
		this.id = id;
	}

	public ApplyInfoModel() {
		
	}
	public ApplyInfoModel(String id, String step) {
		this.id = id;
		this.step = step;
	}
}
