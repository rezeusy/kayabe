package com.example.kayabeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserProfile extends AppCompatActivity {

    FirebaseUser user;
    ImageView avatarImageView;
    FloatingActionButton floating;


    private DatabaseReference reference;
    private String userID;

    Button verify;

    private List<Integer> imageList = Arrays.asList(
            R.drawable.babaea,
            R.drawable.babaeskol,
            R.drawable.lalakiskol,
            R.drawable.lalaki,
            R.drawable.lalakiold,
            R.drawable.babaeold

    );


    private void showDiscountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.custom_discount_dialog, null);
        builder.setView(dialogView);

        final CheckBox checkBoxSeniorCitizen = dialogView.findViewById(R.id.checkBoxSeniorCitizen);
        final CheckBox checkBoxStudent = dialogView.findViewById(R.id.checkBoxStudent);
        final CheckBox checkBoxPersonWithDisability = dialogView.findViewById(R.id.checkBoxPersonWithDisability);
        final CheckBox checkBoxNoDiscount = dialogView.findViewById(R.id.checkBoxNoDiscount);

        final AlertDialog dialog = builder.create();
        dialog.show();

        Button buttonApplyDiscount = dialogView.findViewById(R.id.buttonApplyDiscount);
        buttonApplyDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a list to store the selected discounts
                List<String> selectedDiscounts = new ArrayList<>();

                // Check which checkboxes are selected and add them to the list
                if (checkBoxSeniorCitizen.isChecked()) {
                    selectedDiscounts.add("Senior Citizen");
                }
                if (checkBoxStudent.isChecked()) {
                    selectedDiscounts.add("Student");
                }
                if (checkBoxPersonWithDisability.isChecked()) {
                    selectedDiscounts.add("Person with Disability");
                }
                if (checkBoxNoDiscount.isChecked()) {
                    selectedDiscounts.add("No Discount");
                }

                // Check if at least one discount is selected
                if (selectedDiscounts.isEmpty()) {
                    Toast.makeText(UserProfile.this, "Please select at least one discount.", Toast.LENGTH_SHORT).show();
                } else {
                    // Update the discount preferenc DatabaseReference discountRef = FirebaseDatabase.getInstance().getReference("Users/Customers/" + userID + "/selected_discounts");e in the Firebase Realtime Database
                    DatabaseReference discountRef = FirebaseDatabase.getInstance().getReference("Users/Customers/" + userID + "/selected_discounts");
                    discountRef.setValue(selectedDiscounts);
                    dialog.dismiss();
                }
            }
        });
    }

    private List<String> userTrustedContacts = new ArrayList<>(); // Initialize an empty list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Create an array of avatar resource IDs
        final int[] avatarOptions = {R.drawable.babaea, R.drawable.babaeskol
                , R.drawable.lalakiskol, R.drawable.lalaki, R.drawable.babaeold, R.drawable.lalakiold};

        ImageView avatarImageView = findViewById(R.id.imageView2); // Replace with your ImageView
        avatarImageView.setImageResource(R.drawable.lalaki);


        floating = findViewById(R.id.floatingActionButton);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users/Customers");
        userID = user.getUid();

        final TextView trustedtv = (TextView) findViewById(R.id.trusted);
        final TextView emailtv = (TextView) findViewById(R.id.email);
        final TextView passwordtv = (TextView) findViewById(R.id.password);

        final TextView discounttv = (TextView) findViewById(R.id.discount);
        final TextView usertv = (TextView) findViewById(R.id.user);

        discounttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiscountDialog(); // Call the method to show the discount dialog
            }


        });

        // Create a dialog with a list of avatar options
        AlertDialog.Builder Option = new AlertDialog.Builder(this);
        Option.setTitle("Choose an Avatar");

        Option.setItems(new String[]{"Avatar 1", "Avatar 2", "Avatar 3", "Avatar 4"
                , "Avatar 5", "Avatar 6"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Save the selected avatar to Firebase Realtime Database
                DatabaseReference avatarRef = FirebaseDatabase.getInstance().getReference("Users/Customers/" + userID + "/avatar");
                avatarRef.setValue(avatarOptions[which]);

                // Set the selected avatar as the user's profile picture
                ImageView avatarImageView = findViewById(R.id.imageView2); // Replace with your ImageView
                avatarImageView.setImageResource(avatarOptions[which]);

                dialog.dismiss();
            }
        });


        // Create a reference to the user's trusted contacts in the database
        DatabaseReference trustedContactsRef = FirebaseDatabase.getInstance().getReference("Users/Customers/" + userID + "/trustedContacts");

        // Listen for changes to the user's trusted contacts data
        trustedContactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userTrustedContacts.clear(); // Clear the existing list

                for (DataSnapshot contactSnapshot : snapshot.getChildren()) {
                    String contact = contactSnapshot.getValue(String.class);
                    if (contact != null) {
                        userTrustedContacts.add(contact);
                    }
                }


                // Update the TextView to display the list of trusted contacts
                if (userTrustedContacts.isEmpty()) {
                    trustedtv.setText("No trusted contacts (Tap to add)"); // Set a message when there are no trusted contacts
                } else {
                    String trustedContactsText = TextUtils.join(", ", userTrustedContacts);
                    trustedtv.setText(trustedContactsText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors, if any
            }
        });


        // Get a reference to the Firebase Realtime Database
        DatabaseReference avatarRef = FirebaseDatabase.getInstance().getReference("Users/Customers/" + userID + "/avatar");

