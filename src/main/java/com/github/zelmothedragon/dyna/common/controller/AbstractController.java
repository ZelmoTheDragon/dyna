package com.github.zelmothedragon.dyna.common.controller;

import com.github.zelmothedragon.dyna.common.persistence.entity.Identifiable;
import com.github.zelmothedragon.dyna.common.service.Service;
import java.util.Objects;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class AbstractController<E extends Identifiable<K>, K, S extends Service<E, K>> {

    protected AbstractController() {
    }

    @POST
    @Path("/create")
    public Response create(final E entity) {
        return save(entity);
    }

    @PUT
    @Path("/update")
    public Response update(final E entity) {
        return save(entity);
    }

    @DELETE
    @Path("/delete")
    public Response delete(final E entity) {
        getService().remove(entity);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/read")
    public Response read(K id) {
        Response response;
        Optional<E> optionEntity = getService().findById(id);
        if (optionEntity.isPresent()) {
            response = Response.status(Response.Status.OK).entity(optionEntity.get()).build();
        } else {
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    private Response save(E entity) {
        Response response;
        E savedEntity = getService().save(entity);
        if (Objects.equals(savedEntity.getId(), entity.getId())) {
            response = Response.status(Response.Status.NO_CONTENT).build();
        } else {
            response = Response.status(Response.Status.CREATED).build();
        }
        return response;
    }

    protected abstract S getService();
}
