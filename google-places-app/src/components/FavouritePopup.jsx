import { useSelector, useDispatch } from "react-redux";
import { addSearch, toggleFavourite,  } from "../features/places/placeSlice";
import { saveFavourite, removeFavourite } from "../features/places/placeThunks";

export default function FavouritePopup({ onClose }) {
    const favourites = useSelector(state => state.places.favourites);
    const dispatch = useDispatch();
    const user = useSelector(state => state.auth.user);

    return (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
            <div className="bg-white w-96 rounded shadow p-4">

                <h2 className="text-lg font-bold mb-3">⭐ Favourite Places</h2>

                {favourites.length === 0 && (
                    <p className="text-gray-500">No favourites yet</p>
                )}

                {favourites.map(p => {
                    const isFav = favourites.some(
                        fav => fav.place_id === p.place_id ||
                            fav.placeId === p.place_id
                    );
                    
                    return (
                        //<div key={p.place_id} className="border-b p-2">
                        <div key={p.place_id}
                            className="p-2 hover:bg-gray-100 cursor-pointer"
                            onClick={() => dispatch(addSearch(p))}>
                            {p.name}
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

                <button
                    onClick={onClose}
                    className="mt-4 bg-blue-500 text-white px-4 py-2 rounded w-full"
                >
                    Close
                </button>

            </div>
        </div>
    );
}