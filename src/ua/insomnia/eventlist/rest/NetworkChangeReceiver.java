package ua.insomnia.eventlist.rest;

import ua.insomnia.eventlist.utils.ConnectionUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class NetworkChangeReceiver extends BroadcastReceiver {
	
	public interface NetworkStateListener {
        public void onStateChange(boolean isConnected);
    }

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectionUtils u = new ConnectionUtils(context); 
		boolean is = u.isConnecting();
	}

}
