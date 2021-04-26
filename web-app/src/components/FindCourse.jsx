import React from "react";
import {getSessionCookie} from "../Cookies/Session";
import {FormControl, Form, Table, Modal, Alert} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import { ToastContainer, toast } from 'react-toastify';
import "react-toastify/dist/ReactToastify.css";

class FindCourse extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            courses: "",
            searchInput: "",
        }
    }

    componentDidMount() {
        this.getAllCourses();
    }

    getAllCourses = () => {
        const xhr = new XMLHttpRequest();

        xhr.addEventListener('load', () => {
            const data = xhr.responseText;
            const jsonResponse = JSON.parse(data)
            console.log("woow ", xhr.response)

            console.log(jsonResponse)
            this.setState({
                courses: jsonResponse
            });

        })
        const URL = 'http://localhost:8080/courses';

        xhr.open('GET', URL);
        xhr.send(URL);
    }

    updateSearchFilter = (event) => {
        this.setState({
            searchInput: event.target.value
        });
    }

    handleJoinCourse = (courseID) => {
        const xhr = new XMLHttpRequest()

        toast.success("Successfully sent the join request")

        xhr.addEventListener('load', () => {
            const data = xhr.responseText;
            console.log("json response: ",data)

            if (data["relatedUsers"].includes(username))
              toast.success("Successfully joined the course")
            else
              toast.error("Failed to join the course, try again")
        })

        const username =  getSessionCookie().email;
        const courseUpdateURL = 'http://localhost:8080/courses/' + courseID + "/users/" + username;
        console.log("courseUpdateURL", courseUpdateURL)

        xhr.open('PUT', courseUpdateURL)
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.send()
    }

    renderCourses = () => {
        console.log(this.state.searchInput)
        if (this.state.courses !== "placeholder") {
            return (
                <Table responsive="xl">
                    <thead>
                    <tr>
                        <th></th>
                        <th>Course Name</th>
                        <th>Institution</th>
                        <th>Description</th>
                    </tr>
                    </thead>

                    <tbody>
                    {
                        Object.entries(this.state.courses).map(([key, course]) => {
                            if (course.name.includes(this.state.searchInput )) {
                                return(
                                    <tr>
                                        <Button variant="secondary" onClick={ e=> {this.handleJoinCourse(course.id);}}>
                                            Join
                                        </Button>
                                        <td>{course.name}</td>
                                        <td>{course.institutionName}</td>
                                        <td>{course.description}</td>
                                    </tr>
                                );
                            }
                        })
                    }
                    </tbody>
                </Table>
            );
        }
    }

    render() {
        return (
            <div className="home">
                <div className="container">
                    <div className="row align-items-center my-5">
                        <div className="col-lg-5">
                            <h1 className="font-weight-light">Find Courses</h1>
                            <div>
                                <Form.Group>
                                    <Form.Control
                                        size="sm"
                                        type="text"
                                        placeholder="Course name"
                                        defaultValue=""
                                        onChange = {this.updateSearchFilter.bind(this) }
                                    />
                                </Form.Group>
                            </div>
                            {this.renderCourses()}
                        </div>
                    </div>
                    <ToastContainer />
                </div>
            </div>
        );
    }
}
export default FindCourse
