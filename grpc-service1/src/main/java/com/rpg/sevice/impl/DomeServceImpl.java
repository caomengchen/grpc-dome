package com.rpg.sevice.impl;


import com.rpc.servce.dome1.Dome1Servce;
import org.springframework.stereotype.Service;

@Service
public class DomeServceImpl implements Dome1Servce {
    @Override
    public String getMame() {
        System.out.println("dome-1");
        return "dome-1";
    }
}
