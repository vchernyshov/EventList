package ua.insomnia.eventlist.rest;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class ServiceResultsReceiver extends ResultReceiver {
    
    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle data);
    }
    
    private Receiver receiver;
    
    public ServiceResultsReceiver(Handler handler) {
        super(handler);
    }
    
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }
    
    
}
