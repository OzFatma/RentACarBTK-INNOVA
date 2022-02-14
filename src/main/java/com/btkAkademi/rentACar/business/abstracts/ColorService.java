package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.colorRequests.CreateColorRequest;
import com.btkAkademi.rentACar.business.requests.colorRequests.UpdateColorRequest;
import com.btkAkademi.rentACar.business.responses.ColorListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface ColorService {

    DataResult<List<ColorListResponse>> findAll();

    DataResult<ColorListResponse> findById(int id);

    Result add(CreateColorRequest createColorRequest);

    Result update(UpdateColorRequest updateColorRequest);

    Result delete(int id);

}
