package com.events.model;

import com.fasterxml.jackson.databind.JsonNode;

public class Error {

    public enum Type {
        NoInternet,
        Exception,
        AccountNotActivated,
        Http401,
        Http403,
		Http422, // fields validation incorrect
		Http409, // resource already exists
		Http500,
		Http
    }
    
    private String request;
    private int         code;
    private String status;
    private String msg;
    private JsonNode    message;
    private Exception exception;
    private Type 		type;
    
    public Error () {
    }

    public String getRequest () {
        return request;
    }

    public void setRequest (String request) {
        this.request = request;
    }

    public int getCode () {
        return code;
    }

    public void setCode (int code) {
        this.code = code;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public String getMsg () {
        return msg;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }

    public JsonNode getMessage () {
        return message;
    }

    public void setMessage (JsonNode message) {
        this.message = message;
    }

    public Exception getException () {
        return exception;
    }

    public void setException (Exception exception) {
        this.exception = exception;
    }
    
    public Type getType () {
    	return type;
	}
	
	public void setType (Type type) {
    	this.type = type;
	}
}