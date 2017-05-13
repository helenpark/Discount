package com.td.innovate.savingstracker.onboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.td.innovate.savingstracker.R;

public class PYFExplanationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pyfexplanation, container, false);
    }

    public interface OnFragmentInteractionListener {
    }

}