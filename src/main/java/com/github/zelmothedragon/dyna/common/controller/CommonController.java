package com.github.zelmothedragon.dyna.common.controller;

import com.github.zelmothedragon.dyna.common.persistence.entity.Identifiable;
import com.github.zelmothedragon.dyna.common.service.CommonService;
import com.github.zelmothedragon.dyna.common.util.LoggerFacade;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/v1/entity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommonController {

    @Inject
    private CommonService service;

    public CommonController() {
    }

    @POST
    @Path("/{type}/create")
    public Response create(
            @PathParam("type") final String type,
            final String jsonEntity) {

        LoggerFacade.info(() -> "Getting json: {}", jsonEntity);
        
        Identifiable<?> entity = DynamicEntityMapper.mapToEntity(type, jsonEntity);
        service.save(entity);

        LoggerFacade.info(() -> "Creating entity: {}", Arrays.asList(jsonEntity));

        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("/{type}/update")
    public Response update(
            @PathParam("type") final String type,
            final String jsonEntity) {

        Identifiable<?> entity = DynamicEntityMapper.mapToEntity(type, jsonEntity);
        service.save(entity);

        LoggerFacade.info(() -> "Updating entity: {}", Arrays.asList(jsonEntity));

        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }

    @DELETE
    @Path("/{type}/remove")
    public Response remove(
            @PathParam("type") final String type,
            final String jsonEntity) {

        Identifiable<?> entity = DynamicEntityMapper.mapToEntity(type, jsonEntity);
        service.remove(entity);

        LoggerFacade.info(() -> "Removing entity: {}", Arrays.asList(jsonEntity));

        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }

    @GET
    @Path("/{type}/read")
    public Response read(
            @PathParam("type") final String type) {

        Class<? extends Identifiable<?>> entityClass = DynamicEntityMapper.mapToEntityClass(type);
        List<? extends Identifiable<?>> entities = service.find(entityClass);

        LoggerFacade.info(() -> "Reading entities type: {}", type);

        return Response
                .status(Response.Status.OK)
                .entity(entities)
                .build();
    }

    @GET
    @Path("/{type}/read/{id}")
    public Response read(
            @PathParam("type") final String type,
            @PathParam("id") final String id) {

        Class<? extends Identifiable<?>> entityClass = DynamicEntityMapper.mapToEntityClass(type);
        Object entityId = DynamicEntityMapper.mapToEntityId(type, id);
        return service
                .findById(entityClass, entityId)
                .map(e -> Response.ok(e).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());

    }
}
