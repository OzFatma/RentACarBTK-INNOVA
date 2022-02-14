package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.ColorService;
import com.btkAkademi.rentACar.business.requests.colorRequests.CreateColorRequest;
import com.btkAkademi.rentACar.business.requests.colorRequests.UpdateColorRequest;
import com.btkAkademi.rentACar.business.responses.ColorListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/colors")
public class ColorsContoller {

    private final ColorService colorService;

    public ColorsContoller(ColorService colorService) {
        super();
        this.colorService = colorService;
    }

    @GetMapping("find-all")
    public DataResult<List<ColorListResponse>> findAll() {
        return this.colorService.findAll();
    }

    @GetMapping("find-by-id/{id}")
    public DataResult<ColorListResponse> findById(@PathVariable int id) {
        return this.colorService.findById(id);
    }

    @PostMapping("add")

    public Result add(@RequestBody @Valid CreateColorRequest createColorRequest) {

        return this.colorService.add(createColorRequest);
    }

    @PutMapping("update")

    public Result update(@RequestBody @Valid UpdateColorRequest updateColorRequest) {

        return this.colorService.update(updateColorRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@Valid @PathVariable int id) {

        return this.colorService.delete(id);
    }
}
