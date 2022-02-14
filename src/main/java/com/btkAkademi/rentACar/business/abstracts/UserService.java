package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.responses.UserListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.entities.concretes.User;

import java.util.List;

public interface UserService {

    DataResult<User> findByEmail(String Email);

    DataResult<UserListResponse> findById(int id);

    DataResult<List<UserListResponse>> findAll();

    Result delete(int id);
}
