package com.readingisgood.app.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDto {

    private Long id;

    @Size(min = 3, max = 60)
    @NotNull
    private String fullName;
}
