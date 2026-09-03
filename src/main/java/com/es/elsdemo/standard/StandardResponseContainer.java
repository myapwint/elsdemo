package com.es.elsdemo.standard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StandardResponseContainer {
    private List<Object> result;
    private boolean success;

    public static StandardResponseContainer success(Object obj) {
        return new StandardResponseContainer(List.of(obj), true);
    }
}
