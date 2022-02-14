package com.btkAkademi.rentACar.business.requests.corporateCustomerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCorporateCustomerRequest {
	private int id;
	@Email
	@NotBlank
	private String email;
	@Size(min = 3, max = 255)
	private String companyName;
	@Size(min = 4, max = 255)
	@Pattern(regexp = "[0-9]+")
	private String taxNumber;
}