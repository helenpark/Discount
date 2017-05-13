package com.td.innovate.tdiscount.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.td.innovate.tdiscount.tools.ScrollableFragmentListener;
import com.td.innovate.tdiscount.tools.ScrollableListener;


public abstract class BaseViewPagerFragment extends Fragment implements ScrollableListener {

	private static final String TAG = "BaseViewPagerFragment";
	protected ScrollableFragmentListener mListener;
	protected static final String BUNDLE_FRAGMENT_INDEX = "BaseFragment.BUNDLE_FRAGMENT_INDEX";
	protected int mFragmentIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("yayme","me");
		Bundle bundle = getArguments();
		if (bundle != null) {
			mFragmentIndex = bundle.getInt(BUNDLE_FRAGMENT_INDEX, 0);
		}

		if (mListener != null) {
			mListener.onFragmentAttached(this, mFragmentIndex);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
            mListener = (ScrollableFragmentListener) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, activity.toString() + " must implement ScrollableFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        if (mListener != null) {
            mListener.onFragmentDetached(this, mFragmentIndex);
        }

        super.onDetach();
        mListener = null;
    }
}
