package com.github.zelmothedragon.dyna.search;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@RequestScoped
@Path("/v1/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SearchController {

    @Inject
    private SearchService service;

    public SearchController() {
    }

    @GET
    @Path("{type}")
    public Response search(
            @Context final UriInfo info,
            @PathParam("type") final String type) {

        var parameters = info.getQueryParameters().entrySet();
        var pagination = service.search(type, parameters);

        return Response
                .ok(pagination)
                .build();
    }

}
