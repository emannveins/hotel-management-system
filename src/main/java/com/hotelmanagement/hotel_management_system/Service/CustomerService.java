
package com.hotelmanagement.hotel_management_system.service;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // 1. Creazione cliente
    public CustomerResponse createCustomer(
            CreateCustomerRequest request) {

        // Controllo email duplicata
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email gia registrata"
            );
        }

        // Creazione Entity
        CustomerEntity customer = new CustomerEntity();

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        // Salvataggio nel database
        CustomerEntity savedCustomer =
                customerRepository.save(customer);

        // Conversione Entity -> Response DTO
        return toResponse(savedCustomer);
    }


    // 2. Recupero di tutti i clienti
    public List<CustomerResponse> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // 3. Recupero cliente tramite ID
    public CustomerResponse getCustomerById(Long id) {

        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente non trovato"
                ));

        return toResponse(customer);
    }


    // Metodo di supporto: Entity -> Response DTO
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
