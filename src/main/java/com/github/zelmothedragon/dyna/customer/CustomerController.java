package com.github.zelmothedragon.dyna.customer;

import com.github.zelmothedragon.dyna.common.controller.AbstractController;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController extends AbstractController<Customer, String, CustomerService> {

    @Inject
    private CustomerService service;

    public CustomerController() {
    }

    @Override
    protected CustomerService getService() {
        return service;
    }

}
