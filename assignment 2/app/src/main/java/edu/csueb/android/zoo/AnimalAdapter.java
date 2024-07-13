package edu.csueb.android.zoo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {
    private List<Animal> animalList;
    private Context context;

    public AnimalAdapter(List<Animal> animalList, Context context) {
        this.animalList = animalList;
        this.context = context;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.animal_list_item, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        Animal animal = animalList.get(position);
        holder.name.setText(animal.getName());
        holder.thumbnail.setImageResource(animal.getThumbnail());

        holder.itemView.setOnClickListener(v -> {
            if (position == animalList.size() - 1) {
                new AlertDialog.Builder(context)
                        .setTitle("Warning")
                        .setMessage("This animal is very scary. Do you want to proceed?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            Intent intent = new Intent(context, AnimalDetailActivity.class);
                            intent.putExtra("animalName", animal.getName());
                            intent.putExtra("animalImage", animal.getImage());
                            intent.putExtra("animalDescription", animal.getDescription());
                            context.startActivity(intent);
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                Intent intent = new Intent(context, AnimalDetailActivity.class);
                intent.putExtra("animalName", animal.getName());
                intent.putExtra("animalImage", animal.getImage());
                intent.putExtra("animalDescription", animal.getDescription());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView thumbnail;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.animal_name);
            thumbnail = itemView.findViewById(R.id.animal_thumbnail);
        }
    }
}
