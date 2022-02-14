package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.BrandService;
import com.btkAkademi.rentACar.business.abstracts.CarService;
import com.btkAkademi.rentACar.business.abstracts.CityService;
import com.btkAkademi.rentACar.business.abstracts.ColorService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.carRequests.CreateCarRequest;
import com.btkAkademi.rentACar.business.requests.carRequests.UpdateCarRequest;
import com.btkAkademi.rentACar.business.responses.CarListResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.CarDao;
import com.btkAkademi.rentACar.entities.concretes.Car;
import com.btkAkademi.rentACar.entities.concretes.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarManager implements CarService {

    private final CarDao carDao;
    private final ModelMapperService modelMapperService;
    private final BrandService brandService;
    private final ColorService colorService;
    private final CityService cityService;

    @Autowired
    public CarManager(CarDao carDao, ModelMapperService modelMapperService, BrandService brandService,
                      ColorService colorService, CityService cityService) {
        super();
        this.carDao = carDao;
        this.modelMapperService = modelMapperService;
        this.brandService = brandService;
        this.colorService = colorService;
        this.cityService = cityService;
    }

    //Sayfalama
    @Override
    public DataResult<List<CarListResponse>> findAll(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Car> carList = this.carDao.findAll(pageable).getContent();
        List<CarListResponse> response = carList.stream().map(car -> modelMapperService.forResponse().map(car, CarListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.CARLIST);
    }

    @Override
    public DataResult<List<CarListResponse>> findAllAvailable(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Car> carList = this.carDao.findAvailableCars(pageable);
        List<CarListResponse> response = carList.stream().map(car -> modelMapperService.forResponse().map(car, CarListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.CARLIST);
    }

    @Override
    public DataResult<List<CarListResponse>> findAllByBrandId(int brandId, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Car> carList = this.carDao.findAllByBrandId(brandId, pageable);
        List<CarListResponse> response = carList.stream().map(car -> modelMapperService.forResponse().map(car, CarListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.CARLIST);
    }

    @Override
    public DataResult<List<CarListResponse>> findAllByColorId(int colorId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Car> carList = this.carDao.findAllByColorId(colorId, pageable);
        List<CarListResponse> response = carList.stream().map(car -> modelMapperService.forResponse().map(car, CarListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.CARLIST);
    }


    @Override
    public DataResult<List<CarListResponse>> findAllBySegmentId(int segmentId) {
        List<Car> cars = this.carDao.findAllBySegmentId(segmentId);
        List<CarListResponse> response = cars.stream().map(car -> modelMapperService.forResponse().map(car, CarListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.CARLIST);
    }

    @Override
    public DataResult<List<Integer>> findAvailableCarsBySegmentIdAndCityId(int segmentId, int cityId) {

        if (this.carDao.findAvailableCarBySegmentAndCity(segmentId, cityId).size() < 1) {
            return new ErrorDataResult<>();
        } else
            return new SuccessDataResult<List<Integer>>(this.carDao.findAvailableCarBySegmentAndCity(segmentId, cityId),
                    Messages.CARLIST);
    }

    @Override
    public DataResult<CarListResponse> findCarById(int id) {
        if (carDao.existsById(id)) {

            CarListResponse response = modelMapperService.forResponse().map(this.carDao.findById(id).get(),
                    CarListResponse.class);

            return new SuccessDataResult<CarListResponse>(response, Messages.CARLIST);
        } else
            return new ErrorDataResult<>(Messages.CARNOTFOUND);
    }

    @Override
    public Result add(CreateCarRequest createCarRequest) {
        Result result = BusinessRules.run(checkIfColorExist(createCarRequest.getColorId()),
                checkIfBrandExists(createCarRequest.getBrandId()));
        if (result != null) {
            return result;
        }
        Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);
        this.carDao.save(car);

        return new SuccessResult(Messages.CARADD);
    }

    @Override
    public Result update(UpdateCarRequest updateCarRequest) {

        Result result = BusinessRules.run(checkIfCarIdExists(updateCarRequest.getId()));

        if (result != null) {

            return result;
        }

        Car car = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);

        this.carDao.save(car);
        return new SuccessResult(Messages.CARUPDATE);
    }

    @Override
    public Result updateCarKilometer(int carId, int kilometer) {
        var car = carDao.findById(carId).get();
        car.setKilometer(kilometer);
        carDao.save(car);
        return new SuccessResult("success");

    }

    @Override
    public Result updateCarCity(int carId, int cityId) {
        Car car = carDao.findById(carId).get();
        City city = modelMapperService.forRequest().map(cityService.findById(cityId).getData(), City.class);
        car.setCity(city);
        carDao.save(car);
        return new SuccessResult("success");
    }

    @Override
    public Result delete(int id) {
        if (carDao.existsById(id)) {
            carDao.deleteById(id);
            return new SuccessResult(Messages.CARDELETE);
        }

        return new ErrorResult(Messages.CARNOTFOUND);
    }

    private Result checkIfCarIdExists(int id) {
        if (!this.carDao.existsById(id)) {
            return new ErrorResult(Messages.CARNOTFOUND);
        }
        return new SuccessResult();
    }

    private Result checkIfColorExist(int colorId) {
        if (!colorService.findById(colorId).isSuccess()) {
            return new ErrorResult(Messages.COLORNOTFOUND);
        } else
            return new SuccessResult();
    }

    private Result checkIfBrandExists(int brandId) {
        if (!brandService.findById(brandId).isSuccess()) {
            return new ErrorResult(Messages.BRANDNOTFOUND);
        } else
            return new SuccessResult();
    }
}
