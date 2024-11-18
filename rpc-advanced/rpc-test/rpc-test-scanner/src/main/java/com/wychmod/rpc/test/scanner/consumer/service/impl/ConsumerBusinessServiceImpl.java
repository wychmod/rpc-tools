package com.wychmod.rpc.test.scanner.consumer.service.impl;

import com.wychmod.rpc.annotation.RpcReference;
import com.wychmod.rpc.test.scanner.consumer.service.ConsumerBusinessService;
import com.wychmod.rpc.test.scanner.service.DemoService;

/**
 * @description: 服务消费者业务逻辑实现类
 * @author: wychmod
 * @date: 2024-10-08
 */
public class ConsumerBusinessServiceImpl implements ConsumerBusinessService {

    @RpcReference(registryType = "zookeeper", registryAddress = "127.0.0.1:2181", version = "1.0.0", group = "binghe")
    private DemoService demoService;
}
