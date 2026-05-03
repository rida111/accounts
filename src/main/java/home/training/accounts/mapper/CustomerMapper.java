package home.training.accounts.mapper;

import java.time.LocalDateTime;

import home.training.accounts.dtos.CustomerDto;
import home.training.accounts.entities.Customer;

public class CustomerMapper {

    public static CustomerDto mapToCustomerDto(Customer customer, CustomerDto customerDto) {
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        return customerDto;
    }

    public static Customer mapToCustomer(CustomerDto customerDto, Customer customer) {
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        
        //audit fields:
//        customer.setCreatedAt(LocalDateTime.now());
//        customer.setCreatedBy(loginUser);
//        customer.setUpdatedAt(LocalDateTime.now());
//        customer.setUpdatedBy(loginUser);
//        
        return customer;
    }

}
