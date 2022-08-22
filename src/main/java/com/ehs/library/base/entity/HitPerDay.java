package com.ehs.library.base.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="hit_per_day")
@Getter @Setter
@ToString
public class HitPerDay {
    @Id
    @Column(name="hit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int hit;

    private LocalDate day;

    public HitPerDay(){

    }
}
