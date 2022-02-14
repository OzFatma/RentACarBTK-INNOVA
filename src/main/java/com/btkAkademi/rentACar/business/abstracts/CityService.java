package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.cityRequests.CreateCityRequest;
import com.btkAkademi.rentACar.business.requests.cityRequests.UpdateCityRequest;
import com.btkAkademi.rentACar.business.responses.CityListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface CityService {

    DataResult<List<CityListResponse>> findAll();

    DataResult<CityListResponse> findById(int id);

    Result add(CreateCityRequest createCityRequest);

    Result update(UpdateCityRequest updateCityRequest);

    Result delete(int id);

}
