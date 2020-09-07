package com.ctd.bank.business.model;

import lombok.Data;

import java.util.Date;

@SuppressWarnings("serial")
@Data
public class LicenseInfoModel {


    private Long id;
    private String originalNumber;
    private String duplicateNumber;
    private String batchNumber;
    private String bankCode;
    private String networkCode;
    private String corpName;
    private String usid;
    private String jbr;
    private String jbrPhone;
    private String status;
    private String bankCardNo;
    private String bankAccountNo;
    private String deviceNo;
    private String createUser;
    private String createTime;
    private String updateTime;
    private String vaildFlag;
    private Date printDate;
    private String printDateString;
    private String networkName;
    private String bankName;
    
    private String start;
    private String end;
    
    private String writeOff;
    private String acceptId; // 业务受理号
    private String applyType; // 申请途径
    private String devId; // 设备编号


    public LicenseInfoModel() {
        super();
    }

    public LicenseInfoModel(String originalNumber, String duplicateNumber, String networkCode) {
        super();
        this.originalNumber = originalNumber;
        this.duplicateNumber = duplicateNumber;
        this.networkCode = networkCode;
    }
}
