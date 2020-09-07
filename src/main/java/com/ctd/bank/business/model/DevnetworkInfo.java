package com.ctd.bank.business.model;

import lombok.Data;

/**
 * 网点信息表
 * Created by Hwong on 2019/6/29
 */

public class DevnetworkInfo {
    
    private String id;              //网点id 
    private String bankCode;        //银行编号
    private String bankName;        //银行名称
    private String networkCode;     //网点编号
    private String networkName;     //网点名称
    private String address;         //网点地址
    private String telephone;       //电话
    private String byzd1;           //机构号
    private String byzd2;           //银行号

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getNetworkCode() {
        return networkCode;
    }

    public void setNetworkCode(String networkCode) {
        this.networkCode = networkCode;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getByzd1() {
        return byzd1;
    }

    public void setByzd1(String byzd1) {
        if (byzd1.contains("gs")) {
            byzd1 = byzd1.substring(0,byzd1.indexOf("gs"));
        }
        this.byzd1 = byzd1;
    }

    public String getByzd2() {
        return byzd2;
    }

    public void setByzd2(String byzd2) {
        this.byzd2 = byzd2;
    }

    @Override
    public String toString() {
        return "DevnetworkInfo{" +
                "id='" + id + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", networkCode='" + networkCode + '\'' +
                ", networkName='" + networkName + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", byzd1='" + byzd1 + '\'' +
                ", byzd2='" + byzd2 + '\'' +
                '}';
    }
}
