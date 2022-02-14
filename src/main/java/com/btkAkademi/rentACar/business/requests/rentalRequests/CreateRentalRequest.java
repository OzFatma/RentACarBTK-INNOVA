package com.btkAkademi.rentACar.business.requests.rentalRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRentalRequest {

	private LocalDate rentedDate;
	private LocalDate returnedDate;
	private int customerId;
	private int carId;
	private int promoCodeId;

}
