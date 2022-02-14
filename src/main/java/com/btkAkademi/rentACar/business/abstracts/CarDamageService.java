package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.carDamageRequests.CreateCarDamageRequest;
import com.btkAkademi.rentACar.business.requests.carDamageRequests.UpdateCarDamageRequest;
import com.btkAkademi.rentACar.business.responses.CarDamageListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface CarDamageService {

    DataResult<List<CarDamageListResponse>> findAllByCarId(int id);

    DataResult<CarDamageListResponse> findById(int id);

    Result add(CreateCarDamageRequest createCarDamageRequest);

    Result update(UpdateCarDamageRequest updateCarDamageRequest);

    Result delete(int id);

}
