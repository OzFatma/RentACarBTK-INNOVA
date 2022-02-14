package com.btkAkademi.rentACar.business.requests.paymentRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {
	private LocalDate paymentTime;
	private int rentalId;
	private LocalDate returnDate;
	@NotEmpty
	private String cardNo;
	@NotEmpty
	private String year;
	@NotEmpty
	private String month;
	@NotEmpty
	private String cvv;
}
