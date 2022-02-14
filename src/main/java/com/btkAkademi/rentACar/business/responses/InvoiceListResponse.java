package com.btkAkademi.rentACar.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceListResponse {
	private int id;
	private LocalDate creationDate;
	private int rentalId;
	private String customerRole;
}
