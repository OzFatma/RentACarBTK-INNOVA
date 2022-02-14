package com.btkAkademi.rentACar.business.requests.brandRequests;

import com.btkAkademi.rentACar.business.constants.Messages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBrandRequest {

	private int id;
	@NotBlank
	@Size(min = 3, max = 20, message = Messages.BRANDNAMEERROR)
	private String name;
}