import * as Cookies from "js-cookie";
import React from "react";

//React context, in which the main application is wrapped in.
//Used to store current user's username and access it with different components.

export const setSessionCookie = (session) => {
    Cookies.remove("session");
    Cookies.set("session", session, { expires: 14 });
};

export const initializeCookie = () => {
    Cookies.set("session", {email: "unauthorized"})
}

export const updateSessionCookie = (key, value) => {
    const sessionCookie = JSON.parse(Cookies.get("session"));
    sessionCookie[key] = value
    Cookies.remove("session");
    Cookies.set("session", sessionCookie, { expires: 14 });

};


export const deleteSessionCookie = () => {
    Cookies.remove("session")
}

export const getSessionCookie = () => {
    const sessionCookie = Cookies.get("session");

    if (sessionCookie === undefined || sessionCookie.email === "unauthorized") {
        return "unauthorized";
    } else {
        return JSON.parse(sessionCookie);
    }
};

export const SessionContext = React.createContext(getSessionCookie());