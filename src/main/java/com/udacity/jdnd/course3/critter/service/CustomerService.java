package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PetRepository petRepository;

    /* Retrieve all the existing customers */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }

    /* Retrieve customer details for given Pet Id */
    public Customer getCustomerByPetId(Long petId) {
        Customer customer = petRepository.getOne(petId).getCustomer();
        return customer;
    }

    /* Add new customer aling with his pets list into the system */
    public Customer addCustomer(Customer customer, List<Long> petIds) {
        List<Pet> customerPets = new ArrayList<>();

        if (petIds != null && !petIds.isEmpty()) {
            customerPets = petIds.stream().map((petId) -> petRepository.getOne(petId)).collect(Collectors.toList());
        }
        customer.setPets(customerPets);

        return customerRepository.save(customer);
    }


}
