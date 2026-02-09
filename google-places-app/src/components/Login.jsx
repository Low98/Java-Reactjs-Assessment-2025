import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { setUser } from "../features/auth/authSlice";
import { loadFavourites } from "../features/places/placeThunks";
export default function Login({ onLogin }) {
    const [username, setU] = useState("");
    const [password, setP] = useState("");
    const dispatch = useDispatch();

    const login = async () => {
        const res = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        if (!res.ok) {
            alert("Invalid username or password");
            return;
        }

        const data = await res.json();
        dispatch(setUser(data));
        dispatch(loadFavourites(data.id));

    };

    const signup = async () => {
        const res = await fetch("http://localhost:8080/api/auth/create", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        if (!res.ok) {
            alert("User Exist. Kindly Login");
            return;
        }

        const data = await res.json();
        dispatch(setUser(data));
        dispatch(loadFavourites(data.id));

    };

    return (
        <div className="flex justify-center items-center h-screen bg-gray-100">
            <div className="bg-white p-6 rounded shadow w-80 space-y-3">
                <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
                    Welcome to Places Explorer
                </h2>
                <p className="mt-2 text-center text-sm text-gray-600">
                    Discover and save your favorite places around the world
                </p>
                <h2 className="text-lg font-bold text-center">Username: 
                    <input
                        className="border p-2 w-full items-center rounded"
                        placeholder="Username"
                        onChange={e => setU(e.target.value)}
                    />
                </h2>
            {/*</div>*/}
            {/*<div className="bg-white p-6 rounded shadow w-80 space-y-3">*/}
                <h2 className="text-lg font-bold text-center">Password: 
                    <input
                    type="password"
                    className="border p-2 w-full items-center rounded"
                    placeholder="Password"
                    onChange={e => setP(e.target.value)}
                />
                </h2>
            {/*</div>*/}
            {/*<div className="bg-white p-6 rounded shadow w-80 space-y-3">*/}
                <h2 className="text-lg font-bold text-center">
                    <button
                        onClick={login}
                        className="w-full bg-blue-600 text-white p-2 items-center rounded hover:bg-blue-700"
                    >
                        Login
                    </button>
                </h2>
            {/*</div>*/}
            {/*<div className="bg-white p-6 rounded shadow w-80 space-y-3">*/}
                <h2 className="text-lg font-bold text-center"><button
                    onClick={signup}
                    className="w-full bg-blue-600 text-white p-2 items-center rounded hover:bg-blue-700"
                >
                    Signup
                </button>
                </h2>
            </div>
        </div>
    );
}
