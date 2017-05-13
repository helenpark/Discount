package com.td.mobile.nextgen.view;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.innovate.savingstracker.R;

public class HeaderErrorViewFragment extends Fragment {
	private static final String CLASS_NAME = HeaderErrorViewFragment.class.getName();
	TextView header;
	TextView message;
	ImageView image;
	
	   @Override
	   public View onCreateView(LayoutInflater inflater,
	      ViewGroup container, Bundle savedInstanceState) {
	      	       
	      return inflater.inflate(R.layout.header_error_view, container, false);
	   }
	   
	   public void setHeaderText(String text){
		   header = (TextView) getView().findViewById(R.id.error_header);
		   header.setText(text);
	   }
	   
	   public void setMessageText(String text){
   		   message = (TextView) getView().findViewById(R.id.error_text);
   		   Spanned errorMsg= Html.fromHtml(text);
		   message.setText(errorMsg);
	   }
}
