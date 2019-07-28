package com.pushpal.talkie.model.util;

import com.pushpal.talkie.BuildConfig;

public class Constants {
    // Constants of The Movie Database API
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String MOVIES_URL = "movie/{category}";
    public static final String MOVIE_DETAILS_URL = "movie/{id}";
    public static final String LANGUAGES = "configuration/languages";
    public static final String CREDITS_URL = "movie/{movie_id}/credits";
    public static final String TRAILERS_URL = "movie/{movie_id}/videos";
    public static final String REVIEWS_URL = "movie/{movie_id}/reviews";
    public static final String PERSON_URL = "person/{person_id}";

    public static final String CATEGORY_POPULAR = "popular";
    public static final String CATEGORY_TOP_RATED = "top_rated";

    /**
     * Constants that are used to request the network call
     */
    public static final String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    public static final int PAGE = 1;

    /**
     * Constants for pages used in MovieDataSource
     */
    public static final int PREVIOUS_PAGE_KEY_ONE = 1;
    public static final int NEXT_PAGE_KEY_TWO = 2;
    public static final int PAGE_ONE = 1;

    public static final int GRID_SPAN_COUNT = 2;

    /**
     * The number of fixed thread pools used in the MainActivityViewModel
     */
    public static final int NUMBER_OF_FIXED_THREADS = 5;

    // Constants used in MainActivityViewModel
    /**
     * Size hint for initial load of PagedList
     */
    public static final int INITIAL_LOAD_SIZE_HINT = 10;
    /**
     * Size of each page loaded by the PagedList
     */
    public static final int PAGE_SIZE = 20;
    /**
     * Prefetch distance which defines how far ahead to load
     */
    public static final int PREFETCH_DISTANCE = 50;

    /**
     * Constant for Credits object
     */
    public static final int BYTE = 0x01;

    /**
     * API Status code for invalid API key or Authentication failed
     */
    public static final int API_STATUS_401 = 401;

    /**
     * Constants for menu option in FavoriteAdapter
     */
    public static final String DELETE = "Delete";
    public static final int DELETE_GROUP_ID = 0;
    public static final int DELETE_ORDER = 0;

    /** Key for storing the scroll position in MainActivity */
    public static final String LAYOUT_MANAGER_STATE = "layout_manager_state";


    public static final String DISCOVER_URL = BASE_URL + "discover/movie?api_key=" + API_KEY;

    // Genres
    public static final String GENRES_LIST_URL = BASE_URL + "/genre/movie/list?api_key=" + API_KEY;

    // Returns base_url, secure_base_url, backdrop_sizes, logo_sizes, poster_sizes, profile_sizes, still_sizes,change_keys
    public static final String IMAGE_CONFIGURATION_URL = BASE_URL + "/configuration?api_key=" + API_KEY;
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE_185 = "w185";
    public static final String IMAGE_SIZE_342 = "w342";

    // Grid arrangement types
    public static final String ARRANGEMENT_COMPACT = "compact";
    public static final String ARRANGEMENT_COZY = "cozy";

    // Samples -------------------------------------------->
    // https://api.themoviedb.org/3/movie/popular?api_key=key
    // https://api.themoviedb.org/3/movie/top_rated?api_key=<<api_key>>&language=en-US&page=1
    // https://api.themoviedb.org/3/discover/movie?api_key=key&language=en-US&sort_by=popularity.desc&include_adult=false
    // &include_video=false&page=1

    public static final String EXTRA_MOVIE_ITEM = "movie_image_url";
    public static final String EXTRA_MOVIE_IMAGE_TRANSITION_NAME = "movie_image_transition_name";

    public static final String EXTRA_PERSON_ITEM = "person";

    public static final String SORT_TYPE = "sort_type";
    public static final String ADAPTER_POSITION = "adapter_position";
    public static final String ARRANGEMENT_TYPE = "arrangement_type";
    public static final String CALL_PAGE = "call_page";
    public static final String CALL_PAGE_PENDING = "call_page_pending";
    public static final String RESUME_NORMAL = "normal";
    public static final String RESUME_AFTER_ROTATION = "rotated";
}
