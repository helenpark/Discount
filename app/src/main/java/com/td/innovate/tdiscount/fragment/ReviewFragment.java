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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;
import android.widget.AdapterView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.td.innovate.tdiscount.R;
import com.td.innovate.tdiscount.activity.MainActivity;
import com.td.innovate.tdiscount.delgate.AbsListViewDelegate;
import com.td.innovate.tdiscount.model.Product;
import com.td.innovate.tdiscount.tools.UpdateReviewsInfoCallBack;
import com.td.innovate.tdiscount.tools.UpdateStoreInfoCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class ReviewFragment extends BaseViewPagerFragment implements
        UpdateReviewsInfoCallBack, AbsListView.OnItemClickListener {

    private View view;
    private ImageView reviewStar1;
    private ImageView reviewStar2;
    private ImageView reviewStar3;
    private ImageView reviewStar4;
    private ImageView reviewStar5;
    private TextView noRating;
    private ListView reviewsListView;
    private RelativeLayout starLayout;

    private Product mainProduct;
    private ArrayList<String> reviews;
    private ArrayAdapter<String> reviewsAdapter;
    private boolean hasOffersReceived = false;

    private static final String mLogTag = "ReviewFragmentTag";

    private AbsListViewDelegate mAbsListViewDelegate = new AbsListViewDelegate();


    public static ReviewFragment newInstance(int index) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_FRAGMENT_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(mLogTag, "reviewAttach");
        Log.i("attach","reviewAttach");
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_review, container, false);
        reviewStar1 = (ImageView) view.findViewById(R.id.reviewStar1);
        reviewStar2 = (ImageView) view.findViewById(R.id.reviewStar2);
        reviewStar3 = (ImageView) view.findViewById(R.id.reviewStar3);
        reviewStar4 = (ImageView) view.findViewById(R.id.reviewStar4);
        reviewStar5 = (ImageView) view.findViewById(R.id.reviewStar5);
        noRating = (TextView) view.findViewById(R.id.noRatingAvailable);

        reviewsListView = (ListView) view.findViewById(R.id.reviewsList);

        reviewsListView.setOnItemClickListener(this);
        reviewsListView.setEmptyView(view.findViewById(android.R.id.empty));

        starLayout = (RelativeLayout) view.findViewById(R.id.starLayout);

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public boolean isViewBeingDragged(MotionEvent event) {
        return mAbsListViewDelegate.isViewBeingDragged(event, reviewsListView, starLayout);
    }

    public void update(){
        Log.i(mLogTag, "Updating review fragment");

        mainProduct = MainActivity.mainProduct;
        reviews = new ArrayList<String>();
        reviewsAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.review_list_item, reviews);
        reviewsListView.setAdapter(reviewsAdapter);

        if (mainProduct.getReview_rating().equals("null")) {
            reviewStar1.setVisibility(View.GONE);
            reviewStar2.setVisibility(View.GONE);
            reviewStar3.setVisibility(View.GONE);
            reviewStar4.setVisibility(View.GONE);
            reviewStar5.setVisibility(View.GONE);
        } else {
            noRating.setVisibility(View.GONE);
            Double reviewValue = Double.parseDouble(mainProduct.getReview_rating());
            int reviewOutOf5 = (int) Math.round(reviewValue / 20);
            if (reviewOutOf5 > 0) {
                reviewStar1.setImageResource(R.drawable.star_filled);
            }
            if (reviewOutOf5 > 1) {
                reviewStar2.setImageResource(R.drawable.star_filled);
            }
            if (reviewOutOf5 > 2) {
                reviewStar3.setImageResource(R.drawable.star_filled);
            }
            if (reviewOutOf5 > 3) {
                reviewStar4.setImageResource(R.drawable.star_filled);
            }
            if (reviewOutOf5 > 4) {
                reviewStar5.setImageResource(R.drawable.star_filled);
            }
        }


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("token", "dcb656ebd5476569d85dbe4b1e3c02c9");
        params.put("url", mainProduct.getUrl());
        params.put("fields","title");
        client.get("http://api.diffbot.com/v3/discussion", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONObject obj = new JSONObject(res);
                    Log.i("yay2 - obj", obj.toString());
                    JSONArray postArray = obj.getJSONArray("objects").getJSONObject(0).getJSONArray("posts");
                    for (int i = 0; i < postArray.length(); i++) {
                        reviews.add(postArray.getJSONObject(i).getString("text"));
                    }
                    if (reviews.size() == 0) {
                        reviews.add("No comments available");
                    }
                    reviewsAdapter.notifyDataSetChanged();
                } catch (Throwable t) {
                    Log.i("My App", "Could not parse malformed JSON: \"" + res + "\"");
                    reviews.add("No comments available");
                    reviewsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
//                                 called when response HTTP status is "4XX" (eg. 401, 403, 404)
                CharSequence text = "BAD toast!";
                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
//                toast.show();
                if (res != null) {
                    Log.i("yay", res);
                } else {
                    Log.e(mLogTag, "Empty failure error");
                }

                reviews.add("No comments available");
                reviewsAdapter.notifyDataSetChanged();
            }

        });
    }

    @Override
    public void updateFragmentsCall() {
        hasOffersReceived = true;
        mainProduct = MainActivity.mainProduct;
        update();
    }
}
