import React from "react";
import Button from "react-bootstrap/Button";
import {Modal, Table} from "react-bootstrap";
import Form from "react-bootstrap/Form";
import {getSessionCookie} from "../Cookies/Session";


class MyGroup extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showCreateGroup: false,
      showJoinGroup: false,
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

  setGroupToJoinName = (name) => {
    this.setState({
      groupToJoinName: name
    });
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

    const jsonString = JSON.stringify( {
      "groupName": this.state.groupToCreateName,
      "description": this.state.groupToCreateDescription,
      "creatorName": getSessionCookie().email,

    })
    xhr.send(jsonString)
  }

  sendCreateJoinRequest = () => {
    const xhr = new XMLHttpRequest()

    xhr.open('POST', 'http://localhost:8080/groups')
    xhr.setRequestHeader('Content-Type', 'application/json');

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

  createGroupJoinForm = () =>{
    return(
        <Form >
          <Form.Group controlId="formBasicEmail">
            <Form.Label>Group Name</Form.Label>
            <Form.Control
                onChange={(e) => {this.setGroupToJoinName(e.target.value)}}
            />
          </Form.Group>
        </Form>
    );

  }


  createGroupCreatePopup = ()=> {
    return (
        <>
          <Modal
              show={this.state.showCreateGroup}
              onHide={ e => {this.handleCreateGroupClose()}}
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
              <Button variant="secondary" onClick={ e=> {this.handleCreateGroupClose();}}>
                Close
              </Button>
              <Button variant="primary" onClick={ e=> {this.sendCreateGroupRequest();  this.getGroups();}}>Create</Button>
            </Modal.Footer>
          </Modal>
        </>
    );
  }

  createGroupJoinPopup = () => {
    return (
        <>
          <Modal
              show={this.state.showJoinGroup}
              onHide={ e => {this.handleJoinGroupClose()}}
              backdrop="static"
              keyboard={false}
          >
            <Modal.Header closeButton>
              <Modal.Title>Join group</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {this.createGroupJoinForm()}
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={ e=> {this.handleCreateGroupClose();}}>
                Close
              </Button>
              <Button variant="primary" onClick={ e=> {this.sendCreateJoinRequest();  this.getGroups();}}>Join group</Button>
            </Modal.Footer>
          </Modal>
        </>
    );
  }


  // Create group modal
  handleCreateGroupClose = () => {
    this.setState({
      showCreateGroup: false
    });
  }

  handleCreateGroupShow = () => {
    this.setState({
      showCreateGroup: true
    });
  }

  handleJoinGroupClose = () => {
    this.setState({
      showJoinGroup: false
    });
  }

  handleJoinGroupShow = () => {
    this.setState({
      showJoinGroup: true
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
                      onClick = {e => {this.handleCreateGroupShow()}} // send HTTP request here
                  >
                    + New Group
                  </Button>{' '}
                  {this.createGroupCreatePopup()}

                  <Button
                      variant="primary"
                      size="sm"
                      onClick = {e => {this.handleJoinGroupShow()}} // send HTTP request here
                  >
                    Join Group
                  </Button>{' '}
                  {this.createGroupJoinPopup()}
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

export default MyGroup;