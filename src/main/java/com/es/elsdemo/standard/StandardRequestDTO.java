package com.es.elsdemo.standard;

import lombok.Data;

@Data
public abstract class StandardRequestDTO<T> {
    private T document;
}
