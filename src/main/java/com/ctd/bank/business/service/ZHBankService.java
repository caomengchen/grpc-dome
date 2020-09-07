package com.ctd.bank.business.service;

import com.ctd.bank.common.bean.model.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ZHBankService {

    /**
     * 中国银行报文数据
     * @param paramMap
     * @return
     * @throws Exception
     */
    Result sendChinaBankOne(Map<String, String> paramMap) throws Exception;

    /**
     * @param request
     * @return
     * @throws Exception
     */
    Result sendOpenAccApply(HttpServletRequest request) throws Exception;

}
