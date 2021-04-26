import React from "react";
import { withRouter } from "react-router-dom";
import {getSessionCookie} from "../Cookies/Session";

import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'

function loggedIn() {
    if (getSessionCookie().email !== "unauthorized")
        return getSessionCookie().email
    else
        return ""
}

function Navigation(props) {
  return (
    <Navbar bg="light" expand="lg">
      <Navbar.Brand href="/">Group Project</Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="mr-auto">
          <Nav.Link href= {"/profile/" + loggedIn()}>Profile</Nav.Link>
          <Nav.Link href="/group">Group</Nav.Link>
          <Nav.Link href="/search">Find Group</Nav.Link>
          <Nav.Link href="/searchCourse">Find Course</Nav.Link>
          <Nav.Link href="/createCourse">Create Course</Nav.Link>
        </Nav>
      </Navbar.Collapse>
    </Navbar>
  );
}

export default withRouter(Navigation);