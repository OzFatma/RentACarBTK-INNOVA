package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.UserService;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.responses.UserListResponse;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.UserDao;
import com.btkAkademi.rentACar.entities.concretes.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManager implements UserService {

    private final UserDao userDao;
    private final ModelMapperService modelMapperService;

    @Autowired
    public UserManager(UserDao userDao, ModelMapperService modelMapperService) {
        super();
        this.userDao = userDao;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DataResult<UserListResponse> findById(int id) {
        if (userDao.existsById(id)) {
            UserListResponse response = modelMapperService.forResponse()
                    .map(this.userDao.findById(id).get(), UserListResponse.class);
            return new SuccessDataResult<>(response, Messages.USERLIST);
        } else return new ErrorDataResult<>();
    }

    @Override
    public DataResult<User> findByEmail(String Email) {

        return new SuccessDataResult<>(userDao.findByEmail(Email), Messages.USERFOUND);
    }

    @Override
    public DataResult<List<UserListResponse>> findAll() {
        List<User> userList = this.userDao.findAll();
        List<UserListResponse> response = userList.stream()
                .map(user -> modelMapperService.forResponse()
                        .map(user, UserListResponse.class)).collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.USERLIST);
    }

    @Override
    public Result delete(int id) {
        if (userDao.existsById(id)) {
            userDao.deleteById(id);
            return new SuccessResult(Messages.CUSTOMERDELETE);
        } else
            return new ErrorResult(Messages.NOTFOUND);
    }
}
