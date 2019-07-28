package com.pushpal.talkie.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pushpal.talkie.R;
import com.pushpal.talkie.databinding.MovieItemBinding;
import com.pushpal.talkie.model.model.Movie;
import com.squareup.picasso.Picasso;

import static com.pushpal.talkie.model.util.Constants.IMAGE_BASE_URL;
import static com.pushpal.talkie.model.util.Constants.IMAGE_SIZE_185;

public class MoviePagedListAdapter extends PagedListAdapter<Movie, MoviePagedListAdapter.MoviePagedViewHolder> {

    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final MoviePagedListAdapter.MoviePagedListAdapterOnClickHandler mOnClickHandler;

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

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Movie oldItem, Movie newItem) {
                    return oldItem.equals(newItem);
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
     * @return A new MoviePagedViewHolder that holds the MovieListItemBinding
     */
    @NonNull
    @Override
    public MoviePagedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MovieItemBinding mMovieItemBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.movie_item, parent, false);

        return new MoviePagedViewHolder(mMovieItemBinding);
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

    /**
     * Cache of the children views for a list item.
     */
    public class MoviePagedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /**
         * This field is used for data binding
         */
        private MovieItemBinding mMovieItemBinding;

        /**
         * Constructor for the MoviePagedViewHolder
         *
         * @param movieItemBinding Used to access the layout's variables and views
         */
        public MoviePagedViewHolder(MovieItemBinding movieItemBinding) {
            super(movieItemBinding.getRoot());
            mMovieItemBinding = movieItemBinding;
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
            String thumbnail = IMAGE_BASE_URL + IMAGE_SIZE_185 + movie.getPosterPath();

            // Load thumbnail with Picasso library
            Picasso.with(itemView.getContext())
                    .load(thumbnail)
                    .error(R.drawable.error_image)
                    .into(mMovieItemBinding.ivMoviePoster);

            // Display the title
            mMovieItemBinding.tvTitle.setText(movie.getTitle());

            // Display the vote average
            mMovieItemBinding.tvVoteCount.setText(String.valueOf(movie.getVoteAverage()));
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