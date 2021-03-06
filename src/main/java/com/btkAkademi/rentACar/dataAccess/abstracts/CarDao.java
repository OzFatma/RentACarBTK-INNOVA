package com.btkAkademi.rentACar.dataAccess.abstracts;

import com.btkAkademi.rentACar.entities.concretes.Car;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarDao extends JpaRepository<Car, Integer> {

    List<Car> findAllByBrandId(int brandId, Pageable pagable);

    List<Car> findAllByColorId(int colorId, Pageable pagable);

    List<Car> findAllBySegmentId(int segmentId);

    @Query(value = "select cars.id as rental_id,\r\n" + "	rentals.returned_date\r\n" + "from cars\r\n"
            + "left join car_maintenances on cars.id = car_maintenances.car_id and car_maintenances.maintenance_end is null\r\n"
            + "left join rentals on cars.id = rentals.car_id and (rentals.return_date is null or rentals.return_date>NOW())\r\n"
            + "where car_maintenances.id is null and rentals.id is null and cars.segment_id =?1	and cars.city_id =?2 limit 1",
            nativeQuery = true)
    List<Integer> findAvailableCarBySegmentAndCity(Integer segmentId, Integer cityId);

    @Query(value = "select cars.* from cars \r\n"
            + "left join car_maintenances on cars.id=car_maintenances.car_id and car_maintenances.maintenance_end is null\r\n"
            + "left join rentals on cars.id=rentals.car_id and rentals.return_date is null\r\n"
            + "where car_maintenances.id is null and rentals.id is null order by cars.segment_id ,cars.brand_id " +
            ", cars.car_name \r\n"
            , nativeQuery = true)
    List<Car> findAvailableCars(Pageable pageable);


}
