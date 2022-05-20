package com.example.myfirstapp.controller;

public interface View {

    public android.view.View getRootView();

    public void initViews();

    public void bindDataToView();
}
