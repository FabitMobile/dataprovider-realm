package ru.parking.dataprovider_realm;

import java.util.HashMap;
import java.util.Objects;

import ru.parking.localservice_realm.commonmapper.CommonMapper;

public class RemoteRequest<InputType, ReturnType> {

    private String path;
    private int requestMethod;
    private String entityPathRemote;
    private String entityPathLocal;
    private ResultHandler<InputType> resultHandler;
    private LocalDataStore localDataStore;
    private CommonMapper<InputType, ReturnType> commonMapper;
    private Boolean isAllJsonSave;
    private HashMap<String, Object> params;
    private HashMap<String, String> headers;


    public static class Builder<InputType, ReturnType> {
        private String path;
        private int requestMethod;
        private String entityPathRemote = null;
        private String entityPathLocal = null;
        private ResultHandler<InputType> resultHandler = null;
        private LocalDataStore localDataStore = null;
        private CommonMapper commonMapper = null;
        private Boolean isAllJsonSave = false;
        private HashMap<String, Object> params = new HashMap<>();
        private HashMap<String, String> headers = new HashMap<>();

        public Builder(String path, int requestMethod) {
            this.path = path;
            this.requestMethod = requestMethod;
        }

        public Builder entityPathRemote(String val) {
            entityPathRemote = val;
            return this;
        }

        public Builder entityPathLocal(String val) {
            entityPathLocal = val;
            return this;
        }

        public Builder resultHandler(ResultHandler<InputType> val) {
            resultHandler = val;
            return this;
        }

        public Builder localDataStore(LocalDataStore val) {
            localDataStore = val;
            return this;
        }

        public Builder mapper(CommonMapper<InputType, ReturnType> val) {
            commonMapper = val;
            return this;
        }

        public Builder params(HashMap<String, Object> val) {
            params = val;
            return this;
        }

        public Builder header(HashMap<String, String> val) {
            headers = val;
            return this;
        }

        public Builder isAllJsonSave(Boolean val) {
            isAllJsonSave = val;
            return this;
        }

        public RemoteRequest<InputType, ReturnType> build() {
            return new RemoteRequest<>(this);
        }
    }

    private RemoteRequest(Builder builder) {
        path = builder.path;
        requestMethod = builder.requestMethod;
        entityPathRemote = builder.entityPathRemote;
        entityPathLocal = builder.entityPathLocal;
        resultHandler = builder.resultHandler;
        localDataStore = builder.localDataStore;
        commonMapper = builder.commonMapper;
        params = builder.params;
        headers = builder.headers;
        isAllJsonSave = builder.isAllJsonSave;
    }


    public String getPath() {
        return path;
    }

    public int getRequestMethod() {
        return requestMethod;
    }

    public String getEntityPathRemote() {
        return entityPathRemote;
    }

    public String getEntityPathLocal() {
        return entityPathLocal;
    }

    public ResultHandler<InputType> getResultHandler() {
        return resultHandler;
    }

    public LocalDataStore getLocalDataStore() {
        return localDataStore;
    }

    public CommonMapper<InputType, ReturnType> getDataToDomainMapper() {
        return commonMapper;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public Boolean isAllJsonSave() {
        return isAllJsonSave;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RemoteRequest<?, ?> that = (RemoteRequest<?, ?>) o;

        if (requestMethod != that.requestMethod) return false;
        if (!Objects.equals(path, that.path)) return false;
        if (!Objects.equals(entityPathRemote, that.entityPathRemote))
            return false;
        if (!Objects.equals(entityPathLocal, that.entityPathLocal))
            return false;
        if (!Objects.equals(resultHandler, that.resultHandler))
            return false;
        if (!Objects.equals(localDataStore, that.localDataStore))
            return false;
        if (!Objects.equals(commonMapper, that.commonMapper))
            return false;
        if (!Objects.equals(params, that.params)) return false;
        return Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + requestMethod;
        result = 31 * result + (entityPathRemote != null ? entityPathRemote.hashCode() : 0);
        result = 31 * result + (entityPathLocal != null ? entityPathLocal.hashCode() : 0);
        result = 31 * result + (resultHandler != null ? resultHandler.hashCode() : 0);
        result = 31 * result + (localDataStore != null ? localDataStore.hashCode() : 0);
        result = 31 * result + (commonMapper != null ? commonMapper.hashCode() : 0);
        result = 31 * result + (params != null ? params.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        return result;
    }
}