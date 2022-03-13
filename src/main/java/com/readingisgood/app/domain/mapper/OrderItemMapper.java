package com.readingisgood.app.domain.mapper;

import com.readingisgood.app.domain.dto.OrderItemDto;
import com.readingisgood.app.domain.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemDto map(OrderItem orderItem);
}
