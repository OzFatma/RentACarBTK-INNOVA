package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.additionalServiceItemRequests.CreateAdditionalServiceItemRequest;
import com.btkAkademi.rentACar.business.requests.additionalServiceItemRequests.UpdateAdditionalServiceItemRequest;
import com.btkAkademi.rentACar.business.responses.AdditionalServiceItemListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface AdditionalServiceItemService {

    Result add(CreateAdditionalServiceItemRequest createAdditionalServiceItemRequest);

    Result update(UpdateAdditionalServiceItemRequest updateAdditionalServiceItemRequest);

    Result delete(int id);

    DataResult<AdditionalServiceItemListResponse> findById(int id);

    DataResult<List<AdditionalServiceItemListResponse>> findAll();
}
