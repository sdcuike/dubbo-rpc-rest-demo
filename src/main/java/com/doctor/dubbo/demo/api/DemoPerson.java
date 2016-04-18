package com.doctor.dubbo.demo.api;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author sdcuike
 *
 *         Create At 2016年4月12日 上午10:39:21
 */
public class DemoPerson {
    private String name;
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
