package com.ctd.bank.business.controller;

import com.ctd.bank.business.service.ZHBankService;
import com.ctd.bank.common.bean.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/zhBank")
@Slf4j
public class ZHBankController {

    @Autowired
    private ZHBankService zhBankService ;

    /**
     * 对公企业预开户申请接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/sendOpenAccApply")
    @ResponseBody
    public Result sendOpenAccApply(HttpServletRequest request){
        Result result ;
        try {
            result = zhBankService.sendOpenAccApply(request);
        } catch (Exception e) {
            result = Result.build(e);
        }
        return result;
    }


    @RequestMapping(value = "/sendChinaBankOne")
    @ResponseBody
    public Result sendChinaBankOne(@RequestBody Map<String, String> map){
        Result result ;
        try {
            result = zhBankService.sendChinaBankOne(map);
        } catch (Exception e) {
            result = Result.build(e);
            e.printStackTrace();
        }
        return result;
    }
}
