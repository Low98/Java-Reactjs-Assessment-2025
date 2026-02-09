import { Autocomplete } from "@react-google-maps/api";
import { useDispatch, useSelector } from "react-redux";
import { saveFavourite, removeFavourite } from "../features/places/placeThunks";
import { addSearch, toggleFavourite } from "../features/places/placeSlice";
import FavouritePopup from "./FavouritePopup";
import { useState } from "react";

let autoComplete;

export default function SearchBox() {
    const dispatch = useDispatch();
    const history = useSelector(state => state.places.searches);
    const favourites = useSelector(state => state.places.favourites);
    const user = useSelector(state => state.auth.user);

    const [showFav, setShowFav] = useState(false);
    const [showHistory, setShowHistory] = useState(false);

    const onLoad = ac => {
        autoComplete = ac;
    };

    const onPlaceChanged = () => {
        const place = autoComplete.getPlace();
        if (place.geometry) {
            dispatch(addSearch(place));
        }
    };

    return (
        <div className="relative w-full">

            {/* Search row */}
            <div className="flex gap-2">
                <Autocomplete onLoad={onLoad} onPlaceChanged={onPlaceChanged}>
                    <input
                        className="w-full p-3 border rounded shadow focus:ring-2 focus:ring-blue-500"
                        placeholder="Search place..."
                        onFocus={() => setShowHistory(true)}
                        onBlur={() => setTimeout(() => setShowHistory(false), 200)}
                    />
                </Autocomplete>

                <button
                    onClick={() => setShowFav(true)}
                    className="bg-yellow-400 px-3 py-2 rounded"
                >
                    ⭐
                </button>
            </div>

            {/* Favourite popup */}
            {showFav && <FavouritePopup onClose={() => setShowFav(false)} />}

            {/* History dropdown */}
            {showHistory && history.length > 0 && (
                <div className="absolute top-full left-0 w-full bg-white shadow rounded z-20 max-h-60 overflow-auto">
                    {history.map(p => {
                        const isFav = favourites.some(f => f.place_id === p.place_id);
                        
                        return (
                            <div key={p.place_id} className="flex justify-between p-2 hover:bg-gray-100">
                                {/*<span>{p.name}</span>*/}
                                <span
                                    className="cursor-pointer hover:text-blue-600"
                                    onClick={() => dispatch(addSearch(p))}
                                >
                                    {p.name}
                                </span>
                                <span
                                    className={`cursor-pointer ${isFav ? "text-yellow-400" : "text-gray-400"}`}
                                    onClick={() => {
                                        dispatch(toggleFavourite(p));
                                        if (!isFav) {
                                            dispatch(saveFavourite({ userId: user.id, place: p }));
                                        } else {
                                            dispatch(removeFavourite({ userId: user.id, place: p }));
                                        }
                                    }}
                                >
                                    ★
                                </span>
                            </div>
                        );
                    })}
                </div>
            )}
        </div>
    );
}
