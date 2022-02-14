package com.btkAkademi.rentACar.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyRentalListResponse {
	private int rentalId;
	private String brandName;
	private String carName;
	private LocalDate RentDate;
	private LocalDate returnDate;
	private String pickUpCityName;
	private String returnCityName;
	private double totalPayment;
	private boolean isInvoiceCreated;
	private boolean isRentalFinished;
}
