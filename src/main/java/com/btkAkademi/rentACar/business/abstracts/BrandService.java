package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.brandRequests.CreateBrandRequest;
import com.btkAkademi.rentACar.business.requests.brandRequests.UpdateBrandRequest;
import com.btkAkademi.rentACar.business.responses.BrandListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface BrandService {

    DataResult<List<BrandListResponse>> findAll();

    DataResult<BrandListResponse> findById(int id);

    Result add(CreateBrandRequest createBrandRequest);

    Result update(UpdateBrandRequest updateBrandRequest);

    Result delete(int id);

}
