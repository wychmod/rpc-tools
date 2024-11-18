package com.wychmod.rpc.test.provider.single;

import com.wychmod.rpc.provider.RpcSingleServer;
import org.junit.Test;

/**
 * @description: 测试Java原生启动RPC
 * @author: wychmod
 * @date: 2024-10-10
 */
public class RpcSingleServerTest {

    @Test
    public void startRpcSingleServer(){
        RpcSingleServer singleServer = new RpcSingleServer("127.0.0.1:27880", "com.wychmod.rpc.test");
        singleServer.startNettyServer();
    }

}
