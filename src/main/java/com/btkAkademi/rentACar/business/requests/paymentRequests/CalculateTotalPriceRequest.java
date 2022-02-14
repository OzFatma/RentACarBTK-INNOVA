package com.btkAkademi.rentACar.business.requests.paymentRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateTotalPriceRequest {
	private int rentalId;
	private LocalDate returnDate;

}
