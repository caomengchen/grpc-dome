package com.ctd.bank.common.bean.model;

public class BusinessLogModel {

	private Long id; // 主键
	private String idcardNo; //业务人身份证号
	private String name; // 业务人姓名a
	private String deviceNo; // 自助终端编号    
	private String descrip; // 业务描述
	private String requestArgs;//请求参数
	private String returnInfo;//返回信息
	private String operIp; // 调用IP
	private String operTime; // 调用时间
	private String status; //操作状态
	private String bz;
	private String businessNo; //业务编号
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIdcardNo() {
		return idcardNo;
	}
	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	public String getRequestArgs() {
		return requestArgs;
	}
	public void setRequestArgs(String requestArgs) {
		this.requestArgs = requestArgs;
	}
	public String getReturnInfo() {
		return returnInfo;
	}
	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}
	public String getOperIp() {
		return operIp;
	}
	public void setOperIp(String operIp) {
		this.operIp = operIp;
	}
	public String getOperTime() {
		return operTime;
	}
	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	@Override
	public String toString() {
		return "BusinessLogModel [id=" + id + ", idcardNo=" + idcardNo + ", name=" + name + ", deviceNo=" + deviceNo
				+ ", descrip=" + descrip + ", requestArgs=" + requestArgs + ", returnInfo=" + returnInfo + ", operIp="
				+ operIp + ", operTime=" + operTime + ", status=" + status + ", bz=" + bz + ", businessNo=" + businessNo
				+ "]";
	}

}
