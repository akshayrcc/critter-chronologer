package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    /* Fetch Employee using emp Id */
    public Employee getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        return employee;
    }

    /* Fetch employee on the basis of sills he serves */
    public List<Employee> getEmployeesBySkills(LocalDate date, Set<EmployeeSkill> skills) {
        List<Employee> employees = employeeRepository
                .findByDaysAvailable(date.getDayOfWeek()).stream()
                .filter(employee -> employee.getSkills().containsAll(skills))
                .collect(Collectors.toList());
        return employees;
    }

    /* Persists New employee */
    public Employee addEmployee(Employee employee) {
        Employee newEmployee = employeeRepository.save(employee);
        return newEmployee;
    }

    /* Set Available Days for the given employee */
    public void setEmployeeAvailabilityForDays (Set<DayOfWeek> days, Long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setDaysAvailable(days);
        employeeRepository.save(employee);
    }
}
