package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    /*Conversion method from entity to DTO for Schedule*/
    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {

        List<Long> employeeIds = schedule.getEmployee().stream().map(Employee::getId).collect(Collectors.toList());
        List<Long> petIds = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());

        return new ScheduleDTO(schedule.getId(), employeeIds, petIds, schedule.getDate(), schedule.getActivities());
    }

    /* Create a new schedule */
    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule(scheduleDTO.getDate(), scheduleDTO.getActivities());
        ScheduleDTO convertedScheduleToDTO;
        try {
            convertedScheduleToDTO = convertScheduleToScheduleDTO(scheduleService.addSchedule(schedule, scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds()));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule could not be created", exception);
        }
        return convertedScheduleToDTO;
    }

    /*Fetch all the present schedules*/
    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> allSchedulesAvailable;
        try {
            allSchedulesAvailable = scheduleService.getAllSchedules();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Schedules found.", e);
        }
        return allSchedulesAvailable.stream().map(schedule -> convertScheduleToScheduleDTO(schedule)).collect(Collectors.toList());

    }

    /*Get List of all the schedules present for the given Pet*/
    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedulesAvailableForPet;
        try {
            schedulesAvailableForPet = scheduleService.getScheduleForPet(petId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Schedules found for Pet Id " + petId, e);
        }
        return schedulesAvailableForPet.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }


    /*Get List of Schedules for the given Employee*/
    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedulesForGivenEmployee;
        try {
            schedulesForGivenEmployee = scheduleService.getScheduleForEmployee(employeeId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No Schedules found for Employee Id " + employeeId, e);
        }
        return schedulesForGivenEmployee.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    /*Get List of Schedules for the given Customer*/
    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedulesForGivenCustomer;
        try {
            schedulesForGivenCustomer = scheduleService.getScheduleForCustomer(customerId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " No Schedules found for Employee Id " + customerId, e);
        }
        return schedulesForGivenCustomer.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }
}
