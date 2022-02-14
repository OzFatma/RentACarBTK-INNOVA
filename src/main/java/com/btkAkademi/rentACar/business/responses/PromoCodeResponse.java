package com.btkAkademi.rentACar.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoCodeResponse {
	private int id;
	private String code;
	private double discountRate;
	private LocalDate startDate;
	private LocalDate endDate;
	private String description;
}
