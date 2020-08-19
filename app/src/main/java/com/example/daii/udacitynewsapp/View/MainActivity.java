package com.example.daii.udacitynewsapp.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.daii.udacitynewsapp.Adapters.NewsAdapter;
import com.example.daii.udacitynewsapp.R;
import com.example.daii.udacitynewsapp.ViewModel.NewsViewModel;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements NewsAdapter.NewsOnClick {

    private RecyclerView recyclerView;
    private NewsAdapter mAdapter;
    private NewsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewsAdapter(this);
        recyclerView.setAdapter(mAdapter);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(NewsViewModel.class);
        viewModel.getAllArticle().observe(this, articles -> {
            mAdapter.setNewsData(articles);
            new ItemTouchHelper(new ItemTouchHelper.Callback() {
                @Override
                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    return makeMovementFlags(dragFlags, 0);
                }

                @Override
                public boolean isItemViewSwipeEnabled() {
                    return false;
                }


                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    if (viewHolder.getAdapterPosition() < target.getAdapterPosition()) {
                        for (int i = viewHolder.getAdapterPosition(); i < target.getAdapterPosition(); i++) {
                            Collections.swap(articles, i, i + 1);
                        }
                    } else {
                        for (int i = viewHolder.getAdapterPosition(); i > target.getAdapterPosition(); i--) {
                            Collections.swap(articles, i, i - 1);
                        }
                    }
                    mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    return true;
                }

                @Override
                public boolean isLongPressDragEnabled() {
                    return true;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                }
            }).attachToRecyclerView(recyclerView);
        });
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            callData();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void callData() {
        viewModel.Refresh();
        viewModel.getAllArticle().observe(this, articles -> mAdapter.setNewsData(articles));
    }

    @Override
    public void onClick(String uriNews) {
        Uri newUri = Uri.parse(uriNews);
        Intent linkNew = new Intent(Intent.ACTION_VIEW, newUri);
        startActivity(linkNew);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.About) {
            new AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage("This's the final exercise project for Udacity Android Basics Networking Course. \\n \\nThanks to NEWS API.")
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
