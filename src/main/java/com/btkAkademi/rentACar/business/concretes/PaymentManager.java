package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.*;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.paymentRequests.CalculateTotalPriceRequest;
import com.btkAkademi.rentACar.business.requests.paymentRequests.CreatePaymentRequest;
import com.btkAkademi.rentACar.business.requests.paymentRequests.UpdatePaymentRequest;
import com.btkAkademi.rentACar.business.responses.AdditionalServiceListResponse;
import com.btkAkademi.rentACar.business.responses.PaymentListResponse;
import com.btkAkademi.rentACar.business.responses.PromoCodeResponse;
import com.btkAkademi.rentACar.business.responses.RentalListResponse;
import com.btkAkademi.rentACar.core.adapters.banks.abstracts.BankAdapterService;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.PaymentDao;
import com.btkAkademi.rentACar.entities.concretes.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentManager implements PaymentService {

    private final PaymentDao paymentDao;
    private final ModelMapperService modelMapperService;
    private final RentalService rentalService;
    private final CarService carService;
    private final AdditionalServiceService additionalServiceService;
    private final BankAdapterService bankAdapterService;
    private final PromoCodeService promoCodeService;
    private final AdditionalServiceItemService additionalServiceItemService;

    @Autowired
    public PaymentManager(PaymentDao paymentDao, ModelMapperService modelMapperService, @Lazy RentalService rentalService, CarService carService, AdditionalServiceService additionalServiceService, BankAdapterService bankAdapterService, PromoCodeService promoCodeService, AdditionalServiceItemService additionalServiceItemService) {
        super();
        this.paymentDao = paymentDao;
        this.modelMapperService = modelMapperService;
        this.rentalService = rentalService;
        this.carService = carService;
        this.additionalServiceService = additionalServiceService;
        this.bankAdapterService = bankAdapterService;
        this.promoCodeService = promoCodeService;
        this.additionalServiceItemService = additionalServiceItemService;
    }

    @Override
    public DataResult<List<PaymentListResponse>> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Payment> paymentList = this.paymentDao.findAll(pageable).getContent();
        List<PaymentListResponse> response = paymentList.stream()
                .map(payment -> modelMapperService.forResponse().map(payment, PaymentListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.PAYMENTLIST);

    }

    @Override
    public DataResult<List<PaymentListResponse>> findAllByRentalId(int id) {

        List<Payment> paymentList = this.paymentDao.getAllByRentalId(id);
        List<PaymentListResponse> response = paymentList.stream()
                .map(payment -> modelMapperService.forResponse().map(payment, PaymentListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.PAYMENTLIST);
    }

    @Override
    public DataResult<PaymentListResponse> findById(int id) {
        if (paymentDao.existsById(id)) {
            Payment payment = paymentDao.findById(id).get();
            PaymentListResponse response = modelMapperService.forResponse().map(payment, PaymentListResponse.class);
            return new SuccessDataResult<>(response, Messages.PAYMENTLIST);
        } else
            return new ErrorDataResult<>();
    }

    @Override
    public DataResult<Double> calculateTotalPriceForDisplay(CalculateTotalPriceRequest calculateTotalPriceRequest) {
        RentalListResponse rental = rentalService.findById(calculateTotalPriceRequest.getRentalId()).getData();
        System.out.println(rental.getRentedDate());

        Double price = this.totalPriceCalculator(rental, calculateTotalPriceRequest.getReturnDate());
        System.out.println(price);

        return new SuccessDataResult<>(price, Messages.TOTALPRICECALCULATE);
    }

    @Override
    public Result add(CreatePaymentRequest createPaymentRequest) {
        createPaymentRequest.setPaymentTime(LocalDate.now());
        Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
        int rentalId = createPaymentRequest.getRentalId();
        RentalListResponse rental = rentalService.findById(rentalId).getData();
        double totalPrice = totalPriceCalculator(rental, createPaymentRequest.getReturnDate());
        payment.setTotalPaymentAmount(totalPrice);

        Result result = BusinessRules.run(bankAdapterService.checkIfLimitIsEnough(createPaymentRequest.getCardNo(), createPaymentRequest.getYear(), createPaymentRequest.getMonth(), createPaymentRequest.getCvv(), totalPrice));
        if (result != null) {
            return result;
        }

        payment.setId(0);

        this.paymentDao.save(payment);

        return new SuccessResult(Messages.PAYMENTADD);
    }

    @Override
    public Result update(UpdatePaymentRequest updatePaymentRequest) {

        Payment payment = this.modelMapperService.forRequest().map(updatePaymentRequest, Payment.class);

        this.paymentDao.save(payment);

        return new SuccessResult(Messages.PAYMENTUPDATE);
    }

    @Override
    public Result delete(int id) {
        if (paymentDao.existsById(id)) {
            paymentDao.deleteById(id);
            return new SuccessResult(Messages.PAYMENTDELETE);
        }
        return new ErrorResult();
    }

    private double totalPriceCalculator(RentalListResponse rental, LocalDate returnDate) {

        double totalPrice = 0.0;
        System.out.println(rental.getRentedDate() + " " + returnDate);

        //period sınıfı hesaplamada hata verdi
        long days = ChronoUnit.DAYS.between(rental.getRentedDate(), returnDate);

        //araç kiralandıysa 1 günlük ücret sabit
        if (days == 0) days = 1;
        totalPrice += days * carService.findCarById(rental.getCarId()).getData().getDailyPrice();

        //promosyon kodu
        if (rental.getPromoCodeId() != 0) {
            PromoCodeResponse promoCode = promoCodeService.findById(rental.getPromoCodeId()).getData();
            if (!promoCode.getEndDate().isBefore(LocalDate.now())) {
                double discountRate = 0;
                discountRate = promoCode.getDiscountRate();
                totalPrice = totalPrice - (totalPrice * discountRate);
            }
        }
        // calculates total additional service price
        List<AdditionalServiceListResponse> services = additionalServiceService
                .findAllByRentalId(rental.getId()).getData();
        for (AdditionalServiceListResponse additionalService : services) {
            double additionalServiceItemPrice = additionalServiceItemService
                    .findById(additionalService.getAdditionalServiceItemId()).getData().getPrice();
            totalPrice += additionalServiceItemPrice;
        }
        return totalPrice;
    }
}
