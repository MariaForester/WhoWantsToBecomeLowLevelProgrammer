package com.lowlevelprog.lowlevelprogrammer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lowlevelprog.lowlevelprogrammer.Model.QuestionScore;
import com.lowlevelprog.lowlevelprogrammer.ViewHolder.RankingDetailViewHolder;

public class RankingDetailed extends AppCompatActivity {

    String viewUser = "";
    FirebaseDatabase db;
    DatabaseReference questionScore;
    RecyclerView scoresList;
    RecyclerView.LayoutManager lm;
    FirebaseRecyclerAdapter<QuestionScore, RankingDetailViewHolder> fbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_detailed);

        db = FirebaseDatabase.getInstance();
        questionScore = db.getReference("QuestionScore");

        scoresList = findViewById(R.id.ranking_list_detailed);
        scoresList.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        scoresList.setLayoutManager(lm);

        if (getIntent() != null)
            viewUser = getIntent().getStringExtra("viewUser");
        if (!viewUser.isEmpty())
            loadRankingDetail(viewUser);
    }

    private void loadRankingDetail(final String viewUser) {
        fbAdapter = new FirebaseRecyclerAdapter<QuestionScore, RankingDetailViewHolder>(
                QuestionScore.class, R.layout.ranking_detail_layout, RankingDetailViewHolder.class,
                questionScore.orderByChild("user").equalTo(viewUser)
        ) {
            @Override
            protected void populateViewHolder(RankingDetailViewHolder rankingDetailViewHolder,
                                              QuestionScore questionScore, int i) {
                rankingDetailViewHolder.nameTextView.setText(questionScore.getModeName());
                rankingDetailViewHolder.scoreTextView.setText(questionScore.getScore());
            }
        };
        fbAdapter.notifyDataSetChanged();
        scoresList.setAdapter(fbAdapter);
    }
}
