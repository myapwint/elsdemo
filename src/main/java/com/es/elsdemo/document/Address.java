package com.es.elsdemo.document;

import lombok.Data;

@Data
public class Address {
    private String line1;
    private String line2;
    private String city;
    private String postalCode;
    private String country;
}
