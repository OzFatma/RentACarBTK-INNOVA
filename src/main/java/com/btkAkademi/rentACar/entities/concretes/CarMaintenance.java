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
@Table(name = "car_maintenances")
public class CarMaintenance extends BaseEntity {

    @Column(name = "maintenance_start")
    private LocalDate maintenanceStart;

    @Column(name = "maintenance_end")
    private LocalDate maintenanceEnd;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

}
