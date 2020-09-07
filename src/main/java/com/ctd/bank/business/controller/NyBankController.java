package com.ctd.bank.business.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ctd.bank.business.service.NyBankService;
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
@RequestMapping(value ="/nyBank")
public class NyBankController {
  
	@Autowired
	private NyBankService nyBankService;
	
	/**
	 * 农业银行开户
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/openAccount")
	@ResponseBody
	@requestBodyLog(businessName = "农业银行-预开户")
	public Result openAccount(@RequestBody Map<String, String> paramMap, HttpServletRequest request){
		Result result;
		try {
			result = nyBankService.openAccount(paramMap.get("applyId"));
		} catch (Exception e) {
			result = Result.build(e);
			e.printStackTrace();
		}
		return result;
	}





	
}
