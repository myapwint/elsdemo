package com.es.elsdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDTO {
    private String id;
    private String email;
    private String name;
    private Address address;


}
