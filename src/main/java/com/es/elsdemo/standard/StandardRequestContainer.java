package com.es.elsdemo.standard;

import lombok.Data;

@Data
public class StandardRequestContainer<T extends StandardRequestDTO<?>> {
    private String apiName;
    private T payload;
}
