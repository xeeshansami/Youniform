package com.youniform.android.Interface;


import com.android.volley.VolleyError;

public interface ServerCallbackObjectResult {
    void onSuccess(Object object);

    void OnFelure(VolleyError error);

}