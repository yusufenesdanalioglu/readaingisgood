package com.readingisgood.app.service;

import com.readingisgood.app.domain.dto.CustomerDto;
import com.readingisgood.app.domain.mapper.CustomerMapper;
import com.readingisgood.app.domain.response.PageableResponse;
import com.readingisgood.app.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository repository;

    private final CustomerMapper mapper;

    public CustomerDto create(CustomerDto customer) {
        return mapper.map(repository.save(mapper.map(customer)));
    }

    public PageableResponse<CustomerDto> list(Pageable pageable) {
        return PageableResponse.from(repository.findAll(pageable).map(mapper::map));
    }
}
