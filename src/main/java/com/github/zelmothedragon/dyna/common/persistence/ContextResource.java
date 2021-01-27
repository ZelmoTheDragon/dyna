package com.github.zelmothedragon.dyna.common.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class ContextResource {

    @Produces
    @PersistenceContext
    private EntityManager em;

    public ContextResource() {
    }

}
