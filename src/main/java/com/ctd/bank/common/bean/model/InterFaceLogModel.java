package com.ctd.bank.common.bean.model;

public class InterFaceLogModel {

	private Long id; // 主键
	private String businessNo; //业务编号
	private String jymc; 
	private String param; 
	private String returnInfo; 
	private String createTime; 
	private String updateTime; 
	private String serviceurl;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	public String getJymc() {
		return jymc;
	}
	public void setJymc(String jymc) {
		this.jymc = jymc;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getReturnInfo() {
		return returnInfo;
	}
	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getServiceurl() {
		return serviceurl;
	}
	public void setServiceurl(String serviceurl) {
		this.serviceurl = serviceurl;
	} 
	
}
