package ru.parking.dataprovider_realm;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.RealmModel;
import io.realm.RealmQuery;
import ru.parking.localservice_realm.AggregationFunction;
import ru.parking.localservice_realm.commonmapper.CommonMapper;
import ru.parking.utils.Optional;


public interface EntityDataProvider {

    <InputType, ReturnType> Observable<Optional<List<ReturnType>>> getRemoteList(RemoteRequest<InputType, ReturnType> remoteRequest);

    <InputType, ReturnType> Observable<Optional<ReturnType>> getRemoteObject(RemoteRequest<InputType, ReturnType> remoteRequest);

    <InputType, ReturnType> Observable<Optional<List<ReturnType>>> getLocalList(Class clazz,
                                                                                Function<RealmQuery, RealmQuery> predicate,
                                                                                CommonMapper<InputType, ReturnType> dataToDomainCommonMapper);

    Observable<Optional<Number>> getLocalAggregationFuncValue(Class clazz,
                                                              Function<RealmQuery, RealmQuery> predicate,
                                                              AggregationFunction aggregationFunction,
                                                              String nameField);

    Completable updateLocal(Class clazz,
                            Function<RealmQuery, RealmQuery> predicate,
                            Consumer<RealmModel> action);

    Completable deleteLocal(Class clazz, Function<RealmQuery, RealmQuery> predicate);
}
