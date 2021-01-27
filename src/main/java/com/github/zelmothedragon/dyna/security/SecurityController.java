package com.github.zelmothedragon.dyna.security;

import java.security.Principal;
import javax.annotation.security.DenyAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/protected")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SecurityController {

    @Inject
    private Principal principal;

    public SecurityController() {
    }

    @DenyAll
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "Hello " + principal.getName();
    }

}
