package com.github.zelmothedragon.dyna.common.controller;

import com.github.zelmothedragon.dyna.common.util.LoggerFacade;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebExceptionMapper implements ExceptionMapper<RuntimeException> {

    public WebExceptionMapper() {
    }

    @Override
    public Response toResponse(final RuntimeException ex) {

        JsonObject message = Json
                .createObjectBuilder()
                .add("error", ex.getClass().getCanonicalName())
                .add("message", ex.getMessage())
                .build();

        LoggerFacade.error(() -> "Error catched by ExceptionMapper", ex);

        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(message)
                .build();
    }

}
