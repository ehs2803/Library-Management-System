package com.ehs.library.bookapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookApiResultDto {
    private int total;
    private int start;
    private int display;
    private List<BookApiDto> items = new ArrayList<>();

}
