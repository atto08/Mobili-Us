package com.project.MobiliUs.repository;

import com.project.MobiliUs.entity.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusStopRepository extends JpaRepository<BusStop, Long> {

    List<BusStop> findAllByStationNameContains(String stationName);

    BusStop findBusStopByStationId(Long stationId);
}
