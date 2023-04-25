package com.georgia.gk.pharmafriendapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.georgia.gk.pharmafriendapp.DTOs.Medications;
import com.georgia.gk.pharmafriendapp.databinding.ActivityMedicationCategoryListBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.net.URL;


public class MedicationCategoryListActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private RecyclerView medicationsList;

    private FirestoreRecyclerAdapter adapter;
    private ActivityMedicationCategoryListBinding binding;
    private Thread imageDataThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicationCategoryListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.topNavigationView3.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home2:
                    startActivity(new Intent(MedicationCategoryListActivity.this, HomeActivity.class));
                    break;
            }
             return true;
        });

        firestore = FirebaseFirestore.getInstance();
        medicationsList = findViewById(R.id.medications_list);
        Bundle extras = getIntent().getExtras();

        CollectionReference medications = firestore.collection("Medications");
        Query q = medications.whereEqualTo("category", extras.getString("category"));

        FirestoreRecyclerOptions<Medications> options = new FirestoreRecyclerOptions.Builder<Medications>()
                .setQuery(q, Medications.class).build();

        adapter = new FirestoreRecyclerAdapter<Medications, MedicationsViewHolder>(options) {
            @NonNull
            @Override
            public MedicationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_single, parent, false);
                return new MedicationsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MedicationsViewHolder holder, int position, @NonNull Medications model) {
                holder.medication_Name.setText(model.getMedication_Name());
                holder.medication_Price.setText(model.getMedication_Price());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MedicationCategoryListActivity.this,
                                MedicationActivity.class);
                        intent.putExtra("object", model);
                        startActivity(intent);
                    }
                });

                imageDataThread = new Thread(() -> {
                    try {
                        URL tUrl = new URL(model.medication_Image);
                        Bitmap imageBitmap = BitmapFactory.decodeStream(tUrl.openConnection().getInputStream());
                        runOnUiThread(() -> holder.medication_Image.setImageBitmap(imageBitmap));
                    } catch(IOException pExc) {
                        pExc.printStackTrace();
                    }
                });
                imageDataThread.start();
            }
        };

        medicationsList.setHasFixedSize(true);
        medicationsList.setLayoutManager(new LinearLayoutManager(this));
        medicationsList.setAdapter(adapter);
    }

    private class MedicationsViewHolder extends RecyclerView.ViewHolder {
        private TextView medication_Name;
        private TextView medication_Price;
        private ImageView medication_Image;
        public MedicationsViewHolder(@NonNull View itemView) {
            super(itemView);

            medication_Name = itemView.findViewById(R.id.medication_Name);
            medication_Price = itemView.findViewById(R.id.medication_Price);
            medication_Image = itemView.findViewById(R.id.medication_Image);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}