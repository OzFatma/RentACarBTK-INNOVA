package com.btkAkademi.rentACar.dataAccess.abstracts;

import com.btkAkademi.rentACar.entities.concretes.AdditionalServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalServiceItemDao extends JpaRepository<AdditionalServiceItem, Integer> {

}
