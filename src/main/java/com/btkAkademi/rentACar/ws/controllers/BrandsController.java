package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.BrandService;
import com.btkAkademi.rentACar.business.requests.brandRequests.CreateBrandRequest;
import com.btkAkademi.rentACar.business.requests.brandRequests.UpdateBrandRequest;
import com.btkAkademi.rentACar.business.responses.BrandListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/brands")
public class BrandsController {

    private final BrandService brandService;

    public BrandsController(BrandService brandService) {
        super();
        this.brandService = brandService;
    }

    @GetMapping("find-all")
    public DataResult<List<BrandListResponse>> findAll() {
        return this.brandService.findAll();
    }

    @GetMapping("find-by-id/{id}")
    public Result findById(@PathVariable int id) {
        return this.brandService.findById(id);
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateBrandRequest createBrandRequest) {

        return this.brandService.add(createBrandRequest);
    }

    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdateBrandRequest updateBrandRequest) {

        return this.brandService.update(updateBrandRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable int id) {
        return this.brandService.delete(id);
    }

}
