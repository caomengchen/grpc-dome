package com.ctd.bank.business.model;

import lombok.Data;

/**
 * Created by Hwong on 2019.03.12
 * 人员池model
 */
@Data
public class PersonPool {
    private Long id;                    // 人员信息ID
    private String idcardType;          //证件类型
    private String idcardNo;            //证件号码
    private String name;                //姓名
    private String nation;              //民族
    private String sex;                 //性别
    private String birthDate;           //出生日期
    private String address;             //地址
    private String phone;             //电话号码
    private Object photo;               //照片
    private String operIdCardNo;        //操作人证件号
    private String state;               //数据状态
    private String bizFlowNo;           //业务流水号
    private String email;               //邮政编码
    private String personType;          //人员类型
	private String cardstartdate;		//证件起始日期
	private String cardenddate;			//证件截止日期
	private String accountId;            //e签宝生成的账户ID
	private String post;                //人员邮编
	private String politicalStatus;     //政治面貌
	private String educationDegree;     //文化程度

}
