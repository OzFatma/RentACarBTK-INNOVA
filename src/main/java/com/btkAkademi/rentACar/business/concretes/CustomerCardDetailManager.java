package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.CustomerCardDetailService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.customerCardDetailRequests.CreateCustomerCardDetailRequest;
import com.btkAkademi.rentACar.business.requests.customerCardDetailRequests.UpdateCustomerCardDetailsRequest;
import com.btkAkademi.rentACar.business.responses.CustomerCardDetailListResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.CustomerCardDetailDao;
import com.btkAkademi.rentACar.entities.concretes.CustomerCardDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerCardDetailManager implements CustomerCardDetailService {

    private final ModelMapperService modelMapperService;
    private final CustomerCardDetailDao customerCardDetailDao;

    @Autowired
    public CustomerCardDetailManager(ModelMapperService modelMapperService,
                                     CustomerCardDetailDao customerCardDetailDao) {
        super();
        this.modelMapperService = modelMapperService;
        this.customerCardDetailDao = customerCardDetailDao;
    }

    @Override
    public DataResult<List<CustomerCardDetailListResponse>> findCustomerCardDetailsByCustomerId(int customerId) {

        List<CustomerCardDetail> customerPaymentDetails = customerCardDetailDao.findAllByCustomerId(customerId);
        List<CustomerCardDetailListResponse> response = customerPaymentDetails.stream()
                .map(customerPaymentDetail -> modelMapperService.forResponse().map(customerPaymentDetail,
                        CustomerCardDetailListResponse.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(response, Messages.CREDITCARDLIST);

    }

    @Override
    public DataResult<CustomerCardDetailListResponse> findById(int id) {
        if (customerCardDetailDao.existsById(id)) {
            CustomerCardDetail customerPaymentDetail = customerCardDetailDao.findById(id).get();
            CustomerCardDetailListResponse response = modelMapperService.forResponse().map(customerPaymentDetail,
                    CustomerCardDetailListResponse.class);
            return new SuccessDataResult<>(response, Messages.CREDITCARDLIST);
        }
        return new ErrorDataResult<>(Messages.CREDITCARDNOTFOUND);
    }

    @Override
    public Result add(CreateCustomerCardDetailRequest createCustomerPaymentDetailRequest) {
        Result result = BusinessRules.run(
                checkIfCardNoExist(createCustomerPaymentDetailRequest.getCardNo()));
        if (result != null) {
            return result;
        }
        CustomerCardDetail customerPaymentDetail = this.modelMapperService.forRequest()
                .map(createCustomerPaymentDetailRequest, CustomerCardDetail.class);
        this.customerCardDetailDao.save(customerPaymentDetail);
        return new SuccessResult(Messages.CREDITCARDADD);
    }

    @Override
    public Result update(UpdateCustomerCardDetailsRequest updateCustomerPaymentDetails) {
        CustomerCardDetail customerPaymentDetail = this.modelMapperService.forRequest().map(updateCustomerPaymentDetails,
                CustomerCardDetail.class);
        this.customerCardDetailDao.save(customerPaymentDetail);
        return new SuccessResult(Messages.CREDITCARDUPDATE);
    }

    @Override
    public Result delete(int id) {
        if (customerCardDetailDao.existsById(id)) {
            customerCardDetailDao.deleteById(id);
            return new SuccessResult(Messages.CREDITCARDELETE);
        }
        return new ErrorResult(Messages.CREDITCARDNOTFOUND);
    }

    private Result checkIfCardNoExist(String cardNo) {
        if (customerCardDetailDao.findByCardNo(cardNo) != null) {
            return new ErrorResult(Messages.CREDITCARDALREADYEXISTS);
        } else
            return new SuccessResult();
    }

}
