package com.udacity.jdnd.course3.critter.entities;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Table
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Schedule {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(targetEntity = Employee.class)
    private List<Employee> employee;

    @ManyToMany(targetEntity = Pet.class)
    private List<Pet> pets;

    private LocalDate date;

    @ElementCollection
    private Set<EmployeeSkill> activities;

    public Schedule(LocalDate date, Set<EmployeeSkill> activities) {
        this.date = date;
        this.activities = activities;
    }
}
