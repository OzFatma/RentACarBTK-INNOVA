package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.CorporateCustomerService;
import com.btkAkademi.rentACar.business.abstracts.UserService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.constants.Role;
import com.btkAkademi.rentACar.business.requests.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.btkAkademi.rentACar.business.requests.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.btkAkademi.rentACar.business.responses.CorporateCustomerListResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.CorporateCustomerDao;
import com.btkAkademi.rentACar.entities.concretes.CorporateCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorporateCustomerManager implements CorporateCustomerService {

    private final CorporateCustomerDao corporateCustomerDao;
    private final ModelMapperService modelMapperService;
    private final UserService userService;

    @Autowired
    public CorporateCustomerManager(CorporateCustomerDao corporateCustomerDao, ModelMapperService modelMapperService,
                                    UserService userService) {
        super();
        this.corporateCustomerDao = corporateCustomerDao;
        this.modelMapperService = modelMapperService;
        this.userService = userService;
    }

    @Override
    public DataResult<List<CorporateCustomerListResponse>> findAll(int pageNo, int pageSize) {
        List<CorporateCustomer> corporateCustomers = this.corporateCustomerDao.findAll(PageRequest.of(pageNo - 1, pageSize)).getContent();

        List<CorporateCustomerListResponse> response = corporateCustomers.stream()
                .map(corporateCustomer -> modelMapperService.forResponse()
                        .map(corporateCustomer, CorporateCustomerListResponse.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(response, Messages.CUSTOMERLIST);
    }

    @Override
    public DataResult<CorporateCustomerListResponse> findById(int id) {
        if (corporateCustomerDao.existsById(id)) {
            CorporateCustomer corporateCustomer = corporateCustomerDao.findById(id).get();
            CorporateCustomerListResponse response = modelMapperService.forResponse().map(corporateCustomer,
                    CorporateCustomerListResponse.class);
            return new SuccessDataResult<>(response, Messages.CUSTOMERLIST);
        } else
            return new ErrorDataResult<>(Messages.CUSTOMERNOTFOUND);
    }

    @Override
    public Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest) {
        Result result = BusinessRules.run(
                checkIfCompanyNameExists(createCorporateCustomerRequest.getCompanyName()),
                checkIfEmailExists(createCorporateCustomerRequest.getEmail()));
        if (result != null) {
            return result;
        }

        CorporateCustomer corporateCustomer = this.modelMapperService.forRequest()
                .map(createCorporateCustomerRequest,
                        CorporateCustomer.class);
        corporateCustomer.setRole(Role.CORPORATE_CUSTOMER.getRole());
        this.corporateCustomerDao.save(corporateCustomer);
        return new SuccessResult(Messages.CUSTOMERREGISTRATIONSUCCESSFUL);
    }

    @Override
    public Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest) {
        CorporateCustomer corporateCustomer = this.modelMapperService.forRequest().map(updateCorporateCustomerRequest,
                CorporateCustomer.class);
        corporateCustomer.setRole(Role.CORPORATE_CUSTOMER.getRole());
        corporateCustomer.setPassword(corporateCustomerDao.findById(updateCorporateCustomerRequest.getId())
                .get().getPassword());
        this.corporateCustomerDao.save(corporateCustomer);
        return new SuccessResult(Messages.CUSTOMERUPDATE);
    }

    @Override
    public Result delete(int id) {
        if (corporateCustomerDao.existsById(id)) {
            corporateCustomerDao.deleteById(id);
            return new SuccessResult(Messages.CUSTOMERDELETE);
        }
        return new ErrorResult(Messages.CUSTOMERNOTFOUND);
    }

    private Result checkIfCompanyNameExists(String companyName) {
        if (corporateCustomerDao.findByCompanyName(companyName) != null) {
            return new ErrorResult(Messages.CAMPANYNAMEEXISTS);
        }
        return new SuccessResult();

    }

    private Result checkIfEmailExists(String email) {
        if (userService.findByEmail(email).getData() != null) {
            return new ErrorResult(Messages.EMAILERROR);
        }
        return new SuccessResult();
    }

}
