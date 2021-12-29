package com.rpg.sevice.impl;

import com.rpc.servce.dome2.Dome2Servce;
import org.springframework.stereotype.Service;

@Service
public class DomeServceImpl implements Dome2Servce {
    @Override
    public String getMame()
    {
        System.out.println("dome-2");
        return "dome-2";
    }
}
