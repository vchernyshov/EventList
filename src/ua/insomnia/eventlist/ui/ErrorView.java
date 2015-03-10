package ua.insomnia.eventlist.ui;

import ua.insomnia.eventlist.R;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ErrorView extends RelativeLayout {
	
	private static View.OnClickListener retryListener;

	public ErrorView(Context context) {
		super(context);
	}
	
	public static ErrorView create(Context paramContext) {
		ErrorView localErrorView = (ErrorView)View.inflate(paramContext, R.layout.error_view, null);
		localErrorView.findViewById(R.id.topErrorView).setVisibility(View.VISIBLE);
		localErrorView.findViewById(R.id.btnErrorAction).setOnClickListener(retryListener);
		return localErrorView;
	}
	
	public void setText(int paramInt) {
		((TextViewFonts)findViewById(R.id.txtErrorText)).setText(paramInt);
	}
	
	public void setText(CharSequence paramCharSequence) {
		((TextViewFonts)findViewById(R.id.txtErrorText)).setText(paramCharSequence);
	}
	
	public void setButtonText(int paramInt) {
		((Button)findViewById(R.id.btnErrorAction)).setText(paramInt);
	}
	
	public void setButtonText(CharSequence paramCharSequence) {
		((Button)findViewById(R.id.btnErrorAction)).setText(paramCharSequence);
	}
	
	public void setOnRetryListener(View.OnClickListener paramOnClickListener) {
		this.retryListener = paramOnClickListener;
	}
	
}
