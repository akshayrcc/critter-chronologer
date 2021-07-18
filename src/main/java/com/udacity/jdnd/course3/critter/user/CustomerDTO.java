package com.udacity.jdnd.course3.critter.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the form that customer request and response data takes. Does not map
 * to the database directly.
 */

@Getter
@Setter
public class CustomerDTO {
    private long id;
    private String name;
    private String phoneNumber;
    private String notes;
    private List<Long> petIds;
}
