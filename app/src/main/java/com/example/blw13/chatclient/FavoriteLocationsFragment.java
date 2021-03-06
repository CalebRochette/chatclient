package com.example.blw13.chatclient;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.blw13.chatclient.Content.Connection;
import com.example.blw13.chatclient.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteLocationsFragment extends Fragment implements View.OnClickListener {

    private JSONArray mFavorites;
    private OnSelectFavoriteListener mListener;

    public FavoriteLocationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite_locations, container, false);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 20);
        params.height =ViewGroup.LayoutParams.WRAP_CONTENT;;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        LinearLayout mlayout = (LinearLayout) v.findViewById(R.id.favoritelocation_scroll_layout);



        try {
            JSONObject root = new JSONObject(getArguments().getString("result"));
            if (root.has(getString(R.string.keys_favorite_location))) {
                JSONArray response = root.getJSONArray(
                        getString(R.string.keys_favorite_location));
                mFavorites = response;
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonConnection = response.getJSONObject(i);

                    MyTextView textView = new MyTextView(v.getContext(),
                            jsonConnection.getString("nickname"),
                            jsonConnection.getString("lat"),
                            jsonConnection.getString("long"));

                    textView.setText(textView.getName());

                    if(i==0) {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.coudy_rain, 0, 0, 0);
                    } else if (i ==1) {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.cloud_wind, 0, 0, 0);

                    } else {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.cloud_rain_lightning, 0, 0, 0);
                    }


                    textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

                    textView.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_conversation_list));

                    textView.setTextSize(24);
                    textView.setLayoutParams(params);
                    textView.setOnClickListener(this);
                    textView.setId(i);
                    mlayout.addView(textView);

                }

            } else {
                Log.e("ERROR!", "No response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

        return v;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        JSONObject favorite = null;
        try {
            favorite = mFavorites.getJSONObject(i);
            if (favorite != null) {
                int zip = favorite.getInt("zip");
                double lat = favorite.getDouble("lat");
                double lon = favorite.getDouble("long");

                if (zip != 0) {
                    mListener.OnWeatherLocationChanged(zip);
                } else {
                    Location theNew = new Location("");
                    theNew.setLatitude(lat);
                    theNew.setLongitude(lon);
                    mListener.OnWeatherLocationChanged(theNew);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("onAttach: ", context.toString() );
        if (context instanceof OnSelectFavoriteListener) {
            mListener = (OnSelectFavoriteListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSelectFavoriteListener");
        }
    }

    public interface OnSelectFavoriteListener {
        Boolean OnWeatherLocationChanged(Location theNewLoc);
        Boolean OnWeatherLocationChanged(int theNewZip);
    }


    public class MyTextView extends android.support.v7.widget.AppCompatTextView {


        private String mLat;
        private String mLon;
        private String mName;


        public MyTextView(Context context, String name, String lat, String lon ) {

            super(context);
            mName = name;
            mLat = lat;
            mLon = lon;
        }

        public String getName() {
            return mName;
        }

        public String getLat() {
            return mLat;
        }

        public String getLon() {
            return mLon;
        }


    }

}
