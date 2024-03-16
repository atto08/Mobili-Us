package com.project.MobiliUs.dto.response.subway;

import com.project.MobiliUs.entity.SubwayStation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SubwayListDto {

    private List<SubwayStation> stationList;
}
