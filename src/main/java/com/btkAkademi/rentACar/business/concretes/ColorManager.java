package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.ColorService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.colorRequests.CreateColorRequest;
import com.btkAkademi.rentACar.business.requests.colorRequests.UpdateColorRequest;
import com.btkAkademi.rentACar.business.responses.ColorListResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.ColorDao;
import com.btkAkademi.rentACar.entities.concretes.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorManager implements ColorService {

    private final ColorDao colorDao;
    private final ModelMapperService modelMapperService;

    @Autowired
    public ColorManager(ColorDao colorDao, ModelMapperService modelMapperService) {

        this.colorDao = colorDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<ColorListResponse>> findAll() {
        List<Color> colorList = this.colorDao.findAll();
        List<ColorListResponse> response = colorList.stream()
                .map(color -> modelMapperService.forResponse().map(color, ColorListResponse.class)).collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.COLORLIST);
    }

    @Override
    public DataResult<ColorListResponse> findById(int id) {
        if (colorDao.existsById(id)) {
            Color color = colorDao.findById(id).get();
            ColorListResponse response = modelMapperService.forResponse().map(color, ColorListResponse.class);
            return new SuccessDataResult<>(response, Messages.COLORLIST);
        } else
            return new ErrorDataResult<>(Messages.COLORNOTFOUND);
    }

    @Override
    public Result add(CreateColorRequest createColorRequest) {
        Result result = BusinessRules.run(
                checkIfColorNameExists(createColorRequest.getName())
        );

        if (result != null) {

            return result;
        }

        Color color = this.modelMapperService.forRequest().map(createColorRequest, Color.class);
        this.colorDao.save(color);

        return new SuccessResult(Messages.COLORADD);
    }

    @Override
    public Result update(UpdateColorRequest updateColorRequest) {
        Result result = BusinessRules.run(
                checkIfColorNameExists(updateColorRequest.getName()),
                checkIfColorIdExists(updateColorRequest.getId()));

        if (result != null) {

            return result;
        }

        Color color = this.modelMapperService.forRequest().map(updateColorRequest, Color.class);
        this.colorDao.save(color);

        return new SuccessResult(Messages.COLORUPDATE);
    }

    @Override
    public Result delete(int id) {
        if (colorDao.existsById(id)) {
            colorDao.deleteById(id);
            return new SuccessResult(Messages.COLORDELETE);
        } else
            return new ErrorResult(Messages.COLORNOTFOUND);

    }

    private Result checkIfColorIdExists(int id) {
        if (!this.colorDao.existsById(id)) {

            return new ErrorResult(Messages.COLORNOTFOUND);
        }
        return new SuccessResult();
    }

    private Result checkIfColorNameExists(String colorname) {

        Color color = this.colorDao.findByName(colorname);

        if (color != null) {
            return new ErrorResult(Messages.COLORNAMEEXISTS);
        }
        return new SuccessResult();
    }
}
