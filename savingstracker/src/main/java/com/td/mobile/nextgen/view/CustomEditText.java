package com.td.mobile.nextgen.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.td.innovate.savingstracker.R;

public abstract class CustomEditText extends EditText {
	//private static final int MAX_LENGTH = 25;
		
	Drawable d;
	
	boolean bypassValidation;
	
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			this.setBackgroundResource(android.R.drawable.edit_text);
		}

		setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public synchronized boolean onEditorAction(TextView v,
					int actionId, KeyEvent event) {

				if (event != null && event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					return true;
				}
				return false;
			}
		});

		String value = "";
		final String viewMode = "editing";
		final String viewSide = "right";
		
		toggleButton(R.drawable.delete_icon);
		
		Drawable x2 = viewMode.equals("never") ? null : viewMode
				.equals("always") ? d : viewMode.equals("editing") ? (value
				.equals("") ? null : d)
				: viewMode.equals("unlessEditing") ? (value.equals("") ? d
						: null) : null;

		setCompoundDrawables(null, null, viewSide.equals("right") ? x2 : null,
				null);

		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				

				if (getCompoundDrawables()[viewSide.equals("left") ? 0 : 2] == null) {
					return false;
				}
				if (event.getAction() != MotionEvent.ACTION_UP) {
					return false;
				}
				// d pressed
				if(bypassValidation){
					doAction();
					return true;
				}
				
				if ((viewSide.equals("left") && event.getX() < getPaddingLeft()
						+ d.getIntrinsicWidth())
						|| (viewSide.equals("right") && event.getX() > getWidth()
								- getPaddingRight() - d.getIntrinsicWidth())) {
					Drawable x3 = viewMode.equals("never") ? null : viewMode
							.equals("always") ? d
							: viewMode.equals("editing") ? null : viewMode
									.equals("unlessEditing") ? d : null;

					setCompoundDrawables(null, null,
							viewSide.equals("right") ? x3 : null, null);
					
					doAction();
				}
				return false;
			}
		});
		addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Drawable x4 = viewMode.equals("never") ? null : viewMode
						.equals("always") ? d
						: viewMode.equals("editing") ? (getText().toString()
								.equals("") ? null : d) : viewMode
								.equals("unlessEditing") ? (getText()
								.toString().equals("") ? d : null) : null;
				setCompoundDrawables(null, null, viewSide.equals("right") ? x4
						: null, null);
			}

			@Override
			public void afterTextChanged(Editable s) {
				/*if (s != null && s.length() > MAX_LENGTH) {
					setText(s.subSequence(0, MAX_LENGTH));
					setSelection(MAX_LENGTH);
				}*/
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});
	}
	
	
	public void toggleButton(int drawable){
		d = getResources().getDrawable(drawable);
		setHeight(d.getBounds().height());
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
	}


	public boolean isBypassValidation() {
		return bypassValidation;
	}


	public void setBypassValidation(boolean bypassValidation) {
		this.bypassValidation = bypassValidation;
	} 
	
	public abstract void doAction(); 
	
	public abstract void setAction(int drawable);


}