// Listen for changes to the user's avatar data
        avatarRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer selectedAvatarResourceId = snapshot.getValue(Integer.class);

                if (selectedAvatarResourceId != null) {
                    // Set the user's selected avatar as the ImageView resource
                    avatarImageView.setImageResource(selectedAvatarResourceId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors, if any
            }
        });


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String email = userProfile.email;
                    String password = userProfile.password;
                    List<String> selectedDiscounts = userProfile.selected_discounts; // Assuming selectedDiscounts is a List<String>
                    String name = userProfile.name;

                    // Join the selectedDiscounts list into a single string, separated by commas
                    String selectedDiscountsText = TextUtils.join(", ", selectedDiscounts);

                    discounttv.setText(selectedDiscountsText);
                    emailtv.setText(email);
                    passwordtv.setText(password);
                    usertv.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "ERROR NANAMAN BOI", Toast.LENGTH_LONG).show();
            }
        });


        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Option.show();

            }
        });


        trustedtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddTrustedContactClick();

            }
        });


        emailtv.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this)
                    .setTitle("Edit Email");

            EditText emailEditText = new EditText(UserProfile.this);
            emailEditText.setText(emailtv.getText());

            builder.setView(emailEditText)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newEmail = emailEditText.getText().toString();

                        if (user.isEmailVerified()) {
                            user.updateEmail(newEmail).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser updatedUser = FirebaseAuth.getInstance().getCurrentUser();

                                    if (updatedUser != null) {
                                        updatedUser.sendEmailVerification().addOnCompleteListener(emailTask -> {
                                            if (emailTask.isSuccessful()) {
                                                Toast.makeText(UserProfile.this, "Verification email sent to the new address. Please verify your new email.", Toast.LENGTH_SHORT).show();
                                                reference.child(userID).child("email").setValue(newEmail).addOnCompleteListener(dbUpdateTask -> {
                                                    if (dbUpdateTask.isSuccessful()) {
                                                        emailtv.setText(newEmail);
                                                        Toast.makeText(UserProfile.this, "Email updated in the database.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(UserProfile.this, "Failed to update email in the database", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(UserProfile.this, "Failed to send verification email to the new address", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(UserProfile.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(UserProfile.this, "You must verify your current email address before updating it.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        // User canceled the edit, do nothing
                    });

            builder.show();
        });


        passwordtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show an AlertDialog or a custom dialog for editing the password
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
                builder.setTitle("Edit Password");

                // Create an EditText to allow the user to enter a new password
                final EditText passwordEditText = new EditText(UserProfile.this);
                passwordEditText.setHint("Enter new password");

                builder.setView(passwordEditText);

                // Add buttons to save or cancel the edit
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String newPassword = passwordEditText.getText().toString();

                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Password updated successfully
                                            passwordtv.setText("********"); // Update the TextView with asterisks for security
                                            Toast.makeText(UserProfile.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // If password update fails, display an error message
                                            Toast.makeText(UserProfile.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User canceled the edit, do nothing
                    }
                });

                builder.show();
            }
        });

        usertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show an AlertDialog or a custom dialog for editing the username
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
                builder.setTitle("Edit Username");

                // Create an EditText to allow the user to enter a new username
                final EditText usernameEditText = new EditText(UserProfile.this);
                usernameEditText.setText(usertv.getText()); // Set the current username as the initial text

                builder.setView(usernameEditText);

                // Add buttons to save or cancel the edit
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String newUsername = usernameEditText.getText().toString();

                        // Update the Realtime Database with the new username
                        reference.child(userID).child("username").setValue(newUsername)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Realtime Database updated successfully
                                            // Update the TextView with the latest username
                                            usertv.setText(newUsername);
                                            Toast.makeText(UserProfile.this, "Username updated successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(UserProfile.this, "Failed to update Realtime Database", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User canceled the edit, do nothing
                    }
                });

                builder.show();
            }
        });

    }

    public void onAddTrustedContactClick() {
        // Create a dialog with a custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.editcontacts, null);
        builder.setView(dialogView);
        builder.setTitle("Edit Trusted Contacts");

        final EditText newContactEditText = dialogView.findViewById(R.id.editTextNewContact);
        final Button addButton = dialogView.findViewById(R.id.buttonAdd);
        final LinearLayout contactsContainer = dialogView.findViewById(R.id.contactsContainer);

        final AlertDialog dialog = builder.create();
        dialog.show();

        // Add existing trusted contacts and a "Delete" button for each
        for (String contact : userTrustedContacts) {
            TextView contactTextView = new TextView(this);
            contactTextView.setText(contact);

            Button deleteButton = new Button(this);
            deleteButton.setText("Delete");

            // Set an OnClickListener for the delete button to delete the contact
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String contactToDelete = contactTextView.getText().toString();
                    // Delete the contact from the user's trusted contacts in the database
                    userTrustedContacts.remove(contactToDelete);
                    contactsContainer.removeView(contactTextView); // Remove the contact view
                    contactsContainer.removeView(deleteButton); // Remove the delete button view
                    dialog.dismiss();
                    // Update the database to reflect the removed contact
                    DatabaseReference contactsRef = FirebaseDatabase.getInstance().getReference("Users/Customers/" + userID + "/trustedContacts");
                    contactsRef.setValue(userTrustedContacts);
                }
            });

            contactsContainer.addView(contactTextView);
            contactsContainer.addView(deleteButton);
        }

        // Handle adding a new contact
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newContact = newContactEditText.getText().toString();
                if (!TextUtils.isEmpty(newContact)) {
                    // Validate that the phone number is exactly 10 digits long
                    if (newContact.matches("^[0-9]{10}$")) {
                        // Automatically add "+63" to the beginning of the number
                        newContact = "+63" + newContact;
                        if (userTrustedContacts.size() < 2) {
                            if (!userTrustedContacts.contains(newContact)) {
                                userTrustedContacts.add(newContact);
                                // Update the user's trusted contacts in the database
                                DatabaseReference contactsRef = FirebaseDatabase.getInstance().getReference("Users/Customers/" + userID + "/trustedContacts");
                                contactsRef.setValue(userTrustedContacts);

                                // Create a TextView for the new contact
                                TextView contactTextView = new TextView(UserProfile.this);
                                contactTextView.setText(newContact);

                                // Create a "Delete" button for the new contact
                                Button deleteButton = new Button(UserProfile.this);
                                deleteButton.setText("Delete");

                                // Set an OnClickListener for the delete button to delete the contact
                                String finalNewContact = newContact;
                                deleteButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Delete the contact from the user's trusted contacts in the database
                                        userTrustedContacts.remove(finalNewContact);
                                        contactsContainer.removeView(contactTextView); // Remove the contact view
                                        contactsContainer.removeView(deleteButton); // Remove the delete button view
                                        dialog.dismiss();
                                        // Update the database to reflect the removed contact
                                        DatabaseReference contactsRef = FirebaseDatabase.getInstance().getReference("Users/Customers/" + userID + "/trustedContacts");
                                        contactsRef.setValue(userTrustedContacts);
                                    }
                                });

                                contactsContainer.addView(contactTextView);
                                contactsContainer.addView(deleteButton);
                            } else {
                                Toast.makeText(UserProfile.this, "This contact is already in your trusted contacts.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UserProfile.this, "You can only add up to 2 trusted contacts.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UserProfile.this, "Invalid phone number. It should be exactly 10 digits long.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }




}
