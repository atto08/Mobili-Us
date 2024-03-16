package com.project.MobiliUs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubwayRouteBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    @JsonIgnore
    private Member member;

    @Column(nullable = false)
    private String departure;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private String departureLine;

    @Column(nullable = false)
    private String destinationLine;

    public SubwayRouteBookmark(Member member, String departure, String destination, String departureLine, String destinationLine){
        this.member = member;
        this.departure = departure;
        this.destination = destination;
        this.departureLine = departureLine;
        this.destinationLine =destinationLine;
    }
}
