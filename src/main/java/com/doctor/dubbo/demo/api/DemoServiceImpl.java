package com.doctor.dubbo.demo.api;

/**
 * @author sdcuike
 *
 *         Create At 2016年4月12日 上午10:40:25
 */
public class DemoServiceImpl implements DemoService {

    @Override
    public ModelResult<String> get(DemoPerson demoPerson) {
        ModelResult<String> result = new ModelResult<>();
        result.setRescode("0");
        result.setData("hello  " + demoPerson);
        return result;
    }

}
