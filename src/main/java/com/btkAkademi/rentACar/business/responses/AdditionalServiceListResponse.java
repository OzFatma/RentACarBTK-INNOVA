package com.btkAkademi.rentACar.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalServiceListResponse {
	private int id;
	private int rentalId;
	private int additionalServiceItemId;
}
