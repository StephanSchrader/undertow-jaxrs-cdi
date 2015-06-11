package de.mustnotbenamed.quickstart.undertowserver.example.resources;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author schrader
 */
@RequestScoped
@Path("/")
public class RootResource {

    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public Response index() {
        return Response.ok(new Pong("HTML")).header("X-Template", "index").build();
    }

    @GET
    @Path("plain")
    @Produces(MediaType.TEXT_PLAIN)
    public Response plain() {
        return Response.ok(new Pong("Plain")).build();
    }

    @GET
    @Path("json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response json() {
        return Response.ok(new Pong("JSON")).build();
    }

    @GET
    @Path("html")
    @Produces(MediaType.TEXT_HTML)
    public Response html() {
        return Response.ok(new Pong("HTML")).header("X-Template", "index").build();
    }

}
