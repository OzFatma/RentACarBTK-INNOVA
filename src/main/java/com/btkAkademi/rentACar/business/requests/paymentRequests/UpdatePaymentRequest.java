package com.btkAkademi.rentACar.business.requests.paymentRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePaymentRequest {
	private int id;
	private LocalDate paymentTime;
	private double totalPaymentAmount;
	private int rentalId;
}
