package com.example.kayabeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
    private TextView discountTextView; // TextView for selecting discounts

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // Sample list of discounts (you can replace this with your own data)
    private String[] discountsArray = {"Senior Citizen", "Student", "Person with Disability", "No Discount"};
    private boolean[] selectedDiscounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Get Firebase authentication and database instances
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");

        // Initialize the views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Initialize the discounts array to false
        selectedDiscounts = new boolean[discountsArray.length];

        // Initialize the discounts TextView
        discountTextView = findViewById(R.id.discountTextView);

        // Set an OnClickListener for the TextView to show the discounts dialog
        discountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiscountsDialog();
            }
        });

        // Set a click listener for the register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password, selectedDiscounts);
                } else {
                    Toast.makeText(RegistrationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to show the discounts dialog
    private void showDiscountsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Discounts");
        builder.setMultiChoiceItems(discountsArray, selectedDiscounts, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // Handle checkbox clicks here
                selectedDiscounts[which] = isChecked;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle OK button click
                // Update the TextView with selected discounts
                updateDiscountTextView();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel button click
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Method to update the TextView with selected discounts
    private void updateDiscountTextView() {
        StringBuilder selectedDiscountsText = new StringBuilder();
        for (int i = 0; i < selectedDiscounts.length; i++) {
            if (selectedDiscounts[i]) {
                selectedDiscountsText.append(discountsArray[i]);
                selectedDiscountsText.append(", ");
            }
        }

        // Remove the trailing comma and space
        if (selectedDiscountsText.length() > 0) {
            selectedDiscountsText.setLength(selectedDiscountsText.length() - 2);
        }

        // Set the selected discounts text to the TextView
        discountTextView.setText(selectedDiscountsText.toString());
    }

    // Method to register the user
    private void registerUser(String name, String email, String password, boolean[] selectedDiscounts) {
        // Create the user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration success
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {


                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> emailTask) {
                                                if (emailTask.isSuccessful()) {
                                                    // Email sent successfully
                                                    Toast.makeText(RegistrationActivity.this, "Verification email sent. Please check your email.", Toast.LENGTH_LONG).show();
                                                    // You can optionally navigate to another activity or take further actions here.
                                                } else {
                                                    // Email sending failed, handle the error
                                                    Toast.makeText(RegistrationActivity.this, "Email verification could not be sent. Try again later.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                // Get the user ID and create a database reference
                                String userId = user.getUid();
                                DatabaseReference userRef = mDatabase.child(userId);

                                // Create a user data map
                                HashMap<String, Object> userData = new HashMap<>();
                                userData.put("name", name);
                                userData.put("email", email);
                                userData.put("password", password);

                                // Convert the selected discounts array to a list of selected discounts
                                List<String> selectedDiscountsList = new ArrayList<>();
                                for (int i = 0; i < selectedDiscounts.length; i++) {
                                    if (selectedDiscounts[i]) {
                                        selectedDiscountsList.add(discountsArray[i]);
                                    }
                                }
                                userData.put("selected_discounts", selectedDiscountsList);


                                // Set the user data in the database
                                userRef.setValue(userData);

                                // Show a success message
                                Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                                // Optional: Navigate to another activity or perform any desired action

                                // Finish the current activity
                                finish();
                            }
                        } else {
                            // Registration failed, show an error message
                            Toast.makeText(RegistrationActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
}
