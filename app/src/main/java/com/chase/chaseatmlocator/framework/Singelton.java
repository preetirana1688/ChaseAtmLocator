package com.chase.chaseatmlocator.framework;


public class Singelton {

    private static Singelton instance = null;
    public int position;
    private Response response;

    private Singelton() {
    }

    public synchronized static Singelton getInstance() {
        if (instance == null) {
            instance = new Singelton();
        }
        return instance;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
