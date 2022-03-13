package com.readingisgood.app.domain.mapper;

import com.readingisgood.app.domain.dto.CustomerDto;
import com.readingisgood.app.domain.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto map(Customer customer);

    Customer map(CustomerDto customer);
}
