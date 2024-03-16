package com.project.MobiliUs.repository;

import com.project.MobiliUs.entity.SubwayStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubwayStationRepository extends JpaRepository<SubwayStation, Long> {

    List<SubwayStation> findAllBySubwayLine(String subwayLine);

    List<SubwayStation> findAllByStationNameContains(String stationName);

    SubwayStation findByStationNameAndSubwayLine(Object stationName, Object subwayLine);
}
