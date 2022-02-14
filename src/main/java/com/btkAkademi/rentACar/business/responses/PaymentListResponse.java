package com.btkAkademi.rentACar.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentListResponse {
	private int id;
	private LocalDate paymentTime;
	private double totalPaymentAmount;
	private int rentalId;
}
