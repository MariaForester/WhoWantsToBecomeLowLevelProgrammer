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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lowlevelprog.lowlevelprogrammer.Interface.ItemClickListener;
import com.lowlevelprog.lowlevelprogrammer.Interface.RankingBackCall;
import com.lowlevelprog.lowlevelprogrammer.Model.QuestionScore;
import com.lowlevelprog.lowlevelprogrammer.Model.Ranking;
import com.lowlevelprog.lowlevelprogrammer.ViewHolder.RankingViewHolder;


public class RankingFragment extends Fragment {
    View rankingFragment;
    FirebaseDatabase db;
    DatabaseReference questionScore, rankingData;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> fbAdapter;
    RecyclerView rankingList;
    LinearLayoutManager lm;

    int sum = 0;

    public static RankingFragment newInstance() {
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseDatabase.getInstance();
        questionScore = db.getReference("QuestionScore");
        rankingData = db.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rankingFragment = inflater.inflate(R.layout.fragment_ranking, container,
                false);

        rankingList = rankingFragment.findViewById(R.id.ranking_list);
        lm = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);
        rankingList.setLayoutManager(lm);

        updateScore(OnlineHelper.currentUser.getUserName(), new RankingBackCall<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingData.child(ranking.getUserName()).setValue(ranking);
                //showRanking();
            }
        });

        fbAdapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class, R.layout.layout_ranking, RankingViewHolder.class,
                rankingData.orderByChild("score")
        ) {
            @Override
            protected void populateViewHolder(RankingViewHolder rankingViewHolder, final Ranking ranking,
                                              int i) {
                rankingViewHolder.nameTextView.setText(ranking.getUserName());
                rankingViewHolder.scoreTextView.setText(String.valueOf(ranking.getScore()));

                rankingViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent detail = new Intent(getActivity(), RankingDetailed.class);
                        detail.putExtra("viewUser", ranking.getUserName());
                        startActivity(detail);
                    }
                });
            }
        };

        fbAdapter.notifyDataSetChanged();
        rankingList.setAdapter(fbAdapter);

        return rankingFragment;
    }

    private void updateScore(final String userName, final RankingBackCall<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(userName).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            QuestionScore scoreForQuest = data.getValue(QuestionScore.class);
                            sum += Integer.parseInt(scoreForQuest.getScore());
                        }
                        Ranking ranking = new Ranking(userName, sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }


}
