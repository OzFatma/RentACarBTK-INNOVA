package com.btkAkademi.rentACar.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndividualCustomerListResponse {
	private int id;
	private String citizenShipNumber;
	private String email;
	private String firstName;
	private String lastName;
	private LocalDate birthDate;

}
