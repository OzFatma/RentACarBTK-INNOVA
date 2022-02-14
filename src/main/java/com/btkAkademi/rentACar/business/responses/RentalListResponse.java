package com.btkAkademi.rentACar.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalListResponse {
	private int id;
	private LocalDate rentedDate;
	private LocalDate returnedDate;
	private Integer rentedKilometer;
	private Integer returnedKilometer;
	private int customerId;
	private int carId;
	private int promoCodeId;
}
