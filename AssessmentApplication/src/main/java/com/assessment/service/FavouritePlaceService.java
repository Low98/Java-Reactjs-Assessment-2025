package com.assessment.service;

import com.assessment.entity.FavouritePlace;
import com.assessment.repository.FavouritePlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavouritePlaceService {

    @Autowired
    private FavouritePlaceRepository repo;

    @Transactional
    public FavouritePlace save(FavouritePlace place) {
        if (repo.existsByPlaceId(place.getPlaceId())) {
            throw new RuntimeException("Place already marked as favourite");
        }
        return repo.save(place);
    }
}
