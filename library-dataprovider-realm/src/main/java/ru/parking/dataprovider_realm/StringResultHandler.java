package ru.parking.dataprovider_realm;

import java.io.IOException;


public class StringResultHandler extends ResultHandler<String> {

    @Override
    public String handleResult(String rawJson) throws IOException {
        return rawJson;
    }
}