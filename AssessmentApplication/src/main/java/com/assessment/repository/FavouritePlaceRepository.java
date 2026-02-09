package com.assessment.repository;

import com.assessment.entity.FavouritePlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavouritePlaceRepository extends JpaRepository<FavouritePlace, Long> {
    List<FavouritePlace> findByUserId(Long userId);
    Optional<FavouritePlace> findByUserIdAndPlaceId(Long userId, String placeId);
    boolean existsByPlaceId(String placeId);
}

