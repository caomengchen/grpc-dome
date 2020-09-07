package com.ctd.bank.business.service;

import com.ctd.bank.common.bean.model.Result;
import org.dom4j.DocumentException;

public interface JSBankService {

	Result jsBankOpenAccount(String object) throws DocumentException, Exception;

}
