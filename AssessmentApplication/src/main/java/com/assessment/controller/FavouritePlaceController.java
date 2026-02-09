package com.assessment.controller;

import com.assessment.dto.FavouritePlaceDTO;
import com.assessment.entity.FavouritePlace;
import com.assessment.repository.FavouritePlaceRepository;
import com.assessment.service.FavouritePlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/favourites")
@CrossOrigin(origins = "http://localhost:3000")
public class FavouritePlaceController {

    @Autowired
    private FavouritePlaceService service;
    @Autowired
    private FavouritePlaceRepository repo;

    @PostMapping("/add/{userId}")
    public FavouritePlace add(@PathVariable Long userId,
                              @RequestBody FavouritePlaceDTO dto){

        repo.findByUserIdAndPlaceId(userId, dto.placeId)
                .ifPresent(f -> { throw new RuntimeException("Already favourited"); });

        FavouritePlace f = new FavouritePlace();
        f.setUserId(userId);
        f.setPlaceId(dto.placeId);
        f.setName(dto.name);
        f.setLat(dto.lat);
        f.setLng(dto.lng);

        return repo.save(f);
    }

    @GetMapping("/{userId}")
    public List<FavouritePlace> get(@PathVariable Long userId){
        return repo.findByUserId(userId);
    }

    @DeleteMapping("/{userId}/{placeId}")
    public ResponseEntity<?> removeFavourite(
            @PathVariable Long userId,
            @PathVariable String placeId) {

        try {
            Optional<FavouritePlace> existing = repo.findByUserIdAndPlaceId(userId, placeId);

            if (existing.isPresent()) {
                repo.delete(existing.get());
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Favourite removed successfully",
                        "placeId", placeId
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "NOT_FOUND",
                                "message", "Favourite not found"
                        ));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "INTERNAL_ERROR",
                            "message", "Failed to remove favourite: " + e.getMessage()
                    ));
        }
    }
}
