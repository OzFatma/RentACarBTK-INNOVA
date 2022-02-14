package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.CustomerCardDetailService;
import com.btkAkademi.rentACar.business.requests.customerCardDetailRequests.CreateCustomerCardDetailRequest;
import com.btkAkademi.rentACar.business.requests.customerCardDetailRequests.UpdateCustomerCardDetailsRequest;
import com.btkAkademi.rentACar.business.responses.CustomerCardDetailListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/customer-card-details")
public class CustomerCardDetailsController {
    private final CustomerCardDetailService customerCardDetailService;

    @Autowired
    public CustomerCardDetailsController(CustomerCardDetailService customerCardDetailService) {
        super();
        this.customerCardDetailService = customerCardDetailService;
    }

    @GetMapping("find-by-customer-id/{id}")
    public DataResult<List<CustomerCardDetailListResponse>> findByCustomerId(@PathVariable int id) {

        return this.customerCardDetailService.findCustomerCardDetailsByCustomerId(id);
    }

    @GetMapping("find-by-id/{id}")
    public DataResult<CustomerCardDetailListResponse> findById(@PathVariable int id) {

        return this.customerCardDetailService.findById(id);
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateCustomerCardDetailRequest createCustomerCardDetailRequest) {
        System.out.println(createCustomerCardDetailRequest.getCardNo());
        return this.customerCardDetailService.add(createCustomerCardDetailRequest);
    }

    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdateCustomerCardDetailsRequest updateCustomerPamentDetailsRequest) {

        return this.customerCardDetailService.update(updateCustomerPamentDetailsRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable int id) {

        return this.customerCardDetailService.delete(id);
    }
}
