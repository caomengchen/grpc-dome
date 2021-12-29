package com.rpc.servce.dome2;

import com.anoyi.grpc.annotation.GrpcService;

@GrpcService(server = "dome-2")
public interface Dome2Servce {
    String getMame();
}
