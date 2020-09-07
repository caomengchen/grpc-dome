/**
  * Copyright 2019 bejson.com 
  */
package com.ctd.bank.business.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Auto-generated: 2019-12-26 11:17:1
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Common {
    @JSONField(ordinal = 1)
    private String NumBrch;
    @JSONField(ordinal = 2)
    private String ReqSeqNo;
    @JSONField(ordinal = 3)
    private String Token;
    @JSONField(name = "NumBrch")
    public void setNumBrch(String NumBrch) {
         this.NumBrch = NumBrch;
     }
    @JSONField(name = "NumBrch")
    public String getNumBrch() {
         return NumBrch;
     }
    @JSONField(name = "ReqSeqNo")
    public void setReqSeqNo(String ReqSeqNo) {
         this.ReqSeqNo = ReqSeqNo;
     }
    @JSONField(name = "ReqSeqNo")
     public String getReqSeqNo() {
         return ReqSeqNo;
     }
    @JSONField(name = "Token")
     public void setToken(String Token) {
         this.Token = Token;
     }
    @JSONField(name = "Token")
     public String getToken() {
         return Token;
     }

}