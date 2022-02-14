package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.btkAkademi.rentACar.business.requests.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.btkAkademi.rentACar.business.responses.AdditionalServiceListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface AdditionalServiceService {

    DataResult<List<AdditionalServiceListResponse>> findAllByRentalId(int rentalId);

    Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest);

    Result addAll(List<CreateAdditionalServiceRequest> createAdditionalServiceRequests);

    Result update(UpdateAdditionalServiceRequest updateAdditionalServiceRequest);

    Result delete(int id);

}
