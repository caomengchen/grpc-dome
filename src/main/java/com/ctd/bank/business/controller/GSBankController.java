package com.ctd.bank.business.controller;

import com.ctd.bank.business.service.GSBankService;
import com.ctd.bank.common.annotation.requestBodyLog;
import com.ctd.bank.common.bean.model.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/gsBank")
public class GSBankController {

   @Autowired
    private GSBankService gsBankService;

    /**
     * 工商银行开户
     * @param paramMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/gsBankOpenAccount")
    @ResponseBody
    @requestBodyLog(businessName = "工商银行-预开户")
    public Result dgBankOpenAccount(@RequestBody Map<String, String> paramMap){

        Result result ;
        try {
            result = gsBankService.gsBankOpenAccount(paramMap.get("applyId"));
        } catch (Exception e) {
            result = Result.build(e);
            e.printStackTrace();
        }
        return result;
    }


}
