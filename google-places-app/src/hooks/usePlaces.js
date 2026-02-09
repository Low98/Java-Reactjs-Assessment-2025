import { useSelector } from "react-redux";

export default function usePlaces() {
  return {
    searches: useSelector(state => state.places.searches),
    favourites: useSelector(state => state.places.favourites)
  };
}
