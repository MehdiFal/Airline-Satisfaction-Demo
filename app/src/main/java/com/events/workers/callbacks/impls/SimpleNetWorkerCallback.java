package com.events.workers.callbacks.impls;

import android.content.Context;
import android.view.View;

import com.events.model.Error;

import com.events.utils.ProgressView;
import com.events.workers.NetWorker;
import com.events.workers.callbacks.NetWorkerCallback;

public class SimpleNetWorkerCallback implements NetWorkerCallback {

    private Context context;
    private View    container;
    private boolean uiBlocking;

    public SimpleNetWorkerCallback (Context context) {
        this.context = context;
    }

    public SimpleNetWorkerCallback (View container, boolean uiBlocking) {
        this.container  = container;
        this.context    = container.getContext ();
        this.uiBlocking = uiBlocking;
    }

    @Override
    public void onUIStart () {
        ProgressView.show (context, uiBlocking);
    }

    @Override
    public Object onBGSuccess (Object unprocessedEntity, NetWorker.RespType respType) {
        return null;
    }

    @Override
    public Object onBGFailure (Error error) {
        return null;
    }

    @Override
    public void onUISuccess (Object response) {

    }

    @Override
    public void onUIFailure (Error error) {

    }

    @Override
    public void onUIEnd () {
        ProgressView.dismiss ();
    }
}