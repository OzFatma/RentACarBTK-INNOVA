package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.AdditionalServiceItemService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.additionalServiceItemRequests.CreateAdditionalServiceItemRequest;
import com.btkAkademi.rentACar.business.requests.additionalServiceItemRequests.UpdateAdditionalServiceItemRequest;
import com.btkAkademi.rentACar.business.responses.AdditionalServiceItemListResponse;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.AdditionalServiceItemDao;
import com.btkAkademi.rentACar.entities.concretes.AdditionalServiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdditionalServiceItemManager implements AdditionalServiceItemService {

    private final ModelMapperService modelMapperService;
    private final AdditionalServiceItemDao additionalServiceItemDao;

    @Autowired
    public AdditionalServiceItemManager(
            ModelMapperService modelMapperService,
            AdditionalServiceItemDao additionalServiceItemDao) {
        super();
        this.modelMapperService = modelMapperService;
        this.additionalServiceItemDao = additionalServiceItemDao;
    }

    // Defines Additional Service element To System
    @Override
    public Result add(CreateAdditionalServiceItemRequest createAdditionalServiceItemRequest) {
        AdditionalServiceItem additionalServiceItem = this.modelMapperService.forRequest()
                .map(createAdditionalServiceItemRequest, AdditionalServiceItem.class);
        this.additionalServiceItemDao.save(additionalServiceItem);
        return new SuccessResult(Messages.ADDITIONALSERVICEITEMADDED);
    }

    @Override
    public DataResult<AdditionalServiceItemListResponse> findById(int id) {
        var additionalServiceItem = additionalServiceItemDao.existsById(id);
        if (additionalServiceItem) {
            var item = additionalServiceItemDao.findById(id);//todo: .get()
            AdditionalServiceItemListResponse response = modelMapperService.forResponse().map(item,
                    AdditionalServiceItemListResponse.class);
            return new SuccessDataResult<>(response, Messages.ADDITIONALSERVICEITEMLIST);
        } else
            return new ErrorDataResult<>(Messages.ADDITIONALSERVICEITEMNOTFOUND);

    }

    @Override
    public Result update(UpdateAdditionalServiceItemRequest updateAdditionalServiceItemRequest) {
        AdditionalServiceItem additionalServiceItem = this.modelMapperService.forRequest()
                .map(updateAdditionalServiceItemRequest, AdditionalServiceItem.class);
        this.additionalServiceItemDao.save(additionalServiceItem);
        return new SuccessResult(Messages.ADDITIONALSERVICEITEMUPDATED);
    }

    @Override
    public Result delete(int id) {
        var additionalServiceItem = additionalServiceItemDao.existsById(id);
        if (additionalServiceItem) {
            additionalServiceItemDao.deleteById(id);
            return new SuccessResult(Messages.ADDITIONALSERVICEITEMDELETED);
        } else
            return new ErrorResult(Messages.ADDITIONALSERVICEITEMNOTFOUND);
    }

    @Override
    public DataResult<List<AdditionalServiceItemListResponse>> findAll() {
        List<AdditionalServiceItem> additionalServiceItems = this.additionalServiceItemDao.findAll();
        List<AdditionalServiceItemListResponse> response = additionalServiceItems.stream()
                .map(additionalServiceItem -> modelMapperService.forResponse().map(additionalServiceItem,
                        AdditionalServiceItemListResponse.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(response, Messages.ADDITIONALSERVICEITEMLIST);
    }

}
