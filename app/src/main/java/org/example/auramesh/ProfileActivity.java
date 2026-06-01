package org.example.auramesh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AuraMeshPrefs";
    private EditText edtFullName, edtChronic, edtMedicine, edtAllergy;
    private TextView txtBloodGroup, btnSaveProfile;
    private LinearLayout bloodGroupBox;

    private final String[] bloodGroups = {
            "A Rh+", "A Rh-", "B Rh+", "B Rh-",
            "AB Rh+", "AB Rh-", "0 Rh+", "0 Rh-"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edtFullName = findViewById(R.id.edtFullName);
        edtChronic = findViewById(R.id.edtChronic);
        edtMedicine = findViewById(R.id.edtMedicine);
        edtAllergy = findViewById(R.id.edtAllergy);
        txtBloodGroup = findViewById(R.id.txtBloodGroup);
        bloodGroupBox = findViewById(R.id.bloodGroupBox);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // Load saved profile
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        edtFullName.setText(prefs.getString("profile_name", ""));
        edtChronic.setText(prefs.getString("profile_chronic", ""));
        edtMedicine.setText(prefs.getString("profile_medicine", ""));
        edtAllergy.setText(prefs.getString("profile_allergy", ""));
        txtBloodGroup.setText(prefs.getString("profile_blood", ""));

        bloodGroupBox.setOnClickListener(v -> showBloodGroupDialog());

        btnSaveProfile.setOnClickListener(v -> {
            if (edtFullName.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Lütfen ad soyad giriniz", Toast.LENGTH_SHORT).show();
                return;
            }
            saveProfile();
            Toast.makeText(this, "Profil Bilgileri Kaydedildi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            finish();
        });
    }

    private void saveProfile() {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
            .putString("profile_name", edtFullName.getText().toString().trim())
            .putString("profile_chronic", edtChronic.getText().toString().trim())
            .putString("profile_medicine", edtMedicine.getText().toString().trim())
            .putString("profile_allergy", edtAllergy.getText().toString().trim())
            .putString("profile_blood", txtBloodGroup.getText().toString().trim())
            .apply();
    }

    private void showBloodGroupDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Kan Grubunuzu Seçin")
                .setItems(bloodGroups, (dialog, which) -> txtBloodGroup.setText(bloodGroups[which]))
                .show();
    }
}
