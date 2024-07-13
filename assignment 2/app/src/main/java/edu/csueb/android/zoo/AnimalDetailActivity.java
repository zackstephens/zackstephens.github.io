package edu.csueb.android.zoo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AnimalDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);

        Intent intent = getIntent();
        String name = intent.getStringExtra("animalName");
        int image = intent.getIntExtra("animalImage", -1);
        String description = intent.getStringExtra("animalDescription");

        TextView nameTextView = findViewById(R.id.animal_name);
        ImageView imageView = findViewById(R.id.animal_image);
        TextView descriptionTextView = findViewById(R.id.animal_description);

        nameTextView.setText(name);
        imageView.setImageResource(image);
        descriptionTextView.setText(description);
    }
}
