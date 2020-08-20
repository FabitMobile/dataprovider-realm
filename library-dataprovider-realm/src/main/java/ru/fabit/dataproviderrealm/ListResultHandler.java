package ru.fabit.dataproviderrealm;

import java.io.IOException;
import java.util.List;


public class ListResultHandler<T> extends ResultHandler<List<T>> {

    private Class<T> resultType;

    public ListResultHandler(Class<T> resultType) {
        this.resultType = resultType;
    }

    @Override
    public List<T> handleResult(String rawJson) throws IOException {
        return Mapper.objectOrThrow(rawJson,
                Mapper.get().getTypeFactory().constructCollectionType(List.class, resultType));
    }
}
