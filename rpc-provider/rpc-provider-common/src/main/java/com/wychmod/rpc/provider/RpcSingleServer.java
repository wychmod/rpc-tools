package com.wychmod.rpc.provider;

import com.wychmod.rpc.common.scanner.server.RpcServiceScanner;
import com.wychmod.rpc.provider.common.server.base.BaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 以Java原生方式启动启动Rpc
 * @author: wychmod
 * @date: 2024-10-10
 */
public class RpcSingleServer extends BaseServer {

    private final Logger logger = LoggerFactory.getLogger(RpcSingleServer.class);

    public RpcSingleServer(String serverAddress, String scanPackage) {
        //调用父类构造方法
        super(serverAddress);
        try {
            this.handlerMap = RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(host, port, scanPackage);
        } catch (Exception e) {
            logger.error("RPC Server init error", e);
        }
    }
}
