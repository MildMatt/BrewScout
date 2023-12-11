package com.techelevator.dao;

import com.sun.tools.jconsole.JConsoleContext;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Brewery;
import com.techelevator.model.Review;
import com.techelevator.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class JdbcReviewDao implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcReviewDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Review> getReviewsByUserId(int id) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT user_id, brew_id, beer_id, review, rating, favorite, liked FROM public.reviews WHERE user_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                Review review = mapRowToReview(results);
                reviews.add(review);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return reviews;
    }

    @Override
    public Review createReview(Review review) {
        String sql = "INSERT INTO public.reviews(user_id, brew_id, beer_id, review, rating, favorite, liked) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING user_id;";
        try {
            int rowInserted = jdbcTemplate.queryForObject(sql, int.class, review.getUser_id(), review.getBrew_id(), review.getBeer_id(), review.getReview(), review.getRating(), review.isFavorite(), review.isLiked());
         } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return review;

//   This is the JSON body for create Review
//        {
//                 "user_id": 3,
//                "brew_id": 1,
//                "beer_id": 1,
//                "review": "love this place",
//                "rating": 5,
//                "favorite": "true",
//                "liked": "true"
//        }

    }

    @Override
    public List<Review> getReviewsByBrewId(int id) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT user_id, brew_id, beer_id, review, rating, favorite, liked FROM public.reviews WHERE brew_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                Review review = mapRowToReview(results);
                reviews.add(review);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return reviews;
    }


    @Override
    public List<Review> getReviewsByBeerId(int id) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT user_id, brew_id, beer_id, review, rating, favorite, liked FROM public.reviews WHERE beer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                Review review = mapRowToReview(results);
                reviews.add(review);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return reviews;
    }
    @Override
    public List<Review> listReviews() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT user_id, brew_id, beer_id, review, rating, favorite, liked FROM public.reviews";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Review review = mapRowToReview(results);
                reviews.add(review);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return reviews;
    }





    private Review mapRowToReview(SqlRowSet rs) {
        Review review = new Review();
        review.setUser_id(rs.getInt("user_id"));
        review.setBrew_id(rs.getInt("brew_id"));
        review.setBeer_id(rs.getInt("beer_id"));
        review.setReview(rs.getString("review"));
        review.setRating(rs.getInt("rating"));
        review.setFavorite(rs.getBoolean("favorite"));
        review.setFavorite(rs.getBoolean("liked"));

        return review;
    }
}
