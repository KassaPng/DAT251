import React, { useState } from "react";
import 'bootstrap/dist/css/bootstrap.css';
import { BrowserRouter as Router, Route, Switch} from "react-router-dom";

import Navigation from "./components/Navigation.jsx";
import Footer from "./components/Footer.jsx";
import Login from "./components/Login.jsx";
import Register from "./components/Register.jsx";
import Profile from "./components/Profile.jsx";
import Group from "./components/Group.jsx";
import { SessionContext, getSessionCookie } from "./Cookies/Session.js";

function App() {
  const [session] = useState(getSessionCookie());

  return (
      <SessionContext.Provider value={session}>
        <div className="App">
        <Router>
          <Navigation />
          <Switch>
            <Route path="/" exact component={() => <Login />} />
            <Route path="/register" exact component={() => <Register />} />
            <Route path="/profile/:email/" exact component={() => <Profile />} />
            <Route path="/group" exact component={() => <Group />} />
          </Switch>
          <Footer />
        </Router>
      </div>
    </SessionContext.Provider>

  );
}

export default App;