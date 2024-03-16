package com.project.MobiliUs.dto.response.bus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SearchBusStationListDto {

    private List<BusStationListDto> busStationList;
}
