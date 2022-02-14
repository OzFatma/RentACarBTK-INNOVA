package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.promoCodeRequests.CreatePromoCodeRequest;
import com.btkAkademi.rentACar.business.requests.promoCodeRequests.UpdatePromoCodeRequest;
import com.btkAkademi.rentACar.business.responses.PromoCodeResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface PromoCodeService {

    DataResult<List<PromoCodeResponse>> findAll();

    DataResult<PromoCodeResponse> findById(int promoCodeId);

    DataResult<PromoCodeResponse> findByCode(String promoCode);

    Result add(CreatePromoCodeRequest createPromoCodeRequest);

    Result update(UpdatePromoCodeRequest updatePromoCodeRequest);

    Result delete(int id);

}
