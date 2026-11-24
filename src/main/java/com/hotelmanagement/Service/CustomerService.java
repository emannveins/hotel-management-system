
package com.hotelmanagement.Service;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.hotelmanagement.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // creating user
    public CustomerResponse createCustomer( CreateCustomerRequest request) {

        // controllo email duplicata
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException( HttpStatus.CONFLICT,  "email already exsist! " );
        }

        // creazione entity
        CustomerEntity customer = new CustomerEntity();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        // salvo nel db
        CustomerEntity savedCustomer = customerRepository.save(customer);

        // conversione di entity in response
        return toResponse(savedCustomer);
    }


    // recupero tutti i clienti
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // recupero cliente per l'id
    public CustomerResponse getCustomerById(Long id) {

        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente non trovato"
                ));

        return toResponse(customer);
    }


    // conversione da Entity a Response DTO
    private CustomerResponse toResponse(CustomerEntity customer) {

        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }
}
