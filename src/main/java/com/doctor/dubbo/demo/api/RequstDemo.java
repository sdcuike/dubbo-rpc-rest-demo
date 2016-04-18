package com.doctor.dubbo.demo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author sdcuike
 *
 *         测试api gateway 服务。
 * 
 *         1.api gateway 没启动，dubbo provider后启动／不启动（这个不用测试了）。
 *         2.api gateway 先启动，dubbo provider没启动／后启动（没启动，500 Internal Server Erro，启动，能提供服务）
 *         3.api gateway 后启动，dubbo provider没启动／启动
 * 
 *         Create At 2016年4月12日 下午4:26:14
 */
public class RequstDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        DemoPerson demoPerson = new DemoPerson();
        demoPerson.setAge(888);
        demoPerson.setName("doctor");
        ResponseEntity<ModelResult> responseEntity = restTemplate.postForEntity("http://0.0.0.0:8889/demo/get", demoPerson, ModelResult.class);

        System.out.println(responseEntity.getBody().getData());

    }

}
