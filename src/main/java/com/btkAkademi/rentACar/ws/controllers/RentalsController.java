package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.RentalService;
import com.btkAkademi.rentACar.business.requests.rentalRequests.CreateRentalRequest;
import com.btkAkademi.rentACar.business.requests.rentalRequests.UpdateRentalRequest;
import com.btkAkademi.rentACar.business.responses.MyRentalListResponse;
import com.btkAkademi.rentACar.business.responses.RentalAddResponse;
import com.btkAkademi.rentACar.business.responses.RentalListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/rentals")
public class RentalsController {

    private final RentalService rentalService;

    @Autowired
    public RentalsController(RentalService rentalService) {
        super();
        this.rentalService = rentalService;
    }

    @GetMapping("find-all")
    public DataResult<List<RentalListResponse>> findAll(@RequestParam(defaultValue = "1") int pageNo,
                                                        @RequestParam(defaultValue = "1000") int pageSize) {
        return rentalService.findAll(pageNo, pageSize);
    }

    @GetMapping("find-all-by-customer-id/{id}")
    public DataResult<List<MyRentalListResponse>> findAllByCustomerId(@PathVariable int id) {
        return rentalService.findAllByCustomerId(id);
    }

    @GetMapping("find-active-rental-by-car-id/{id}")
    public DataResult<RentalListResponse> findActiveRentalByCarId(@PathVariable int id) {
        return rentalService.findActiveRentalByCarId(id);
    }

    @GetMapping("find-by-id/{id}")
    public DataResult<RentalListResponse> findById(@PathVariable int id) {
        return rentalService.findById(id);
    }

    @PostMapping("add-for-individual-customer")
    public DataResult<RentalAddResponse> addForIndividualCustomer(
            @RequestBody @Valid CreateRentalRequest createRentalRequest) {
        return this.rentalService.addForIndividualCustomer(createRentalRequest);
    }

    @PostMapping("add-for-corporate-customer")
    public DataResult<RentalAddResponse> addForCorporateCustomer(
            @RequestBody @Valid CreateRentalRequest createRentalRequest) {
        return this.rentalService.addForCorporateCustomer(createRentalRequest);
    }

    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdateRentalRequest updateRentalRequest) {
        return this.rentalService.update(updateRentalRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable int id) {
        return this.rentalService.delete(id);
    }
}
