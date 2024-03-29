package com.project.MobiliUs.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.MobiliUs.dto.request.SubwayRouteDto;
import com.project.MobiliUs.dto.response.*;
import com.project.MobiliUs.dto.response.subway.SubwayArrivalDto;
import com.project.MobiliUs.dto.response.subway.SubwayArrivalListDto;
import com.project.MobiliUs.dto.response.subway.SubwayListDto;
import com.project.MobiliUs.dto.response.subway.SubwayRouteBookmarkListDto;
import com.project.MobiliUs.entity.Member;
import com.project.MobiliUs.entity.SubwayRouteBookmark;
import com.project.MobiliUs.entity.SubwayStation;
import com.project.MobiliUs.repository.SubwayRouteBookmarkRepository;
import com.project.MobiliUs.repository.SubwayStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SubwayService {

    private final SubwayStationRepository subwayStationRepository;

    private final SubwayRouteBookmarkRepository subwayRouteBookmarkRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpHeaders headers = new HttpHeaders();

    @Value("${subwayKey}")
    String subwayKey;


    @Transactional
    public ResponseEntity<?> getSubwayArrivalInfo(String station) {

        SubwayArrivalListDto subwayArrivalList = new SubwayArrivalListDto();
        List<SubwayArrivalDto> arrivalInfoList = new ArrayList<>();

        if (station.isEmpty()) {

            subwayArrivalList.setStationName("정류장을 찾을 수 없습니다.");
            subwayArrivalList.setSubwayArrivalList(arrivalInfoList);

            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(subwayArrivalList, headers, ResCode.DATA_EMPTY.getStatus());
        }

        try {
            // 현재 역명에 문제가 있는 지하철 역 임시로 구분
            if (station.equals("응암")) {
                station = "응암순환(상선)";

            } else if (station.equals("공릉")) {
                station = "공릉(서울산업대입구)";

            } else if (station.equals("남한산성입구(성남법원·검찰청)")) {
                station = "남한산성입구(성남법원, 검찰청)";
            }

            String url = "http://swopenAPI.seoul.go.kr/api/subway/" + subwayKey + "/json/realtimeStationArrival/0/5/" + station;

            RestTemplate restTemplate = new RestTemplate();
            String jsonResult = restTemplate.getForObject(url, String.class);

            // 응답 JSON 파싱
            JsonNode rootNode = objectMapper.readTree(jsonResult);
            JsonNode arrivalList = rootNode.path("realtimeArrivalList");

            String stationName = arrivalList.get(0).path("statnNm").asText();

            if (arrivalList.isArray()) {
                for (JsonNode arrival : arrivalList) {
                    int id = arrival.path("rowNum").asInt();
                    String arrivalArea = arrival.path("trainLineNm").asText();
                    String arrivalMsg = arrival.path("arvlMsg2").asText();
                    String upDownLine = arrival.path("updnLine").asText();
                    String locationNow = arrival.path("arvlMsg3").asText();

                    SubwayArrivalDto subwayArrivalDto = new SubwayArrivalDto();

                    subwayArrivalDto.setId(id);
                    subwayArrivalDto.setArrivalArea(arrivalArea);
                    subwayArrivalDto.setArrivalMsg(arrivalMsg);
                    subwayArrivalDto.setUpDownLine(upDownLine);
                    subwayArrivalDto.setLocationNow(locationNow);

                    arrivalInfoList.add(subwayArrivalDto);
                }
            }
            subwayArrivalList.setStationName(stationName);
            subwayArrivalList.setSubwayArrivalList(arrivalInfoList);

        } catch (IOException e) {
            e.printStackTrace();
        }
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(subwayArrivalList, headers, ResCode.DATA_LOAD_SUCCESS.getStatus());
    }


    @Transactional
    public ResponseEntity<?> searchSubwayStation(String station) {

        SubwayListDto stationList = new SubwayListDto();
        List<SubwayStation> subwayStations = new ArrayList<>();

        System.out.println("station = " + station);
        // Null check
        if (station == null || station.isEmpty()) {

            stationList.setStationList(subwayStations);
            // response 타입 지정
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(stationList, headers, ResCode.DATA_EMPTY.getStatus());
        }

        // subway station 테이블에서 station 키워드를 포함한 전부를 찾기
        List<SubwayStation> subwayStationList = subwayStationRepository.findAllByStationNameContains(station);


        // 순차적으로 배열에 집어넣기
        for (SubwayStation subwayStation : subwayStationList) {
            System.out.println(subwayStation.getStationName());
            subwayStations.add(subwayStation);
        }

        stationList.setStationList(subwayStations);
        // response 타입 지정
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(stationList, headers, ResCode.DATA_LOAD_SUCCESS.getStatus());
    }


    @Transactional
    public ResponseEntity<?> findAllSubwayStationList(String subwayLine) {

        Map<String, Object> stationList = new HashMap<>();

        List<SubwayStation> subwayStationList = subwayStationRepository.findAllBySubwayLine(subwayLine);
        List<SubwayStation> subwayStations = new ArrayList<>();

        subwayStations.addAll(subwayStationList);

        stationList.put("stationList", subwayStations);

        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(stationList, headers, ResCode.DATA_LOAD_SUCCESS.getStatus());
    }


    @Transactional
    public ResponseEntity<?> findSubwayStation(String station) {

        List<SubwayStation> subwayStationList = subwayStationRepository.findAllByStationNameContains(station);
        List<SubwayStation> subwayStations = new ArrayList<>();

        subwayStations.addAll(subwayStationList);


        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(subwayStations, headers, ResCode.DATA_LOAD_SUCCESS.getStatus());
    }


    @Transactional
    public ResponseEntity<?> parseSubwayStation() {

        String filePath = "src/main/resources/subwayStationList.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<String[]> rows = new ArrayList<>();

            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                rows.add(values);
            }

            for (String[] row : rows) {
                subwayStationRepository.save(new SubwayStation(row[0], row[1], row[2], Double.parseDouble(row[3]), Double.parseDouble(row[4])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("저장성공");
    }


    @Transactional
    public ResponseEntity<?> bookmarkSubwayStation(Member member, SubwayRouteDto subwayRouteDto) {
        Optional<SubwayRouteBookmark> existingBookmark = subwayRouteBookmarkRepository.findByMemberAndDepartureAndDestination(member, subwayRouteDto.getDeparture(), subwayRouteDto.getDestination());

        if (existingBookmark.isPresent()) {
            subwayRouteBookmarkRepository.delete(existingBookmark.get());
            return ResponseEntity.ok("북마크 해제 ☆");
        } else {
            SubwayRouteBookmark subwayRoute = new SubwayRouteBookmark(member, subwayRouteDto.getDeparture(), subwayRouteDto.getDestination(), subwayRouteDto.getDepartureLine(), subwayRouteDto.getDestinationLine());
            subwayRouteBookmarkRepository.save(subwayRoute);
            return ResponseEntity.ok("북마크 추가 ★");
        }
    }


    public ResponseEntity<?> myBookmarkSubwayStation(Member member) {

        SubwayRouteBookmarkListDto bookmarkList = new SubwayRouteBookmarkListDto();

        List<SubwayRouteBookmark> subwayRouteBookmarkList = subwayRouteBookmarkRepository.findAllByMember(member);
        List<SubwayRouteBookmark> bookmarks = new ArrayList<>();

        for (SubwayRouteBookmark bookmark : subwayRouteBookmarkList) {

            bookmarks.add(bookmark);
        }

        bookmarkList.setBookmarkList(bookmarks);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(bookmarks, headers, ResCode.DATA_LOAD_SUCCESS.getStatus());
    }
}
