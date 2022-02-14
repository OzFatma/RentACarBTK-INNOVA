package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.AdditionalServiceService;
import com.btkAkademi.rentACar.business.requests.additionalServiceRequests.CreateAdditionalServiceRequest;
import com.btkAkademi.rentACar.business.requests.additionalServiceRequests.UpdateAdditionalServiceRequest;
import com.btkAkademi.rentACar.business.responses.AdditionalServiceListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/additional-services")
public class AdditionalServicesController {
    private final AdditionalServiceService additionalServiceService;

    @Autowired
    public AdditionalServicesController(AdditionalServiceService additionalServiceService) {
        super();
        this.additionalServiceService = additionalServiceService;
    }

    @GetMapping("find-all-by-rental-id/{id}")
    public DataResult<List<AdditionalServiceListResponse>> findAllByRentalId(@PathVariable int id) {
        return additionalServiceService.findAllByRentalId(id);

    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateAdditionalServiceRequest createAdditionalService) {
        return this.additionalServiceService.add(createAdditionalService);
    }

    @PostMapping("add-all")
    public Result addAll(@RequestBody @Valid List<CreateAdditionalServiceRequest> createAdditionalService) {
        return this.additionalServiceService.addAll(createAdditionalService);
    }

    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdateAdditionalServiceRequest updateAdditionalServiceRequest) {
        return this.additionalServiceService.update(updateAdditionalServiceRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result update(@PathVariable int id) {
        return this.additionalServiceService.delete(id);
    }
}
