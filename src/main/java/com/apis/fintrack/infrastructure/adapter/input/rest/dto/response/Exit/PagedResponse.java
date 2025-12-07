package com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse <T>{
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int size;
    private boolean last;

}
