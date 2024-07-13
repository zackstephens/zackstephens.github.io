package edu.csueb.android.zoo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AnimalListingActivity extends AppCompatActivity {
    private List<Animal> animalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_listing);

        animalList = new ArrayList<>();
        animalList.add(new Animal("Lion", R.drawable.lion_thumbnail, "The lion is a big cat.", R.drawable.lion));
        animalList.add(new Animal("Tiger", R.drawable.tiger_thumbnail, "The tiger is a large wild cat.", R.drawable.tiger));
        animalList.add(new Animal("Elephant", R.drawable.elephant_thumbnail, "The elephant is the largest land animal.", R.drawable.elephant));
        animalList.add(new Animal("Giraffe", R.drawable.giraffe_thumbnail, "The giraffe is the tallest animal.", R.drawable.giraffe));
        animalList.add(new Animal("Bear", R.drawable.bear_thumbnail, "The bear is a large, powerful mammal.", R.drawable.bear));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        AnimalAdapter adapter = new AnimalAdapter(animalList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_info) {
            startActivity(new Intent(this, ZooInfoActivity.class));
            return true;
        } else if (id == R.id.action_uninstall) {
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
            uninstallIntent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(uninstallIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
