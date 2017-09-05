package com.fow.core.rest.view;

/**
 * Created by Greg.Chen on 2015/8/29.
 */
public class SuccessView {

    private String response;

    public SuccessView(){
        this.response = "successfully";
    }

    public SuccessView(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

