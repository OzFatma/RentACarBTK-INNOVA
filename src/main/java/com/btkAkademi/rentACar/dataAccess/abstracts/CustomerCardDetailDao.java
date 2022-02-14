package com.btkAkademi.rentACar.dataAccess.abstracts;

import com.btkAkademi.rentACar.entities.concretes.CustomerCardDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerCardDetailDao extends JpaRepository<CustomerCardDetail, Integer> {

    List<CustomerCardDetail> findAllByCustomerId(int customerId);

    CustomerCardDetail findByCardNo(String cardNo);
}
