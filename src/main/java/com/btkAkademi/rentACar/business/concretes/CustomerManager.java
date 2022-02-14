package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.CustomerService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.ErrorDataResult;
import com.btkAkademi.rentACar.core.utilities.results.SuccessDataResult;
import com.btkAkademi.rentACar.dataAccess.abstracts.CustomerDao;
import com.btkAkademi.rentACar.entities.concretes.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CustomerManager implements CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerManager(CustomerDao customerDao) {
        super();
        this.customerDao = customerDao;
    }

    @Override
    public DataResult<Customer> findCustomerById(int id) {
        if (customerDao.existsById(id)) {
            return new SuccessDataResult<>(customerDao.findById(id).get(), Messages.CUSTOMERLIST);

        } else
            return new ErrorDataResult<Customer>(Messages.CUSTOMERNOTFOUND);
    }

}
