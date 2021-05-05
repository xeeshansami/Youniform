package com.youniform.android.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.youniform.android.Adapters.PlaceAutocompleteAdapter;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Interface.PlaceAutoCompleteInterface;
import com.youniform.android.Interface.ServerCallbackObjectResult;
import com.youniform.android.Model.MakeBookings.LocationModel;
import com.youniform.android.Model.PlacesModels.PlacesRoot;
import com.youniform.android.Model.Searched.PlaceAutocomplete;
import com.youniform.android.Model.Searched.RootSearched;
import com.youniform.android.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements PlaceAutoCompleteInterface {


    private static final String TAG = "pok";
    //  private static ApplicationController application;
    private static SharedPreferences preferences;
    public String MAP_KEY = "AIzaSyD_pPOU0msOC8ToW0qKwEMOsvc091yGEc4";
    @BindView(R.id.list_search)
    RecyclerView mRecyclerView;
    @BindView(R.id.nosearchData)
    TextView nosearchData;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.searchView)
    SearchView searchView;
    ArrayList<PlaceAutocomplete> mResultList = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private Dialog progressDialog;
    private PlaceAutocompleteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupClients();
        backClick();
        setSearchAdpter();
        searchingwork();
    }

    private void setupClients() {
        ButterKnife.bind(this);
        progressDialog = Utils.progressDialog(SearchActivity.this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Search Place");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void backClick() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    finish();
                } catch (Exception e) {
                    Utils.showToastMessage("Unexpected Error Has Occured", SearchActivity.this);

                }
            }
        });
    }

    private void setSearchAdpter() {
        mAdapter = new PlaceAutocompleteAdapter(mResultList, SearchActivity.this, R.layout.singlerow_placesearch);
        LinearLayoutManager manager = new LinearLayoutManager(SearchActivity.this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPlaceClick(ArrayList<PlaceAutocomplete> mResultList, int position) {
        if (mResultList != null) {
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeid);
                String placedtails = "https://maps.googleapis.com/maps/api/place/details/json?key=" + MAP_KEY + "&placeid=" + placeId;
                GetDataFromApiGET_REQUEST(placedtails, SearchActivity.this, progressDialog, new ServerCallbackObjectResult() {
                    @Override
                    public void onSuccess(Object object) {
                        Gson gson;
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gson = gsonBuilder.create();
                        PlacesRoot rootNearBy = gson.fromJson(object.toString(), PlacesRoot.class);
                        if (rootNearBy.getStatus().equals("OK")) {
                            LocationModel model = new LocationModel();
                            Double lat = rootNearBy.getResult().getGeometry().getLocation().getLat();
                            Double lng = rootNearBy.getResult().getGeometry().getLocation().getLng();

                            Geocoder geocoder;
                            List<Address> addresses = null;
                            geocoder = new Geocoder(SearchActivity.this, Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();

                            model.setPlaceName((String) mResultList.get(position).placename);
                            model.setAddress((String) mResultList.get(position).description);
                            model.setLatitude(lat);
                            model.setLongitude(lng);
                            model.setLat(lat);
                            model.setLng(lng);
                            model.setAddress(rootNearBy.getResult().getFormatted_address());
                            model.setAddress(address);
                            model.setCity(city);
                            model.setCountry(country);
                            model.setState(state);
                            model.setPostalCode(postalCode);
                            model.setKnownName(knownName);


//                                setResult(Activity.RESULT_OK, model);
//                                finish();
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Service Not Available", Toast.LENGTH_SHORT).show();
//                            }
                            if (country.equals("Pakistan")) {
                                Intent intent = getIntent();
                                intent.putExtra("place", model);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        } else {
                            Utils.showToastMessage("Location details Not Availble", SearchActivity.this);
                        }
                    }

                    @Override
                    public void OnFelure(VolleyError error) {
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    private void getSearchedDataFromApi(String searchQuery) {
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + searchQuery + "&key=" + MAP_KEY + "&components=country:PK";
        GetDataFromApiGET_REQUEST(url, SearchActivity.this, progressDialog, new ServerCallbackObjectResult() {
            @Override
            public void onSuccess(Object object) {
                Log.d(TAG, "Success");
                Gson gson;
                GsonBuilder gsonBuilder = new GsonBuilder();
                gson = gsonBuilder.create();
                RootSearched rootNearBy = gson.fromJson(object.toString(), RootSearched.class);
                if (rootNearBy.getStatus().equals("OK")) {
                    mResultList.clear();
                    for (int a = 0; a < rootNearBy.getPredictions().size(); a++) {
                        mResultList.add(new PlaceAutocomplete(rootNearBy.getPredictions().get(a).getPlace_id(),
                                rootNearBy.getPredictions().get(a).getStructured_formatting().getMain_text(),
                                rootNearBy.getPredictions().get(a).getDescription()));
                    }
                } else {
                    mResultList.clear();
                }
                mAdapter.notifyDataSetChanged();
                searchEmptyView();
            }

            @Override
            public void OnFelure(VolleyError error) {
                mAdapter.notifyDataSetChanged();
                Log.d(TAG, error.toString());
            }
        });
    }

    public void GetDataFromApiGET_REQUEST(String ApiUrl, final Context context, final Dialog dialog, final ServerCallbackObjectResult callback) {
        try {
            StringRequest stringRequest = new StringRequest(ApiUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    callback.onSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, error.toString());
                    callback.OnFelure(error);
                }
            });
            addToRequestedQueue(stringRequest, "mart");
        } catch (Exception e) {
            Log.d(TAG, "DeletDaat: " + e.getMessage());
        }

    }


    private void searchingwork() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.equals("")) {
                    getSearchedDataFromApi(s);
                } else {
                    getSearchedDataFromApi("");
                }
                return false;
            }
        });
        searchEmptyView();
    }

    private void searchEmptyView() {
        if (mAdapter.getItemCount() > 0) {
            nosearchData.setVisibility(View.GONE);
        } else {
            nosearchData.setVisibility(View.VISIBLE);
        }
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestedQueue(Request<T> request, Object tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null)
            getRequestQueue().cancelAll(tag);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}