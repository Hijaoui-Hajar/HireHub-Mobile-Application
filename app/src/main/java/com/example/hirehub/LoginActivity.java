package com.example.hirehub;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

public class LoginActivity extends Activity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox keepConnectedCheckBox;
    private Button loginButton;
    private TextView textViewForgotPassword;
    private TextView signUpTextView;
    private HireHubDbHelper dbHelper;

    // Ajoutez une clé pour stocker l'état de la case à cocher dans SharedPreferences
    private static final String KEY_KEEP_CONNECTED = "keepConnected";

    // Déclarez SharedPreferences
    private SharedPreferences sharedPreferences;

    // Ajoutez une variable pour stocker l'état de la case à cocher
    private boolean keepConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the database helper
        dbHelper = new HireHubDbHelper(this);

        // Initialize UI components
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        keepConnectedCheckBox = findViewById(R.id.keepConnectedCheckBox);
        loginButton = findViewById(R.id.loginButton);
        textViewForgotPassword = findViewById(R.id.forgotPasswordTextView);
        signUpTextView = findViewById(R.id.signUpTextView);

        // Initialiser SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initialiser l'état de la case à cocher à partir de SharedPreferences
        keepConnected = sharedPreferences.getBoolean(KEY_KEEP_CONNECTED, false);

        // Définir l'état de la case à cocher
        keepConnectedCheckBox.setChecked(keepConnected);

        // Définir un écouteur pour la case à cocher
        keepConnectedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Mettre à jour l'état de la case à cocher lorsqu'elle est cochée ou décochée
            keepConnected = isChecked;
            // Enregistrer l'état de la case à cocher dans SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_KEEP_CONNECTED, keepConnected);
            editor.apply();
        });
        loginButton.setOnClickListener(v -> attemptLogin());

        textViewForgotPassword.setOnClickListener(v -> {
            // Navigate to ForgotPasswordActivity or similar
            Toast.makeText(LoginActivity.this, "Forgot Password functionality not implemented.", Toast.LENGTH_SHORT).show();
        });

        signUpTextView.setOnClickListener(v -> {
            // Navigate to SignUpActivity
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        // Query the database for the email
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                HireHubContract.UsersTable.COLUMN_NAME_EMAIL,
                HireHubContract.UsersTable.COLUMN_NAME_PASSWORD
        };
        String selection = HireHubContract.UsersTable.COLUMN_NAME_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                HireHubContract.UsersTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            String dbPassword = cursor.getString(cursor.getColumnIndexOrThrow(HireHubContract.UsersTable.COLUMN_NAME_PASSWORD));
            if (dbPassword.equals(password)) {
                // Password matches
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                // Navigate to PostingActivity
                Intent intent = new Intent(LoginActivity.this, PostingActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Password does not match
                Toast.makeText(this, "Password or username is incorrect", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Email not found, show error message
            textViewForgotPassword.setVisibility(View.VISIBLE); // Show error message
            Toast.makeText(this, "Email not found", Toast.LENGTH_SHORT).show(); // Optional: show toast message
        }
        cursor.close();
        // Si l'utilisateur a coché la case, conserver l'utilisateur connecté
        if (keepConnected) {
            // Enregistrer l'état de connexion dans SharedPreferences ou dans une autre méthode de gestion de session
        }
    }
}

