package com.doctor.dubbo.demo.api.manyversionservice;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.util.internal.StringUtil;

/**
 * @author sdcuike
 *
 *         Create At 2016年4月13日 下午3:53:38
 */

@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class MyContainerRequestFilter implements ContainerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String version = requestContext.getHeaderString("version");
        if (StringUtil.isNullOrEmpty(version)) {
            return;
        }

        URI requestUri = requestContext.getUriInfo().getRequestUri();
        try {
            URI newUri = new URI(requestUri.getScheme(), requestUri.getAuthority(), "/" + version + requestUri.getPath(), requestUri.getQuery(), requestUri.getFragment());
            requestContext.setRequestUri(newUri);

        } catch (URISyntaxException e) {
            log.error("requestContext.setRequestUri error:{}=>{}", version, requestUri);
        }

    }

}
