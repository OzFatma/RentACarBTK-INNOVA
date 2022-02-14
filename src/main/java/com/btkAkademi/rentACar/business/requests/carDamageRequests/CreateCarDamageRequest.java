package com.btkAkademi.rentACar.business.requests.carDamageRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarDamageRequest {
	@NotBlank
	@Size(min = 0, max = 255)
	private String description;
	private int carId;

}
