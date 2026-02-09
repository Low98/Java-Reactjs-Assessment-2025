import { useSelector, useDispatch } from "react-redux";
import { toggleFavourite } from "../features/places/placeSlice";
import { saveFavourite } from "../features/places/placeThunks";

export default function History() {
    const history = useSelector(state => state.places.searches);
    const favourites = useSelector(state => state.places.favourites);
    const user = useSelector(state => state.auth.user);
    const dispatch = useDispatch();

    if (!user) return null;

    return (
        <div className="mt-4">
            <h5 className="font-bold">Search History</h5>

            {history.map(p => {
                const isFav = favourites.some(f => f.place_id === p.place_id);

                return (
                    <div key={p.place_id} className="flex justify-between p-2 border-b">
                        <span>{p.name}</span>

                        <div className="flex gap-2">
                            {/*<span*/}
                            {/*    className={`cursor-pointer text-xl ${isFav ? "text-yellow-400" : "text-gray-400"}`}*/}
                            {/*    onClick={() => dispatch(toggleFavourite(p))}*/}
                            {/*>*/}
                            {/*    ★*/}
                            {/*</span>*/}

                            {/*<button*/}
                            {/*    className="bg-blue-500 text-white px-2 rounded"*/}
                            {/*    onClick={() => dispatch(saveFavourite({ userId: user.id, place: p }))}*/}
                            {/*>*/}
                            {/*    Save*/}
                            {/*</button>*/}
                            <span
                                className={`cursor-pointer ${isFav ? "text-yellow-400" : "text-gray-400"}`}
                                onClick={() => {
                                    dispatch(toggleFavourite(p));
                                    if (!isFav) {
                                        dispatch(saveFavourite({ userId: user.id, place: p }));
                                    }
                                }}
                            >
                                ★
                            </span>
                        </div>
                    </div>
                );
            })}
        </div>
    );
}
