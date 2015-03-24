package ua.insomnia.eventlist;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey = "", mailTo = "morkovkacorp@gmail.com", customReportContent = {
		ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
		ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL,
		ReportField.STACK_TRACE, ReportField.LOGCAT })
public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
	}
}
