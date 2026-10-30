package com.example.kayabeapp;

public class Feedback {
    private float rating;
    private String feedbackText;

    public Feedback() {

    }

    public Feedback(float rating, String feedbackText) {
        this.rating = rating;
        this.feedbackText = feedbackText;
    }

    public float getRating() {
        return rating;
    }

    public String getFeedbackText() {
        return feedbackText;
    }
}
