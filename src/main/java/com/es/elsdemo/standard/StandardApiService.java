package com.es.elsdemo.standard;

public abstract class StandardApiService<T extends StandardRequestContainer<?>, S extends StandardResponseContainer> {
    public abstract S fetch(T request);
    public abstract S save(T request);
}
