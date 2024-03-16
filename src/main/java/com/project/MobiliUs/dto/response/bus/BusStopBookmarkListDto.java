package com.project.MobiliUs.dto.response.bus;

import com.project.MobiliUs.entity.BusStopBookmark;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusStopBookmarkListDto {

    private List<BusStopBookmark> bookmarkList;
}
