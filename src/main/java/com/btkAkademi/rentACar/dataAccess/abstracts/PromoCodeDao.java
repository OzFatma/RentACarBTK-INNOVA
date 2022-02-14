package com.btkAkademi.rentACar.dataAccess.abstracts;

import com.btkAkademi.rentACar.entities.concretes.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoCodeDao extends JpaRepository<PromoCode, Integer> {

    PromoCode findByCode(String code);
}
