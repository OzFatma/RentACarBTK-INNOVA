package com.btkAkademi.rentACar.entities.concretes;

import com.btkAkademi.rentACar.entities.abstracts.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cities")
public class City extends BaseEntity {

    @Column(name = "city_name")
    private String cityName;

    @OneToMany(mappedBy = "pickUpCity")
    private List<Rental> pickUpCityRentals;

    @OneToMany(mappedBy = "returnCity")
    private List<Rental> returnCityRentals;

    @OneToMany(mappedBy = "city")
    private List<Car> cars;
}
