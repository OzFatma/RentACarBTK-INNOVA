package com.btkAkademi.rentACar.business.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceIndividualCustomerResponse {
	private int id;
	private String citizenShipNumber;
	private String firstName;
	private String lastName;
	private String email;
	private LocalDate rentDate;
	private LocalDate returnedDate;
	private double rentPrice;
	private double totalPrice;
	LocalDate creationDate;
	private List<AdditionalServiceItemListResponse> additionalServiceItems;
}
