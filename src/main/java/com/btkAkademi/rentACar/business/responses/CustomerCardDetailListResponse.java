package com.btkAkademi.rentACar.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCardDetailListResponse {
	private int id;
	private String cardNo;
	private String year;
	private String month;
	private String cvv;
	private int customerId;
}
