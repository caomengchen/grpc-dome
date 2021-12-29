package com.rpc.servce.dome1;

import com.anoyi.grpc.annotation.GrpcService;

@GrpcService(server = "dome-1")
public interface Dome1Servce {
    String getMame();
}
