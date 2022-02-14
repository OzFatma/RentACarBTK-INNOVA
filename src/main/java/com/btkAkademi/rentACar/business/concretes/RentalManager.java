package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.*;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.requests.rentalRequests.CreateRentalRequest;
import com.btkAkademi.rentACar.business.requests.rentalRequests.UpdateRentalRequest;
import com.btkAkademi.rentACar.business.responses.*;
import com.btkAkademi.rentACar.core.adapters.creditScore.abstracts.CreditScoreAdapterService;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.RentalDao;
import com.btkAkademi.rentACar.entities.concretes.City;
import com.btkAkademi.rentACar.entities.concretes.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalManager implements RentalService {

    private final RentalDao rentalDao;
    private final ModelMapperService modelMapperService;
    private final CustomerService customerService;
    private final CarMaintenanceService carMaintenanceService;
    private final CityService cityService;
    private final CreditScoreAdapterService creditScoreAdapterService;
    private final IndividualCustomerService individualCustomerService;
    private final CorporateCustomerService corporateCustomerService;
    private final CarService carService;
    private final PaymentService paymentService;
    private final InvoiceService invoiceService;

    @Autowired
    public RentalManager(RentalDao rentalDao, ModelMapperService modelMapperService, CustomerService customerService,
                         CarMaintenanceService carMaintenanceService, CityService cityService,
                         CreditScoreAdapterService creditScoreAdapterService, IndividualCustomerService individualCustomerService,
                         CorporateCustomerService corporateCustomerService, CarService carService, PaymentService paymentService,
                         InvoiceService invoiceService) {
        super();
        this.rentalDao = rentalDao;
        this.modelMapperService = modelMapperService;
        this.customerService = customerService;
        this.carMaintenanceService = carMaintenanceService;
        this.cityService = cityService;
        this.creditScoreAdapterService = creditScoreAdapterService;
        this.individualCustomerService = individualCustomerService;
        this.corporateCustomerService = corporateCustomerService;
        this.carService = carService;
        this.paymentService = paymentService;
        this.invoiceService = invoiceService;
    }

    @Override
    public DataResult<List<RentalListResponse>> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Rental> rentalList = this.rentalDao.findAll(pageable).getContent();
        List<RentalListResponse> response = rentalList.stream()
                .map(rental -> modelMapperService.forResponse()
                        .map(rental, RentalListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.RENTALLIST);
    }

    @Override
    public DataResult<List<MyRentalListResponse>> findAllByCustomerId(int id) {

        List<Rental> rentalList = this.rentalDao.findAllByCustomerId(id);
        List<MyRentalListResponse> response = new ArrayList<MyRentalListResponse>();
        for (Rental rental : rentalList) {

            MyRentalListResponse responseItem = new MyRentalListResponse();
            responseItem.setRentalId(rental.getId());
            responseItem.setRentDate(rental.getRentedDate());
            responseItem.setBrandName(carService.findCarById(rental.getCar().getId()).getData().getBrandName());
            responseItem.setCarName(carService.findCarById(rental.getCar().getId()).getData().getCarName());
            responseItem.setPickUpCityName(rental.getPickUpCity().getCityName());

            if (paymentService.findAllByRentalId(rental.getId()).isSuccess()) {
                double totalPrice = 0;
                List<PaymentListResponse> payments = paymentService.findAllByRentalId(rental.getId()).getData();
                for (PaymentListResponse payment : payments) {
                    totalPrice += payment.getTotalPaymentAmount();
                }
                responseItem.setTotalPayment(totalPrice);
                responseItem.setReturnDate(rental.getReturnedDate());
            }
            if (isRentalFinished(rental.getId())) {
                responseItem.setRentalFinished(true);
                responseItem.setReturnCityName(rental.getReturnCity().getCityName());

                if (invoiceService.getInvoiceForIndividualCustomer(rental.getId()).isSuccess()
                        || invoiceService.getInvoiceForCorporateCustomer(rental.getId()).isSuccess()) {

                    responseItem.setInvoiceCreated(true);
                }
            } else {
                responseItem.setRentalFinished(false);
                responseItem.setInvoiceCreated(false);
            }
            response.add(responseItem);
        }

        return new SuccessDataResult<>(response, Messages.RENTALLIST);
    }

    @Override
    public DataResult<RentalListResponse> findById(int id) {

        if (rentalDao.existsById(id)) {
            RentalListResponse response = modelMapperService.forResponse()
                    .map(rentalDao.findById(id).get(), RentalListResponse.class);

            return new SuccessDataResult<>(response, Messages.RENTALLIST);
        } else
            return new ErrorDataResult<>(Messages.RENTALNOTFOUND);

    }

    @Override
    public DataResult<RentalListResponse> findActiveRentalByCarId(int id) {
        if (rentalDao.findByCarIdAndReturnedDateIsNull(id) != null) {

            RentalListResponse response = modelMapperService.forResponse().map(rentalDao.findByCarIdAndReturnedDateIsNull(id),
                    RentalListResponse.class);
            return new SuccessDataResult<>(response, Messages.RENTALLIST);
        } else
            return new ErrorDataResult<>(Messages.RENTALNOTFOUND);
    }

    @Override
    public DataResult<RentalAddResponse> addForIndividualCustomer(CreateRentalRequest createRentalRequest) {
        CarListResponse wantedCar = carService.findCarById(createRentalRequest.getCarId()).getData();
        if (!checkIfIsCarInMaintenance(createRentalRequest.getCarId()).isSuccess()
                || !checkIfIsCarAlreadyRented(createRentalRequest.getCarId()).isSuccess()) {
            CarListResponse car = findAvailableCar(wantedCar.getSegmentId(), wantedCar.getCityId()).getData();
            if (car != null) {
                createRentalRequest.setCarId(car.getId());
            } else
                return new ErrorDataResult<>(Messages.NOAVAILABLECARINTHISSEGMENT);
        }
        Result result = BusinessRules.run(
                checkIfCustomerExist(createRentalRequest.getCustomerId()),
                checkIfIndividualCustomerHasEnoughCreditScore(
                        individualCustomerService.findById(createRentalRequest.getCustomerId()).getData()
                                .getCitizenShipNumber(),
                        carService.findCarById(createRentalRequest.getCarId()).getData().getFindexScore()),
                checkIfCustomerAgeIsEnough(createRentalRequest.getCustomerId(), createRentalRequest.getCarId()),
                checkIfDatesAreCorrectForAdd(createRentalRequest.getRentedDate(), createRentalRequest.getReturnedDate())

        );
        if (result != null) {
            return new ErrorDataResult<>(result.getMessage());
        }
        var car = carService.findCarById(createRentalRequest.getCarId()).getData();

        Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
        rental.setReturnedKilometer(null);
        rental.setRentedKilometer(car.getKilometer());
        City pickUpCity = modelMapperService.forRequest().map(cityService.findById(car.getCityId()).getData(),
                City.class);
        rental.setPickUpCity(pickUpCity);
        rental.setReturnedDate(null);
        this.rentalDao.save(rental);
        return new SuccessDataResult<RentalAddResponse>(new RentalAddResponse(rental.getId(), rental.getCar().getId()),
                Messages.RENTALADD);
    }

    @Override
    public DataResult<RentalAddResponse> addForCorporateCustomer(CreateRentalRequest createRentalRequest) {
        CarListResponse wantedCar = carService.findCarById(createRentalRequest.getCarId()).getData();
        if (!checkIfIsCarInMaintenance(createRentalRequest.getCarId()).isSuccess()
                || !checkIfIsCarAlreadyRented(createRentalRequest.getCarId()).isSuccess()) {
            CarListResponse car = findAvailableCar(wantedCar.getSegmentId(), wantedCar.getCityId()).getData();
            if (car != null) {
                createRentalRequest.setCarId(car.getId());

            } else
                return new ErrorDataResult<>(Messages.NOAVAILABLECARINTHISSEGMENT);
        }
        Result result = BusinessRules.run(checkIfCustomerExist(createRentalRequest.getCustomerId()),

                checkIfCorporateCustomerHasEnoughCreditScore(
                        corporateCustomerService.findById(createRentalRequest.getCustomerId()).getData().getTaxNumber(),
                        carService.findCarById(createRentalRequest.getCarId()).getData().getFindexScore()),
                checkIfDatesAreCorrectForAdd(createRentalRequest.getRentedDate(), createRentalRequest.getReturnedDate()));
        if (result != null) {
            return new ErrorDataResult<>(result.getMessage());
        }
        CarListResponse car = carService.findCarById(createRentalRequest.getCarId()).getData();
        Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
        rental.setReturnedKilometer(null);
        rental.setRentedKilometer(car.getKilometer());
        City pickUpCity = modelMapperService.forRequest().map(cityService.findById(car.getCityId()).getData(),
                City.class);
        rental.setPickUpCity(pickUpCity);
        rental.setReturnedDate(null);
        this.rentalDao.save(rental);
        return new SuccessDataResult<>(new RentalAddResponse(rental.getId(), rental.getCar().getId()),
                Messages.RENTALADD);
    }

    @Override
    public Result update(UpdateRentalRequest updateRentalRequest) {
        Rental rentalById = rentalDao.findById(updateRentalRequest.getId()).get();
        Result result = BusinessRules.run(checkIfCityExist(updateRentalRequest.getReturnCityId()),
                checkIfKilometersAreCorrect(rentalById.getRentedKilometer(),
                        updateRentalRequest.getReturnedKilometer()),
                checkIfDatesAreCorrectForUpdate(rentalById.getRentedDate(), updateRentalRequest.getReturnedDate()));
        if (result != null) {
            return result;
        }

        carService.updateCarCity(rentalById.getCar().getId(), updateRentalRequest.getReturnCityId());
        carService.updateCarKilometer(rentalById.getCar().getId(), updateRentalRequest.getReturnedKilometer());

        Rental rental = this.modelMapperService.forRequest().map(updateRentalRequest, Rental.class);

        rental.setRentedDate(rentalById.getRentedDate());
        rental.setCar(rentalById.getCar());
        rental.setRentedKilometer(rentalById.getRentedKilometer());
        rental.setCustomer(rentalById.getCustomer());
        rental.setPickUpCity(rentalById.getPickUpCity());
        rental.setPromoCode(rentalById.getPromoCode());
        this.rentalDao.save(rental);
        return new SuccessResult(Messages.RENTALUPDATE);
    }

    @Override
    public Result delete(int id) {
        if (rentalDao.existsById(id)) {
            rentalDao.deleteById(id);
            return new SuccessResult(Messages.RENTALDELETE);
        }
        return new ErrorResult(Messages.RENTALNOTFOUND);
    }

    @Override
    public boolean isCarRented(int carId) {
        if (rentalDao.findByCarIdAndReturnedDateIsNull(carId) != null) {
            return true;
        } else
            return false;
    }

    private Result checkIfDatesAreCorrectForAdd(LocalDate rentDate, LocalDate returnDate) {
        if (!rentDate.isBefore(returnDate) || rentDate.isBefore(LocalDate.now())) {
            return new ErrorResult(Messages.RENTALDATEERROR);
        }
        return new SuccessResult();
    }

    private Result checkIfDatesAreCorrectForUpdate(LocalDate rentDate, LocalDate returnDate) {
        if (!rentDate.isBefore(returnDate)) {
            return new ErrorResult(Messages.RENTALDATEERROR);
        }
        return new SuccessResult();
    }

    private Result checkIfKilometersAreCorrect(int rentedKilometer, int returnedKilometer) {
        if (rentedKilometer > returnedKilometer) {
            return new ErrorResult(Messages.KILOMETERERROR);
        }

        return new SuccessResult();
    }

    private Result checkIfCustomerExist(int customerId) {
        if (!customerService.findCustomerById(customerId).isSuccess()) {
            return new ErrorResult(Messages.CUSTOMERNOTFOUND);
        }

        return new SuccessResult();
    }

    private Result checkIfCityExist(int cityId) {
        if (!cityService.findById(cityId).isSuccess()) {
            return new ErrorResult(Messages.CITYNOTFOUND);
        }
        return new SuccessResult();
    }

    private Result checkIfIsCarInMaintenance(int carId) {
        if (carMaintenanceService.isCarInMaintenance(carId)) {
            return new ErrorResult(Messages.CARINMANTANANCE);
        }
        return new SuccessResult();
    }

    private Result checkIfIsCarAlreadyRented(int carId) {
        if (isCarRented(carId)) {
            return new ErrorResult(Messages.CARRENTED);
        }
        return new SuccessResult();
    }

    private Result checkIfIndividualCustomerHasEnoughCreditScore(String nationalityId, int minCreditScore) {
        System.out.println("min :" + minCreditScore);
        if (creditScoreAdapterService.getScoreOfIndividualCustomer(nationalityId).getData() >= minCreditScore) {
            return new SuccessResult();
        } else
            return new ErrorResult(Messages.RENTALFINDEXSCOREERROR);
    }

    private Result checkIfCorporateCustomerHasEnoughCreditScore(String taxNumber, int minCreditScore) {
        System.out.println("min :" + minCreditScore);
        if (creditScoreAdapterService.getScoreOfCorporateCustomer(taxNumber).getData() >= minCreditScore) {
            return new SuccessResult();
        } else
            return new ErrorResult(Messages.RENTALFINDEXSCOREERROR);

    }

    private Result checkIfCustomerAgeIsEnough(int customerId, int carId) {

        int age = Period
                .between(individualCustomerService.findById(customerId).getData().getBirthDate(), LocalDate.now())
                .getYears();
        int minAge = carService.findCarById(carId).getData().getMinAge();
        if (age < minAge) {
            return new ErrorResult(Messages.AGENOTENOUGH);
        }
        return new SuccessResult();
    }

    private DataResult<CarListResponse> findAvailableCar(int SegmentId, int cityId) {
        if (carService.findAvailableCarsBySegmentIdAndCityId(SegmentId, cityId).isSuccess()) {
            CarListResponse car = carService
                    .findCarById(carService.findAvailableCarsBySegmentIdAndCityId(SegmentId, cityId).getData().get(0))
                    .getData();
            return new SuccessDataResult<>(car);
        } else
            return new ErrorDataResult<>();
    }

    private boolean isRentalFinished(int rentalId) {
        if (this.findById(rentalId).getData() != null) {
            if (this.findById(rentalId).getData().getReturnedDate() != null) {
                return true;
            } else
                return false;
        } else
            return false;
    }
}
