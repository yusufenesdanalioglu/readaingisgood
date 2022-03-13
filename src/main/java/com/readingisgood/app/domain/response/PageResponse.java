package com.readingisgood.app.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageResponse {

    private Integer number;

    private Integer size;

    private Long totalElements;

    private Integer totalPages;
}
