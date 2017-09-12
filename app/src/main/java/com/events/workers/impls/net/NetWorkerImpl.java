package com.events.workers.impls.net;

import android.os.Handler;
import android.os.Looper;
import com.events.utils.ErrorUtils;
import com.events.utils.Http;
import com.events.utils.Json;
import com.events.utils.Lang;
import com.events.utils.Logger;
import com.events.workers.NetWorker;
import com.events.workers.callbacks.NetWorkerCallback;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.events.model.Error;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetWorkerImpl implements NetWorker {

    // TODO : add failure Network handling
	// TODO : clean this code and remove duplicate code
	// TODO : make the network interface consistent between verbs
    public NetWorkerImpl () {
    }

    @Override
    public void get (String baseUrl, LinkedHashMap<String, Object> queryParams, Map<String, String> headers, final RespType respType, final NetWorkerCallback networkCallback) {
        if (Lang.isNullOrEmpty (baseUrl)) {
            networkCallback.onUIFailure (ErrorUtils.getError ("No url specified.", null));
            return;
        }

        networkCallback.onUIStart ();

        Request.Builder builder = buildReqBuilder (baseUrl, queryParams, headers);

        Request request         = builder.get ().build ();

        fireRequest (request, respType, networkCallback);
    }

    @Override
    public JsonNode get (String baseUrl, LinkedHashMap<String, Object> queryParams, Map<String, String> headers) throws IOException {
        if (Lang.isNullOrEmpty (baseUrl)) {
            throw new NullPointerException ("No url specified.");
        }

        Request.Builder builder = buildReqBuilder (baseUrl, queryParams, headers);

        Request request         = builder.get ().build ();

        OkHttpClient httpClient = Http.client ();

        Response response       = httpClient.newCall (request).execute ();
        return Json.build (response.body ().string ());

    }
    
    public void post (String baseUrl, JsonNode reqBody, NetWorkerCallback networkCallback) {
    	Map<String, Object> body = new LinkedHashMap<> ();
    	body.put (Payload, reqBody);
    	
    	post (baseUrl, null, null, ReqType.Json, RespType.Json, body, networkCallback);
	}
	
	public void post (String baseUrl, JsonNode reqBody, String authToken, NetWorkerCallback networkCallback) {
		Map<String, Object> body = new LinkedHashMap<> ();
		body.put (Payload, reqBody);
		
		Map<String, String> headers = new HashMap<> ();
		headers.put (Header.Accept, Type.AppJson);
		headers.put (Header.ContentType, Type.AppJson);
		
		if (!Lang.isNullOrEmpty (authToken)) {
			headers.put (Header.Authorization, authToken);
		}
		
		post (baseUrl, null, headers, ReqType.Json, RespType.Json, body, networkCallback);
	}
    
    @Override
    public void post (String baseUrl, LinkedHashMap<String, Object> queryParams, Map<String, String> headers, ReqType reqType, RespType respType, Map<String, Object> reqBody, NetWorkerCallback networkCallback) {
        if (Lang.isNullOrEmpty (baseUrl)) {
            if (networkCallback == null) {
                return;
            }
            networkCallback.onUIFailure (ErrorUtils.getError ("No url specified.", null));
            return;
        }

        if (networkCallback != null) {
            networkCallback.onUIStart ();
        }

        RequestBody body = buildRequestBody (reqType, reqBody);

        enrichUrl (baseUrl, queryParams);
        Request.Builder requestBuilder = new Request.Builder ()
            .post (body)
            .url (baseUrl);
        addHeaders (requestBuilder, headers);

        Request request = requestBuilder.build ();

        fireRequest (request, respType, networkCallback);
    }
	
	@Override
	public JsonNode post (String baseUrl, JsonNode reqBody) throws IOException {
		if (Lang.isNullOrEmpty (baseUrl)) {
			throw new NullPointerException ("No url specified.");
		}
		
		RequestBody body 		= RequestBody.create (MediaType.parse (Type.AppJson), Json.toString (reqBody));
		
		Request.Builder builder = buildReqBuilder (baseUrl, null, null);
		
		Request request  		= builder.post (body).url (baseUrl).build ();
		
		OkHttpClient httpClient = Http.client ();
		
		Response response       = httpClient.newCall (request).execute ();
		
		return Json.build (response.body ().string ());
	}
	
	@Override
    public void put (String baseUrl, LinkedHashMap<String, Object> queryParams, Map<String, String> headers, ReqType reqType, RespType respType, Map<String, Object> reqBody, NetWorkerCallback networkCallback) {
        if (Lang.isNullOrEmpty (baseUrl)) {
            networkCallback.onUIFailure (ErrorUtils.getError ("No url specified.", null));
            return;
        }

        networkCallback.onUIStart ();

        RequestBody body = buildRequestBody (reqType, reqBody);

        enrichUrl (baseUrl, queryParams);
        Request.Builder requestBuilder = new Request.Builder ()
			.put (body)
			.url (baseUrl);
        addHeaders (requestBuilder, headers);

        Request request = requestBuilder.build ();

        fireRequest (request, respType, networkCallback);
    }

    @Override
    public void delete (String baseUrl, LinkedHashMap<String, Object> queryParams, Map<String, String> headers, RespType respType, NetWorkerCallback networkCallback) {
        if (Lang.isNullOrEmpty (baseUrl)) {
            networkCallback.onUIFailure (ErrorUtils.getError ("No url specified.", null));
            return;
        }

        enrichUrl (baseUrl, queryParams);
        Request.Builder requestBuilder = new Request.Builder ()
            .delete ()
            .url (baseUrl);
        addHeaders (requestBuilder, headers);

        Request request = requestBuilder.build ();

        fireRequest (request, respType, networkCallback);
    }

    private void fireRequest (Request request, RespType respType, NetWorkerCallback networkCallback) {
        OkHttpClient httpClient = Http.client ();
        httpClient.newCall (request).enqueue (getCallback ((respType == null ? RespType.Json : respType), networkCallback));
    }
    
    private RequestBody buildRequestBody (ReqType reqType, Map<String, ?> reqBody) {
        RequestBody body;
        if (reqType == null || (reqType == ReqType.Json && reqBody.containsKey (Payload))) {
            body = RequestBody.create (MediaType.parse (Type.AppJson), Json.toString (reqBody.get (Payload)));
        } else if (reqType == ReqType.AppFormData) {
            FormBody.Builder builder = new FormBody.Builder ();
            for (Map.Entry<String, ?> entry : reqBody.entrySet ()) {
                builder.add (entry.getKey (), String.valueOf (entry.getValue ()));
            }
            body = builder.build ();
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder ();
            builder.setType (MultipartBody.FORM);
            for (Map.Entry<String, ?> entry : reqBody.entrySet ()) {
                String key = entry.getKey ();
                Object val = entry.getValue ();
                if (val instanceof File) {
                    builder.addFormDataPart (key, ((File)val).getName (), RequestBody.create (MediaType.parse (Type.Stream), ((File)val)));
                } else {
                    builder.addFormDataPart (key, (String)val);
                }
            }
            body = builder.build ();
        }
        return body;
    }

    private Request.Builder buildReqBuilder (String baseUrl, LinkedHashMap<String, Object> queryParams, Map<String, String> headers) {
        baseUrl = enrichUrl (baseUrl, queryParams);

        Logger.error (NetWorkerImpl.class.getSimpleName () + " -- BaseURL", "BaseURL : " + baseUrl);

        Request.Builder builder = new Request.Builder ().url (baseUrl);

        addHeaders (builder, headers);

        return builder;
    }

    private Callback getCallback (final RespType respType, final NetWorkerCallback networkCallback) {
        return new Callback () {

            @Override
            public void onFailure (Call call, IOException e) {
                if (networkCallback == null) {
                    Logger.error (NetWorkerImpl.class.getSimpleName () + " --> OnFailure", e);
                    return;
                }
                Error error = ErrorUtils.getError (null, e);
                networkCallback.onBGFailure (error);
                runOnUIThread (error, networkCallback);
            }

            @Override
            public void onResponse (Call call, Response response) throws IOException {
                Object outputResp;
                if (response.isSuccessful ()) {
                    if (networkCallback == null) {
                        Logger.debug (NetWorkerImpl.class.getSimpleName () + " --> getCallback", "Success");
                        return;
                    }
                    outputResp = networkCallback.onBGSuccess (response.body (), respType);
                } else {
                    Error err = new Error ();
                    err.setCode (response.code ());
                    err.setMessage (Json.build (response.body ().string ()));
                    if (networkCallback == null) {
                        Logger.debug (NetWorkerImpl.class.getSimpleName () + " --> getCallback", "Error: " + err.getCode () + " / " + err.getMsg ());
                        return;
                    }
                    outputResp = networkCallback.onBGFailure (err);
                }
                
                runOnUIThread (outputResp, networkCallback);
            }
        };
    }

    private void runOnUIThread (final Object response, final NetWorkerCallback networkCallback) {
        Handler mainHandler = new Handler (Looper.getMainLooper ());

        mainHandler.post (new Runnable () {

            @Override
            public void run () {
                if (response instanceof Error) {
                    networkCallback.onUIFailure ((Error) response);
                } else {
                    networkCallback.onUISuccess (response);
                }
                networkCallback.onUIEnd ();
            }
        });
    }

    private String enrichUrl (String baseUrl, LinkedHashMap<String, Object> queryParams) {
	    if (queryParams == null) {
		    return baseUrl;
	    }
	    
        if (baseUrl.endsWith (Lang.SLASH)) {
            baseUrl = baseUrl.replace (Character.toString (baseUrl.charAt (baseUrl.length () - 1)), Lang.EMPTY);
        }

        StringBuilder sBuilder = new StringBuilder ();
        if (!queryParams.isEmpty ()) {
            int i = 0;
            for (Map.Entry<String, Object> entry : queryParams.entrySet ()) {
                String key = entry.getKey ();
                Object val = entry.getValue ();
                try {
                    Integer.valueOf (key);
                    sBuilder.append (Lang.SLASH).append (val);
                } catch (NumberFormatException e) {
                    if (i == 0) {
						sBuilder.append (Lang.QMARK);
                    } else {
						sBuilder.append (Lang.AMPERSAND);
                    }
					sBuilder.append (key).append (Lang.EQUALS).append (val);
                }
                i ++;
            }
        }
        return baseUrl + sBuilder.toString ();
    }

    private void addHeaders (Request.Builder builder, Map<String, String> headers) {
        if (headers == null || headers.isEmpty ()) {
            builder.addHeader (Header.ContentType, Type.AppJsonUTF8);
            builder.addHeader (Header.Accept, Type.AppJson);
            
            return;
        }
        
        for (Map.Entry<String, String> entry : headers.entrySet ()) {
            String key = entry.getKey ();
            String val = entry.getValue ();

            builder.addHeader (key, val);
        }
    }
}