package com.ctd.bank.business.model;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class PeopleDutyModel {
	
	
	private Long id;
	
	private String dutyTypeName;//职位类型名称
	
	private String dutyName;//职位名称
	
	private String duty;//职位：01-董事 02-监事 03-经理 04-财务负责人 05-公司联系人 06-公司经办人 07-投资人

	private List<String> dutys;

	public List<String> getDutys() {
		return dutys;
	}

	public void setDutys(List<String> dutys) {
		this.dutys = dutys;
	}

	/**
	 * 董事描述   此董事在董事会中
	 */  
	 
	private String dutyType;//职位类型： 1-董事长 2-副董事 3-不任职董事 4-监事主席 5-不任职监事

	/**
	 * 董事 监视 经理
	 */
	private String year;//任职年限
	
	private String appointedType;//委任方式       1 董事会委派产生     2 执行董事  3  股东委培产生
	
	/**
	 * 董事  职务      和 监视  的描述
	 */
	private String dutySource;//职位来源：01-股东代表  02 职工代表
	
	
	/**
	 * 经办人
	 */
	private String startDate;//  授权起始日期
	
	/**
	 * 投资人 经办人共有
	 */
	private String endDate;//经办人指:授权介质截止日期  投资人 指: 交款截止时间   
	
	/**
	 * 外键
	 */
	private String personId;
	
	private String bizFlowNo;//业务ID(关联我们自己的业务ID)
	
	
	/**
	 * 投资人出资信息
	 */
	private String financingInfo;
	
	
	private List<Map> financingMap;

	private Map financingInfos;
	
	private String isCorporation;//未使用

	private String uuid;

	private String totleFinancing;



	public String cgbl;   // 持股比例
	public String czje;   // 出资金额

	public String getCgbl() {
		return cgbl;
	}

	public void setCgbl(String cgbl) {
		this.cgbl = cgbl;
	}

	public String getCzje() {
		return czje;
	}

	public void setCzje(String czje) {
		this.czje = czje;
	}

	@Override
	public String toString() {
		return "PeopleDutyModel [id=" + id + ", dutyTypeName=" + dutyTypeName
				+ ", dutyName=" + dutyName + ", duty=" + duty + ", dutyType="
				+ dutyType + ", year=" + year + ", appointedType="
				+ appointedType + ", dutySource=" + dutySource
				+ ", isCorporation=" + isCorporation + ", startDate="
				+ startDate + ", endDate=" + endDate + ", personId=" + personId
				+ ", bizFlowNo=" + bizFlowNo + ", financingInfo="
				+ financingInfo + ", financingMap=" + financingMap
				+ ", idcardType=" + idcardType + ", idcardNo=" + idcardNo
				+ ", name=" + name + ", nation=" + nation + ", sex=" + sex
				+ ", birthDate=" + birthDate + ", address=" + address
				+ ", phone=" + phone + ", photo=" + photo + ", operIdCardNo="
				+ operIdCardNo + ", state=" + state + ", email=" + email
				+ ", personType=" + personType + "]";
	}
	public List<Map> getFinancingMap() {
		return financingMap;
	}
	public void setFinancingMap(List<Map> financingMap) {
		this.financingInfo = JSON.toJSONString(financingMap);
		this.financingMap = financingMap;
	}
	
	
	public String getTotleFinancing() {
		return totleFinancing;
	}
	public void setTotleFinancing(String totleFinancing) {
		this.totleFinancing = totleFinancing;
	}
	public String getFinancingInfo() {
		return financingInfo;
	}
	
	public void setFinancingInfo(String financingInfo) {
		List<Map> list = JSON.parseArray(financingInfo, Map.class);
		this.financingInfo = financingInfo;
		this.financingMap = list;
		BigDecimal num = new BigDecimal("0");
		if(list==null){
			this.totleFinancing = num.toString();
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).get("val") != null)
			num = num.add(new BigDecimal(list.get(i).get("val").toString()));
		}
		this.totleFinancing = num.toString();
	}

	public Map getFinancingInfos() {
		return financingInfos;
	}

	public void setFinancingInfos(Map financingInfos) {
		this.financingInfos = financingInfos;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getDutyName() {
		return dutyName;
	}
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
	public String getDutyType() {
		return dutyType;
	}
	public void setDutyType(String dutyType) {
		if (StringUtils.isBlank(dutyType)) {
			dutyType = "";
		}
		this.dutyType = dutyType;
	}
	public String getDutyTypeName() {
		return dutyTypeName;
	}
	public void setDutyTypeName(String dutyTypeName) {
		this.dutyTypeName = dutyTypeName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getAppointedType() {
		return appointedType;
	}
	public void setAppointedType(String appointedType) {
		if (StringUtils.isBlank(appointedType)) {
			appointedType = "";
		}
		this.appointedType = appointedType;
	}
	public String getDutySource() {
		return dutySource;
	}
	public void setDutySource(String dutySource) {
		if (StringUtils.isBlank(dutySource)) {
			dutySource = "";
		}
		this.dutySource = dutySource;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getBizFlowNo() {
		return bizFlowNo;
	}
	public void setBizFlowNo(String bizFlowNo) {
		this.bizFlowNo = bizFlowNo;
	}
	public String getIsCorporation() {
		return isCorporation;
	}
	public void setIsCorporation(String isCorporation) {
		this.isCorporation = isCorporation;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	private String idcardType;                //证件类型
    private String idcardNo;            //证件号码
    private String name;                //姓名
    private String nation;              //民族
    private String sex;                 //性别
	private String cardstartdate;		//证件起始日期
	private String cardenddate;			//证件截止日期
    private String birthDate;           //出生日期
    private String address;             //地址
    private String phone;             //电话号码
    private Object photo;               //照片
    private String operIdCardNo;        //操作人证件号
    private String state;               //数据状态
    private String email;               //邮政编码
    private String personType;          //人员类型
	private String post;                //人员邮编
	private String politicalStatus;     //政治面貌
	private String educationDegree;     //文化程度
	private String accountId;           //e签宝生成的账户ID
	public String getIdcardType() {
		return idcardType;
	}
	public void setIdcardType(String idcardType) {
		this.idcardType = idcardType;
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
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Object getPhoto() {
		return photo;
	}
	public void setPhoto(Object photo) {
		this.photo = photo;
	}
	public String getOperIdCardNo() {
		return operIdCardNo;
	}
	public void setOperIdCardNo(String operIdCardNo) {
		this.operIdCardNo = operIdCardNo;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPersonType() {
		return personType;
	}
	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getCardstartdate() {
		return cardstartdate;
	}

	public void setCardstartdate(String cardstartdate) {
		this.cardstartdate = cardstartdate;
	}

	public String getCardenddate() {
		return cardenddate;
	}

	public void setCardenddate(String cardenddate) {
		this.cardenddate = cardenddate;
	}
	public PeopleDutyModel() {
		
	}
	public PeopleDutyModel(String duty, String bizFlowNo) {
		this.duty = duty;
		this.bizFlowNo = bizFlowNo;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getPoliticalStatus() {
		return politicalStatus;
	}
	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus;
	}
	public String getEducationDegree() {
		return educationDegree;
	}
	public void setEducationDegree(String educationDegree) {
		this.educationDegree = educationDegree;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Integer slfs; // 设立方式

	public Integer getSlfs() {
		return slfs;
	}

	public void setSlfs(Integer slfs) {
		this.slfs = slfs;
	}
}
