package ua.insomnia.eventlist;


import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class StateActivity extends FragmentActivity {
	
	public final String TAG = getClass().getSimpleName();

	@Override
	protected void onPause() {
		super.onPause();
		onSaveState();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		onRestoreState();
	}
	
	public void onSaveState() {
		Log.d(TAG, "onSaveState");
	}
	
	public void onRestoreState() {
		Log.d(TAG, "onRestoreState");
	}
}
