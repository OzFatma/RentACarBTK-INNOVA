package com.btkAkademi.rentACar.core.adapters.banks.conctrates;

import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.core.adapters.banks.abstracts.BankAdapterService;
import com.btkAkademi.rentACar.core.externalServices.banks.IsBank;
import com.btkAkademi.rentACar.core.utilities.results.ErrorResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import com.btkAkademi.rentACar.core.utilities.results.SuccessResult;
import org.springframework.stereotype.Service;

@Service
public class BankAdapterManager implements BankAdapterService {

    @Override
    public Result checkIfLimitIsEnough(String cardNo, String day, String month, String cvv, double amount) {
        IsBank isBank = new IsBank();
        if (isBank.isLimitExists(cardNo, day, month, cvv, amount)) {
            return new SuccessResult();
        } else {
            return new ErrorResult(Messages.NOTFOUND);
        }

    }

}
