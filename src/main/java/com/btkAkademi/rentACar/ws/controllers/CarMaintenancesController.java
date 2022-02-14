package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.CarMaintenanceService;
import com.btkAkademi.rentACar.business.requests.carMaintenanceRequests.CreateCarMaintenanceRequest;
import com.btkAkademi.rentACar.business.requests.carMaintenanceRequests.UpdateCarMaintenanceRequest;
import com.btkAkademi.rentACar.business.responses.CarMaintenanceListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/car-maintenances")
public class CarMaintenancesController {
    private final CarMaintenanceService carMaintenanceService;

    @Autowired
    public CarMaintenancesController(CarMaintenanceService carMaintenanceService) {
        super();
        this.carMaintenanceService = carMaintenanceService;
    }

    @GetMapping("find-all")
    public DataResult<List<CarMaintenanceListResponse>> findAll() {
        return this.carMaintenanceService.findAll();
    }

    @GetMapping("find-all-by-car-id/{id}")
    public DataResult<List<CarMaintenanceListResponse>> findAllByCarId(@PathVariable int id) {
        return this.carMaintenanceService.findAllByCarId(id);
    }

    @GetMapping("find-by-id/{id}")
    public DataResult<CarMaintenanceListResponse> findById(@PathVariable int id) {
        return this.carMaintenanceService.findById(id);
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateCarMaintenanceRequest createCarMaintananceRequest) {
        return this.carMaintenanceService.add(createCarMaintananceRequest);
    }

    @PutMapping("update")
    public Result add(@RequestBody @Valid UpdateCarMaintenanceRequest updateCarMaintenanceRequest) {
        return this.carMaintenanceService.update(updateCarMaintenanceRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result add(@PathVariable int id) {
        return this.carMaintenanceService.delete(id);
    }
}
