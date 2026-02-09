package com.assessment.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="favourite_place",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","placeId"}))
public class FavouritePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String placeId;
    private String name;
    private double lat;
    private double lng;

    public FavouritePlace() {}

    public FavouritePlace(Long userId, String placeId, String name, Double lat, Double lng) {
        this.userId = userId;
        this.placeId = placeId;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }
    // Getters and setters
    public void setUserId(Long userId) { this.userId = userId; }

    public String getPlaceId() { return placeId; }
    public void setPlaceId(String placeId) { this.placeId = placeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }

}

