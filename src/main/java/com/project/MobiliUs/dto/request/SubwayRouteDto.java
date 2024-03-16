package com.project.MobiliUs.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class SubwayRouteDto {

    @NotNull
    private String departure;

    @NotNull
    private String destination;

    @NotNull
    private String departureLine;

    @NotNull
    private String destinationLine;
}
