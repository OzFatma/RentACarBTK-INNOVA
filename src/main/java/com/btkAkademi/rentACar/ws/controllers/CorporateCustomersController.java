package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.CorporateCustomerService;
import com.btkAkademi.rentACar.business.requests.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.btkAkademi.rentACar.business.requests.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.btkAkademi.rentACar.business.responses.CorporateCustomerListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/corporate-customers")
public class CorporateCustomersController {

    private final CorporateCustomerService corporateCustomerService;

    @Autowired
    public CorporateCustomersController(CorporateCustomerService corporateCustomerService) {
        super();
        this.corporateCustomerService = corporateCustomerService;
    }

    @GetMapping("find-all")
    public DataResult<List<CorporateCustomerListResponse>> findAll(@RequestParam(defaultValue = "1") int pageNo,
                                                                   @RequestParam(defaultValue = "1000") int pageSize) {

        return this.corporateCustomerService.findAll(pageNo, pageSize);
    }

    @GetMapping("find-by-id/{id}")
    public DataResult<CorporateCustomerListResponse> findById(@PathVariable int id) {

        return this.corporateCustomerService.findById(id);
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateCorporateCustomerRequest createCorporateCustomerRequest) {

        return this.corporateCustomerService.add(createCorporateCustomerRequest);
    }

    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdateCorporateCustomerRequest updateCorporateCustomerRequest) {

        return this.corporateCustomerService.update(updateCorporateCustomerRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable int id) {
        return this.corporateCustomerService.delete(id);
    }

}
