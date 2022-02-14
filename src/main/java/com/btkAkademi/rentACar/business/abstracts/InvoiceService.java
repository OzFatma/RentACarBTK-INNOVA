package com.btkAkademi.rentACar.business.abstracts;

import com.btkAkademi.rentACar.business.requests.invoiceRequests.CreateInvoiceRequest;
import com.btkAkademi.rentACar.business.requests.invoiceRequests.UpdateInvoiceRequest;
import com.btkAkademi.rentACar.business.responses.InvoiceCorporateCustomerResponse;
import com.btkAkademi.rentACar.business.responses.InvoiceIndividualCustomerResponse;
import com.btkAkademi.rentACar.business.responses.InvoiceListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;

import java.util.List;

public interface InvoiceService {

    DataResult<InvoiceIndividualCustomerResponse> getInvoiceForIndividualCustomer(int rentalId);

    DataResult<InvoiceCorporateCustomerResponse> getInvoiceForCorporateCustomer(int rentalId);

    Result add(CreateInvoiceRequest createInvoiceRequest);

    Result update(UpdateInvoiceRequest updateInvoiceRequest);

    Result delete(int id);

    DataResult<List<InvoiceListResponse>> findAll();

}
