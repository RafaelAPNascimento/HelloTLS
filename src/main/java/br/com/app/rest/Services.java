package br.com.app.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/service")
@Produces(TEXT_PLAIN)
public interface Services {

    @GET
    @Path("/{name}")
    public String getMessage(@PathParam("name") String name);
}
