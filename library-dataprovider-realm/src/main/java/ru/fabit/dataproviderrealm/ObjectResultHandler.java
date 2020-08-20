package ru.fabit.dataproviderrealm;

import java.io.IOException;


public class ObjectResultHandler<T> extends ResultHandler<T> {

    private Class<T> resultType;

    public ObjectResultHandler(Class<T> resultType) {
        this.resultType = resultType;
    }

    @Override
    public T handleResult(String rawJson) throws IOException {
        return Mapper.objectOrThrow(rawJson, resultType);
    }
}