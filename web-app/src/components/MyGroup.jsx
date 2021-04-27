import React from "react";
import Button from "react-bootstrap/Button";
import {InputGroup, Modal, Table} from "react-bootstrap";
import Form from "react-bootstrap/Form";
import { ToastContainer, toast } from 'react-toastify';
import "react-toastify/dist/ReactToastify.css";
import {getSessionCookie} from "../Cookies/Session";



class MyGroup extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showCreateGroup: false,
      showJoinGroup: false,
      showCreateCourse: false,
      showEditGroup: null,
      courseSelection: ["Courses"],
      groups: [""]
    }
  }
  componentDidMount() {
    this.getGroups();
    this.getCourses();
  }

  updateSelections = () => {
    for (const course of this.state.courses)
      if (!this.state.courseSelection.includes((course.name)))
        this.setState(prevState => ({
          courseSelection: [...prevState.courseSelection, course.name]
        }))
  }

  getCourses = () => {
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

  getGroups = () => {
    const xhr = new XMLHttpRequest();

    xhr.addEventListener('load', () => {
      const data = xhr.responseText;
      const jsonResponse = JSON.parse(data)
      this.setState({
        groups: jsonResponse
      });

    })
    const URL = 'http://localhost:8080/users/' + getSessionCookie().email + "/groups";

    xhr.open('GET', URL);
    xhr.send(URL);
  }

  setGroupToJoinID = (name) => {
    this.setState({
      groupToJoinID: name
    });
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

  setGroupToCreateName = (name) => {
    this.setState({
      groupToCreateName: name
    });
  }
  setCourseToCreateDescription = (name) => {
    this.setState({
      courseToCreateDescription: name
    });
  }

  setGroupToCreateDescription = (name) => {
    this.setState({
      groupToCreateDescription : name
    });
  }
  setGroupToEditDescription = (name) => {
    this.setState({
      groupToEditDescription : name
    });
  }
  setGroupToEditName = (name) => {
    this.setState({
      groupToEditName : name
    });
  }

  sendCreateGroupEditRequest = (groupID) => {
    toast.success("Successfully sent the group edit request")
    const xhr = new XMLHttpRequest()

    xhr.addEventListener('load', () => {
      const data = xhr.responseText;
      console.log("json response: ",data)
      // TODO add different metrics to see if the request has been successful

      // if (data.toLowerCase().includes("success"))
      //   toast.success("Successfully updated the group")
      // else
      //   toast.error("Failed to update the group, try again")


    })

    const URL = 'http://localhost:8080/groups/' + groupID
    xhr.open('PUT',URL)

    xhr.setRequestHeader('Content-Type', 'application/json');

    const jsonString = JSON.stringify( {
      "groupName": this.state.groupToEditName,
      "description": this.state.groupToEditDescription,
    })

    xhr.send(jsonString)
  }

  sendDeleteGroupRequest = (groupID) => {
    toast.success("Successfully sent the group deletion request")
    const xhr = new XMLHttpRequest()

    xhr.addEventListener('load', () => {
      const data = xhr.responseText;
      // TODO add different metrics to see if the request has been successful
      // if (data.toLowerCase().includes("success"))
      //   toast.success("Successfully deleted the group")
      // else
      //   toast.error("Failed to delete the group, try again")

    })
    const URL = 'http://localhost:8080/groups/' + groupID
    xhr.open('DELETE',URL)
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send()
  }


  sendCreateGroupRequest = () => {
    toast.success("Successfully sent the group creation request")
    const xhr = new XMLHttpRequest()


    xhr.addEventListener('load', () => {
      const data = xhr.responseText;
      console.log("json response: ",data)
      if (data.toLowerCase().includes("success"))
        toast.success("Successfully created the group")
      else
        toast.error("Failed to create a group, try again")


    })

    xhr.open('POST', 'http://localhost:8080/groups')
    xhr.setRequestHeader('Content-Type', 'application/json');

    const jsonString = JSON.stringify( {
      "groupName": this.state.groupToCreateName,
      "description": this.state.groupToCreateDescription,
      "creatorName": getSessionCookie().email,
      "courseName":  this.state.selectedCourse,

    })
    xhr.send(jsonString)
  }

  sendCreateCourseRequest = () => {
    toast.success("Successfully sent the course creation request")
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

    const jsonString = JSON.stringify( {
      "courseName": this.state.courseToCreateName,
      "institution": this.state.courseToCreateInstitutionName,
      "description": this.state.courseToCreateDescription,

    })
    xhr.send(jsonString)
  }

  sendCreateJoinRequest = () => {
    toast.success("Successfully sent group join request request")

    const xhr = new XMLHttpRequest()

    xhr.addEventListener('load', () => {
      const data = xhr.responseText;
      if (data.toLowerCase().includes("success"))
        toast.success("Successfully joined the group")
      else
        toast.error("Failed to join a group, try again")
    })

    const username =  getSessionCookie().email;
    const groupUpdateURL = 'http://localhost:8080/groups/' + this.state.groupToJoinID + "/members";

    xhr.open('PUT', groupUpdateURL)
    xhr.setRequestHeader('Content-Type', 'application/json');

    const jsonString = JSON.stringify( {
      "userName": username,
    })

    xhr.send(jsonString)

  }

  createCourseCreateForm = () => {
    return(
        <Form >
          <Form.Group controlId="formBasicEmail">
            <Form.Label>Course Name</Form.Label>
            <Form.Control
                onChange={(e) => {this.setCourseToCreateName(e.target.value)}}
            />
          </Form.Group>
          <Form.Group controlId="formBasicEmail">
            <Form.Label>Institution Name</Form.Label>
            <Form.Control
                onChange={(e) => {this.setCourseToCreateInstitutionName(e.target.value)}}
            />
          </Form.Group>
          <Form.Group controlId="exampleForm.ControlTextarea1">
            <Form.Label>Description</Form.Label>
            <Form.Control
                as="textarea" rows={3}
                onChange={(e) => {this.setCourseToCreateDescription(e.target.value)}}
            />
          </Form.Group>
        </Form>
    );
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
        </Form>
    );

  }

  setSelectedCourse = (courseName) => {
    this.setState({
      selectedCourse: courseName
    });

  }

  createGroupJoinForm = () =>{
    return(
        <Form >
          <Form.Group controlId="formBasicEmail">
            <Form.Label>Group ID</Form.Label>
            <Form.Control
                onChange={(e) => {this.setGroupToJoinID(e.target.value)}}
            />
          </Form.Group>
        </Form>
    );

  }

  createGroupEditForm = () => {
    const currentGroup = this.getGroupByID(this.state.showEditGroup)
    console.log("currentGroup: ", currentGroup)

    if ( currentGroup !== undefined) {
      return(
          <Form >
            <Form.Group controlId="formBasicEmail">
              <Form.Label>Group Name</Form.Label>
              <Form.Control
                  defaultValue = {currentGroup.groupName}
                  onChange={(e) => {this.setGroupToEditName(e.target.value)}}
              />
            </Form.Group>
            <Form.Group controlId="exampleForm.ControlTextarea1">
              <Form.Label>Description</Form.Label>
              <Form.Control
                  as="textarea" rows={3}
                  defaultValue = {currentGroup.description}
                  onChange={(e) => {this.setGroupToEditDescription(e.target.value)}}
              />
            </Form.Group>
          </Form>
      );
    }


  }

  createCourseCreatePopup = () => {
    return (
        <>
          <Modal
              show={this.state.showCreateCourse}
              onHide={ e => {this.handleCreateCourseClose()}}
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
              <Button variant="primary" onClick={ e=> {this.sendCreateCourseRequest()}}>Create</Button>
            </Modal.Footer>
          </Modal>
        </>
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
              <Button variant="secondary" onClick={ e=> {this.handleJoinGroupClose();}}>
                Close
              </Button>
              <Button variant="primary" onClick={ e=> {this.sendCreateJoinRequest();  this.getGroups();}}>Join group</Button>
            </Modal.Footer>
          </Modal>
        </>
    );
  }

  createGroupEditPopup = () => {
    return (
        <>
          <Modal
              show={this.state.showEditGroup !== null}
              onHide={ e => {this.handleEditGroupClose()}}
              backdrop="static"
              keyboard={false}
          >
            <Modal.Header closeButton>
              <Modal.Title>Edit group {this.state.showEditGroup}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {this.createGroupEditForm()}
            </Modal.Body>
            <Modal.Footer>
              <Button variant="danger" onClick={ e=> {
                this.sendDeleteGroupRequest(this.state.showEditGroup);
                this.getGroups();
                this.handleEditGroupClose();
              }}>
                Delete group
              </Button>
              <Button variant="primary" onClick={ e=> {
                this.sendCreateGroupEditRequest(this.state.showEditGroup);
                this.getGroups();
                this.handleEditGroupClose();
              }}>
                Update group
              </Button>
            </Modal.Footer>
          </Modal>
        </>
    );
  }

  getGroupByID = (id) => {
    for (const group of this.state.groups)
      if (group.id === id)
        return group
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
  handleEditGroupShow = (id) => {
    this.setState({
      showEditGroup: id
    });
  }

  handleEditGroupClose = () => {
    this.setState({
      showEditGroup: null
    });
  }




  renderGroups = () => {
    return (
        <Table group table size="sm">
          <thead>
          <tr>
            <th>ID</th>
            <th>Group name</th>
            <th>Description</th>
            <th>Participants</th>
          </tr>
          </thead>

          <tbody>

          {
            this.state.groups.map((group,index) =>
            <tr>
              <td onMouseDown= {() => {this.handleEditGroupShow(group.id)} }>{<b style={{ color: 'RoyalBlue' }}>{group.id}</b>}</td>
              <td>{group.groupName}</td>
              <td>{group.description}</td>
              <td>{group.members}</td>
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
                      onClick = {e => {this.handleCreateCourseShow()}} // send HTTP request here
                  >
                    + New Course
                  </Button>{' '}
                  {this.createCourseCreatePopup()}

                  <Button
                      variant="primary"
                      size="sm"
                      onClick = {e => {this.handleJoinGroupShow()}} // send HTTP request here
                  >
                    Join Group
                  </Button>{' '}
                  {this.createGroupJoinPopup()}
                  {this.createGroupEditPopup()}
                </div>
                <ToastContainer />

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