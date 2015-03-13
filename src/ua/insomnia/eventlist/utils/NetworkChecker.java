package ua.insomnia.eventlist.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkChecker {

	private static final String TAG = "NetworkChecker";

	public static boolean isNetworkAvailable(Context context) {
		boolean isAvailable = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo() != null;
		Log.d(TAG, "network available " + isAvailable);
		return isAvailable;
	}

}
