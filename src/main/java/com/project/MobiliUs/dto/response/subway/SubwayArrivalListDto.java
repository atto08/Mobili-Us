package com.project.MobiliUs.dto.response.subway;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SubwayArrivalListDto {

    private String stationName;

    private List<SubwayArrivalDto> subwayArrivalList;
}
