package com.doctor.dubbo.demo.manyversionservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.doctor.dubbo.demo.api.DemoPerson;
import com.doctor.dubbo.demo.api.DemoService;

import io.netty.channel.ChannelOption;

/**
 * @author sdcuike
 *         实验：编程API方式消费服务，并让resteasy再转暴露服务，为的是让duubo的消费端统一api gateway
 *         Create At 2016年4月12日 上午10:29:45
 */
public class DubboDemoClientVersionService {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        String hostname = "0.0.0.0";
        int port = 8889;
        SecurityDomain securityDomain = null;
        String rootResourcePath = "/";
        int ioWorkerCount = Runtime.getRuntime().availableProcessors() * 2;
        int executorThreadCount = 12;

        NettyJaxrsServer nettyJaxrsServer = new NettyJaxrsServer();

        nettyJaxrsServer.setHostname(hostname);
        nettyJaxrsServer.setPort(port);
        nettyJaxrsServer.setSecurityDomain(securityDomain);
        nettyJaxrsServer.setRootResourcePath(rootResourcePath);

        nettyJaxrsServer.setIoWorkerCount(ioWorkerCount);
        nettyJaxrsServer.setExecutorThreadCount(executorThreadCount);
        Map<ChannelOption, Object> channelOptions = new HashMap<ChannelOption, Object>();
        channelOptions.put(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100);
        nettyJaxrsServer.setChannelOptions(channelOptions);
        nettyJaxrsServer.setChildChannelOptions(channelOptions);

        ResteasyDeployment deployment = nettyJaxrsServer.getDeployment();

        List<Object> resources = new ArrayList<>();

        // dubo 消费远程服务：当前应用配置
        ApplicationConfig dubboApp = new ApplicationConfig();
        dubboApp.setName("dubboAppClient");
        // 注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("127.0.0.1:2181");
        registryConfig.setProtocol("zookeeper");
        registryConfig.setClient("curator");
        registryConfig.setGroup("dubbo");

        ReferenceConfig<DemoService> demoServiceReferenceConfig1 = new ReferenceConfig<>();
        demoServiceReferenceConfig1.setApplication(dubboApp);
        demoServiceReferenceConfig1.setRegistry(registryConfig);
        demoServiceReferenceConfig1.setInterface(DemoService.class);
        demoServiceReferenceConfig1.setVersion("1.0");
        demoServiceReferenceConfig1.setCheck(false);
        DemoService demoService1 = demoServiceReferenceConfig1.get();

        ReferenceConfig<DemoService> demoServiceReferenceConfig2 = new ReferenceConfig<>();
        demoServiceReferenceConfig2.setApplication(dubboApp);
        demoServiceReferenceConfig2.setRegistry(registryConfig);
        demoServiceReferenceConfig2.setInterface(DemoService.class);
        demoServiceReferenceConfig2.setVersion("2.0");
        demoServiceReferenceConfig2.setCheck(false);

        DemoService demoService2 = demoServiceReferenceConfig2.get();

        ReferenceConfig<DemoService> demoServiceReferenceConfig3 = new ReferenceConfig<>();
        demoServiceReferenceConfig3.setApplication(dubboApp);
        demoServiceReferenceConfig3.setRegistry(registryConfig);
        demoServiceReferenceConfig3.setInterface(DemoService.class);
        demoServiceReferenceConfig3.setCheck(false);

        DemoService demoService3 = demoServiceReferenceConfig3.get();

        deployment.setResources(resources);

        nettyJaxrsServer.start();

        // resteasy 暴露服务
        deployment.getRegistry().addSingletonResource(demoService1, "1.0");
        deployment.getRegistry().addSingletonResource(demoService2, "2.0");
        deployment.getRegistry().addSingletonResource(demoService3);

        DemoPerson demoPerson = new DemoPerson();
        demoPerson.setAge(88);
        demoPerson.setName("doctor");
        System.out.println(demoService1.get(demoPerson));
        System.out.println(demoService2.get(demoPerson));
        System.out.println(demoService3.get(demoPerson));
        TimeUnit.MINUTES.sleep(6);
        nettyJaxrsServer.stop();
        ProtocolConfig.destroyAll();
    }

}
