package com.btkAkademi.rentACar.ws.controllers;

import com.btkAkademi.rentACar.business.abstracts.PromoCodeService;
import com.btkAkademi.rentACar.business.requests.promoCodeRequests.CreatePromoCodeRequest;
import com.btkAkademi.rentACar.business.requests.promoCodeRequests.UpdatePromoCodeRequest;
import com.btkAkademi.rentACar.business.responses.PromoCodeResponse;
import com.btkAkademi.rentACar.core.utilities.results.DataResult;
import com.btkAkademi.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/promo-codes")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    @Autowired
    public PromoCodeController(PromoCodeService promoCodeService) {
        super();
        this.promoCodeService = promoCodeService;
    }

    @GetMapping("find-all")
    public DataResult<List<PromoCodeResponse>> findAll() {
        return this.promoCodeService.findAll();
    }

    @GetMapping("find-by-code/{code}")
    public DataResult<PromoCodeResponse> findByCode(@PathVariable String code) {
        return this.promoCodeService.findByCode(code);
    }

    @GetMapping("find-by-id/{id}")
    public DataResult<PromoCodeResponse> findByCode(@PathVariable int id) {
        return this.promoCodeService.findById(id);
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreatePromoCodeRequest createPromoCodeRequest) {

        return this.promoCodeService.add(createPromoCodeRequest);
    }

    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdatePromoCodeRequest updatePromoCodeRequest) {
        return this.promoCodeService.update(updatePromoCodeRequest);
    }

    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable int id) {
        return this.promoCodeService.delete(id);
    }

}
