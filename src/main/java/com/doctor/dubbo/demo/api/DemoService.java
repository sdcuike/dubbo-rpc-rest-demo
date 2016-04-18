package com.doctor.dubbo.demo.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author sdcuike
 *
 *         Create At 2016年4月12日 上午10:38:07
 */
@Path("/demo")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface DemoService {

    @Path("/get")
    @POST
    ModelResult<String> get(DemoPerson demoPerson);
}
