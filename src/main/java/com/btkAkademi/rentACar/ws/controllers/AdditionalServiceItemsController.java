package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.AdditionalServiceItemService;
import com.btkAkademi.rentACar.business.requests.additionalServiceItemRequests.CreateAdditionalServiceItemRequest;
import com.btkAkademi.rentACar.business.requests.additionalServiceItemRequests.UpdateAdditionalServiceItemRequest;
import com.btkAkademi.rentACar.business.responses.AdditionalServiceItemListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/additional-service-items")
public class AdditionalServiceItemsController {
    private final AdditionalServiceItemService additionalServiceItemService;

    @Autowired
    public AdditionalServiceItemsController(AdditionalServiceItemService additionalServiceItemService) {
        super();
        this.additionalServiceItemService = additionalServiceItemService;
    }

    @GetMapping("find-all")
    public DataResult<List<AdditionalServiceItemListResponse>> findAllByRentalId() {
        return additionalServiceItemService.findAll();

    }

    @GetMapping("find-by-id/{id}")
    public DataResult<AdditionalServiceItemListResponse> findById(@PathVariable int id) {
        return additionalServiceItemService.findById(id);

    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateAdditionalServiceItemRequest createAdditionalServiceItemRequest) {
        return this.additionalServiceItemService.add(createAdditionalServiceItemRequest);
    }

    @PutMapping("update")
    public Result add(@RequestBody @Valid UpdateAdditionalServiceItemRequest updateAdditionalServiceItemRequest) {
        return this.additionalServiceItemService.update(updateAdditionalServiceItemRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable int id) {
        return this.additionalServiceItemService.delete(id);
    }

}
