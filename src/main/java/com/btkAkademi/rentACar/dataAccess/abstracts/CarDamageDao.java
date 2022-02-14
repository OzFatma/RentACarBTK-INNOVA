package com.btkAkademi.rentACar.dataAccess.abstracts;

import com.btkAkademi.rentACar.entities.concretes.CarDamage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarDamageDao extends JpaRepository<CarDamage, Integer> {

    List<CarDamage> findAllByCarId(int id);

}
