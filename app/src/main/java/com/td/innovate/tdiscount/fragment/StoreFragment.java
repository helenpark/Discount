package com.td.innovate.tdiscount.fragment;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.innovate.tdiscount.R;
import com.td.innovate.tdiscount.activity.MainActivity;
import com.td.innovate.tdiscount.adapter.StoreAdapter;
import com.td.innovate.tdiscount.delgate.AbsListViewDelegate;
import com.td.innovate.tdiscount.delgate.RelViewDelgate;
import com.td.innovate.tdiscount.delgate.ViewDelegate;
import com.td.innovate.tdiscount.model.Offer;
import com.td.innovate.tdiscount.model.Product;
import com.td.innovate.tdiscount.tools.UpdateStoreInfoCallBack;

import java.util.ArrayList;


public class StoreFragment extends BaseViewPagerFragment
    implements UpdateStoreInfoCallBack, AbsListView.OnItemClickListener  {


    private View view;
    public static ListView offersListView;
    private Product mainProduct;
    private boolean hasOffersReceived = false;
    private AbsListViewDelegate mAbsListViewDelegate = new AbsListViewDelegate();

    ArrayList<Offer> offersWithoutRecommended;


    private boolean isTouched = false;
    private LinearLayout linlay;
    private TextView recommendedStoreName;
    private TextView recommendedStorePrice;
    private RelativeLayout rl;




    private static final String mLogTag = "StoreFragmentTag";


    // TODO: Rename and change types and number of parameters

    public static StoreFragment newInstance(int index) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_FRAGMENT_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(mLogTag,"oncreateStore");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store, container, false);

        recommendedStoreName = (TextView) view.findViewById(R.id.fragment_recommended_store_name);
        recommendedStorePrice = (TextView) view.findViewById(R.id.fragment_recommended_store_price);


        offersListView = (ListView) view.findViewById(R.id.main_options_offer_listview);

        rl = (RelativeLayout) view.findViewById(R.id.rellay);



        return view;
    }

    @Override
    public void onStart() {
        Log.i(mLogTag, "start");
        super.onStart();

        if(hasOffersReceived){
            update();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    public void update(){

        final Offer recommendedOffer = mainProduct.getOffers().get(0);

        offersWithoutRecommended = new ArrayList<Offer>();
        for(int i = 0; i<mainProduct.getOffers().size() -1; i++){
            offersWithoutRecommended.add(mainProduct.getOffers().get(i+1));
        }
        StoreAdapter adapter = new StoreAdapter(getActivity().getApplicationContext(), offersWithoutRecommended);

        recommendedStoreName.setText(recommendedOffer.getShop_name());
        recommendedStorePrice.setText("$ " + recommendedOffer.getPrice()
                + " "
                + recommendedOffer.getCurrency());

        offersListView.setAdapter(adapter);

        linlay = (LinearLayout) view.findViewById(R.id.linlay);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public boolean isViewBeingDragged( MotionEvent event) {
//        Log.i("TOUCH",String.valueOf(isTouched));
        return mAbsListViewDelegate.isViewBeingDragged(event, offersListView, rl);
    }

    @Override
    public void updateFragmentsCall() {
        hasOffersReceived = true;
        mainProduct = MainActivity.mainProduct;
        update();
    }

}
