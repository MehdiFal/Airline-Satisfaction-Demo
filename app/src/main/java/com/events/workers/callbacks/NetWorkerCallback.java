package com.events.workers.callbacks;

import com.events.model.Error;

import com.events.workers.NetWorker;

public interface NetWorkerCallback extends WorkerCallback {

    void    onUIStart       ();
    
    Object  onBGSuccess     (Object unprocessedEntity, NetWorker.RespType respType);
    Object  onBGFailure     (Error error);
    
    void    onUISuccess     (Object response);
    void    onUIFailure     (Error error);
    
    void    onUIEnd ();
}