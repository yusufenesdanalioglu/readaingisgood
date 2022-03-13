package com.readingisgood.app.domain.mapper;

import com.readingisgood.app.domain.dto.OrderDto;
import com.readingisgood.app.domain.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto map(Order order);
}
