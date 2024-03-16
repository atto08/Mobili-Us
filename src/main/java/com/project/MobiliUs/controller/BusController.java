package com.project.MobiliUs.controller;

import com.project.MobiliUs.dto.request.BusStopDto;
import com.project.MobiliUs.security.MemberDetailsImpl;
import com.project.MobiliUs.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/bus")
public class BusController {

    private final BusService busService;

    @GetMapping(value = "/arrival")
    public ResponseEntity<?> getBusArrival(@AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                           @RequestParam Long stationId,
                                           @RequestParam int localState) {

        if (memberDetails == null){
            return busService.getBusArrival(null, stationId, localState);
        } else
            return busService.getBusArrival(memberDetails.getMember(), stationId, localState);
    }


    @GetMapping(value = "/search/busStation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchBusStation(@RequestParam String station) {

        return busService.searchBusStation(station);
    }


    @GetMapping(value = "/nearby/busStation")
    public ResponseEntity<?> findNearByBusStationList(@RequestParam double x,
                                                      @RequestParam double y,
                                                      @RequestParam int distance) {

        return busService.findNearByStationList(x, y, distance);
    }


    @GetMapping(value = "/parse/busRouteStation")
    public ResponseEntity<?> parseBusRouteStation() {

        return busService.parseBusRouteStation();
    }


    @GetMapping(value = "/parse/busStop")
    public ResponseEntity<?> parseBusStop() {

        return busService.parseBusStop();
    }


    @PostMapping(value = "/bookmark")
    public ResponseEntity<?> bookmarkBusStop(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @RequestBody BusStopDto busStopDto) {

        return busService.bookmarkBusStop(memberDetails.getMember(), busStopDto);
    }


    @GetMapping(value = "/bookmark/show")
    public ResponseEntity<?> myBookmarkBusStop(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return busService.myBookmarkBusStop(memberDetails.getMember());
    }
}
