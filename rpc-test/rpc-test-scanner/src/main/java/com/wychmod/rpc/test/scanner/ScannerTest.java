package com.wychmod.rpc.test.scanner;

import com.wychmod.rpc.common.scanner.ClassScanner;
import com.wychmod.rpc.common.scanner.reference.RpcReferenceScanner;
import com.wychmod.rpc.common.scanner.server.RpcServiceScanner;
import org.junit.Test;

import java.util.List;

/**
 * @description: 测试类扫描工具类
 * @author: wychmod
 * @date: 2024-10-08
 */
public class ScannerTest {

    /**
     * 扫描com.wychmod.rpc.test.scanner包下所有的类
     */
    @Test
    public void testScannerClassNameList() throws Exception {
        List<String> classNameList = ClassScanner.getClassNameList("com.wychmod.rpc.test.scanner");
        classNameList.forEach(System.out::println);
    }

    /**
     * 扫描com.wychmod.rpc.test.scanner包下所有标注了@RpcService注解的类
     */
    @Test
    public void testScannerClassNameListByRpcService() throws Exception {
        RpcServiceScanner.
                doScannerWithRpcServiceAnnotationFilterAndRegistryService("com.wychmod.rpc.test.scanner");
    }


    /**
     * 扫描com.wychmod.rpc.test.scanner包下所有标注了@RpcReference注解的类
     */
    @Test
    public void testScannerClassNameListByRpcReference() throws Exception {
        RpcReferenceScanner.
                doScannerWithRpcReferenceAnnotationFilter("com.wychmod.rpc.test.scanner");
    }
}
