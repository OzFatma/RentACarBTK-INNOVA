package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.CarService;
import com.btkAkademi.rentACar.business.requests.carRequests.CreateCarRequest;
import com.btkAkademi.rentACar.business.requests.carRequests.UpdateCarRequest;
import com.btkAkademi.rentACar.business.responses.CarListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/cars")
public class CarsController {

    private final CarService carService;

    public CarsController(CarService carService) {
        super();
        this.carService = carService;
    }

    @GetMapping("find-all")
    public DataResult<List<CarListResponse>> findAll(@RequestParam(defaultValue = " 1") int pageNo,
                                                     @RequestParam(defaultValue = " 100") int pageSize) {
        return this.carService.findAll(pageNo, pageSize);
    }

    @GetMapping("find-all-available")
    public DataResult<List<CarListResponse>> findAllAvailable(@RequestParam(defaultValue = " 1") int pageNo,
                                                              @RequestParam(defaultValue = " 1000") int pageSize) {
        return this.carService.findAllAvailable(pageNo, pageSize);
    }

    @GetMapping("find-all-by-brand-id")
    public DataResult<List<CarListResponse>> findAllByBrandId(@RequestParam int brandId, @RequestParam int pageNo,
                                                              @RequestParam(defaultValue = " 100") int pageSize) {
        return this.carService.findAllByBrandId(brandId, pageNo, pageSize);
    }

    @GetMapping("find-all-by-color-id")
    public DataResult<List<CarListResponse>> findAllByColorId(@RequestParam int colorId, @RequestParam int pageNo,
                                                              @RequestParam(defaultValue = " 100") int pageSize) {
        return this.carService.findAllByColorId(colorId, pageNo, pageSize);
    }

    @GetMapping("find-by-id/{id}")
    public DataResult<CarListResponse> findById(@PathVariable int id) {
        return this.carService.findCarById(id);
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateCarRequest createCarRequest) {
        return this.carService.add(createCarRequest);
    }

    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdateCarRequest updateCarRequest) {
        return this.carService.update(updateCarRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@Valid @PathVariable int id) {
        return this.carService.delete(id);
    }
}
