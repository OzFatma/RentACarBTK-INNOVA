package com.btkAkademi.rentACar.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarMaintenanceListResponse {
	private int id;
	private int carId;
	private LocalDate maintenanceStart;
	private LocalDate maintenanceEnd;
}
