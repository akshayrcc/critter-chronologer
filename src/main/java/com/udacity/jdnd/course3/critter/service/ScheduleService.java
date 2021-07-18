package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PetRepository petRepository;

    /*Fetch all the existing schedules*/
    public List<Schedule> getAllSchedules() {
        List<Schedule> allSchedules = scheduleRepository.findAll();
        return allSchedules;
    }

    /*Fetch Schedules for given customer*/
    public List<Schedule> getScheduleForCustomer(Long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        List<Schedule> schedules = scheduleRepository.findByPetsIn(customer.getPets());
        return schedules;
    }

    /*Fetch schedule for given employee*/
    public List<Schedule> getScheduleForEmployee(Long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        List<Schedule> schedules = scheduleRepository.findByEmployee(employee);
        return schedules;
    }

    /*Fetch schedule for given pet*/
    public List<Schedule> getScheduleForPet(Long petId) {
        Pet pet = petRepository.getOne(petId);
        List<Schedule> schedules = scheduleRepository.findByPets(pet);
        return schedules;
    }

    /*Store a new schedule for emps with pets*/
    public Schedule addSchedule(Schedule schedule, List<Long> employeeIds, List<Long> petIds) {
        List<Pet> pets = petRepository.findAllById(petIds);
        schedule.setPets(pets);

        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        schedule.setEmployee(employees);

        return scheduleRepository.save(schedule);
    }
}
