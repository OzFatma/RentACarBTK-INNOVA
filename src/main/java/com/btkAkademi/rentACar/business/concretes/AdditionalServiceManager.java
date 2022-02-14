package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.AdditionalServiceService;
import com.btkAkademi.rentACar.business.abstracts.RentalService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.btkAkademi.rentACar.business.requests.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.btkAkademi.rentACar.business.responses.AdditionalServiceListResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.AdditionalServiceDao;
import com.btkAkademi.rentACar.entities.concretes.AdditionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdditionalServiceManager implements AdditionalServiceService {

    private final AdditionalServiceDao additionalServiceDao;
    private final ModelMapperService modelMapperService;
    private final RentalService rentalService;

    @Autowired
    public AdditionalServiceManager(AdditionalServiceDao additionalServiceDao,
                                    ModelMapperService modelMapperService,
                                    @Lazy RentalService rentalService) {
        super();
        this.additionalServiceDao = additionalServiceDao;
        this.modelMapperService = modelMapperService;
        this.rentalService = rentalService;
    }

    @Override
    public DataResult<List<AdditionalServiceListResponse>> findAllByRentalId(int RentalId) {
        List<AdditionalService> additionalServiceList = this.additionalServiceDao.findAllByRentalId(RentalId);
        List<AdditionalServiceListResponse> response = additionalServiceList.stream()
                .map(additionalService -> modelMapperService.forResponse().map(additionalService, AdditionalServiceListResponse.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(response, Messages.ADDITIONALSERVICELIST);
    }

    @Override
    public Result add(CreateAdditionalServiceRequest createAdditionalService) {
        Result result = BusinessRules.run(
                checkIfRentalExists(createAdditionalService.getRentalId())
        );
        if (result != null) {
            return result;
        }

        AdditionalService additionalService = this.modelMapperService.forRequest().map(createAdditionalService,
                AdditionalService.class);
        // avoid error
        additionalService.setId(0);
        this.additionalServiceDao.save(additionalService);
        return new SuccessResult(Messages.ADDITIONALSERVICEADD);
    }

    @Override
    public Result addAll(List<CreateAdditionalServiceRequest> createAdditionalServiceRequests) {
        Result result = BusinessRules.run(
                checkIfRentalExists(createAdditionalServiceRequests.get(0).getRentalId())
        );
        if (result != null) {
            return result;
        }

        List<AdditionalService> response = createAdditionalServiceRequests.stream()
                .map(service -> modelMapperService.forRequest().map(service, AdditionalService.class))
                .collect(Collectors.toList());
        for (AdditionalService service : response) {
            service.setId(0);
        }
        this.additionalServiceDao.saveAll(response);
        return new SuccessResult(Messages.ADDITIONALSERVICEADD);
    }

    @Override
    public Result update(UpdateAdditionalServiceRequest updateAdditionalServiceRequest) {
        Result result = BusinessRules.run(
                checkIfRentalExists(updateAdditionalServiceRequest.getRentalId())
        );

        if (result != null) {
            return result;
        }
        AdditionalService additionalService = modelMapperService.forRequest().map(updateAdditionalServiceRequest,
                AdditionalService.class);
        additionalServiceDao.save(additionalService);
        return new SuccessResult(Messages.ADDITIONALSERVICEUPDATE);
    }

    @Override
    public Result delete(int id) {
        var additionalService = additionalServiceDao.existsById(id);
        if (additionalService) {
            additionalServiceDao.deleteById(id);
            return new SuccessResult(Messages.ADDITIONALSERVICEDELETE);
        } else
            return new ErrorResult(Messages.ADDITIONALSERVICENOTFOUND);
    }

    private Result checkIfRentalExists(int rentalId) {
        if (!rentalService.findById(rentalId).isSuccess()) {
            return new ErrorResult(Messages.RENTALNOTFOUND);
        } else
            return new SuccessResult();
    }

}
