package com.wychmod.rpc.test.scanner.provider;

import com.wychmod.rpc.annotation.RpcService;
import com.wychmod.rpc.test.scanner.service.DemoService;

/**
 * @description: DemoService实现类
 * @author: wychmod
 * @date: 2024-10-08
 */

@RpcService(interfaceClass = DemoService.class, interfaceClassName = "com.wychmod.rpc.test.scanner.service.DemoService", version = "1.0.0", group = "wychmod")
public class ProviderDemoServiceImpl implements DemoService {
}
