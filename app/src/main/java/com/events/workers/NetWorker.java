package com.events.workers;

import com.events.workers.callbacks.NetWorkerCallback;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public interface NetWorker extends Worker {

    String Payload = "payload";

    interface Header {
        String ContentType      = "Content-Type";
        String Accept           = "Accept";
        String Authorization    = "Authorization";
    }

    interface Type {
        String AppJson          = "application/json";
        String AppJsonUTF8      = "application/json; charset=utf-8";
        String Stream           = "application/octet-stream";
    }

    enum ReqType {
        Json,
        MultiPart,
        AppFormData
    }

    enum RespType {
        Json,
        Stream
    }

    void        get 	(String baseUrl, LinkedHashMap<String, Object> queryParams, Map<String, String> headers, RespType respType, NetWorkerCallback networkCallback);
    JsonNode    get 	(String baseUrl, LinkedHashMap<String, Object> queryParams, Map<String, String> headers) throws IOException;
    
    void        post 	(String baseUrl, JsonNode reqBody, NetWorkerCallback networkCallback);
    void        post 	(String baseUrl, JsonNode reqBody, String authToken, NetWorkerCallback networkCallback);
    void        post 	(String baseUrl, LinkedHashMap<String, Object> queryParams, Map<String, String> headers, ReqType reqType, RespType respType, Map<String, Object> reqBody, NetWorkerCallback networkCallback);
    JsonNode    post 	(String baseUrl, JsonNode reqBody) throws IOException;
    
    void        put 	(String baseUrl, LinkedHashMap<String, Object> queryParams, Map<String, String> headers, ReqType reqType, RespType respType, Map<String, Object> reqBody, NetWorkerCallback networkCallback);

    void        delete 	(String baseUrl, LinkedHashMap<String, Object> queryParams, Map<String, String> headers, RespType respType, NetWorkerCallback networkCallback);

}