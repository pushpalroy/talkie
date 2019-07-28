package com.pushpal.talkie.view.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pushpal.talkie.R;
import com.pushpal.talkie.databinding.MovieItemBinding;
import com.pushpal.talkie.model.data.AppExecutors;
import com.pushpal.talkie.model.data.MovieDatabase;
import com.pushpal.talkie.model.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.pushpal.talkie.model.util.Constants.DELETE;
import static com.pushpal.talkie.model.util.Constants.DELETE_GROUP_ID;
import static com.pushpal.talkie.model.util.Constants.DELETE_ORDER;
import static com.pushpal.talkie.model.util.Constants.IMAGE_BASE_URL;
import static com.pushpal.talkie.model.util.Constants.IMAGE_SIZE_185;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    /**
     * Member variable for the list of mMovies that holds movie data
     */
    private List<Movie> mMovies;

    /**
     * Context we use to utility methods, app resources and layout inflaters
     */
    private Context mContext;

    /**
     * An on-click handler that we've defined to make it easy for a Activity to interface with
     * our RecyclerView
     */
    private final FavoriteAdapterOnClickHandler mOnClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface FavoriteAdapterOnClickHandler {
        void onFavItemClick(Movie movie);
    }

    /**
     * Constructor for the FavoriteAdapter
     */
    public FavoriteAdapter(Context context, FavoriteAdapterOnClickHandler onClickHandler) {
        mContext = context;
        mOnClickHandler = onClickHandler;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView
     *
     * @return A new FavoriteViewHolder that holds the FavListItemBinding
     */
    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        MovieItemBinding favItemBinding = DataBindingUtil
                .inflate(layoutInflater, R.layout.movie_item, parent, false);
        return new FavoriteViewHolder(favItemBinding);
    }

    /**
     * Called by the RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.bind(movie);
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }

    /**
     * When data changes, updates the list of mMovies
     * and notifies the adapter to use the new values on it
     */
    public void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    /**
     * Returns the list of mMovies
     */
    public List<Movie> getMovies() {
        return mMovies;
    }

    /**
     * Cache of the children views for favorite movie list item.
     */
    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        /**
         * This field is used for data binding
         */
        MovieItemBinding mFavItemBinding;

        /**
         * Constructor for FavoriteViewHolder
         *
         * @param favItemBinding Used to access the layout's variables and views
         */
        public FavoriteViewHolder(MovieItemBinding favItemBinding) {
            super(favItemBinding.getRoot());

            mFavItemBinding = favItemBinding;
            // Call setOnClickListener on the view
            itemView.setOnClickListener(this);
            // Call setOnCreateContextMenuListener on the view
            itemView.setOnCreateContextMenuListener(this);
        }

        void bind(Movie movie) {
            // Get the complete thumbnail path
            String thumbnail = IMAGE_BASE_URL + IMAGE_SIZE_185 + movie.getPosterPath();

            // Load thumbnail with Picasso library
            Picasso.with(itemView.getContext())
                    .load(thumbnail)
                    .into(mFavItemBinding.ivMoviePoster);

            // Set title of the movie to the TextView
            mFavItemBinding.tvMovieTitle.setText(movie.getTitle());
        }

        /**
         * Called whenever a user clicks on an movie in the list
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPosition);
            mOnClickHandler.onFavItemClick(movie);
        }

        /**
         * When the user performs a long-click on a favorite movie item, a floating menu appears.
         * <p>
         * Reference @see "https://stackoverflow.com/questions/36958800/recyclerview-getmenuinfo-always-null"
         * "https://stackoverflow.com/questions/37601346/create-options-menu-for-recyclerview-item"
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            int adapterPosition = getAdapterPosition();
            // Set the itemId to adapterPosition to retrieve movie later
            MenuItem item = menu.add(DELETE_GROUP_ID,
                    adapterPosition,
                    DELETE_ORDER,
                    v.getContext().getString(R.string.action_delete));
            item.setOnMenuItemClickListener(this);
        }

        /**
         * This gets called when a menu item is clicked.
         */
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getTitle().toString()) {
                case DELETE:
                    int adapterPosition = item.getItemId();
                    Movie movie = mMovies.get(adapterPosition);
                    // Delete a favorite movie
                    delete(movie);
                    return true;
                default:
                    return false;
            }
        }

        /**
         * Delete a favorite movie when the user clicks "Delete" menu option.
         */
        private void delete(final Movie movie) {
            // Get the MovieDatabase instance
            final MovieDatabase db = MovieDatabase.getInstance(mContext);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    // Delete a favorite movie from the MovieDatabase by using the movieDao
                    db.movieDao().deleteMovie(movie);
                }
            });
        }
    }
}