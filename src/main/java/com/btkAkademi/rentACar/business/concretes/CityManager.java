package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.CityService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.cityRequests.CreateCityRequest;
import com.btkAkademi.rentACar.business.requests.cityRequests.UpdateCityRequest;
import com.btkAkademi.rentACar.business.responses.CityListResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.CityDao;
import com.btkAkademi.rentACar.entities.concretes.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityManager implements CityService {

    private final CityDao cityDao;
    private final ModelMapperService modelMapperService;

    @Autowired
    public CityManager(CityDao cityDao, ModelMapperService modelMapperService) {
        super();
        this.cityDao = cityDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<CityListResponse>> findAll() {

        List<City> cityList = this.cityDao.findAllByOrderByCityName();
        List<CityListResponse> response = cityList.stream()
                .map(city -> modelMapperService.forResponse().map(city, CityListResponse.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(response, Messages.CITYLIST);
    }

    @Override
    public DataResult<CityListResponse> findById(int id) {
        Result result = BusinessRules.run(
                checkIfCityIdExist(id)
        );
        if (result != null) {
            return new ErrorDataResult<>(result.getMessage());
        }
        City city = cityDao.findById(id).get();
        CityListResponse response = modelMapperService.forResponse().map(city, CityListResponse.class);
        return new SuccessDataResult<>(response, Messages.CITYLIST);
    }

    @Override
    public Result add(CreateCityRequest createCityRequest) {
        Result result = BusinessRules.run(
                checkIfCityNameExists(createCityRequest.getCityName())
        );
        if (result != null) {
            return result;
        }
        City city = this.modelMapperService.forRequest().map(createCityRequest, City.class);
        this.cityDao.save(city);
        return new SuccessResult(Messages.CITYADD);
    }

    @Override
    public Result update(UpdateCityRequest updateCityRequest) {

        City city = this.modelMapperService.forRequest().map(updateCityRequest, City.class);
        this.cityDao.save(city);
        return new SuccessResult(Messages.CITYUPDATE);
    }

    @Override
    public Result delete(int id) {
        Result result = BusinessRules.run(
                checkIfCityIdExist(id)
        );
        if (result != null) {
            return new ErrorDataResult<CityListResponse>(result.getMessage());
        }

        cityDao.deleteById(id);
        return new SuccessResult(Messages.CITYDELETE);

    }

    private Result checkIfCityNameExists(String cityName) {
        if (cityDao.findByCityName(cityName) != null) {
            return new ErrorResult(Messages.CITYNAMEEXISTS);
        }
        return new SuccessResult();
    }

    private Result checkIfCityIdExist(int id) {
        if (!cityDao.existsById(id)) {
            return new ErrorResult(Messages.CITYNOTFOUND);
        }
        return new SuccessResult();
    }
}
