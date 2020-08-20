package ru.parking.dataprovider_realm;

import java.io.IOException;


public abstract class ResultHandler<T> {
    public abstract T handleResult(String rawJson) throws IOException;
}
