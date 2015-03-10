package ua.insomnia.eventlist.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionUtils {
	private Context mContext;

	public ConnectionUtils(Context context) {
		this.mContext = context;
	}

	public boolean isConnecting() {
		ConnectivityManager mManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mManager != null) {
			NetworkInfo[] mInfo = mManager.getAllNetworkInfo();
			if (mInfo != null)
				for (int i = 0; i < mInfo.length; i++)
					if (mInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}
}
