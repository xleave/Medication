package com.group1.medicationreminder;

import com.group1.medicationreminder.service.AudioAlert;
public class Main {

    public static void main(String[] args) {
        AudioAlert alert = new AudioAlert();
        alert.playAlert("It's time to take your medication.");
        alert.shutdown();



    }





}