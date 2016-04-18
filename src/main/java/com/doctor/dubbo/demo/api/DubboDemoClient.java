package com.doctor.dubbo.demo.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

import io.netty.channel.ChannelOption;

/**
 * @author sdcuike
 *         实验：编程API方式消费服务，并让resteasy再转暴露服务，为的是让duubo的消费端统一api gateway
 *         Create At 2016年4月12日 上午10:29:45
 */
public class DubboDemoClient {

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

        ReferenceConfig<DemoService> demoServiceReferenceConfig = new ReferenceConfig<>();
        demoServiceReferenceConfig.setApplication(dubboApp);
        demoServiceReferenceConfig.setRegistry(registryConfig);
        demoServiceReferenceConfig.setInterface(DemoService.class);
        demoServiceReferenceConfig.setCheck(false);
        DemoService demoService = demoServiceReferenceConfig.get();

        // resteasy 暴露服务

        resources.add(demoService);
        deployment.setResources(resources);
        nettyJaxrsServer.start();

        // TimeUnit.SECONDS.sleep(6);
        // nettyJaxrsServer.stop();
        // demoServiceReferenceConfig.destroy();

    }

}
