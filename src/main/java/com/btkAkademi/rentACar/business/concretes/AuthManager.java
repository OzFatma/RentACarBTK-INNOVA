package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.AuthService;
import com.btkAkademi.rentACar.business.abstracts.UserService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.LoginRequest;
import com.btkAkademi.rentACar.business.responses.LoginResponse;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.entities.concretes.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthManager implements AuthService {

    private final UserService userService;

    @Autowired
    public AuthManager(UserService userService) {
        super();
        this.userService = userService;
    }

    @Override
    public DataResult<LoginResponse> login(LoginRequest loginRequest) {
        Result result = BusinessRules.run(
                checkIfUserExistsByEmail(loginRequest.getEmail()),
                checkIfUsersPasswordIsCorrect(loginRequest.getEmail(), loginRequest.getPassword())
        );

        if (result != null) {
            return new ErrorDataResult<>(result.getMessage());
        }

        User user = userService.findByEmail(loginRequest.getEmail()).getData();
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return new SuccessDataResult<>(response, Messages.LOGINSUCCESS);

    }

    private Result checkIfUserExistsByEmail(String email) {
        if (userService.findByEmail(email).getData() == null) {
            return new ErrorResult(Messages.USERNOTFOUND);
        } else
            return new SuccessResult();
    }

    private Result checkIfUsersPasswordIsCorrect(String email, String password) {
        User user = userService.findByEmail(email).getData();
        if (user != null && !password.equals(user.getPassword())) {
            return new ErrorResult(Messages.LOGINPASSWORDERROR);
        } else
            return new SuccessResult();

    }
}
