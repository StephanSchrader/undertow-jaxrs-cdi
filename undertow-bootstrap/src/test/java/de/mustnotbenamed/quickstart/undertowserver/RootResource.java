package de.mustnotbenamed.quickstart.undertowserver;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author schrader
 */
@Path("/")
@RequestScoped
public class RootResource {

    @GET
    @Path("ping")
    @Produces(MediaType.TEXT_PLAIN)
    public Response plain() {
        return Response.ok("Pong").build();
    }

}
