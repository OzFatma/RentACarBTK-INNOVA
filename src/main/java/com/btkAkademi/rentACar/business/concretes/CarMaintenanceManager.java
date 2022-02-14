package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.CarMaintenanceService;
import com.btkAkademi.rentACar.business.abstracts.CarService;
import com.btkAkademi.rentACar.business.abstracts.RentalService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.carMaintenanceRequests.CreateCarMaintenanceRequest;
import com.btkAkademi.rentACar.business.requests.carMaintenanceRequests.UpdateCarMaintenanceRequest;
import com.btkAkademi.rentACar.business.responses.CarMaintenanceListResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.CarMaintenanceDao;
import com.btkAkademi.rentACar.entities.concretes.CarMaintenance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarMaintenanceManager implements CarMaintenanceService {

    private final CarMaintenanceDao carMaintenanceDao;
    private final ModelMapperService modelMapperService;
    private final RentalService rentalService;
    private final CarService carService;

    @Autowired
    public CarMaintenanceManager(CarMaintenanceDao carMaintenanceDao, ModelMapperService modelMapperService,
                                 @Lazy RentalService rentalService, CarService carService) {
        super();
        this.carMaintenanceDao = carMaintenanceDao;
        this.modelMapperService = modelMapperService;
        this.rentalService = rentalService;
        this.carService = carService;
    }

    @Override
    public DataResult<List<CarMaintenanceListResponse>> findAll() {
        List<CarMaintenance> carMaintenanceList = this.carMaintenanceDao.findAll();
        List<CarMaintenanceListResponse> response = carMaintenanceList.stream()
                .map(carMaintenance -> modelMapperService.forResponse().map(carMaintenance, CarMaintenanceListResponse.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(response, Messages.CARMAINTENANCELIST);
    }

    @Override
    public DataResult<List<CarMaintenanceListResponse>> findAllByCarId(int id) {
        List<CarMaintenance> carMaintenanceList = this.carMaintenanceDao.findAllByCarId(id);
        List<CarMaintenanceListResponse> response = carMaintenanceList.stream()
                .map(carMaintenance -> modelMapperService.forResponse().map(carMaintenance, CarMaintenanceListResponse.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(response, Messages.CARMAINTENANCELIST);
    }

    @Override
    public DataResult<CarMaintenanceListResponse> findById(int id) {
        if (carMaintenanceDao.existsById(id)) {
            var carMaintenance = carMaintenanceDao.findById(id);
            CarMaintenanceListResponse response = modelMapperService.forResponse().map(carMaintenance,
                    CarMaintenanceListResponse.class);
            return new SuccessDataResult<>(response, Messages.CARMAINTENANCELIST);
        }
        return new ErrorDataResult<CarMaintenanceListResponse>(Messages.CARMAINTENANCENOTFOUND);
    }

    // Sends the car to maintenance
    @Override
    public Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest) {
        Result result = BusinessRules.run(
                checkIfCarIsExists(createCarMaintenanceRequest.getCarId()),
                checkIfCarIsRented(createCarMaintenanceRequest.getCarId()),
                checkIfCarIsAlreadyInMaintenance(createCarMaintenanceRequest.getCarId())
        );
        if (result != null) {
            return result;
        }

        CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(createCarMaintenanceRequest,
                CarMaintenance.class);
        carMaintenance.setId(0);
        this.carMaintenanceDao.save(carMaintenance);
        return new SuccessResult(Messages.CARMAINTENANCEADD);
    }

    @Override
    public Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest) {
        Result result = BusinessRules.run(
                checkIfCarIsExists(updateCarMaintenanceRequest.getCarId()),
                checkIfCarIsRented(updateCarMaintenanceRequest.getCarId()));
        if (result != null) {
            return result;
        }
        CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(updateCarMaintenanceRequest,
                CarMaintenance.class);

        this.carMaintenanceDao.save(carMaintenance);
        return new SuccessResult(Messages.CARMAINTENANCEUPDATE);
    }

    @Override
    public Result delete(int id) {
        if (carMaintenanceDao.existsById(id)) {
            carMaintenanceDao.deleteById(id);
            return new SuccessResult(Messages.CARMAINTENANCEDELETE);
        } else
            return new ErrorResult(Messages.CARMAINTENANCENOTFOUND);
    }

    @Override
    public boolean isCarInMaintenance(int carId) {
        return carMaintenanceDao.findByCarIdAndMaintenanceEndIsNull(carId) != null;
    }

    private Result checkIfCarIsExists(int carId) {
        if (!carService.findCarById(carId).isSuccess()) {
            return new ErrorResult(Messages.CARNOTFOUND);
        } else
            return new SuccessResult();
    }

    private Result checkIfCarIsRented(int carId) {
        if (rentalService.isCarRented(carId)) {
            return new ErrorResult(Messages.CARRENTED);
        } else
            return new SuccessResult();
    }

    private Result checkIfCarIsAlreadyInMaintenance(int carId) {
        if (isCarInMaintenance(carId)) {
            return new ErrorResult(Messages.CARINMANTANANCE);
        } else
            return new SuccessResult();
    }

}
