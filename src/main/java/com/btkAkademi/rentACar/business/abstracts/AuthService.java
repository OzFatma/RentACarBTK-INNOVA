package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.LoginRequest;
import com.btkAkademi.rentACar.business.responses.LoginResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;

public interface AuthService {

    DataResult<LoginResponse> login(LoginRequest loginRequest);

}
