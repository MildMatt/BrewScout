package com.techelevator.dao;

import com.techelevator.model.Beer;

import java.util.List;

public interface BeerDao {
   List<Beer> listBeersByBreweryId( int brewId);
}