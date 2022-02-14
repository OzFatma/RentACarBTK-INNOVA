package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.btkAkademi.rentACar.business.requests.individualCustomerRequests.UpdateIndividualCustomerRequest;
import com.btkAkademi.rentACar.business.responses.IndividualCustomerListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface IndividualCustomerService {

    DataResult<List<IndividualCustomerListResponse>> findAll(int pageNo, int pageSize);

    DataResult<IndividualCustomerListResponse> findById(int id);

    Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest);

    Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest);

    Result delete(int id);
}
