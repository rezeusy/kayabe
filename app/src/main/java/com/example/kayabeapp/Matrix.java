package com.example.kayabeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Matrix extends AppCompatActivity {

    private ImageView imageView;
    private Button btnPrevious;
    private Button btnNext;
    private int currentImageIndex = 0;
    private int[] imageResources = {R.drawable.matrix, R.drawable.matrix2, R.drawable.matrix3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);

        imageView = findViewById(R.id.imageView);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        // Set the initial image
        imageView.setImageResource(imageResources[currentImageIndex]);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousImage();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextImage();
            }
        });
    }

    private void showPreviousImage() {
        if (currentImageIndex > 0) {
            currentImageIndex--;
            imageView.setImageResource(imageResources[currentImageIndex]);
        }
    }

    private void showNextImage() {
        if (currentImageIndex < imageResources.length - 1) {
            currentImageIndex++;
            imageView.setImageResource(imageResources[currentImageIndex]);
        }
    }
}