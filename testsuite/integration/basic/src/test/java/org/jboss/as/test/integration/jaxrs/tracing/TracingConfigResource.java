package org.jboss.as.test.integration.jaxrs.tracing;

import org.jboss.resteasy.tracing.RESTEasyTracingLogger;
import org.jboss.resteasy.tracing.api.RESTEasyTracing;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;

@Path("/")
public class TracingConfigResource {

    @GET
    @Path("/type")
    public String type(@Context Configuration config) {
        return RESTEasyTracingLogger.getTracingConfig(config);
    }

    @GET
    @Path("/level")
    public String level(@Context Configuration config) {
        return RESTEasyTracingLogger.getTracingThreshold(config);
    }

    @GET
    @Path("/logger")
    public String logger(@Context HttpServletRequest request) {
        RESTEasyTracingLogger logger = (RESTEasyTracingLogger) request.getAttribute(RESTEasyTracing.PROPERTY_NAME);
        if (logger == null) {
            return "";
        } else {
            return RESTEasyTracingLogger.class.getName();
        }
    }

}
