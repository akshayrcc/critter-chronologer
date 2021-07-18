package com.udacity.jdnd.course3.critter.entities;

import com.udacity.jdnd.course3.critter.pet.PetType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pet {
    @Id
    @GeneratedValue
    private Long id;

    private PetType type;

    @Nationalized
    private String name;

    @ManyToOne(targetEntity = Customer.class, optional = false)
    private Customer customer;

    private LocalDate birthDate;
    private String notes;

    public Pet(PetType type, String name, LocalDate birthDate, String notes) {
        this.type = type;
        this.name = name;
        this.birthDate = birthDate;
        this.notes = notes;
    }
}
