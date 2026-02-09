import { LoadScript } from "@react-google-maps/api";
import MapView from "./components/MapView";
import History from "./components/History";
import Login from "./components/Login";
import { useSelector } from "react-redux";

export default function App() {
    const user = useSelector(state => state.auth.user);

    return (
        <LoadScript
            googleMapsApiKey="AIzaSyAECW7de2G2SQGXi1FWTMtpdm3rWKJZb0A"
            libraries={["places"]}
        >
            {user ? (
                <div style={{ width: "100vw", height: "100vh" }}>
                    <MapView />
                    {/*<History />*/}
                </div>
            ) : (
                <Login />
            )}
        </LoadScript>
    );
}