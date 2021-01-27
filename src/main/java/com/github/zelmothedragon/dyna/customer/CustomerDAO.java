package com.github.zelmothedragon.dyna.customer;

import com.github.zelmothedragon.dyna.common.persistence.repository.AbstractDAO;
import javax.enterprise.context.Dependent;

@Dependent
public class CustomerDAO extends AbstractDAO<Customer, String> {

    public CustomerDAO() {
    }

}
