import React, { useState } from "react"; //import React, { useState, setState } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import "../containers/Login.css";

function Register(props) {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const universities = useState(["UiB", "HVL", "UiO", "NTNU"]) // TODO: import this from a list


    function validateForm() {
        return email.length > 0 && password.length > 0 && password === repeatPassword;
    }
    function sendRegisterRequest() {
        const xhr = new XMLHttpRequest()

        xhr.addEventListener('load', () => {
            props.history.push("../profile/" + email)
        })
        xhr.open('POST', 'http://localhost:8080/users')
        xhr.setRequestHeader('Content-Type', 'application/json');

        const jsonString = JSON.stringify( {
            "userName": email,
            "password": password,
            "repeatPassword" : repeatPassword

        })
        xhr.send(jsonString)
    }

    function handleSubmit(event) {
        event.preventDefault();
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
                <Form.Group>
                    <Form.Label>University</Form.Label>
                    <Form.Control as="select">
                        {universities[0].map((value, index) => {
                            return <option>{value}</option>
                        })}
                    </Form.Control>
                </Form.Group>
                <Form.Group size="lg" controlId="password">
                    <Form.Label>Password</Form.Label>
                    <Form.Control
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </Form.Group>
                <Form.Group size="lg" controlId="repeat-password">
                    <Form.Label>Repeat Password</Form.Label>
                    <Form.Control
                        type="password"
                        value={repeatPassword}
                        onChange={(e) => setRepeatPassword(e.target.value)}
                    />
                </Form.Group>
                <Button
                    block size="lg"
                    type="submit"
                    disabled={!validateForm()}
                    onClick = {e => {sendRegisterRequest()}} // send HTTP request here
                >
                    Register
                </Button>

            </Form>
        </div>
    );
}

export default Register