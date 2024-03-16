package com.project.MobiliUs.repository;

import com.project.MobiliUs.entity.Member;
import com.project.MobiliUs.entity.SubwayRouteBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubwayRouteBookmarkRepository extends JpaRepository<SubwayRouteBookmark, Long> {

    List<SubwayRouteBookmark> findAllByMember(Member member);

    Optional<SubwayRouteBookmark> findByMemberAndDepartureAndDestination(Member member, String departure, String destination);

    Boolean existsByMemberAndDepartureAndDestinationAndDepartureLineAndDestinationLine(Member member, String departure, String destination,
                                                                                       String departureLine, String destinationLine);
}
