package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.BrandService;
import com.btkAkademi.rentACar.business.constants.Constraints;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.brandRequests.CreateBrandRequest;
import com.btkAkademi.rentACar.business.requests.brandRequests.UpdateBrandRequest;
import com.btkAkademi.rentACar.business.responses.BrandListResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.BrandDao;
import com.btkAkademi.rentACar.entities.concretes.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandManager implements BrandService {

    private final BrandDao brandDao;
    private final ModelMapperService modelMapperService;

    @Autowired
    public BrandManager(BrandDao brandDao, ModelMapperService modelMapperService) {
        this.brandDao = brandDao;
        this.modelMapperService = modelMapperService;

    }

    @Override
    public DataResult<List<BrandListResponse>> findAll() {
        List<Brand> brandList = this.brandDao.findAll();
        List<BrandListResponse> response = brandList.stream()
                .map(brand -> modelMapperService.forResponse()
                        .map(brand, BrandListResponse.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(response, Messages.BRANDLIST);
    }

    @Override
    public DataResult<BrandListResponse> findById(int id) {
        if (brandDao.existsById(id)) {
            var brand=brandDao.findById(id);
            BrandListResponse response = modelMapperService.forResponse().map(brand, BrandListResponse.class);
            return new SuccessDataResult<>(response, Messages.BRANDLIST);
        } else
            return new ErrorDataResult<>(Messages.BRANDNOTFOUND);
    }

    @Override
    public Result add(CreateBrandRequest createBrandRequest) {

        Result result = BusinessRules.run(
                checkIfBrandNameExists(createBrandRequest.getName()),
                checkIfBrandLimitExceeded()
        );
        if (result != null) {

            return result;
        }

        Brand brand = this.modelMapperService.forRequest().map(createBrandRequest, Brand.class);
        this.brandDao.save(brand);

        return new SuccessResult(Messages.BRANDADD);
    }

    @Override
    public Result update(UpdateBrandRequest updateBrandRequest) {
        Result result = BusinessRules.run(
                checkIfBrandNameExists(updateBrandRequest.getName()),
                checkIfBrandIdExists(updateBrandRequest.getId())
        );

        if (result != null) {

            return result;
        }

        Brand brand = this.modelMapperService.forRequest().map(updateBrandRequest, Brand.class);
        this.brandDao.save(brand);

        return new SuccessResult(Messages.BRANDUPDATE);
    }

    @Override
    public Result delete(int id) {
        var brand = brandDao.existsById(id);
        if (brand) {
            brandDao.deleteById(id);
            return new SuccessResult(Messages.BRANDDELETE);
        } else
            return new ErrorResult(Messages.BRANDNOTFOUND);

    }

    private Result checkIfBrandNameExists(String brandName) {

        Brand brand = this.brandDao.findByName(brandName);

        if (brand != null) {
            return new ErrorResult(Messages.BRANDNAMEEXISTS);
        }
        return new SuccessResult();

    }

    private Result checkIfBrandLimitExceeded() { //parametre int limit, >=limit
        if (this.brandDao.count() >= Constraints.BRANDLIMIT) {

            return new ErrorResult(Messages.BRANDLIMITEXCEED);
        }
        return new SuccessResult();
    }

    // Checks is there a brand with that id
    private Result checkIfBrandIdExists(int id) {

        if (!this.brandDao.existsById(id)) {
            return new ErrorResult(Messages.BRANDNOTFOUND);

        }

        return new SuccessResult();
    }

}
