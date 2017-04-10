package com.cnkaptan.transferwisehomework.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnkaptan.transferwisehomework.MovieApplication;
import com.cnkaptan.transferwisehomework.R;
import com.cnkaptan.transferwisehomework.data.DataManager;
import com.cnkaptan.transferwisehomework.data.Movie;
import com.cnkaptan.transferwisehomework.data.Review;
import com.cnkaptan.transferwisehomework.data.Trailer;
import com.cnkaptan.transferwisehomework.presenter.detail.DetailContract;
import com.cnkaptan.transferwisehomework.presenter.detail.DetailPresenter;
import com.cnkaptan.transferwisehomework.util.Constants;
import com.cnkaptan.transferwisehomework.util.ItemOffsetDecoration;
import com.cnkaptan.transferwisehomework.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MovieDetailFragment extends Fragment implements DetailContract.View{
    public static final String MOVIE = "movie";

    private static final String MOVIE_VIDEOS_KEY = "MovieVideos";
    private static final String MOVIE_REVIEWS_KEY = "MovieReviews";
    private static final String LOG_TAG = "MovieDetailFragment";

    private static final double VOTE_PERFECT = 9.0;
    private static final double VOTE_GOOD = 7.0;
    private static final double VOTE_NORMAL = 5.0;

    @BindView(R.id.image_movie_detail_poster)
    ImageView movieImagePoster;
    @BindView(R.id.text_movie_original_title)
    TextView movieOriginalTitle;
    @BindView(R.id.text_movie_user_rating)
    TextView movieUserRating;
    @BindView(R.id.text_movie_release_date)
    TextView movieReleaseDate;
    @BindView(R.id.text_movie_overview)
    TextView movieOverview;
    @BindView(R.id.card_movie_detail)
    CardView cardMovieDetail;
    @BindView(R.id.card_movie_overview)
    CardView cardMovieOverview;

    @BindView(R.id.card_movie_videos)
    CardView cardMovieVideos;
    @BindView(R.id.movie_videos)
    RecyclerView movieVideos;

    @BindView(R.id.card_movie_reviews)
    CardView cardMovieReviews;
    @BindView(R.id.movie_reviews)
    RecyclerView movieReviews;
    Unbinder unbinder;
    private Movie movie;
    private MovieVideosAdapter videosAdapter;
    private MovieReviewsAdapter reviewsAdapter;
    private DetailContract.Presenter presenter;
    @Inject
    DataManager dataManager;

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE,movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(MOVIE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        initViews();
        initVideosList();
        initReviewsList();
        setupCardsElevation();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MovieApplication)context.getApplicationContext()).getApiComponent().inject(this);
        presenter = new DetailPresenter(dataManager);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
        presenter = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videosAdapter.getItemCount() != 0) {
            outState.putParcelableArrayList(MOVIE_VIDEOS_KEY, new ArrayList<>(videosAdapter.getMovieVideos()));
        }
        if (reviewsAdapter.getItemCount() != 0) {
            outState.putParcelableArrayList(MOVIE_REVIEWS_KEY, new ArrayList<>(reviewsAdapter.getMovieReviews()));
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            videosAdapter.setMovieVideos(savedInstanceState.getParcelableArrayList(MOVIE_VIDEOS_KEY));
            reviewsAdapter.setMovieReviews(savedInstanceState.getParcelableArrayList(MOVIE_REVIEWS_KEY));
        }
    }


    private void initViews() {
        Glide.with(this)
                .load(Constants.POSTER_IMAGE_BASE_URL + Constants.POSTER_IMAGE_SIZE + movie.getPosterPath())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(movieImagePoster);
        movieOriginalTitle.setText(movie.getOriginalTitle());
        movieUserRating.setText(String.format(Locale.US, "%.1f", movie.getAverageVote()));
        movieUserRating.setTextColor(getRatingColor(movie.getAverageVote()));
        String releaseDate = String.format(getString(R.string.movie_detail_release_date),
                movie.getReleaseDate());
        movieReleaseDate.setText(releaseDate);
        movieOverview.setText(movie.getOverview());
    }

    @ColorInt
    private int getRatingColor(double averageVote) {
        if (averageVote >= VOTE_PERFECT) {
            return ContextCompat.getColor(getContext(), R.color.vote_perfect);
        } else if (averageVote >= VOTE_GOOD) {
            return ContextCompat.getColor(getContext(), R.color.vote_good);
        } else if (averageVote >= VOTE_NORMAL) {
            return ContextCompat.getColor(getContext(), R.color.vote_normal);
        } else {
            return ContextCompat.getColor(getContext(), R.color.vote_bad);
        }
    }

    private void initVideosList() {
        videosAdapter = new MovieVideosAdapter(getContext());
        videosAdapter.setOnItemClickListener((itemView, position) -> onMovieVideoClicked(position));
        movieVideos.setAdapter(videosAdapter);
        movieVideos.setItemAnimator(new DefaultItemAnimator());
        movieVideos.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen.movie_item_offset));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        movieVideos.setLayoutManager(layoutManager);
        presenter.getTrailersList(movie.getMovideId());
    }

    private void initReviewsList() {
        reviewsAdapter = new MovieReviewsAdapter();
        reviewsAdapter.setOnItemClickListener((itemView, position) -> onMovieReviewClicked(position));
        movieReviews.setAdapter(reviewsAdapter);
        movieReviews.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        movieReviews.setLayoutManager(layoutManager);
        presenter.getReviewsList(movie.getMovideId());
    }

    private void onMovieReviewClicked(int position) {
        Review review = reviewsAdapter.getItem(position);
        if (review != null && review.getReviewUrl() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getReviewUrl()));
            startActivity(intent);
        }
    }

    private void onMovieVideoClicked(int position) {
        Trailer video = videosAdapter.getItem(position);
        if (video != null && video.isYoutubeVideo()) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
            startActivity(intent);
        }
    }

    private void setupCardsElevation() {
        setupCardElevation(cardMovieDetail);
        setupCardElevation(cardMovieVideos);
        setupCardElevation(cardMovieOverview);
        setupCardElevation(cardMovieReviews);
    }

    private void setupCardElevation(View view) {
        ViewCompat.setElevation(view,
                Utils.convertDpToPixel(getResources().getInteger(R.integer.movie_detail_content_elevation_in_dp),getContext().getResources()));
    }


    @Override
    public void initTrailersData(List<Trailer> trailers) {
        videosAdapter.setMovieVideos(trailers);
    }

    @Override
    public void initReviewsData(List<Review> reviews) {
        reviewsAdapter.setMovieReviews(reviews);
    }

    @Override
    public void showError(String message) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(R.string.okey, (dialog1, which) -> dialog1.dismiss())
                .setCancelable(false)
                .create();
        dialog.show();
    }
}
