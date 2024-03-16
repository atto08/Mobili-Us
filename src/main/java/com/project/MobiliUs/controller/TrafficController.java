package com.project.MobiliUs.controller;

import com.project.MobiliUs.security.MemberDetailsImpl;
import com.project.MobiliUs.service.TrafficService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/traffic")
public class TrafficController {

    private final TrafficService trafficService;

    @GetMapping(value = "/calculate/travel-time")
    public ResponseEntity<?> calculateTravelTime(@AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                                 @RequestParam String departurePoint,
                                                 @RequestParam String destinationPoint,
                                                 @RequestParam String departureLine,
                                                 @RequestParam String destinationLine) {
        if (memberDetails == null){
            return trafficService.calculateTravelTime(null, departurePoint, destinationPoint, departureLine, destinationLine);
        } else {
            return trafficService.calculateTravelTime(memberDetails.getMember(), departurePoint, destinationPoint, departureLine, destinationLine);
        }
    }
}
