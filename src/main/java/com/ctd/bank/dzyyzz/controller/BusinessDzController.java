package com.ctd.bank.dzyyzz.controller;

import com.ctd.bank.common.annotation.requestBodyLog;
import com.ctd.bank.common.bean.model.Result;
import com.ctd.bank.dzyyzz.service.BusinessDzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 电子营业执照 卡
 */
@CrossOrigin
@Controller
@RequestMapping(value ="/dzdy")
public class BusinessDzController {
	
	@Autowired
	private BusinessDzService BusinessDzServiceImpl;

	@RequestMapping(value = "/upload")
	@ResponseBody
	@requestBodyLog(businessName = "打卡数据上传")
	public Result upload(@RequestBody Map<String,String> paramMap){
		Result result;
		try {
			result = BusinessDzServiceImpl.upload(paramMap.get("applyId"));
		} catch (Exception e) {
			result = Result.build(e);
			e.printStackTrace();
		}
		return result;
	}

}
