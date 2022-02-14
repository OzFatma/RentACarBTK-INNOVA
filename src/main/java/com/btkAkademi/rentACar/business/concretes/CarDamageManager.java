package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.CarDamageService;
import com.btkAkademi.rentACar.business.abstracts.CarService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.carDamageRequests.CreateCarDamageRequest;
import com.btkAkademi.rentACar.business.requests.carDamageRequests.UpdateCarDamageRequest;
import com.btkAkademi.rentACar.business.responses.CarDamageListResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.CarDamageDao;
import com.btkAkademi.rentACar.entities.concretes.CarDamage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarDamageManager implements CarDamageService {

    private final CarDamageDao carDamageDao;
    private final ModelMapperService modelMapperService;
    private final CarService carService;

    @Autowired
    public CarDamageManager(CarDamageDao carDamageDao, ModelMapperService modelMapperService, CarService carService) {
        super();
        this.carDamageDao = carDamageDao;
        this.modelMapperService = modelMapperService;
        this.carService = carService;
    }

    @Override
    public DataResult<List<CarDamageListResponse>> findAllByCarId(int id) {
        List<CarDamage> carDamages = carDamageDao.findAllByCarId(id);
        List<CarDamageListResponse> response = carDamages.stream()
                .map(carDamage -> modelMapperService.forResponse().map(carDamage, CarDamageListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.CARDAMAGELIST);
    }

    @Override
    public DataResult<CarDamageListResponse> findById(int id) {
        if (carDamageDao.existsById(id)) {
            var carDamage = carDamageDao.findById(id);
            CarDamageListResponse response = modelMapperService.forResponse().map(carDamage, CarDamageListResponse.class);
            return new SuccessDataResult<>(response, Messages.CARDAMAGELIST);
        }

        return new ErrorDataResult<>();
    }

    @Override
    public Result add(CreateCarDamageRequest createCarDamageRequest) {
        Result result = BusinessRules.run(
                checkIfCarIsExists(createCarDamageRequest.getCarId())
        );
        if (result != null) {
            return result;
        }
        CarDamage carDamage = this.modelMapperService.forRequest().map(createCarDamageRequest, CarDamage.class);
        carDamage.setId(0);
        this.carDamageDao.save(carDamage);
        return new SuccessResult(Messages.CARDAMAGEADD);
    }

    @Override
    public Result update(UpdateCarDamageRequest updateCarDamageRequest) {
        Result result = BusinessRules.run(checkIfCarIsExists(updateCarDamageRequest.getCarId()));
        if (result != null) {
            return result;
        }
        CarDamage carDamage = this.modelMapperService.forRequest().map(updateCarDamageRequest, CarDamage.class);

        this.carDamageDao.save(carDamage);
        return new SuccessResult(Messages.CARDAMAGEUPDATE);
    }

    @Override
    public Result delete(int id) {
        if (carDamageDao.existsById(id)) {
            carDamageDao.deleteById(id);
            return new SuccessResult(Messages.CARDAMAGEDELETE);
        } else
            return new ErrorResult(Messages.CARDAMAGENOTFOUND);
    }

    private Result checkIfCarIsExists(int carId) {
        if (!carService.findCarById(carId).isSuccess()) {
            return new ErrorResult(Messages.CARDAMAGENOTFOUND);
        } else
            return new SuccessResult();
    }

}
