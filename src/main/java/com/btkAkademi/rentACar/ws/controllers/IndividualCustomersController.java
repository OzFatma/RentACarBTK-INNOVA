package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.IndividualCustomerService;
import com.btkAkademi.rentACar.business.requests.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.btkAkademi.rentACar.business.requests.individualCustomerRequests.UpdateIndividualCustomerRequest;
import com.btkAkademi.rentACar.business.responses.IndividualCustomerListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/individual-customers")
public class IndividualCustomersController {

    private final IndividualCustomerService individualCustomerService;

    @Autowired
    public IndividualCustomersController(IndividualCustomerService individualCustomerService) {
        super();
        this.individualCustomerService = individualCustomerService;
    }

    @GetMapping("find-all")
    public DataResult<List<IndividualCustomerListResponse>> findAll(@RequestParam(defaultValue = "1") int pageNo,
                                                                    @RequestParam(defaultValue = "10") int pageSize) {

        return this.individualCustomerService.findAll(pageNo, pageSize);
    }

    @GetMapping("find-by-id/{id}")
    public DataResult<IndividualCustomerListResponse> findById(@PathVariable int id) {

        return this.individualCustomerService.findById(id);
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateIndividualCustomerRequest createIndividualCustomerRequest) {

        return this.individualCustomerService.add(createIndividualCustomerRequest);
    }

    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdateIndividualCustomerRequest updateIndividualCustomerRequest) {

        return this.individualCustomerService.update(updateIndividualCustomerRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable int id) {

        return this.individualCustomerService.delete(id);
    }
}
