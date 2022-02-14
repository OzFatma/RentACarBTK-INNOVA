package com.btkAkademi.rentACar.entities.concretes;

import com.btkAkademi.rentACar.entities.abstracts.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice extends BaseEntity {

    @Column(name = "creation")
    private LocalDate creationDate;

    @OneToOne
    @JoinColumn(name = "rental_id", unique = true)
    private Rental rental;
}
