package com.georgia.gk.pharmafriendapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {

private ImageView category1Image;

    Map<String, String> categories = new HashMap();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories.put("01", "Cold and Flu");
        categories.put("02", "Ear Care");
        categories.put("03", "Eye Care");
        categories.put("04", "Indigestion and Heartburn");
        categories.put("05", "Mouth Ulcers");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Resources r = getResources();
        String name = getActivity().getPackageName();
        for(Map.Entry<String, String> entry : categories.entrySet()){
            ImageView catView = view.findViewById(r.getIdentifier("category_"+entry.getKey(), "id", name));
            catView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MedicationCategoryListActivity.class);
                    intent.putExtra("category", entry.getValue());
                    startActivity(intent);
                }
            });
        }

//        category1Image = view.findViewById(R.id.category_coldnflu);
//        category1Image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),ColdAndFluCategoryActivity.class);
//
//                startActivity(intent);
//            }
//        });

        return view;
    }
}