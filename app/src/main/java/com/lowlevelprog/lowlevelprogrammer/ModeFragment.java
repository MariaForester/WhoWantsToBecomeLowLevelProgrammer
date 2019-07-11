package com.lowlevelprog.lowlevelprogrammer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lowlevelprog.lowlevelprogrammer.Interface.ItemClickListener;
import com.lowlevelprog.lowlevelprogrammer.Model.Category;
import com.lowlevelprog.lowlevelprogrammer.ViewHolder.ModeViewHolder;
import com.squareup.picasso.Picasso;

public class ModeFragment extends Fragment {

    View myFragment;
    RecyclerView listCategory;
    RecyclerView.LayoutManager lm;
    FirebaseRecyclerAdapter<Category, ModeViewHolder> fbAdapter;
    FirebaseDatabase db;
    DatabaseReference categories;

    public static ModeFragment newInstance() {
        ModeFragment categoryFragment = new ModeFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseDatabase.getInstance();
        categories = db.getReference("Category");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_mode, container,
                false);

        listCategory = myFragment.findViewById(R.id.list_categories);
        listCategory.setHasFixedSize(true);
        lm = new LinearLayoutManager(container.getContext());
        listCategory.setLayoutManager(lm);

        loadCategories();

        return myFragment;
    }

    private void loadCategories() {
        fbAdapter = new FirebaseRecyclerAdapter<Category, ModeViewHolder>(
                Category.class, R.layout.mode_layout, ModeViewHolder.class,
                categories
        ) {
            @Override
            protected void populateViewHolder(ModeViewHolder viewHolder,
                                              final Category model, int i) {
                viewHolder.categoryName.setText(model.getName());
                Picasso.with(getActivity()).load(model.getImage()).into(viewHolder.categoryImage);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(), String.format("%s",
                                model.getName()),
                                Toast.LENGTH_SHORT).show();
                        OnlineHelper.modeID = fbAdapter.getRef(position).getKey();
                        OnlineHelper.modeName = model.getName();
                        if (fbAdapter.getRef(position).getKey().equals("01")) {
                            Intent startGame = new Intent(getActivity(), SimpleMode.class);
                            startActivity(startGame);
                            getActivity().finish();
                        } else if (fbAdapter.getRef(position).getKey().equals("02")) {
                            Intent startGame = new Intent(getActivity(), HardMode.class);
                            startActivity(startGame);
                            getActivity().finish();
                        }
                    }
                });
            }
        };
        fbAdapter.notifyDataSetChanged();
        listCategory.setAdapter(fbAdapter);
    }
}
