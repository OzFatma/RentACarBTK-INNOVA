package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.rentalRequests.CreateRentalRequest;
import com.btkAkademi.rentACar.business.requests.rentalRequests.UpdateRentalRequest;
import com.btkAkademi.rentACar.business.responses.MyRentalListResponse;
import com.btkAkademi.rentACar.business.responses.RentalAddResponse;
import com.btkAkademi.rentACar.business.responses.RentalListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface RentalService {

    DataResult<List<RentalListResponse>> findAll(int pageNo, int pageSize);

    DataResult<List<MyRentalListResponse>> findAllByCustomerId(int id);

    DataResult<RentalListResponse> findById(int id);

    DataResult<RentalAddResponse> addForIndividualCustomer(CreateRentalRequest createRentalRequest);

    DataResult<RentalAddResponse> addForCorporateCustomer(CreateRentalRequest createRentalRequest);

    Result update(UpdateRentalRequest updateRentalRequest);

    Result delete(int id);

    boolean isCarRented(int carId);

    DataResult<RentalListResponse> findActiveRentalByCarId(int id);

}
