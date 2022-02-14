package com.btkAkademi.rentACar.business.requests.individualCustomerRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateIndividualCustomerRequest {

	@Email
	@NotBlank
	private String email;
	@Size(min = 4, max = 30)
	private String password;
	@Size(min = 2, max = 100)
	private String firstName;
	@Size(min = 2, max = 100)
	private String lastName;
	@NotNull
	private LocalDate birthDate;
	@Pattern(regexp = "[0-9]+")
	@Size(min = 11, max = 11)
	private String citizenShipNumber;
}
