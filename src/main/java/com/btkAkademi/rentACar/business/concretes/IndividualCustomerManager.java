package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.IndividualCustomerService;
import com.btkAkademi.rentACar.business.abstracts.UserService;
import com.btkAkademi.rentACar.business.constants.Constraints;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.constants.Role;
import com.btkAkademi.rentACar.business.requests.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.btkAkademi.rentACar.business.requests.individualCustomerRequests.UpdateIndividualCustomerRequest;
import com.btkAkademi.rentACar.business.responses.IndividualCustomerListResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.IndividualCustomerDao;
import com.btkAkademi.rentACar.entities.concretes.IndividualCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndividualCustomerManager implements IndividualCustomerService {

    private final IndividualCustomerDao individualCustomerDao;
    private final ModelMapperService modelMapperService;
    private final UserService userService;

    @Autowired
    public IndividualCustomerManager(IndividualCustomerDao individualCustomerDao, ModelMapperService modelMapperService,
                                     UserService userService) {
        super();
        this.individualCustomerDao = individualCustomerDao;
        this.modelMapperService = modelMapperService;
        this.userService = userService;
    }

    @Override
    public DataResult<List<IndividualCustomerListResponse>> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<IndividualCustomer> individualCustomers = this.individualCustomerDao.findAll(pageable).getContent();
        List<IndividualCustomerListResponse> response = individualCustomers.stream()
                .map(individualCustomer -> modelMapperService.forResponse().map(individualCustomer,
                        IndividualCustomerListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.CUSTOMERLIST);
    }

    @Override
    public DataResult<IndividualCustomerListResponse> findById(int id) {
        if (individualCustomerDao.existsById(id)) {
            IndividualCustomer individualCustomer = individualCustomerDao.findById(id).get();
            IndividualCustomerListResponse response = modelMapperService.forResponse().map(individualCustomer,
                    IndividualCustomerListResponse.class);
            return new SuccessDataResult<>(response, Messages.CUSTOMERLIST);
        } else
            return new ErrorDataResult<>(Messages.CUSTOMERNOTFOUND);
    }

    @Override
    public Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) {
        Result result = BusinessRules.run(
                checkIfEmailExists(createIndividualCustomerRequest.getEmail()),
                checkIfUserInAgeLimit(createIndividualCustomerRequest.getBirthDate()));
        if (result != null) {
            return result;
        }

        IndividualCustomer individualCustomer = this.modelMapperService.forRequest()
                .map(createIndividualCustomerRequest, IndividualCustomer.class);
        individualCustomer.setRole(Role.INDIVIDUAL_CUSTOMER.getRole());
        this.individualCustomerDao.save(individualCustomer);

        return new SuccessResult(Messages.CUSTOMERREGISTRATIONSUCCESSFUL);
    }

    @Override
    public Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest) {
        Result result = BusinessRules.run(
                checkIfUserInAgeLimit(updateIndividualCustomerRequest.getBirthDate()));
        if (result != null) {
            return result;
        }

        IndividualCustomer individualCustomer = this.modelMapperService.forRequest()
                .map(updateIndividualCustomerRequest, IndividualCustomer.class);
        individualCustomer.setPassword(individualCustomerDao.findById(individualCustomer.getId())
                .get().getPassword());
        individualCustomer.setRole(Role.INDIVIDUAL_CUSTOMER.getRole());
        this.individualCustomerDao.save(individualCustomer);

        return new SuccessResult(Messages.CUSTOMERUPDATE);
    }

    @Override
    public Result delete(int id) {
        if (individualCustomerDao.existsById(id)) {
            individualCustomerDao.deleteById(id);
            return new SuccessResult();
        } else
            return new ErrorResult(Messages.CUSTOMERNOTFOUND);
    }

    private Result checkIfEmailExists(String email) {
        System.out.println(userService.findByEmail(email).getData());
        if (userService.findByEmail(email).getData() != null) {
            return new ErrorResult(Messages.EMAILERROR);
        }
        return new SuccessResult();
    }

    private Result checkIfUserInAgeLimit(LocalDate birthDate) {
        int Age = Period.between(birthDate, LocalDate.now()).getYears();
        if (Age < Constraints.AGELIMIT) {
            return new ErrorResult(Messages.AGENOTENOUGH);
        }
        return new SuccessResult();
    }

}
