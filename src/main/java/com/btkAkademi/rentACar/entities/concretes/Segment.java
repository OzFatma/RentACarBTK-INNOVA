package com.btkAkademi.rentACar.entities.concretes;

import com.btkAkademi.rentACar.entities.abstracts.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "segments")
public class Segment extends BaseEntity {

    @Column(name = "segment_name", unique = true)
    private String segmentName;

    @OneToMany(mappedBy = "segment")
    private List<Car> cars;
}
