package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.customerCardDetailRequests.CreateCustomerCardDetailRequest;
import com.btkAkademi.rentACar.business.requests.customerCardDetailRequests.UpdateCustomerCardDetailsRequest;
import com.btkAkademi.rentACar.business.responses.CustomerCardDetailListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface CustomerCardDetailService {

    DataResult<List<CustomerCardDetailListResponse>> findCustomerCardDetailsByCustomerId(int customerId);

    DataResult<CustomerCardDetailListResponse> findById(int id);

    Result add(CreateCustomerCardDetailRequest createCustomerPaymentDetailRequest);

    Result update(UpdateCustomerCardDetailsRequest updateCustomerPaymentDetails);

    Result delete(int id);
}
