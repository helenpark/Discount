package com.td.mobile.nextgen.view;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.td.innovate.savingstracker.R;

import java.util.List;

abstract public class ActionSheetArrayAdapter<T> extends ArrayAdapter<T> {
	private int selection=-1;
	private boolean editMode=false;

	public ActionSheetArrayAdapter(Context context, int resource,
			int textViewResourceId, List<T> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public ActionSheetArrayAdapter(Context context, int resource,
			int textViewResourceId, T[] objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public ActionSheetArrayAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	public ActionSheetArrayAdapter(Context context, int resource,
			List<T> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	public ActionSheetArrayAdapter(Context context, int resource, T[] objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	public ActionSheetArrayAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

	public int getSelection() {
		return selection;
	}

	public void setSelection(int selection) {
		if(this.selection!=selection){
			this.selection=selection;
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=createItemView(position, parent);
		ImageView checkMark=(ImageView)view.findViewById(R.id.imgActionCheckMark);
		if(checkMark!=null && !isEditMode()){
			if(position==getSelection()){
				checkMark.setVisibility(View.VISIBLE);
			} else {
				checkMark.setVisibility(View.INVISIBLE);
			}
		}
		ImageView imgEdit=(ImageView)view.findViewById(R.id.imgActionEdit); 
		if(imgEdit!=null){
			int visibility=(isEditMode())? View.VISIBLE: View.INVISIBLE;
			imgEdit.setVisibility(visibility);
		}
		
		return view;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		try{
			return super.getCount();
		}catch(NullPointerException e){
			
		}
		return 0;
	}
	
	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		if(this.editMode!=editMode){
			this.editMode = editMode;
			notifyDataSetChanged();
		}
	}

	abstract protected View createItemView(int position, ViewGroup parent);
}
