package com.project.MobiliUs.dto.response.subway;

import com.project.MobiliUs.entity.SubwayRouteBookmark;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubwayRouteBookmarkListDto {

    private List<SubwayRouteBookmark> bookmarkList;
}
