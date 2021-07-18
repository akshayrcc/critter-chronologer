package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    /*Adding Pet with the help of the PetDTO object
     * conversion.
     */
    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = new Pet(petDTO.getType(), petDTO.getName(), petDTO.getBirthDate(), petDTO.getNotes());
        PetDTO convertedPet;
        try {
            convertedPet = convertPetToPetDTO(petService.addPet(pet, petDTO.getOwnerId()));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet could not be added", exception);
        }
        return convertedPet;
    }

    /*Get the pet by Pet Id given*/
    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet;
        try {
            pet = petService.getPetByPetId(petId);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet with id: " + petId + " is not found", exception);
        }
        return convertPetToPetDTO(pet);
    }

    /*Return all the pets available as list*/
    @GetMapping
    public List<PetDTO> getPets() {
        List<Pet> pets = petService.getAllPets();
        return pets.stream().map(this::convertPetToPetDTO).collect(Collectors.toList());
    }

    /*Get the pet for given owner Id*/
    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets;
        try {
            pets = petService.getPetsByCustomerId(ownerId);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pets for owner id: " + ownerId + " not found", e);
        }
        return pets.stream().map(this::convertPetToPetDTO).collect(Collectors.toList());
    }


    /*Entity to DTO Conversion method*/
    private PetDTO convertPetToPetDTO(Pet pet) {
        return new PetDTO(pet.getId(), pet.getType(), pet.getName(), pet.getCustomer().getId(), pet.getBirthDate(), pet.getNotes());
    }
}
