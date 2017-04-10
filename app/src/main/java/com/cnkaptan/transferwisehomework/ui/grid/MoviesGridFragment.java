package com.cnkaptan.transferwisehomework.ui.grid;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cnkaptan.transferwisehomework.MovieApplication;
import com.cnkaptan.transferwisehomework.R;
import com.cnkaptan.transferwisehomework.data.DataManager;
import com.cnkaptan.transferwisehomework.data.pojos.Movie;
import com.cnkaptan.transferwisehomework.presenter.grid.MoviesGridFragmentContract;
import com.cnkaptan.transferwisehomework.presenter.grid.MoviesGridPresenter;
import com.cnkaptan.transferwisehomework.utils.ItemOffsetDecoration;
import com.cnkaptan.transferwisehomework.utils.OnItemClickListener;
import com.cnkaptan.transferwisehomework.utils.OnItemSelectedListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MoviesGridFragment extends Fragment implements OnItemClickListener,MoviesGridFragmentContract.View{

    private static final String TAG = MoviesGridFragment.class.getSimpleName();
    @BindView(R.id.movies_grid)
    RecyclerView moviesGrid;
    @BindView(R.id.image_no_movies)
    ImageView imageNoMovies;
    @BindView(R.id.view_no_movies)
    RelativeLayout viewNoMovies;
    Unbinder unbinder;
    MoviesAdapter adapter;

    @Inject
    DataManager dataManager;
    MoviesGridFragmentContract.Presenter presenter;
    private OnItemSelectedListener onItemSelectedListener;

    public static MoviesGridFragment newInstance() {
        MoviesGridFragment fragment = new MoviesGridFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            onItemSelectedListener = (OnItemSelectedListener) context;
        } else {
            throw new IllegalStateException("The activity must implement " +
                    OnItemSelectedListener.class.getSimpleName() + " interface.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies_grid, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MovieApplication)getActivity().getApplicationContext()).getApiComponent().inject(this);
        initMoviesGrid();
        presenter = new MoviesGridPresenter(dataManager);
        presenter.attachView(this);
        presenter.getDatasFromLocal();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
        presenter = null;
    }

    private void initMoviesGrid() {
        adapter = new MoviesAdapter(null, this);
        moviesGrid.setAdapter(adapter);
        moviesGrid.setItemAnimator(new DefaultItemAnimator());
        int columns = getResources().getInteger(R.integer.movies_columns);
        moviesGrid.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.movie_item_offset));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), columns);
        moviesGrid.setLayoutManager(gridLayoutManager);
    }


    @Override
    public void onItemClick(View itemView, int position) {
        if (adapter != null && adapter.getItemCount() > 0) {
            onItemSelectedListener.onItemSelected(adapter.getItem(position));
        }
    }

    @Override
    public void initDatas(List<Movie> movies) {
        adapter.setDatas(movies);

    }

    @Override
    public void onError(String message) {
        Log.e(TAG,message);
    }
}