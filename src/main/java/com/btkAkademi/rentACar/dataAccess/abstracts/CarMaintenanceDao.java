package com.btkAkademi.rentACar.dataAccess.abstracts;

import com.btkAkademi.rentACar.entities.concretes.CarMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarMaintenanceDao extends JpaRepository<CarMaintenance, Integer> {

	// Bakımı devam eden araçlar
	CarMaintenance findByCarIdAndMaintenanceEndIsNull(int carId);

	List<CarMaintenance> findAllByCarId(int carId);

}
