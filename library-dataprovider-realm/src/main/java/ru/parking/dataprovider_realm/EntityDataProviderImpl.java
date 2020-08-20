package ru.parking.dataprovider_realm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmModel;
import io.realm.RealmQuery;
import io.realm.Sort;
import ru.parking.localservice_realm.AggregationFunction;
import ru.parking.localservice_realm.LocalService;
import ru.parking.localservice_realm.LocalServiceParams;
import ru.parking.localservice_realm.commonmapper.CommonMapper;
import ru.parking.remoteservice.RemoteService;
import ru.parking.utils.Optional;


@SuppressWarnings("unchecked")
public class EntityDataProviderImpl implements EntityDataProvider {

    protected final RemoteService remoteService;
    protected final LocalService localService;


    public EntityDataProviderImpl(RemoteService remoteService,
                                  LocalService localService) {
        this.remoteService = remoteService;
        this.localService = localService;
    }

    //region ===================== Remote ======================

    public <InputType, ReturnType> Observable<Optional<ReturnType>> getRemoteObject(RemoteRequest<InputType, ReturnType> remoteRequest) {
        String path = remoteRequest.getPath();
        int requestMethod = remoteRequest.getRequestMethod();
        final String entityPathRemote = remoteRequest.getEntityPathRemote();
        final String entityPathLocal = remoteRequest.getEntityPathLocal();
        final ResultHandler<InputType> resultHandler = remoteRequest.getResultHandler();
        final LocalDataStore localDataStore = remoteRequest.getLocalDataStore();
        final CommonMapper<InputType, ReturnType> dataToDomainMapper = remoteRequest.getDataToDomainMapper();
        HashMap<String, Object> params = remoteRequest.getParams();
        HashMap<String, String> headers = remoteRequest.getHeaders();
        final Boolean isAllJsonSave = remoteRequest.isAllJsonSave();

        Observable<JSONObject> jsonObjectObservable = getJsonObjectObservable(path, requestMethod, params, headers);

        return jsonObjectObservable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<JSONObject, Optional<ReturnType>>() {
                    @Override
                    public Optional<ReturnType> apply(JSONObject jsonObject) {
                        InputType entity = null;
                        if (resultHandler != null) {
                            try {
                                entity = getEntity(jsonObject,
                                        localDataStore,
                                        resultHandler,
                                        entityPathRemote,
                                        entityPathLocal,
                                        isAllJsonSave);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Optional<ReturnType> domain = Optional.createEmpty();
                        if (entity != null && dataToDomainMapper != null) {
                            if (resultHandler != null) {
                                domain = new Optional<>(dataToDomainMapper.map(entity));
                            }
                        }
                        return domain;
                    }
                });
    }

    public <InputType, ReturnType> Observable<Optional<List<ReturnType>>> getRemoteList(RemoteRequest<InputType, ReturnType> remoteRequest) {
        String path = remoteRequest.getPath();
        int requestMethod = remoteRequest.getRequestMethod();
        final String entityPathRemote = remoteRequest.getEntityPathRemote();
        final String entityPathLocal = remoteRequest.getEntityPathLocal();
        final ResultHandler<InputType> resultHandler = remoteRequest.getResultHandler();
        final LocalDataStore localDataStore = remoteRequest.getLocalDataStore();
        final CommonMapper<InputType, ReturnType> dataToDomainMapper = remoteRequest.getDataToDomainMapper();
        HashMap<String, Object> params = remoteRequest.getParams();
        HashMap<String, String> headers = remoteRequest.getHeaders();
        final Boolean isAllJsonSave = remoteRequest.isAllJsonSave();

        Observable<JSONObject> jsonObjectObservable = getJsonObjectObservable(path, requestMethod, params, headers);

        return jsonObjectObservable
                .subscribeOn(Schedulers.io())
                .map(new Function<JSONObject, Optional<List<ReturnType>>>() {
                    @Override
                    public Optional<List<ReturnType>> apply(JSONObject jsonObject) {
                        InputType entity = null;
                        if (resultHandler != null) {
                            try {
                                entity = getEntity(jsonObject,
                                        localDataStore,
                                        resultHandler,
                                        entityPathRemote,
                                        entityPathLocal,
                                        isAllJsonSave);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Optional<List<ReturnType>> domain = Optional.createEmpty();
                        if (entity != null && dataToDomainMapper != null) {
                            if (resultHandler != null) {
                                domain = new Optional<>(dataToDomainMapper.map((List<InputType>) entity));
                            }
                        }
                        return domain;
                    }
                });
    }

    private Observable<JSONObject> getJsonObjectObservable(String path, int requestMethod, HashMap<String, Object> params, HashMap<String, String> headers) {
        return remoteService.getRemoteJson(
                requestMethod,
                path,
                params,
                headers,
                this);
    }

    //endregion

    //region ===================== Local ======================

    public <InputType, ReturnType> Observable<Optional<List<ReturnType>>> getLocalList(Class clazz,
                                                                                       Function<RealmQuery, RealmQuery> predicate,
                                                                                       CommonMapper<InputType, ReturnType> commonMapper) {
        return getLocalList(clazz, predicate, null, commonMapper);
    }

    public <InputType, ReturnType> Observable<Optional<List<ReturnType>>> getLocalList(Class clazz,
                                                                                       Function<RealmQuery, RealmQuery> predicate,
                                                                                       Map.Entry<String[], Sort[]> sortPair,
                                                                                       CommonMapper<InputType, ReturnType> commonMapper) {
        return localService.get(new LocalServiceParams.Builder<InputType, ReturnType>(clazz)
                .predicate(predicate)
                .sortPair(sortPair)
                .mapper(commonMapper)
                .build())
                .toObservable()
                .map(new Function<List<ReturnType>, Optional<List<ReturnType>>>() {
                    @Override
                    public Optional<List<ReturnType>> apply(List<ReturnType> returnTypes) throws Exception {
                        Optional<List<ReturnType>> optional = new Optional(returnTypes);
                        return optional;
                    }
                });
    }

    public <InputType, ReturnType> Observable<Optional<ReturnType>> getLocalObject(Class clazz,
                                                                                   Function<RealmQuery, RealmQuery> predicate,
                                                                                   CommonMapper<InputType, ReturnType> commonMapper) {
        return getLocalList(clazz, predicate, commonMapper)
                .map(new Function<Optional<List<ReturnType>>, Optional<ReturnType>>() {
                    @Override
                    public Optional<ReturnType> apply(Optional<List<ReturnType>> optionalRealmObjects) throws Exception {
                        Optional<ReturnType> optionalRealmObject = Optional.createEmpty();
                        if (optionalRealmObjects.isNotNull() && optionalRealmObjects.getValue().size() > 0) {
                            optionalRealmObject = new Optional<>(optionalRealmObjects.getValue().get(0));
                        }
                        return optionalRealmObject;
                    }
                });
    }

    @Override
    public Completable updateLocal(Class clazz,
                                   Function<RealmQuery, RealmQuery> predicate,
                                   Consumer<RealmModel> action) {
        return localService.update(clazz, predicate, action);
    }

    @Override
    public Completable deleteLocal(Class clazz, Function<RealmQuery, RealmQuery> predicate) {
        return localService.delete(clazz, predicate);
    }

    @Override
    public Observable<Optional<Number>> getLocalAggregationFuncValue(Class clazz, Function<RealmQuery, RealmQuery> predicate, AggregationFunction aggregationFunction, String nameField) {
        return localService.get(clazz, predicate, aggregationFunction, nameField)
                .toObservable()
                .map(new Function<Optional<Number>, Optional<Number>>() {
                    @Override
                    public Optional<Number> apply(Optional<Number> numberOptional) throws Exception {
                        Optional<Number> optionalRealmObject = Optional.createEmpty();
                        if (numberOptional.isNotNull()) {
                            optionalRealmObject = new Optional<>(numberOptional.getValue());
                        }
                        return optionalRealmObject;
                    }
                });
    }

    //endregion

    //region ===================== Internal logic ======================

    private <T> T getEntity(JSONObject response,
                            LocalDataStore localDataStore,
                            ResultHandler<T> resultHandler,
                            String entityPathRemote,
                            String entityPathLocal,
                            Boolean isAllJsonSave) throws JSONException {

        long start = System.currentTimeMillis();
        T entity = null;

        try {
            String entityJson = response.toString();
            response = new JSONObject(entityJson);

            if (entityPathLocal != null && response.has(entityPathLocal)) {
                String entityJsonLocal = response.get(entityPathLocal).toString();
                localDataStore.store(entityJsonLocal);
            } else if (isAllJsonSave && localDataStore != null) {
                localDataStore.store(response.toString());
            }

            String entityJsonRemote;
            if (entityPathRemote != null) {
                entityJsonRemote = response.get(entityPathRemote).toString();
            } else {
                entityJsonRemote = response.toString();
            }
            entity = resultHandler.handleResult(entityJsonRemote);

        } catch (IOException e) {
            e.printStackTrace();
        }
        long time = System.currentTimeMillis() - start;
        return entity;
    }

    //endregion
}