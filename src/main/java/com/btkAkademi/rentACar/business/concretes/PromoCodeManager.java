package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.PromoCodeService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.promoCodeRequests.CreatePromoCodeRequest;
import com.btkAkademi.rentACar.business.requests.promoCodeRequests.UpdatePromoCodeRequest;
import com.btkAkademi.rentACar.business.responses.PromoCodeResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.PromoCodeDao;
import com.btkAkademi.rentACar.entities.concretes.PromoCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromoCodeManager implements PromoCodeService {

    private final PromoCodeDao promoCodeDao;
    private final ModelMapperService modelMapperService;

    @Autowired
    public PromoCodeManager(PromoCodeDao promoCodeDao, ModelMapperService modelMapperService) {
        super();
        this.promoCodeDao = promoCodeDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<List<PromoCodeResponse>> findAll() {
        List<PromoCode> promoCodeList = this.promoCodeDao.findAll();
        List<PromoCodeResponse> response = promoCodeList.stream()
                .map(promoCode -> modelMapperService.forResponse()
                        .map(promoCode, PromoCodeResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.PROMOCODELIST);
    }

    @Override
    public DataResult<PromoCodeResponse> findById(int promoCodeId) {
        if (promoCodeDao.existsById(promoCodeId)) {
            PromoCode promoCode = promoCodeDao.findById(promoCodeId).get();
            PromoCodeResponse response = modelMapperService.forResponse().map(promoCode, PromoCodeResponse.class);
            return new SuccessDataResult<>(response, Messages.PROMOCODELIST);
        } else
            return new ErrorDataResult<>(Messages.PROMOCODENOTFOUND);

    }

    @Override
    public DataResult<PromoCodeResponse> findByCode(String code) {
        PromoCode promoCode = promoCodeDao.findByCode(code);
        if (promoCode == null) {
            return new ErrorDataResult<>(Messages.PROMOCODENOTFOUND);
        } else {
            PromoCodeResponse response = modelMapperService.forResponse().map(promoCode, PromoCodeResponse.class);

            Result result = BusinessRules.run(checkIfCodeStillValid(response));
            if (result != null) {
                return new ErrorDataResult<>(Messages.PROMOCODEEXPIRED);
            } else
                return new SuccessDataResult<>(response, Messages.PROMOCODELIST);
        }

    }

    @Override
    public Result add(CreatePromoCodeRequest createPromoCodeRequest) {
        Result result = BusinessRules.run(
                checkIfPromoCodeExistsByCode(createPromoCodeRequest.getCode()),
                checkIfDatesAreCorrect(createPromoCodeRequest.getStartDate(), createPromoCodeRequest.getEndDate()));
        if (result != null) {
            return result;
        }
        PromoCode promoCode = this.modelMapperService.forRequest().map(createPromoCodeRequest, PromoCode.class);
        promoCode.setId(0);
        this.promoCodeDao.save(promoCode);
        return new SuccessResult(Messages.PROMOCODEADD);

    }

    @Override
    public Result update(UpdatePromoCodeRequest updatePromoCodeRequest) {
        Result result = BusinessRules.run(
                checkIfDatesAreCorrect(updatePromoCodeRequest.getStartDate(), updatePromoCodeRequest.getEndDate()));
        if (result != null) {
            return result;
        }
        PromoCode promoCode = this.modelMapperService.forRequest().map(updatePromoCodeRequest, PromoCode.class);
        this.promoCodeDao.save(promoCode);
        return new SuccessResult(Messages.PROMOCODEUPDATE);
    }

    @Override
    public Result delete(int id) {
        if (promoCodeDao.existsById(id)) {
            promoCodeDao.deleteById(id);
            return new SuccessResult(Messages.PROMOCODEDELETE);
        } else
            return new ErrorResult();
    }

    private Result checkIfPromoCodeExistsByCode(String code) {
        if (promoCodeDao.findByCode(code) != null) {
            return new ErrorResult(Messages.PROMOCODEALREADYEXISTS);
        } else
            return new SuccessResult();
    }

    private Result checkIfDatesAreCorrect(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            return new ErrorResult(Messages.DATESARENOTCORRECT);
        } else
            return new SuccessResult();
    }

    private Result checkIfCodeStillValid(PromoCodeResponse code) {
        if (code.getEndDate().isBefore(LocalDate.now())) {
            return new ErrorResult(Messages.PROMOCODEEXPIRED);
        } else
            return new SuccessResult();
    }
}
