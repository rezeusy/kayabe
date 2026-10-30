package com.example.kayabeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Reporting extends AppCompatActivity {
    TextView tvFeedback;
    RatingBar rbStars;

    EditText report;
    Button send;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting);

        tvFeedback = findViewById(R.id.tvFeedback);
        rbStars = findViewById(R.id.rbStars);
        send = findViewById(R.id.btnSend);
        report  = findViewById(R.id.etFeedback);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference feedbackRef = database.getReference("feedback"); // Change "feedback" to your desired database path



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String feedback = report.getText().toString();

                // Create a unique key for each feedback entry
                String feedbackKey = feedbackRef.push().getKey();

                // Create a Feedback object with rating and feedback text
                Feedback feedbackData = new Feedback(rbStars.getRating(), feedback);

                // Save the feedback to Firebase under the unique key
                feedbackRef.child(feedbackKey).setValue(feedbackData);

                // Optionally, clear the feedback EditText and RatingBar
                report.setText("");
                rbStars.setRating(0);

                // Provide user feedback (e.g., a Toast) to confirm successful submission
                Toast.makeText(Reporting.this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();

            }
        });




        rbStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating == 0 || rating == 0.5)
                {
                    tvFeedback.setText("Very Dissatisfied");
                }
                else if (rating == 1 || rating == 1.5)
                {
                    tvFeedback.setText("Dissatisfied");
                }
                else if (rating == 2 || rating == 2.5 || rating == 3 || rating == 3.5)
                {
                    tvFeedback.setText("Ok");
                }
                else if (rating == 4 || rating == 4.5) {
                    tvFeedback.setText("Satisfied");
                }
                else {
                    tvFeedback.setText("Very Satisfied");
                }

            }



        });
    }
}