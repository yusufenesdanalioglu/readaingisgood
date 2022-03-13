package com.readingisgood.app.service;

import com.readingisgood.app.TestUtils;
import com.readingisgood.app.domain.dto.CustomerDto;
import com.readingisgood.app.domain.entity.Customer;
import com.readingisgood.app.domain.mapper.CustomerMapper;
import com.readingisgood.app.domain.response.PageableResponse;
import com.readingisgood.app.repository.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    private CustomerService service;

    @BeforeEach
    public void init() {
        service = new CustomerService(repository, Mappers.getMapper(CustomerMapper.class));
    }

    @Test
    public void create_WhenInputGiven_ThenShouldReturnSavedCustomer() {
        //Given
        Customer customer = TestUtils.getCustomer();
        CustomerDto customerDto = TestUtils.getCustomerDto();
        Mockito.when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);
        //When
        CustomerDto savedCustomer = service.create(customerDto);
        //Then
        Mockito.verify(repository).save(Mockito.any());
        Assertions.assertThat(savedCustomer).usingRecursiveComparison().isEqualTo(customer);
    }

    @Test
    public void list_WhenInputGiven_ThenShouldReturnListOfCustomers() {
        //Given
        int pageSize = 10;
        Pageable pageable = Pageable.ofSize(pageSize);
        Page<Customer> page = new PageImpl(List.of(TestUtils.getCustomer()), pageable, 1);
        PageableResponse<Customer> expected = PageableResponse.from(page);
        Mockito.when(repository.findAll(Mockito.any(Pageable.class))).thenReturn(page);
        //When
        PageableResponse<CustomerDto> customers = service.list(pageable);
        //Then
        Mockito.verify(repository).findAll(Mockito.any(Pageable.class));
        Assertions.assertThat(customers).usingRecursiveComparison().isEqualTo(expected);
    }
}
