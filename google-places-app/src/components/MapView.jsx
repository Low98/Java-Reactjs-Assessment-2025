import { GoogleMap, Marker } from "@react-google-maps/api";
import { useSelector } from "react-redux";
import SearchBox from "./SearchBox";
import { useMemo } from "react";

export default function MapView() {
    const selected = useSelector(state => state.places.selected);

    const center = useMemo(() => {
        if (!selected) return { lat: 3.139, lng: 101.6869 };

        // From Google Places
        if (selected.geometry?.location) {
            return {
                lat: selected.geometry.location.lat(),
                lng: selected.geometry.location.lng()
            };
        }

        // From SQL Server favourite
        if (selected.lat && selected.lng) {
            return {
                lat: selected.lat,
                lng: selected.lng
            };
        }

        return { lat: 3.139, lng: 101.6869 };
    }, [selected]);

    return (
        <div style={{ width: "100%", height: "100vh", position: "relative" }}>
            <GoogleMap
                center={center}
                zoom={13}
                mapContainerStyle={{ width: "100%", height: "100%" }}
            >
                {/* Floating search box */}
                <div
                    style={{
                        position: "absolute",
                        top: 20,
                        left: "50%",
                        transform: "translateX(-50%)",
                        zIndex: 1000,
                        width: "600px"
                    }}
                >
                    <SearchBox />
                </div>

                {selected && <Marker position={center} />}
            </GoogleMap>
        </div>
    );
}

//import { GoogleMap, Marker } from "@react-google-maps/api";
//import { useSelector } from "react-redux";
//import SearchBox from "./SearchBox";

//export default function MapView() {
//    const selected = useSelector(state => state.places.selected);

//    const center = selected
//        ? {
//            lat: selected.geometry.location.lat(),
//            lng: selected.geometry.location.lng()
//        }
//        : { lat: 3.139, lng: 101.6869 };

//    return (
//        <div style={{ width: "100%", height: "100vh", position: "relative" }}>
//            <GoogleMap
//                center={center}
//                zoom={13}
//                mapContainerStyle={{ width: "100%", height: "100%" }}
//            >
//                <div
//                    style={{
//                        position: "absolute",
//                        top: 20,
//                        left: "50%",
//                        transform: "translateX(-50%)",
//                        zIndex: 1000,
//                        width: "400px"
//                    }}
//                >
//                    <SearchBox />
//                </div>

//                {selected && (
//                    <Marker
//                        position={{
//                            lat: selected.geometry.location.lat(),
//                            lng: selected.geometry.location.lng()
//                        }}
//                    />
//                )}
//            </GoogleMap>
//        </div>
//    );
//}


//import { GoogleMap, Marker } from "@react-google-maps/api";
//import { useSelector } from "react-redux";
//import SearchBox from "./SearchBox"; // 添加这行导入

//export default function MapView() {
//  const places = useSelector(state => state.places.searches);
//  const last = places[places.length - 1];
//  const [selectedPlace, setSelectedPlace] = useState(null);

//  if (!last?.geometry) return null;

//  const loc = last.geometry.location;

//    return (

//    <GoogleMap
//      zoom={14}
//      center={{ lat: loc.lat(), lng: loc.lng() }}
//      mapContainerStyle={{ width: "100%", height: "400px" }}
//    >
//      <Marker position={{ lat: loc.lat(), lng: loc.lng() }} />
//    </GoogleMap>

//<div className="relative w-full h-[600px]">
//    <GoogleMap
//        mapContainerStyle={{ width: "100%", height: "100%" }}
//        zoom={14}
//        center={{ lat: loc.lat(), lng: loc.lng() }}
//    >
//        {/* SEARCH BOX */}
//        <div className="absolute top-4 left-1/2 transform -translate-x-1/2 z-10 w-[400px]">
//            <SearchBox />
//        </div>

//        {selectedPlace && <Marker position={selectedPlace.location} />}
//    </GoogleMap>
//</div >
//  );
//}

//import { GoogleMap, Marker } from "@react-google-maps/api";
//import { useSelector } from "react-redux";
//import SearchBox from "./SearchBox"; // 添加这行导入
//import { useState } from "react"; // 如果需要 selectedPlace 状态

//export default function MapView() {
//    const places = useSelector(state => state.places.searches);
//    const last = places[places.length - 1];

//    // 如果需要有状态控制选中的地点
//    const [selectedPlace, setSelectedPlace] = useState(null);

//    // 如果没有历史记录，使用默认中心
//    const defaultCenter = { lat: 40.7128, lng: -74.0060 }; // 纽约市坐标

//    if (!last?.geometry?.location) {
//        return (
//            <div className="relative w-full h-[600px]">
//                <GoogleMap
//                    mapContainerStyle={{ width: "100%", height: "100%" }}
//                    zoom={10}
//                    center={defaultCenter}
//                >
//                    {/* SEARCH BOX */}
//                    <div className="absolute top-4 left-1/2 transform -translate-x-1/2 z-10 w-[400px]">
//                        <SearchBox onPlaceSelected={setSelectedPlace} />
//                    </div>

//                    {selectedPlace && <Marker position={selectedPlace.geometry.location} />}
//                </GoogleMap>
//            </div>
//        );
//    }

//    const loc = last.geometry.location;
//    const center = { lat: loc.lat(), lng: loc.lng() };

//    return (
//        <div className="relative w-full h-[600px]">
//            <GoogleMap
//                mapContainerStyle={{ width: "100%", height: "100%" }}
//                zoom={13}
//                center={center}
//            >
//                {/* SEARCH BOX */}
//                <div className="absolute top-4 left-1/2 transform -translate-x-1/2 z-10 w-[400px]">
//                    <SearchBox onPlaceSelected={setSelectedPlace} />
//                </div>

//                {/* 显示最后一个搜索地点的标记 */}
//                <Marker
//                    position={{ lat: loc.lat(), lng: loc.lng() }}
//                    label="Last Search"
//                />

//                {/* 显示选中的地点的标记 */}
//                {selectedPlace && (
//                    <Marker
//                        position={selectedPlace.geometry.location}
//                        label="Selected"
//                        icon={{
//                            url: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"
//                        }}
//                    />
//                )}
//            </GoogleMap>
//        </div>
//    );
//}