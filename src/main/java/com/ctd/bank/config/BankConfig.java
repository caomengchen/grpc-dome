package com.ctd.bank.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 统一认证配置文件
 */
@Component
@Data
@ConfigurationProperties(prefix = "bank", ignoreUnknownFields = false)
public class BankConfig {

    // 工商银行
    public   String gsAppId;

    public   String gsAccountUrl;

    private  String apigwPublicKey;

    private  String gsMyPrivateKey;

    // 农业银行
    private  String nyBankNyIP;


    // 中国银行
    private  String zhHostTest;

    private  String zhHsot;

    private  String zhPort;

    private  String zhStr;

    // 建设银行
    private  String jsAccountIp;

    private  String jSAccountPortTest;

    private  String jSAccountPort;







}
