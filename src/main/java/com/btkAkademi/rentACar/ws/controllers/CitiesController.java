package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.CityService;
import com.btkAkademi.rentACar.business.requests.cityRequests.CreateCityRequest;
import com.btkAkademi.rentACar.business.requests.cityRequests.UpdateCityRequest;
import com.btkAkademi.rentACar.business.responses.CityListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/cities")
public class CitiesController {

    private final CityService cityService;

    @Autowired
    public CitiesController(CityService cityService) {
        super();
        this.cityService = cityService;
    }

    @GetMapping("find-all")
    public DataResult<List<CityListResponse>> findAll() {

        return cityService.findAll();
    }

    @GetMapping("find-by-id/{id}")
    public DataResult<CityListResponse> findById(@PathVariable int id) {
        return cityService.findById(id);
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateCityRequest createCityRequest) {
        System.out.println(createCityRequest.getCityName());
        return this.cityService.add(createCityRequest);
    }

    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdateCityRequest updateCityRequest) {
        return this.cityService.update(updateCityRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable int id) {
        return this.cityService.delete(id);
    }

}
