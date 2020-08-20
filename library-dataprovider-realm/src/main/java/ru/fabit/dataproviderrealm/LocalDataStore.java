package ru.fabit.dataproviderrealm;

import org.json.JSONException;

import ru.fabit.localservicerealm.LocalService;


public abstract class LocalDataStore {

    protected final LocalService localService;

    protected LocalDataStore(LocalService localService) {
        this.localService = localService;
    }

    public abstract void store(String rawJson) throws JSONException;
}
