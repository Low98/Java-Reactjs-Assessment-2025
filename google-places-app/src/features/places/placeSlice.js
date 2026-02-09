import { createSlice } from "@reduxjs/toolkit";
import { saveFavourite, loadFavourites, removeFavourite } from "./placeThunks";

const placeSlice = createSlice({
    name: "places",
    initialState: {
        searches: [],
        favourites: []
    },
    reducers: {
        addSearch: (state, action) => {
            //state.searches.unshift(action.payload);
            //state.selected = action.payload;
            const place = action.payload;

            console.log("addSearch received:", place);

            const normalizedPlace = {
                ...place,

                place_id: place.place_id || place.placeId,

                geometry: place.geometry || {
                    location: {
                        lat: () => place.lat || place.location?.lat || 3.139,
                        lng: () => place.lng || place.location?.lng || 101.6869
                    }
                },

                lat: place.lat || (place.geometry?.location ?
                    (typeof place.geometry.location.lat === 'function'
                        ? place.geometry.location.lat()
                        : place.geometry.location.lat)
                    : undefined),
                lng: place.lng || (place.geometry?.location ?
                    (typeof place.geometry.location.lng === 'function'
                        ? place.geometry.location.lng()
                        : place.geometry.location.lng)
                    : undefined),
                timestamp: new Date().toISOString(),
                id: Date.now()
            };


            state.searches.unshift(normalizedPlace);

            state.selected = normalizedPlace;
        },
        addFavourite: (state, action) => {
            const exists = state.favourites.some(
                fav => fav.place_id === action.payload.place_id ||
                    fav.placeId === action.payload.place_id
            );
            if (!exists) {
                state.favourites.push(action.payload);
            }
        },
        toggleFavourite: (state, action) => {
            const place = action.payload;

            const exists = state.favourites.some(
                fav => fav.place_id === action.payload.place_id ||
                    fav.placeId === action.payload.place_id
            );
            if (exists) {
                state.favourites = state.favourites.filter(p => p.place_id !== place.place_id);
            } else {
                state.favourites.push(place);
            }
        },
        setSelectedPlace: (state, action) => {
            state.selectedPlace = action.payload;
        },
        //removeFavouriteLocal: (state, action) => {
        //    const placeId = action.payload;
        //    state.favourites = state.favourites.filter(
        //        fav => fav.place_id !== placeId && fav.placeId !== placeId
        //    );
        //},
        //clearError: (state) => {
        //    state.error = null;
        //    state.lastActionStatus = "idle";
        //}
    },

    extraReducers: builder => {
        builder
            .addCase(saveFavourite.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.lastAction = "save_favourite";
                state.lastActionStatus = "loading";
            })
            .addCase(saveFavourite.fulfilled, (state, action) => {
                state.loading = false;
                state.lastActionStatus = "success";

                // 关键：只有在 action.payload 是有效数据时才添加
                if (action.payload && action.payload.placeId) {
                    // 再次检查是否已存在（防止重复）
                    const exists = state.favourites.some(
                        fav => fav.place_id === action.payload.placeId ||
                            fav.placeId === action.payload.placeId
                    );
                    if (!exists) {
                        state.favourites.push(action.payload);
                    }
                }

            })
            .addCase(saveFavourite.rejected, (state, action) => {
                alert(action.payload);
            })
            // loadFavourites
            .addCase(loadFavourites.pending, (state) => {
                state.loading = true;
                state.error = null;
                state.lastAction = "load_favourites";
                state.lastActionStatus = "loading";
            })
            //.addCase(loadFavourites.fulfilled, (state, action) => {
            //    state.favourites = action.payload;
            //});
            .addCase(loadFavourites.fulfilled, (state, action) => {
                state.loading = false;
                state.lastActionStatus = "success";

                state.favourites = action.payload.map(fav => ({
                    ...fav,
                    place_id: fav.placeId,
                    fromBackend: true
                }));
                //state.favourites = action.payload;
            })
            .addCase(loadFavourites.rejected, (state, action) => {
                state.loading = false;
                state.lastActionStatus = "error";
                state.error = {
                    type: "load_error",
                    message: action.payload?.message || "Failed to load favourites"
                };
            })
            .addCase(removeFavourite.pending, (state) => {
                state.loading = true;
                state.removalInProgress = true;
                state.error = null;
                state.lastAction = "remove_favourite";
                state.lastActionStatus = "loading";
            })
            .addCase(removeFavourite.fulfilled, (state, action) => {
                state.loading = false;
                state.removalInProgress = false;
                state.lastActionStatus = "success";

                // 从状态中删除对应的收藏
                const placeId = action.payload.placeId;
                const previousCount = state.favourites.length;

                state.favourites = state.favourites.filter(
                    fav => fav.place_id !== placeId && fav.placeId !== placeId
                );

                console.log(`Removed favourite. Before: ${previousCount}, After: ${state.favourites.length}`);
            })
            .addCase(removeFavourite.rejected, (state, action) => {
                state.loading = false;
                state.removalInProgress = false;
                state.lastActionStatus = "error";

                // 设置错误信息
                if (action.payload) {
                    state.error = {
                        type: action.payload.type || "remove_error",
                        message: action.payload.message || "Failed to remove favourite",
                        details: action.payload
                    };
                } else {
                    state.error = {
                        type: "unknown_error",
                        message: action.error?.message || "Unknown error occurred while removing"
                    };
                }
            });
    }
});


export const { addSearch, addFavourite, setSelectedPlace, toggleFavourite, removeFavouriteLocal, clearError } = placeSlice.actions;
export default placeSlice.reducer;