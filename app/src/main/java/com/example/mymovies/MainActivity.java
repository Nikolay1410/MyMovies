package com.example.mymovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovies.Adapter.MovieAdapter;
import com.example.mymovies.data.MainViewModal;
import com.example.mymovies.data.Movie;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPosters;
    private MovieAdapter movieAdapter;
    private Switch switchSort;
    private TextView textViewTopRated;
    private TextView textViewPopularity;

    private MainViewModal viewModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModal = new ViewModelProvider(this).get(MainViewModal.class);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        switchSort = findViewById(R.id.switchSort);
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);
        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter();
        recyclerViewPosters.setAdapter(movieAdapter);
        switchSort.setChecked(true);

        textViewTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMethodOfSort(true);
                switchSort.setChecked(true);
                textViewTopRated.setTextColor(getResources().getColor(R.color.teal_200));
                textViewPopularity.setTextColor(getResources().getColor(R.color.white));
            }
        });
        textViewPopularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMethodOfSort(false);
                switchSort.setChecked(false);
                textViewTopRated.setTextColor(getResources().getColor(R.color.white));
                textViewPopularity.setTextColor(getResources().getColor(R.color.teal_200));
            }
        });

        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setMethodOfSort(b);
            }
        });
            switchSort.setChecked(false);
            movieAdapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
                @Override
                public void onPosterClick(int position) {
                   Movie movie = movieAdapter.getMovies().get(position);
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("id", movie.getId());
                    startActivity(intent);
                }
            });
            movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
                @Override
                public void onReachEnd() {
                    Toast.makeText(MainActivity.this, "JJjjj", Toast.LENGTH_SHORT).show();
                }
            });
        LiveData<List<Movie>> moviesFromLiveData = viewModal.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieAdapter.setMovies(movies);
            }
        });
    }
    private void setMethodOfSort(boolean isTopRated){
        int methodOfSort;
        if(isTopRated){
            methodOfSort = NetworkUtils.TOP_RATED;
        }else {
            methodOfSort = NetworkUtils.POPULARITY;
        }
        downloadData(methodOfSort, 1);
    }
    private void downloadData(int methodOfSort, int page){
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(methodOfSort, 1);
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);
        if (movies!=null && !movies.isEmpty()){
            viewModal.deleteAllMovie();
            for (Movie movie : movies){
                viewModal.insertMovies(movie);
            }
        }
    }
}