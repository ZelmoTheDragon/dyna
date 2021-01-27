package com.github.zelmothedragon.dyna.common.util;

import java.io.IOException;
import java.util.Objects;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import org.slf4j.MDC;

@Provider
public class LoggerFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String REMOTE_ADDRESS_HEADER = "X-FORWARDED-FOR";

    @Inject
    private HttpServletRequest request;

    public LoggerFilter() {
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {

        LoggerFacade.trace(() -> "Inside LoggerFilter");
        String remoteAddr = request.getHeader(REMOTE_ADDRESS_HEADER);
        if (Objects.isNull(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }
        MDC.put("remoteAddr", remoteAddr);
    }

    @Override
    public void filter(
            final ContainerRequestContext requestContext,
            final ContainerResponseContext responseContext) throws IOException {

        LoggerFacade.trace(() -> "Clear...");
        MDC.clear();
    }

}
