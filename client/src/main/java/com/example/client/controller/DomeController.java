package com.example.client.controller;


import com.rpc.servce.dome1.Dome1Servce;
import com.rpc.servce.dome2.Dome2Servce;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/v1/user")
public class DomeController {

    @Resource
    private Dome1Servce dome1Servce;
    @Resource
    private Dome2Servce dome2Servce;

    @GetMapping("/dome1")
    public String dome1(){
        return dome1Servce.getMame();
    }
    @GetMapping("/dome2")
    public String dome2(){
        return dome2Servce.getMame();
    }

}
