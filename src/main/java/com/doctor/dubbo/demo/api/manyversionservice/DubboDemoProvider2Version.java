package com.doctor.dubbo.demo.api.manyversionservice;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.doctor.dubbo.demo.api.DemoPerson;
import com.doctor.dubbo.demo.api.DemoService;
import com.doctor.dubbo.demo.api.DemoServiceImpl;
import com.doctor.dubbo.demo.api.ModelResult;

/**
 * @author sdcuike
 *
 *         编程API暴露服务 多版本服务
 *         Create At 2016年4月12日 上午10:36:28
 */
public class DubboDemoProvider2Version {
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

        // verson
        ServiceConfig<DemoService> demoServiceConfig1 = new ServiceConfig<>();
        demoServiceConfig1.setApplication(applicationConfig);
        demoServiceConfig1.setRegistry(registryConfig);
        demoServiceConfig1.setProtocol(protocolConfig);
        demoServiceConfig1.setInterface(DemoService.class);
        demoServiceConfig1.setRef(new DemoService() {

            @Override
            public ModelResult<String> get(DemoPerson demoPerson) {
                ModelResult<String> modelResult = new ModelResult<>();
                modelResult.setData("version 2.0 " + demoPerson);
                return modelResult;
            }
        });
        demoServiceConfig1.setVersion("2.0");
        demoServiceConfig1.export();

        ServiceConfig<DemoService> demoServiceConfig2 = new ServiceConfig<>();
        demoServiceConfig2.setApplication(applicationConfig);
        demoServiceConfig2.setRegistry(registryConfig);
        demoServiceConfig2.setProtocol(protocolConfig);
        demoServiceConfig2.setInterface(DemoService.class);
        demoServiceConfig2.setRef(new DemoService() {

            @Override
            public ModelResult<String> get(DemoPerson demoPerson) {
                ModelResult<String> modelResult = new ModelResult<>();
                modelResult.setData("version 1.0 " + demoPerson);
                return modelResult;
            }
        });
        demoServiceConfig2.setVersion("1.0");
        demoServiceConfig2.export();
    }

}
