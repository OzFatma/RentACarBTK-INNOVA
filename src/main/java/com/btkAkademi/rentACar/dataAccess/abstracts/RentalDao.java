package com.btkAkademi.rentACar.dataAccess.abstracts;

import com.btkAkademi.rentACar.entities.concretes.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalDao extends JpaRepository<Rental, Integer> {

    Rental findByCarIdAndReturnedDateIsNull(int carId);

    List<Rental> findAllByCustomerId(int customerId);

}
