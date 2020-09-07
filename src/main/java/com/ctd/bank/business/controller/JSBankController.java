package com.ctd.bank.business.controller;

import java.util.Map;

import com.ctd.bank.business.service.JSBankService;
import com.ctd.bank.common.annotation.requestBodyLog;
import com.ctd.bank.common.bean.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin
@Controller
@RequestMapping(value = "/jsBank")
public class JSBankController {
  
	@Autowired
	private JSBankService jsBankService;
	
	/**
	 * 建设银行开户
	 * @param parmMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/jsBankOpenAccount")
	@ResponseBody
	@requestBodyLog(businessName = "建设银行-预开户")
	public Result dgBankOpenAccount(@RequestBody Map<String, String> parmMap){
		Result result = null;
		try {
			result = jsBankService.jsBankOpenAccount(parmMap.get("applyId"));
		} catch (Exception e) {
			result = Result.build(e);
			e.printStackTrace();
		}
		return result;
	}
	
	
}
