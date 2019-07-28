package com.pushpal.talkie.model.api;

import com.pushpal.talkie.model.model.CreditResponse;
import com.pushpal.talkie.model.model.Language;
import com.pushpal.talkie.model.model.MovieDetails;
import com.pushpal.talkie.model.model.MovieResponse;
import com.pushpal.talkie.model.model.Person;
import com.pushpal.talkie.model.model.ReviewResponse;
import com.pushpal.talkie.model.model.TrailerResponse;
import com.pushpal.talkie.model.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RESTApi {
    @GET(Constants.MOVIES_URL)
    Call<MovieResponse> getMovies(@Path("category") String category,
                                  @Query("api_key") String apiKey,
                                  @Query("page") int page);

    @GET(Constants.MOVIE_DETAILS_URL)
    Call<MovieDetails> getMovieDetails(@Path("id") int id,
                                       @Query("api_key") String apiKey);

    @GET(Constants.LANGUAGES)
    Call<List<Language>> getLanguages(@Query("api_key") String apiKey);

    @GET(Constants.CREDITS_URL)
    Call<CreditResponse> getCredits(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET(Constants.TRAILERS_URL)
    Call<TrailerResponse> getTrailers(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET(Constants.REVIEWS_URL)
    Call<ReviewResponse> getReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET(Constants.PERSON_URL)
    Call<Person> getPersonDetails(@Path("person_id") int personId, @Query("api_key") String apiKey);
}
