package com.btkAkademi.rentACar.business.requests.colorRequests;

import com.btkAkademi.rentACar.business.constants.Messages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateColorRequest {
	@NotBlank
	@Size(min = 2, message = Messages.COLORNAMEERROR)
	private String name;
}
