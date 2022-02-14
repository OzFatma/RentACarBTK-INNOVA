package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.InvoiceService;
import com.btkAkademi.rentACar.business.requests.invoiceRequests.CreateInvoiceRequest;
import com.btkAkademi.rentACar.business.requests.invoiceRequests.UpdateInvoiceRequest;
import com.btkAkademi.rentACar.business.responses.InvoiceCorporateCustomerResponse;
import com.btkAkademi.rentACar.business.responses.InvoiceIndividualCustomerResponse;
import com.btkAkademi.rentACar.business.responses.InvoiceListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/invoices")
public class InvoiceControllers {
    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceControllers(InvoiceService invoiceService) {
        super();
        this.invoiceService = invoiceService;
    }

    @GetMapping("find-all")
    public DataResult<List<InvoiceListResponse>> findAll() {
        return this.invoiceService.findAll();
    }

    @GetMapping("find-invoice-for-corporate-customer/{rentalId}")
    public DataResult<InvoiceCorporateCustomerResponse> getInvoiceForCorporateCustomer(@PathVariable int rentalId) {
        return this.invoiceService.getInvoiceForCorporateCustomer(rentalId);
    }

    @GetMapping("find-invoice-for-individual-customer/{rentalId}")
    public DataResult<InvoiceIndividualCustomerResponse> getInvoiceForIndividualCustomer(@PathVariable int rentalId) {
        return this.invoiceService.getInvoiceForIndividualCustomer(rentalId);
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateInvoiceRequest createInvoiceRequest) {

        return this.invoiceService.add(createInvoiceRequest);
    }

    @PostMapping("update")
    public Result update(@RequestBody @Valid UpdateInvoiceRequest updateInvoiceRequest) {

        return this.invoiceService.update(updateInvoiceRequest);
    }

    @PostMapping("delete/{id}")
    public Result delete(@PathVariable int id) {

        return this.invoiceService.delete(id);
    }
}
