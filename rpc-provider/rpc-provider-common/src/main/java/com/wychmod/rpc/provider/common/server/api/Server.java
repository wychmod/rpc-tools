package com.wychmod.rpc.provider.common.server.api;

/**
 * @description: 启动RPC服务的接口
 * @author: wychmod
 * @date: 2024-10-10
 */
public interface Server {

    /**
     * 启动Netty服务
     */
    void startNettyServer();
}
