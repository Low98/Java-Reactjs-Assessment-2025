import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

export const saveFavourite = createAsyncThunk(
    "places/saveFavourite",
    async ({ userId, place }, { rejectWithValue }) => {
        try {

            const payload = {
                placeId: place.place_id,
                name: place.name,
                lat: place.geometry.location.lat(),
                lng: place.geometry.location.lng()
            };

            const res = await axios.post(
                `http://localhost:8080/api/favourites/add/${userId}`,
                payload
            );

            return res.data;   // send saved favourite back to Redux
        } catch (error) {
            if (error.response) {
				// rejected and display error message from server
                return rejectWithValue(error.response.data.message || error.response.data.error);
            } else if (error.request) {
				// request made but no response received
                return rejectWithValue("Network error. Please check your connection.");
            } else {
				// other errors
                return rejectWithValue(error.message);
            }
        }
    }
);

export const loadFavourites = createAsyncThunk(
    "places/loadFavourites",
    async userId => {
        const res = await axios.get(`http://localhost:8080/api/favourites/${userId}`);
        return res.data;
    }
);

export const removeFavourite = createAsyncThunk(
    "places/removeFavourite",
    async ({ userId, place }, { rejectWithValue }) => {
        try {
            const response = await axios.delete(
                `http://localhost:8080/api/favourites/${userId}/${place.place_id}`
            );

            return response.data;

        } catch (error) {
            if (error.response) {
                const errorData = error.response.data;
                return rejectWithValue({
                    type: errorData?.error || "server_error",
                    message: errorData?.message || "Failed to remove favourite",
                    status: error.response.status,
                    data: errorData
                });
            } else if (error.request) {
                return rejectWithValue({
                    type: "network_error",
                    message: "Network error. Please check your connection."
                });
            } else {
                return rejectWithValue({
                    type: "unknown_error",
                    message: error.message || "Unknown error occurred"
                });
            }
        }
    }
);