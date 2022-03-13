package com.readingisgood.app.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageableResponse<T> {

    private List<T> content;

    private PageResponse page;

    public static <T> PageableResponse<T> from(Page<T> page) {
        PageResponse pageResponse = new PageResponse(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
        return new PageableResponse<>(page.getContent(), pageResponse);
    }
}
