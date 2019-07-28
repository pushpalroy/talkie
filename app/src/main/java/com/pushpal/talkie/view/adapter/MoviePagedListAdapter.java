package com.pushpal.talkie.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pushpal.talkie.R;
import com.pushpal.talkie.databinding.MovieItemBinding;
import com.pushpal.talkie.model.data.AppExecutors;
import com.pushpal.talkie.model.data.MovieDatabase;
import com.pushpal.talkie.model.model.Movie;
import com.squareup.picasso.Picasso;

import static com.pushpal.talkie.model.util.Constants.IMAGE_BASE_URL;
import static com.pushpal.talkie.model.util.Constants.IMAGE_SIZE_342;

public class MoviePagedListAdapter extends PagedListAdapter<Movie, MoviePagedListAdapter.MoviePagedViewHolder> {

    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final MoviePagedListAdapter.MoviePagedListAdapterOnClickHandler mOnClickHandler;

    // Parent context
    private Context mContext;

    /**
     * The interface that receives onClick messages.
     */
    public interface MoviePagedListAdapterOnClickHandler {
        void onItemClick(Movie movie);
    }

    /**
     * Tell MoviePagedListAdapter how to compute the differences between the two elements
     */
    private static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie>() {
                // The ID property identifies when items are the same
                @Override
                public boolean areItemsTheSame(Movie oldItem, Movie newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(Movie oldItem, @NonNull Movie newItem) {
                    return oldItem.contentEquals(newItem);
                }
            };

    /**
     * Constructor for MoviePagedListAdapter
     *
     * @param onClickHandler The on-click handler for this adapter. This single handler
     *                       is called when an item is clicked
     */
    public MoviePagedListAdapter(MoviePagedListAdapterOnClickHandler onClickHandler) {
        super(MoviePagedListAdapter.DIFF_CALLBACK);
        mOnClickHandler = onClickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent   The ViewGroup that these ViewHolders are contained within.
     * @param viewType If your RecyclerView has more than one type of item (which ours doesn't) you
     *                 can use this viewType integer to provide a different layout.
     * @return A new CompactMoviePagedViewHolder that holds the MovieListItemBinding
     */
    @NonNull
    @Override
    public MoviePagedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        MovieItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.movie_item,
                parent, false);
        return new MoviePagedViewHolder(binding);
    }

    /**
     * Called by the RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MoviePagedViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    /**
     * Cache of the children views for a list item.
     */
    public class MoviePagedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /**
         * This field is used for data binding
         */
        private MovieItemBinding mBinding;

        /**
         * Constructor for the CompactMoviePagedViewHolder
         *
         * @param compactBinding Used to access the layout's variables and views
         */
        MoviePagedViewHolder(MovieItemBinding compactBinding) {
            super(compactBinding.getRoot());
            mBinding = compactBinding;
            // Call setOnClickListener on the view
            itemView.setOnClickListener(this);
        }

        /**
         * This method will take a Movie object as input and use that movie to display the appropriate
         * text and an image within a list item.
         *
         * @param movie The movie object
         */
        void bind(Movie movie) {
            // Get the complete thumbnail path
            String thumbnail = IMAGE_BASE_URL + IMAGE_SIZE_342 + movie.getPosterPath();

            // Load thumbnail with Picasso library
            Picasso.with(itemView.getContext())
                    .load(thumbnail)
                    .fit().centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_image)
                    .into(mBinding.ivMoviePoster);

            // Display movie title
            mBinding.tvMovieTitle.setText(movie.getTitle());

            // Display vote average
            mBinding.tvVoteCount.setText(String.valueOf(movie.getVoteAverage()));

            // Display release year
            String[] date = movie.getReleaseDate().split("-");
            mBinding.tvMovieReleaseYear.setText(date[0]);

            // Set like button listener
            mBinding.btnLike.setOnCheckStateChangeListener((view, checked) -> {
                // Using single thread executor
                AppExecutors.getInstance().diskIO().execute(() -> {
                    final String updateMessage;
                    if (checked) {
                        MovieDatabase.getInstance(mContext).movieDao().insertMovie(movie);
                        updateMessage = "Added to favourites";
                    } else {
                        MovieDatabase.getInstance(mContext).movieDao().deleteMovie(movie);
                        updateMessage = "Removed from favourites";
                    }
                    ((Activity) mContext).runOnUiThread(() ->
                            Toast.makeText(mContext, updateMessage, Toast.LENGTH_SHORT).show());
                });
            });
        }

        /**
         * Called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = getItem(adapterPosition);
            mOnClickHandler.onItemClick(movie);
        }
    }
}