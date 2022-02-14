package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.carMaintenanceRequests.CreateCarMaintenanceRequest;
import com.btkAkademi.rentACar.business.requests.carMaintenanceRequests.UpdateCarMaintenanceRequest;
import com.btkAkademi.rentACar.business.responses.CarMaintenanceListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface CarMaintenanceService {

    DataResult<List<CarMaintenanceListResponse>> findAll();

    DataResult<List<CarMaintenanceListResponse>> findAllByCarId(int id);

    DataResult<CarMaintenanceListResponse> findById(int id);

    Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest);

    Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest);

    Result delete(int id);

    boolean isCarInMaintenance(int carId);

}
