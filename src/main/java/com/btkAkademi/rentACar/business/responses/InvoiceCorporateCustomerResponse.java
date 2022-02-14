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
public class InvoiceCorporateCustomerResponse {
	private int id;
	private String taxNumber;
	private String companyName;
	private String email;
	private LocalDate rentDate;
	private LocalDate returnedDate;
	private double totalPrice;
	private double rentPrice;
	private LocalDate creationDate;
	private List<AdditionalServiceItemListResponse> additionalServiceItems;

}
