import React from "react";
import 'bootstrap/dist/css/bootstrap.css';
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

import Navigation from "./components/Navigation.jsx";
import Footer from "./components/Footer.jsx";
import Login from "./components/Login.jsx";
import Profile from "./components/Profile.jsx";
import Group from "./components/Group.jsx";

function App() {
  return (
    <div className="App">
      <Router>
        <Navigation />
        <Switch>
          <Route path="/login" exact component={() => <Login />} />
          <Route path="/profile" exact component={() => <Profile />} />
          <Route path="/group" exact component={() => <Group />} />
        </Switch>
        <Footer />
      </Router>
    </div>
  );
}

export default App;