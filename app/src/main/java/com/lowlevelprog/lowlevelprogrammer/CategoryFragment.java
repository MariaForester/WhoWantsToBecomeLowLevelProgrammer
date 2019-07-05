package com.lowlevelprog.lowlevelprogrammer;

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
import com.lowlevelprog.lowlevelprogrammer.ViewHolder.CategoryViewHolder;
import com.squareup.picasso.Picasso;

public class CategoryFragment extends Fragment {

    private View categoryFragment;
    private RecyclerView listCategory;
    private RecyclerView.LayoutManager lm;
    private FirebaseRecyclerAdapter<Category, CategoryViewHolder> fbAdapter;
    private FirebaseDatabase db;
    private DatabaseReference categories;

    public static CategoryFragment newInstance() {
        CategoryFragment categoryFragment = new CategoryFragment();
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
        categoryFragment = inflater.inflate(R.layout.fragment_category, container,
                false);
        listCategory = categoryFragment.findViewById(R.id.list_categories);
        listCategory.setHasFixedSize(true);
        lm = new LinearLayoutManager(container.getContext());
        listCategory.setLayoutManager(lm);

        loadCategories();

        return categoryFragment;
    }

    private void loadCategories() {
        fbAdapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                Category.class, R.layout.category_layout, CategoryViewHolder.class,
                categories
        ) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder,
                                              final Category category, int i) {
                viewHolder.categoryName.setText(category.getName());
                Picasso.with(getActivity()).load(category.getImage()).into(viewHolder.categoryImage);

                viewHolder.setIcl(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getActivity(), String.format("%s|%s",
                                fbAdapter.getRef(position).getKey(), category.getName()),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        fbAdapter.notifyDataSetChanged();
        listCategory.setAdapter(fbAdapter);
    }
}
