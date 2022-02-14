package com.btkAkademi.rentACar.business.concretes;

import com.btkAkademi.rentACar.business.abstracts.*;
import com.btkAkademi.rentACar.business.constants.Messages;
import com.btkAkademi.rentACar.business.constants.Role;
import com.btkAkademi.rentACar.business.requests.invoiceRequests.CreateInvoiceRequest;
import com.btkAkademi.rentACar.business.requests.invoiceRequests.UpdateInvoiceRequest;
import com.btkAkademi.rentACar.business.responses.*;
import com.btkAkademi.rentACar.core.utilities.business.BusinessRules;
import com.btkAkademi.rentACar.core.utilities.mapping.ModelMapperService;
import com.btkAkademi.rentACar.core.utilities.results.*;
import com.btkAkademi.rentACar.dataAccess.abstracts.InvoiceDao;
import com.btkAkademi.rentACar.entities.concretes.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceManager implements InvoiceService {

    private final InvoiceDao invoiceDao;
    private final ModelMapperService modelMapperService;
    private final IndividualCustomerService individualCustomerService;
    private final CorporateCustomerService corporateCustomerService;
    private final RentalService rentalService;
    private final PaymentService paymentService;
    private final AdditionalServiceService additionalServiceService;
    private final AdditionalServiceItemService additionalServiceItemService;
    private final UserService userService;

    @Autowired
    public InvoiceManager(InvoiceDao invoiceDao, ModelMapperService modelMapperService,
                          IndividualCustomerService individualCustomerService, CorporateCustomerService corporateCustomerService,
                          @Lazy RentalService rentalService, PaymentService paymentService,
                          AdditionalServiceService additionalServiceService,
                          AdditionalServiceItemService additionalServiceItemService, UserService userService) {
        super();
        this.invoiceDao = invoiceDao;
        this.modelMapperService = modelMapperService;
        this.individualCustomerService = individualCustomerService;
        this.corporateCustomerService = corporateCustomerService;
        this.rentalService = rentalService;
        this.paymentService = paymentService;
        this.additionalServiceService = additionalServiceService;
        this.additionalServiceItemService = additionalServiceItemService;
        this.userService = userService;
    }

    @Override
    public DataResult<InvoiceIndividualCustomerResponse> getInvoiceForIndividualCustomer(int rentalId) {
        Result result = BusinessRules.run(
                checkIfRentalIsFinished(rentalId),
                checkIfPaymentIsMade(rentalId),
                checkIfInvoiceExistsByRentalId(rentalId),
                checkIfCustomerIndividual(rentalId)
        );
        if (result != null) {
            return new ErrorDataResult<>(result.getMessage());
        }
        Invoice invoice = invoiceDao.findByRentalId(rentalId);
        RentalListResponse rental = rentalService.findById(rentalId).getData();
        IndividualCustomerListResponse customer = individualCustomerService.findById(rental.getCustomerId()).getData();
        //List<AdditionalServiceListResponse> additionalServices = additionalServiceService.findAllByRentalId(rentalId)
        //.getData(); duplicate uyarısı veriyor.
        List<AdditionalServiceItemListResponse> additionalServiceItems = new ArrayList<AdditionalServiceItemListResponse>();
        double itemPrices = 0;
        for (AdditionalServiceListResponse additionalServiceListResponse : additionalServiceService.findAllByRentalId(rentalId)
                .getData()) {
            itemPrices += additionalServiceItemService.findById(additionalServiceListResponse.getAdditionalServiceItemId())
                    .getData().getPrice();
            additionalServiceItems.add(additionalServiceItemService
                    .findById(additionalServiceListResponse.getAdditionalServiceItemId()).getData());
        }

        List<PaymentListResponse> payments = paymentService.findAllByRentalId(rentalId).getData();
        double totalPrice = 0;
        for (PaymentListResponse payment : payments) {
            totalPrice += payment.getTotalPaymentAmount();
        }
        double rentPrice = totalPrice - itemPrices;

        //builder pattern kullandık
        InvoiceIndividualCustomerResponse responseCustomerDto = InvoiceIndividualCustomerResponse
                .builder()
                .id(invoice.getId()).firstName(customer.getFirstName()).lastName(customer.getLastName())
                .citizenShipNumber(customer.getCitizenShipNumber()).email(customer.getEmail()).totalPrice(totalPrice)
                .rentDate(rental.getRentedDate()).returnedDate(rental.getReturnedDate())
                .creationDate(invoice.getCreationDate()).additionalServiceItems(additionalServiceItems)
                .rentPrice(rentPrice).build();
        return new SuccessDataResult<>(responseCustomerDto, Messages.INVOICELIST);
    }

    @Override
    public DataResult<InvoiceCorporateCustomerResponse> getInvoiceForCorporateCustomer(int rentalId) {
        Result result = BusinessRules.run(
                checkIfRentalIsFinished(rentalId), checkIfPaymentIsMade(rentalId),
                checkIfInvoiceExistsByRentalId(rentalId), checkIfCustomerCorporate(rentalId)
        );
        if (result != null) {
            return new ErrorDataResult<>(result.getMessage());
        }
        Invoice invoice = invoiceDao.findByRentalId(rentalId);
        RentalListResponse rental = rentalService.findById(rentalId).getData();
        CorporateCustomerListResponse customer = corporateCustomerService.findById(rental.getCustomerId()).getData();
        var additionalServices = additionalServiceService.findAllByRentalId(rentalId)
                .getData();
        List<AdditionalServiceItemListResponse> additionalServiceItems = new ArrayList<AdditionalServiceItemListResponse>();
        double itemPrices = 0;
        for (AdditionalServiceListResponse additionalServiceListResponse : additionalServices) {
            itemPrices += additionalServiceItemService.findById(additionalServiceListResponse.getAdditionalServiceItemId())
                    .getData().getPrice();
            additionalServiceItems.add(additionalServiceItemService
                    .findById(additionalServiceListResponse.getAdditionalServiceItemId()).getData());
        }

        List<PaymentListResponse> payments = paymentService.findAllByRentalId(rentalId).getData();
        double totalPrice = 0;
        for (PaymentListResponse payment : payments) {
            totalPrice += payment.getTotalPaymentAmount();
        }
        double rentPrice = totalPrice - itemPrices;

        InvoiceCorporateCustomerResponse responseCustomerDto = InvoiceCorporateCustomerResponse
                .builder()
                .id(invoice.getId()).companyName(customer.getCompanyName()).taxNumber(customer.getTaxNumber())
                .email(customer.getEmail()).totalPrice(totalPrice).rentDate(rental.getRentedDate())
                .returnedDate(rental.getReturnedDate()).creationDate(invoice.getCreationDate())
                .additionalServiceItems(additionalServiceItems).rentPrice(rentPrice).build();
        return new SuccessDataResult<>(responseCustomerDto, Messages.INVOICELIST);
    }

    @Override
    public DataResult<List<InvoiceListResponse>> findAll() {
        List<Invoice> invoiceList = invoiceDao.findAll();
        List<InvoiceListResponse> response = invoiceList.stream().map(invoice ->
                        modelMapperService.forResponse().map(invoice, InvoiceListResponse.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(response, Messages.INVOICELIST);
    }

    @Override
    public Result add(CreateInvoiceRequest createInvoiceRequest) {
        Result result = BusinessRules.run(
                checkIfInvoiceAlreadyExists(createInvoiceRequest.getRentalId()));
        if (result != null) {
            return result;
        }
        Invoice invoice = this.modelMapperService.forRequest().map(createInvoiceRequest, Invoice.class);
        invoice.setCreationDate(LocalDate.now());
        this.invoiceDao.save(invoice);
        return new SuccessResult(Messages.INVOICEADD);
    }

    @Override
    public Result update(UpdateInvoiceRequest updateInvoiceRequest) {
        Invoice invoice = this.modelMapperService.forRequest().map(updateInvoiceRequest, Invoice.class);
        this.invoiceDao.save(invoice);
        return new SuccessResult(Messages.INVOICEUPDATE);
    }

    @Override
    public Result delete(int id) {
        if (invoiceDao.existsById(id)) {
            invoiceDao.deleteById(id);
            return new SuccessResult(Messages.INVOICEDELETE);
        } else
            return new ErrorResult();
    }

    private Result checkIfInvoiceAlreadyExists(int rentalId) {
        if (invoiceDao.findByRentalId(rentalId) != null) {
            return new ErrorResult(Messages.INVOICENUMBERAlREADYEXISTS);
        }
        return new SuccessResult();
    }

    private Result checkIfInvoiceExistsByRentalId(int rentalId) {
        if (invoiceDao.findByRentalId(rentalId) == null) {
            return new ErrorResult(Messages.INVOICENOTFOUND);
        }
        return new SuccessResult();
    }

    private Result checkIfRentalIsFinished(int rentalId) {
        if (rentalService.findById(rentalId).getData() != null) {
            if (rentalService.findById(rentalId).getData().getReturnedDate() == null) {
                return new ErrorResult(Messages.RENTALNOTFINISHED);
            } else
                return new SuccessResult();
        } else
            return new ErrorResult(Messages.RENTALNOTFOUND);
    }

    private Result checkIfPaymentIsMade(int rentalId) {
        if (rentalService.findById(rentalId).getData() != null) {
            return new SuccessResult();
        } else
            return new ErrorResult(Messages.PAYMENTNOTFOUND);
    }

    private Result checkIfCustomerIndividual(int rentalId) {
        int customerId = rentalService.findById(rentalId).getData().getCustomerId();
        if (userService.findById(customerId).getData().getRole().equals(Role.INDIVIDUAL_CUSTOMER.getRole())) {
            return new SuccessResult();
        } else return new ErrorResult(Messages.NOTFOUND);
    }

    private Result checkIfCustomerCorporate(int rentalId) {
        int customerId = rentalService.findById(rentalId).getData().getCustomerId();

        if (userService.findById(customerId).getData().getRole().equals(Role.CORPORATE_CUSTOMER.getRole())) {
            return new SuccessResult();
        } else return new ErrorResult(Messages.NOTFOUND);
    }

}
