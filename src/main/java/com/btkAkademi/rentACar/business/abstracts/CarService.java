package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.carRequests.CreateCarRequest;
import com.btkAkademi.rentACar.business.requests.carRequests.UpdateCarRequest;
import com.btkAkademi.rentACar.business.responses.CarListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface CarService {

    DataResult<List<CarListResponse>> findAll(int pageNo, int pageSize);

    DataResult<List<CarListResponse>> findAllAvailable(int pageNo, int pageSize);

    DataResult<List<CarListResponse>> findAllByBrandId(int brandId, int pageNo, int pageSize);

    DataResult<List<CarListResponse>> findAllByColorId(int colorId, int pageNo, int pageSize);

    DataResult<CarListResponse> findCarById(int id);

    DataResult<List<Integer>> findAvailableCarsBySegmentIdAndCityId(int segmentId, int cityId);

    Result add(CreateCarRequest createCarRequest);

    Result update(UpdateCarRequest updateCarRequest);

    Result updateCarKilometer(int carId, int kilometer);

    Result updateCarCity(int carId, int cityId);

    Result delete(int id);

    DataResult<List<CarListResponse>> findAllBySegmentId(int segmentId);

}
