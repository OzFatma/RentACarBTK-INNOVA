package com.btkAkademi.rentACar.entities.concretes;

import com.btkAkademi.rentACar.entities.abstracts.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "additional_services")
public class AdditionalService extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "additional_service_item_id")
    private AdditionalServiceItem additionalServiceItem;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

}
