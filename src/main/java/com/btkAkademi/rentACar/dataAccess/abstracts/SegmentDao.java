package com.btkAkademi.rentACar.dataAccess.abstracts;

import com.btkAkademi.rentACar.entities.concretes.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentDao extends JpaRepository<Segment, Integer> {

    Segment findBySegmentName(String segmentName);
}
