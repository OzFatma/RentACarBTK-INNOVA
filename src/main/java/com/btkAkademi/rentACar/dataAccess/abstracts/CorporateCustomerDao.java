package com.btkAkademi.rentACar.dataAccess.abstracts;

import com.btkAkademi.rentACar.entities.concretes.CorporateCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorporateCustomerDao extends JpaRepository<CorporateCustomer, Integer> {

    //CorporateCustomer findByEmail(String email);

    CorporateCustomer findByCompanyName(String companyName);
}
