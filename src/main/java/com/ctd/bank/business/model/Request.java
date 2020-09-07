/**
  * Copyright 2019 bejson.com 
  */
package com.ctd.bank.business.model;
import java.util.List;

/**
 * Auto-generated: 2019-12-26 11:17:1
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Request {

    private String codPlt;
    private String codBus;
    private String orgNo;
    private String accOrgNo;
    private AccountInfo accountInfo;
    private List<CtrlInfoList> ctrlInfoList;
    public void setCodPlt(String codPlt) {
         this.codPlt = codPlt;
     }
     public String getCodPlt() {
         return codPlt;
     }

    public void setCodBus(String codBus) {
         this.codBus = codBus;
     }
     public String getCodBus() {
         return codBus;
     }

    public void setOrgNo(String orgNo) {
         this.orgNo = orgNo;
     }
     public String getOrgNo() {
         return orgNo;
     }

    public void setAccOrgNo(String accOrgNo) {
         this.accOrgNo = accOrgNo;
     }
     public String getAccOrgNo() {
         return accOrgNo;
     }

    public void setAccountInfo(AccountInfo accountInfo) {
         this.accountInfo = accountInfo;
     }
     public AccountInfo getAccountInfo() {
         return accountInfo;
     }

    public void setCtrlInfoList(List<CtrlInfoList> ctrlInfoList) {
         this.ctrlInfoList = ctrlInfoList;
     }
     public List<CtrlInfoList> getCtrlInfoList() {
         return ctrlInfoList;
     }

}