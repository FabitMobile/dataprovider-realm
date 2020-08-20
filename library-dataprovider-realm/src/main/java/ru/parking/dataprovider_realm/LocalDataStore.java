package ru.parking.dataprovider_realm;

import org.json.JSONException;

import ru.parking.localservice_realm.LocalService;


public abstract class LocalDataStore {

    protected final LocalService localService;

    protected LocalDataStore(LocalService localService) {
        this.localService = localService;
    }

    public abstract void store(String rawJson) throws JSONException;
}
