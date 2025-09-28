package com.ecommerce.api.shared.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private List<T> data;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;
}
