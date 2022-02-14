package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.CarDamageService;
import com.btkAkademi.rentACar.business.requests.carDamageRequests.CreateCarDamageRequest;
import com.btkAkademi.rentACar.business.requests.carDamageRequests.UpdateCarDamageRequest;
import com.btkAkademi.rentACar.business.responses.CarDamageListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/car-damages")
public class CarDamagesController {
    private final CarDamageService carDamageService;

    @Autowired
    public CarDamagesController(CarDamageService carDamageService) {
        super();
        this.carDamageService = carDamageService;
    }

    @GetMapping("find-all-by-car-id/{id}")
    public DataResult<List<CarDamageListResponse>> findAllByCarId(@PathVariable int id) {
        return this.carDamageService.findAllByCarId(id);
    }

    @GetMapping("find-by-id/{id}")
    public Result findById(@PathVariable int id) {
        return this.carDamageService.findById(id);
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateCarDamageRequest createCarDamageRequest) {
        return this.carDamageService.add(createCarDamageRequest);
    }

    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdateCarDamageRequest updateCarDamageRequest) {
        return this.carDamageService.update(updateCarDamageRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable int id) {
        return this.carDamageService.delete(id);
    }
}
