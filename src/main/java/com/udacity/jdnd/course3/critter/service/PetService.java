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

@Service
@Transactional
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    /* Fetch all available Pets */
    public List<Pet> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets;
    }

    /*Fetch a pet details using Pet Id*/
    public Pet getPetByPetId(Long petId) {
        Pet pet = petRepository.getOne(petId);
        return pet;
    }

    /*Fetch Pets for the given customer*/
    public List<Pet> getPetsByCustomerId(long customerId) {
        List<Pet> pets = petRepository.findPetByCustomerId(customerId);
        return pets;
    }

    /*Add given pet for a given customer*/
    public Pet addPet(Pet pet, Long customerId) {

        Customer customer = customerRepository.getOne(customerId);
        pet.setCustomer(customer);
        pet = petRepository.save(pet);

        List<Pet> pets = new ArrayList<>();
        pets.add(pet);
        customer.setPets(pets);

        customerRepository.save(customer);

        return pet;
    }

}
