import { configureStore } from "@reduxjs/toolkit";
import placesReducer from "../features/places/placeSlice";
import authReducer from "../features/auth/authSlice";

export const store = configureStore({
    reducer: {
        places: placesReducer,
        auth: authReducer
    }
});
