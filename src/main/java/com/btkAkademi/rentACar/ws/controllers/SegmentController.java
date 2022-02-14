package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.SegmentService;
import com.btkAkademi.rentACar.business.requests.segmentRequest.CreateSegmentRequest;
import com.btkAkademi.rentACar.business.requests.segmentRequest.UpdateSegmentRequest;
import com.btkAkademi.rentACar.business.responses.SegmentListResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/segments")
public class SegmentController {
    private final SegmentService segmentService;

    @Autowired
    public SegmentController(SegmentService segmentService) {
        super();
        this.segmentService = segmentService;
    }

    @GetMapping("find-all")
    public DataResult<List<SegmentListResponse>> findAll() {
        return this.segmentService.findAll();
    }

    @GetMapping("find-by-id/{id}")
    public DataResult<SegmentListResponse> findById(@PathVariable int id) {
        return this.segmentService.findById(id);
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateSegmentRequest createSegmentRequest) {
        return this.segmentService.add(createSegmentRequest);
    }

    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdateSegmentRequest createSegmentRequest) {
        return this.segmentService.update(createSegmentRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable int id) {
        return this.segmentService.delete(id);
    }

}
