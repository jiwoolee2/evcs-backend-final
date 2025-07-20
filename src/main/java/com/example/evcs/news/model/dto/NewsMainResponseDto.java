package com.example.evcs.news.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsMainResponseDto {
    private int total;
    private int start;
    private int display;
    private List<NewsItem> items;
}

