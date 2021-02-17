package com.zhezhe.viewpractice.view.shot_list;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonSyntaxException;
import com.zhezhe.viewpractice.R;
import com.zhezhe.viewpractice.objects.Shot;
import com.zhezhe.viewpractice.tiktok.Tiktok;
import com.zhezhe.viewpractice.view.base.SpaceItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShotListFragment extends Fragment {
    private RecyclerView recyclerView;

    private ShotListAdapter adapter;

    public static ShotListFragment newInstance() {
        return new ShotListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new
                SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_medium)));

        adapter = new ShotListAdapter(new ArrayList<Shot>(), new ShotListAdapter.LoadMoreListener() {

            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(new LoadShotTask(adapter.getItemCount() / Tiktok.COUNT_PER_PAGE + 1));
            }
        });
    }

    private class LoadShotTask extends AsyncTask<Void, Void, List<Shot>> {
        int page;

        public LoadShotTask(int page) {
            this.page = page;
        }

        @Override
        protected List<Shot> doInBackground(Void... voids) {
            try {
                return Tiktok.callGetShotsApi(page);
            } catch (IOException | JsonSyntaxException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Shot> shots) {
            if (shots != null) {
                adapter.append(shots);
                adapter.setShowLoading(shots.size() == Tiktok.COUNT_PER_PAGE);
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
