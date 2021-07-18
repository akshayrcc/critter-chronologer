package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;


    /*Entities to DTO Conversion Methods for Employee and Customer Objects*/
    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee) {
        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getSkills(), employee.getDaysAvailable());
    }

    private CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
        List<Long> petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getPhoneNumber(), customer.getNotes(), petIds);
    }

    /*Add a new Customer*/
    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {

        Customer customer = new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getPhoneNumber(), customerDTO.getNotes());

        List<Long> petIds = customerDTO.getPetIds();

        CustomerDTO convertedCustomerDTO;
        try {
            convertedCustomerDTO = convertCustomerToCustomerDTO(customerService.addCustomer(customer, petIds));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer could not be saved", exception);
        }

        return convertedCustomerDTO;
    }

    /*Fetch all the available Customers*/
    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> allCustomersavailable;
        try {
            allCustomersavailable = customerService.getAllCustomers();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customers could not be found", e);
        }
        return allCustomersavailable.stream().map(this::convertCustomerToCustomerDTO).collect(Collectors.toList());
    }

    /*Fetch Customer-Owner of given Pet */
    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Customer custForGivenPetId;
        try {
            custForGivenPetId = customerService.getCustomerByPetId(petId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customers could not be found for Pet Id " + petId, e);
        }
        return convertCustomerToCustomerDTO(custForGivenPetId);
    }

    /*Add new employee*/
    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {

        Employee newEmployee = new Employee(employeeDTO.getId(), employeeDTO.getName(), employeeDTO.getSkills(), employeeDTO.getDaysAvailable());
        EmployeeDTO convertedNewEmpToDTO;

        try {
            convertedNewEmpToDTO = convertEmployeeToEmployeeDTO(employeeService.addEmployee(newEmployee));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee Could Not be added.", e);
        }

        return convertedNewEmpToDTO;

    }

    /*Fetch Employee using Emp Id*/
    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee empById;
        try {
            empById = employeeService.getEmployeeById(employeeId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Could Not be found for Id " + employeeId, e);
        }
        return convertEmployeeToEmployeeDTO(empById);
    }

    /*Set the appointment for employee on the given date*/
    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try {
            employeeService.setEmployeeAvailabilityForDays(daysAvailable, employeeId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error setting the appointment with employee" + employeeId, e);
        }
    }

    /*Get the availability for the given date*/
    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees;
        try {
            employees = employeeService.getEmployeesBySkills(employeeDTO.getDate(), employeeDTO.getSkills());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee Not Available", e);
        }

        return employees.stream().map(this::convertEmployeeToEmployeeDTO).collect(Collectors.toList());
    }

    /*Get all Employees*/
    @GetMapping("/employee")
    public List<EmployeeDTO> findAllEmployees() {
        List<Employee> employees;
        try {
            employees = employeeService.getAllEmployees();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employees Not Available", e);
        }

        return employees.stream().map(this::convertEmployeeToEmployeeDTO).collect(Collectors.toList());
    }

}
