package com.btkAkademi.rentACar.business.requests.promoCodeRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePromoCodeRequest {
	@NotEmpty
	private String code;
	private double discountRate;
	private LocalDate startDate;
	private LocalDate endDate;
	private String description;

}
