package org.jboss.as.test.integration.jaxrs.tracing;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.test.integration.common.HttpRequest;
import org.jboss.as.test.integration.jaxrs.packaging.war.WebXml;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(Arquillian.class)
@RunAsClient
public class TracingFeatureTestCase {
    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "tracing.war");
        war.addPackage(HttpRequest.class.getPackage());
        war.addClasses(TracingConfigResource.class);
        war.addAsWebInfResource(WebXml.get(
                "<servlet-mapping>\n"
                        + "        <servlet-name>javax.ws.rs.core.Application</servlet-name>\n"
                        + "        <url-pattern>/*</url-pattern>\n" + "</servlet-mapping>\n"
                        + "<context-param>\n" +
                        "    <param-name>resteasy.server.tracing.type</param-name>\n" +
                        "    <param-value>ALL</param-value>\n" +
                        "</context-param>\n" +
                        "<context-param>\n" +
                        "    <param-name>resteasy.server.tracing.threshold</param-name>\n" +
                        "    <param-value>VERBOSE</param-value>\n" +
                        "</context-param>\n"), "web.xml");
        return war;
    }

    @ArquillianResource
    private URL url;

    private String performCall(String urlPattern) throws Exception {
        return HttpRequest.get(url + urlPattern, 10, TimeUnit.SECONDS);
    }

    @Test
    public void testTracingConfig() throws Exception {
        String result = performCall("level");
        assertEquals("VERBOSE", result);
        String result2 = performCall("type");
        assertEquals("ALL", result2);
        String result3 = performCall("logger");
        assertNotEquals("", result3);
    }
}
