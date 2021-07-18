package com.udacity.jdnd.course3.critter.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    @Nationalized
    private String name;

    private String phoneNumber;

    private String notes;

    @OneToMany(targetEntity = Pet.class)
    private List<Pet> pets;

    public Customer(Long id, String name, String phoneNumber, String notes) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
    }

}
