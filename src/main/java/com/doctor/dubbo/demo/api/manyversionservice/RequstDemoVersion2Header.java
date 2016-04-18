package com.doctor.dubbo.demo.api.manyversionservice;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.doctor.dubbo.demo.api.DemoPerson;

/**
 * @author sdcuike
 *
 *         多版本dubbo服务测试。不通过。dubbo rest协议不支持多版本服务。需改当当源码
 * 
 *         Create At 2016年4月12日 下午4:26:14
 */
public class RequstDemoVersion2Header {

    /**
     * @param args
     */
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        DemoPerson demoPerson = new DemoPerson();
        demoPerson.setAge(888);
        demoPerson.setName("doctor");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("version", "1.0");
        // httpHeaders.set("version", "2.0");
        HttpEntity<Object> httpEntity = new HttpEntity<>(demoPerson, httpHeaders);
        ResponseEntity<String> postForEntity = restTemplate.postForEntity("http://0.0.0.0:8889/demo/get", httpEntity, String.class);
        // ResponseEntity<ModelResult> exchange = restTemplate.exchange("http://0.0.0.0:8889/demo/get", HttpMethod.POST, httpEntity, ModelResult.class);
        System.out.println(postForEntity.getBody());

    }

}
