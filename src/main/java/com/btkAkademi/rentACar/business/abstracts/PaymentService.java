package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.paymentRequests.CalculateTotalPriceRequest;
import com.btkAkademi.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import com.btkAkademi.rentACar.business.requests.paymentRequests.UpdatePaymentRequest;
import com.btkAkademi.rentACar.business.responses.PaymentListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface PaymentService {

    DataResult<List<PaymentListResponse>> findAll(int pageNo, int pageSize);

    DataResult<List<PaymentListResponse>> findAllByRentalId(int id);

    DataResult<Double> calculateTotalPriceForDisplay(CalculateTotalPriceRequest calculateTotalPriceRequest);

    DataResult<PaymentListResponse> findById(int id);

    Result add(CreatePaymentRequest createPaymentRequest);

    Result update(UpdatePaymentRequest updatePaymentRequest);

    Result delete(int id);

}
