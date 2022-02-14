package com.btkAkademi.rentACar.business.requests.individualCustomerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIndividualCustomerRequest {

	private int id;

	@Pattern(regexp = "[0-9]+")
	@Size(min = 11, max = 11)
	private String citizenShipNumber;
	@Email
	@NotBlank
	private String email;
	@Size(min = 2, max = 100)
	private String firstName;
	@Size(min = 2, max = 100)
	private String lastName;
	private LocalDate birthDate;
}
