import React, { useState } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
//import Nav from 'react-bootstrap/Nav'
import "../containers/Login.css";

function Login(props) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [matching, setMatching] = useState("");

  function validateForm() {
    return email.length > 0 && password.length > 0;
  }

  function handleSubmit(event) {
    event.preventDefault();
  }


  function sendLoginRequest(){
    const xhr = new XMLHttpRequest();

    xhr.addEventListener('load', () => {
      const data = xhr.responseText;
      if (data !== "") {
        const jsonResponse = JSON.parse(data);
        const actualPassword = jsonResponse.password; // TODO: password matching
        props.history.push("../profile/" + email)
      }
      else {
        alert("Invalid information, try again");
      }

    })
    const URL = 'http://localhost:8080/users/' + email;

    xhr.open('GET', URL);
    xhr.send(URL);
  }


  return (
    <div className="Login">
      <Form onSubmit={handleSubmit}>
        <Form.Group size="lg" controlId="email">
          <Form.Label>Email</Form.Label>
          <Form.Control
            autoFocus
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </Form.Group>
        <Form.Group size="lg" controlId="password">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </Form.Group>
        <Button block size="lg"
                type="submit"
                disabled={!validateForm()}
                onClick = {e => {sendLoginRequest()}} // send HTTP request here
        >
          Login
        </Button>
        <Form.Text className="text-muted"  href="/home">
          New user? Register <a href="/register">here</a>

        </Form.Text>
      </Form>
    </div>
  );
}

export default Login