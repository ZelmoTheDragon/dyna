package com.github.zelmothedragon.dyna.customer;

import com.github.zelmothedragon.dyna.common.service.AbstractService;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CustomerService extends AbstractService<Customer, UUID, CustomerDAO> {

    @Inject
    private CustomerDAO repository;

    public CustomerService() {
    }

    @Override
    protected CustomerDAO getRepository() {
        return repository;
    }

}
