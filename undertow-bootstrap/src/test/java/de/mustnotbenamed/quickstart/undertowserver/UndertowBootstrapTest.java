package de.mustnotbenamed.quickstart.undertowserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public abstract class UndertowBootstrapTest {

    private UndertowBootstrap undertowBootstrap;

    protected abstract UndertowBootstrap createSUT();

    @Before
    public void setUp() throws Exception {
        undertowBootstrap = createSUT();
        undertowBootstrap.startup();
    }

    @After
    public void tearDown() throws Exception {
        undertowBootstrap.shutdown();
    }

    @Test
    public void ping_RootResource_With_Plain_MediaType() throws Exception {
        Invocation invocation = ClientBuilder.newClient().target(createTarget("/ping"))
                .request(MediaType.TEXT_PLAIN_TYPE)
                .buildGet();

        String entity = invocation.invoke(String.class);
        assertThat("Response", entity, is("Pong"));

        Response response = invocation.invoke();
        assertThat("Response status", response.getStatus(), is(200));
        assertThat("Response", response.readEntity(String.class), is("Pong"));
//    assertThat("Response", response.getEntity().toString(), is("Hello World"));
    }

    protected URI createTarget(String path) {
        return UriBuilder.fromPath(path).scheme("http").host("localhost").port(8080).build();
    }
}