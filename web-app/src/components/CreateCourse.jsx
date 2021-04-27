import React from "react";
import {getSessionCookie} from "../Cookies/Session";
import {FormControl, Form, Table, Modal, Alert, InputGroup, Spinner} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {ToastContainer, toast} from 'react-toastify';
import "react-toastify/dist/ReactToastify.css";

class CreateCourse extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            courses: ["Course"],
            courseSelection: ["Courses"],
            selectedCourseName: "Course",
            members: {},
            memberToGroupStatusMap: {},
            isDataFetched: false,
        }
    }

    componentDidMount() {
        this.getUserCourses();
    }

    updateSelections = () => {
        for (const course of this.state.courses)
            if (!this.state.courseSelection.includes((course.name)))
                this.setState(prevState => ({
                    courseSelection: [...prevState.courseSelection, course.name]
                }))
    }

    updateMembers = () => {
        for (const course of this.state.courses)
            if (!this.state.courseSelection.includes((course.name)))
                this.setState(prevState => ({
                    courseSelection: [...prevState.courseSelection, course.name]
                }))
    }

    setSelectedCourse = (courseName) => {
        this.setState({
            selectedCourseName: courseName
        }, () => {
            this.getCourseMembers()
            this.renderMembers()
            this.updateMemberGroupStatus()
        });
    }

    updateMemberGroupStatus = () => {
        Object.entries(this.state.members).map(([key, member]) => {
            this.getCourseParticipantGroupStatus(member)
        })
    }

    setCourseToCreateName = (name) => {
        this.setState({
            courseToCreateName: name
        });
    }

    setCourseToCreateInstitutionName = (name) => {
        this.setState({
            courseToCreateInstitutionName: name
        });
    }

    setCourseToCreateDescription = (name) => {
        this.setState({
            courseToCreateDescription: name
        });
    }

    // Create group modal
    handleCreateCourseClose = () => {
        this.setState({
            showCreateCourse: false
        });
    }

    handleCreateCourseShow = () => {
        this.setState({
            showCreateCourse: true
        });
    }

    getUserCourses = () => {
        const xhr = new XMLHttpRequest();

        xhr.addEventListener('load', () => {
            const data = xhr.responseText;
            const jsonResponse = JSON.parse(data)
            this.setState({
                courses: jsonResponse
            }, () => {
                this.updateSelections()
            });

        })
        const URL = 'http://localhost:8080/users/' + getSessionCookie().email + '/courses'

        xhr.open('GET', URL);
        xhr.send(URL);
    }

    getCourseMembers = () => {
        const xhr = new XMLHttpRequest();

        xhr.addEventListener('load', () => {
            const data = xhr.responseText;
            const jsonResponse = JSON.parse(data)
            this.setState({
                members: jsonResponse.relatedUsers
            }, () => {
                this.updateSelections()
            });

        })
        const URL = 'http://localhost:8080/courses/' + this.state.selectedCourseName

        xhr.open('GET', URL);
        xhr.send(URL);
    }

    sendCreateCourseRequest = () => {
        //toast.success("Successfully sent the course creation request")
        const xhr = new XMLHttpRequest()

        xhr.open('POST', 'http://localhost:8080/courses')
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.addEventListener('load', () => {
            const data = xhr.responseText;
            if (data.toLowerCase().includes("success"))
                toast.success("Successfully created the course")
            else
                toast.error("Failed to create a course, try again")
        })

        const jsonString = JSON.stringify({
            "courseName": this.state.courseToCreateName,
            "institution": this.state.courseToCreateInstitutionName,
            "description": this.state.courseToCreateDescription,

        })
        xhr.send(jsonString)
        this.getUserCourses()
        this.handleCreateCourseClose()
    }

    createCourseCreatePopup = () => {
        return (
            <>
                <Modal
                    show={this.state.showCreateCourse}
                    onHide={e => {
                        this.handleCreateCourseClose()
                    }}
                    backdrop="static"
                    keyboard={false}
                >
                    <Modal.Header closeButton>
                        <Modal.Title>Create group</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {this.createCourseCreateForm()}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="primary" onClick={e => {
                            this.sendCreateCourseRequest()
                        }}>Create</Button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }

    createCourseCreateForm = () => {
        return (
            <Form>
                <Form.Group controlId="formBasicEmail">
                    <Form.Label>Course Name</Form.Label>
                    <Form.Control
                        onChange={(e) => {
                            this.setCourseToCreateName(e.target.value)
                        }}
                    />
                </Form.Group>
                <Form.Group controlId="formBasicEmail">
                    <Form.Label>Institution Name</Form.Label>
                    <Form.Control
                        onChange={(e) => {
                            this.setCourseToCreateInstitutionName(e.target.value)
                        }}
                    />
                </Form.Group>
                <Form.Group controlId="exampleForm.ControlTextarea1">
                    <Form.Label>Description</Form.Label>
                    <Form.Control
                        as="textarea" rows={3}
                        onChange={(e) => {
                            this.setCourseToCreateDescription(e.target.value)
                        }}
                    />
                </Form.Group>
            </Form>
        );
    }

    renderMembers = () => {
        if (!this.state.isDataFetched){
            return (
                <Spinner animation="border" role="status">
                    <span className="sr-only">Loading...</span>
                </Spinner>
            )
        }
        if (this.state.selectedCourseName !== "Courses") {
            return (
                <Table responsive="xl">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Has group in this course</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        Object.entries(this.state.members).map(([key, member]) => {
                            return (
                                <tr>
                                    <td>{member}</td>
                                    <td>{this.state.memberToGroupStatusMap[member]}</td>
                                </tr>
                            );
                        })
                    }
                    </tbody>
                </Table>
            );
        }
    }

    getCourseParticipantGroupStatus = (username) => {
        const xhr = new XMLHttpRequest();

        xhr.addEventListener('load', () => {
            const data = xhr.responseText;
            const map = this.state.memberToGroupStatusMap
            map[username] = data
            this.setState({
                memberToGroupStatusMap: map
            })

            if (Object.keys(this.state.members).length === Object.keys(this.state.memberToGroupStatusMap).length)
                this.setState({
                    isDataFetched : true
                }, () => {this.renderMembers()})

        })
        const URL = 'http://localhost:8080/users/' + username + "/courses/" + this.state.selectedCourseName

        xhr.open('GET', URL);
        xhr.send(URL);
    }


    render() {
        return (
            <div className="home">
                <div className="container">
                    <div class="row align-items-center my-5">
                        <div class="col-lg-5">
                            <h1 class="font-weight-light">Manage Courses</h1>
                            <br/>
                            <div>
                                <Button
                                    variant="primary"
                                    size="sm"
                                    onClick={e => {
                                        this.handleCreateCourseShow()
                                    }} // send HTTP request here
                                >
                                    + New Course
                                </Button>{' '}
                                {this.createCourseCreatePopup()}
                                <br/>
                                <InputGroup className="mb-3">
                                    <Form.Control
                                        as="select"
                                        onChange={(e) => {
                                            this.setSelectedCourse(e.target.value)
                                        }}
                                    >
                                        {this.state.courseSelection.map((value, index) => {
                                            return <option>{value}</option>
                                        })}
                                    </Form.Control>
                                </InputGroup>
                                <div>
                                    {this.renderMembers()}
                                </div>
                            </div>
                        </div>
                    </div>
                    <ToastContainer/>
                </div>
            </div>
        );
    }
}

export default CreateCourse
