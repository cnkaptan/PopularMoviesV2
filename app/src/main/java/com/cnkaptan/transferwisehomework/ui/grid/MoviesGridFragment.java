package com.cnkaptan.transferwisehomework.ui.grid;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cnkaptan.transferwisehomework.MovieApplication;
import com.cnkaptan.transferwisehomework.R;
import com.cnkaptan.transferwisehomework.data.DataManagerImpl;
import com.cnkaptan.transferwisehomework.model.Movie;
import com.cnkaptan.transferwisehomework.presenter.grid.MoviesGridFragmentContract;
import com.cnkaptan.transferwisehomework.presenter.grid.MoviesGridPresenter;
import com.cnkaptan.transferwisehomework.util.EndlessRecyclerViewOnScrollListener;
import com.cnkaptan.transferwisehomework.util.ItemOffsetDecoration;
import com.cnkaptan.transferwisehomework.util.OnItemClickListener;
import com.cnkaptan.transferwisehomework.util.OnItemSelectedListener;
import com.cnkaptan.transferwisehomework.util.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MoviesGridFragment extends Fragment implements OnItemClickListener, MoviesGridFragmentContract.View
        , SwipeRefreshLayout.OnRefreshListener {

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
    DataManagerImpl dataManagerImpl;
    MoviesGridFragmentContract.Presenter presenter;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    private OnItemSelectedListener onItemSelectedListener;
    private EndlessRecyclerViewOnScrollListener endlessRecyclerViewOnScrollListener;
    private GridLayoutManager gridLayoutManager;
    private Dialog dialog;

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
        ((MovieApplication) getActivity().getApplicationContext()).getApiComponent().inject(this);
        initMoviesGrid();
        initSwipeRefreshLayout();
        presenter = new MoviesGridPresenter(dataManagerImpl, Schedulers.io(), AndroidSchedulers.mainThread());
        presenter.attachView(this);
        presenter.getDatasFromLocal();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateGridLayout();
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
        gridLayoutManager = new GridLayoutManager(getActivity(), columns);
        moviesGrid.setLayoutManager(gridLayoutManager);
        onMoviesGridInitialisationFinished();
    }

    private void initSwipeRefreshLayout() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.primary_material_dark,
                R.color.accent_material_light);
    }


    private void onMoviesGridInitialisationFinished() {
        endlessRecyclerViewOnScrollListener = new EndlessRecyclerViewOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore() {
                presenter.loadMoreMovies();
            }
        };
        moviesGrid.addOnScrollListener(endlessRecyclerViewOnScrollListener);
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
        updateGridLayout();

    }

    @Override
    public void onError(String message) {
        endlessRecyclerViewOnScrollListener.setLoading(false);
        Snackbar.make(swipeLayout, R.string.error_failed_to_update_movies,
                Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showLoading() {
        dialog = Utils.showLoading(getContext());
    }

    @Override
    public void hideLoading() {
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void hideRefresh() {
        swipeLayout.setRefreshing(false);

    }

    @Override
    public void showRefresh() {
        swipeLayout.setRefreshing(true);
    }

    @Override
    public void addNewMovies(List<Movie> movies) {
        adapter.addDatas(movies);
        endlessRecyclerViewOnScrollListener.setLoading(false);
        updateGridLayout();
    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(true);
        presenter.refreshMovies();
    }

    protected void updateGridLayout() {
        if (adapter.getItemCount() == 0) {
            moviesGrid.setVisibility(View.GONE);
            viewNoMovies.setVisibility(View.VISIBLE);
        } else {
            moviesGrid.setVisibility(View.VISIBLE);
            viewNoMovies.setVisibility(View.GONE);
        }
    }

}
