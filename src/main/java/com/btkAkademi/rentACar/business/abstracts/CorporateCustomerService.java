package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.btkAkademi.rentACar.business.requests.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.btkAkademi.rentACar.business.responses.CorporateCustomerListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface CorporateCustomerService {

    DataResult<List<CorporateCustomerListResponse>> findAll(int pageNo, int pageSize);

    DataResult<CorporateCustomerListResponse> findById(int id);

    Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest);

    Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest);

    Result delete(int id);
}
