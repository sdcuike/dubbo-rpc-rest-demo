package com.doctor.dubbo.demo.api;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;

/**
 * @author sdcuike
 *
 *         编程API暴露服务
 *         Create At 2016年4月12日 上午10:36:28
 */
public class DubboDemoProvider {
    static String address = "127.0.0.1:2181";

    public static void main(String[] args) {
        // 服务实现
        DemoService demoService = new DemoServiceImpl();
        // 当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig("demoService");
        applicationConfig.setLogger("slf4j");
        applicationConfig.setCompiler("javassist");

        // 连接注册中心配置
        RegistryConfig registryConfig = new RegistryConfig(address);
        registryConfig.setClient("curator");
        registryConfig.setGroup("dubbo");
        registryConfig.setProtocol("zookeeper");

        // 服务提供者协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig("resteasy", 7788);
        protocolConfig.setIothreads(2);
        protocolConfig.setThreads(3);
        protocolConfig.setServer("netty");
        protocolConfig.setHost("0.0.0.0");

        // 服务提供者暴露服务配置
        ServiceConfig<DemoService> demoServiceConfig = new ServiceConfig<>();
        demoServiceConfig.setApplication(applicationConfig);
        demoServiceConfig.setRegistry(registryConfig);
        demoServiceConfig.setProtocol(protocolConfig);
        demoServiceConfig.setInterface(DemoService.class);
        demoServiceConfig.setRef(demoService);
        demoServiceConfig.export();
    }

}
