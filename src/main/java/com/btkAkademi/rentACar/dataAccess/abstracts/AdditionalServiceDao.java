package com.btkAkademi.rentACar.dataAccess.abstracts;

import com.btkAkademi.rentACar.entities.concretes.AdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdditionalServiceDao extends JpaRepository<AdditionalService, Integer> {

    List<AdditionalService> findAllByRentalId(int RentalId);
}
