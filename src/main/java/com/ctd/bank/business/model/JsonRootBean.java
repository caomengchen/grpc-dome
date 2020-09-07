/**
  * Copyright 2019 bejson.com 
  */
package com.ctd.bank.business.model;

/**
 * Auto-generated: 2019-12-26 11:17:1
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private String cityCode;
    private String channelCode;
    private String msgType;
    private Extension extension;
    private Data data;
    public void setCityCode(String cityCode) {
         this.cityCode = cityCode;
     }
     public String getCityCode() {
         return cityCode;
     }

    public void setChannelCode(String channelCode) {
         this.channelCode = channelCode;
     }
     public String getChannelCode() {
         return channelCode;
     }

    public void setMsgType(String msgType) {
         this.msgType = msgType;
     }
     public String getMsgType() {
         return msgType;
     }

    public void setExtension(Extension extension) {
         this.extension = extension;
     }
     public Extension getExtension() {
         return extension;
     }

    public void setData(Data data) {
         this.data = data;
     }
     public Data getData() {
         return data;
     }

}