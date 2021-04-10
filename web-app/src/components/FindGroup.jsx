import React from "react";
import {getSessionCookie} from "../Cookies/Session";
import {FormControl, Form, Table, Modal, Alert} from "react-bootstrap";
import Button from "react-bootstrap/Button";


class FindGroup extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            groups: "placeholder",
            searchInput: "",
        }
    }

    componentDidMount() {
        this.getAllGroups();
    }

    getAllGroups = () => {
        const xhr = new XMLHttpRequest();

        xhr.addEventListener('load', () => {
            const data = xhr.responseText;
            const jsonResponse = JSON.parse(data)
            console.log("woow ", xhr.response)

            console.log(jsonResponse)
            this.setState({
                groups: jsonResponse
            });

        })
        const URL = 'http://localhost:8080/groups';

        xhr.open('GET', URL);
        xhr.send(URL);
    }

    updateSearchFilter = (event) => {
        this.setState({
            searchInput: event.target.value
        });
    }
    handleJoinGroup = (groupID) => {
        const xhr = new XMLHttpRequest()
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

        xhr.addEventListener('load', () => {
            const data = xhr.responseText;
            const jsonResponse = JSON.parse(data)
            console.log("json response: ",data  )
            alert("Successfully sent the request")
        })

        const username =  getSessionCookie().email;
        console.log("my username: ", username)
        const groupUpdateURL = 'http://localhost:8080/groups/' + groupID + "/members";

        xhr.open('PUT', groupUpdateURL)
        xhr.setRequestHeader('Content-Type', 'application/json');

        const jsonString = JSON.stringify( {
            "userName": username,
        })

        xhr.send(jsonString)


    }

    renderGroups = () => {
        console.log(this.state.searchInput)
        if (this.state.groups !== "placeholder") {
            return (
                <Table  responsive="xl">
                    <thead>
                    <tr>
                        <th > </th>
                        <th>ID</th>
                        <th>Group name</th>
                        <th >Description</th>
                        <th>Participants</th>
                    </tr>
                    </thead>

                    <tbody>

                    {
                        Object.entries(this.state.groups).map(([key, group]) => {
                            if (group.groupName.includes(this.state.searchInput )) {
                                return(
                                    <tr>
                                        <Button variant="secondary" onClick={ e=> {this.handleJoinGroup(group.id);}}>
                                            Join
                                        </Button>
                                        <td> <b> {group.id} </b></td>
                                        <td>{group.groupName}</td>
                                        <td>{group.description}</td>
                                        <td>{group.members}</td>

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
                            <h1 className="font-weight-light">Find Groups</h1>
                            <div>
                                <Form.Group>
                                    <Form.Control
                                        size="sm"
                                        type="text"
                                        placeholder="Group name"
                                        defaultValue=""
                                        onChange = {this.updateSearchFilter.bind(this) }
                                    />
                                </Form.Group>
                            </div>
                            {this.renderGroups()}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
export default FindGroup