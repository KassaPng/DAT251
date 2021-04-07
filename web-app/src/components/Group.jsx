import React from "react";
import Button from "react-bootstrap/Button";
import {Modal, Table} from "react-bootstrap";
import Form from "react-bootstrap/Form";
import {getSessionCookie} from "../Cookies/Session";


class Group extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      show: false,
      groups: ["placeholder"]
    }
  }
  componentDidMount() {
    this.getGroups();
  }

  getGroups = () => {
    const xhr = new XMLHttpRequest();

    xhr.addEventListener('load', () => {
      const data = xhr.responseText;
      const jsonResponse = JSON.parse(data)

      this.setState({
        groups: jsonResponse["groups"]
      });

    })
    const URL = 'http://localhost:8080/users/' + getSessionCookie().email;

    xhr.open('GET', URL);
    xhr.send(URL);
  }

  setGroupToCreateName = (name) => {
    this.setState({
      groupToCreateName: name
    });
  }

  setGroupToCreateDescription = (name) => {
    this.setState({
      groupToCreateDescription : name
    });
  }


  sendCreateGroupRequest = () => {
    const xhr = new XMLHttpRequest()

    xhr.open('POST', 'http://localhost:8080/groups')
    xhr.setRequestHeader('Content-Type', 'application/json');
    console.log(" getSessionCookie().email",  getSessionCookie().email);
    console.log(" getSessionCookie()",  getSessionCookie())

    const jsonString = JSON.stringify( {
      "groupName": this.state.groupToCreateName,
      "description": this.state.groupToCreateDescription,
      "creatorName": getSessionCookie().email,

    })
    xhr.send(jsonString)
  }

  createGroupCreateForm = () =>{
    return(
        <Form >
          <Form.Group controlId="formBasicEmail">
            <Form.Label>Group Name</Form.Label>
            <Form.Control
                onChange={(e) => {this.setGroupToCreateName(e.target.value)}}
            />
          </Form.Group>
          <Form.Group controlId="exampleForm.ControlTextarea1">
            <Form.Label>Description</Form.Label>
            <Form.Control
                as="textarea" rows={3}
                onChange={(e) => {this.setGroupToCreateDescription(e.target.value)}}
            />
          </Form.Group>
        </Form>
    );

  }

  createGroupPopup = ()=> {
    return (
        <>
          <Modal
              show={this.state.show}
              onHide={ e => {this.handleClose()}}
              backdrop="static"
              keyboard={false}
          >
            <Modal.Header closeButton>
              <Modal.Title>Create group</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {this.createGroupCreateForm()}
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={ e=> {this.handleClose();}}>
                Close
              </Button>
              <Button variant="primary" onClick={ e=> {this.sendCreateGroupRequest();  this.getGroups();}}>Create</Button>
            </Modal.Footer>
          </Modal>
        </>
    );
  }


  // Create group modal
  handleClose = () => {
    this.setState({
      show: false
    });
  }

  handleShow = () => {
    this.setState({
      show: true
    });
  }




  renderGroups = () => {
    return (
        <Table group table size="sm">
          <thead>
          <tr>
            <th>Group name</th>
            <th>Description</th>
            <th>Number of participants</th>
          </tr>
          </thead>

          <tbody>

          {
            this.state.groups.map((value,index) =>
            <tr>
              <td>{value}</td>
              <td>{value.description}</td>
              <td>1</td>
            </tr>
            )
          }
          </tbody>
        </Table>
        );

  }

  render() {
    return (

        <div className="home">
          <div class="container">
            <div class="row align-items-center my-5">
              <div class="col-lg-5">
                <h1 class="font-weight-light">My Groups</h1>
                <br/>
                <div>
                  <Button
                      variant="primary"
                      size="sm"
                      onClick = {e => {this.handleShow()}} // send HTTP request here
                  >
                    + New Group
                  </Button>{' '}
                  {this.createGroupPopup()}
                </div>
                <br/>
                {this.renderGroups()}
              </div>
            </div>
          </div>
        </div>
    );
  }

}

export default Group;