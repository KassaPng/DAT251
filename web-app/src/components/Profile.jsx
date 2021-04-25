import React from "react";
import { getSessionCookie } from "../Cookies/Session.js";
import {Table, Form, InputGroup, FormControl} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {toast} from "react-toastify";

class Profile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userAbilities : "",
      courseSelection: ["Courses"],
      selectedCourse: "Course",
      courses: "",
      abilityScoresUpdated :  {}
    }
  }

  updateSelections = () => {
    for (const course of this.state.courses)
      if (!this.state.courseSelection.includes((course.name)))
        this.setState(prevState => ({
          courseSelection: [...prevState.courseSelection, course.name]
        }))
  }

  getUserData = () => {
    const xhr = new XMLHttpRequest();

    xhr.addEventListener('load', () => {
      const data = xhr.responseText;
      console.log("data: ", data)
      const jsonResponse = JSON.parse(data)
      console.log("json response: ", jsonResponse)

      this.setState({
        name: jsonResponse["name"],
        userName: jsonResponse["userName"],
        groups: jsonResponse["groups"],
        // courses: jsonResponse["courses"]
        courses: [
          {
            "id": 0,
            "name": "course name",
            "institutionName": "instutituion name",
            "description": "description",
            "abilities": [
              "Ambition",
              "Work-rate",
              "Knowledge"
            ],
            "relatedGroups": [],
            "relatedUsers": [
              "asd@asd.asd"
            ]
          },
          {
            "id": 101,
            "name": "asdasd",
            "institutionName": "asdasdasd",
            "description": "asdasd",
            "abilities": [
              "Ambition",
              "Work-rate",
              "Knowledge"
            ],
            "relatedGroups": [],
            "relatedUsers": [
              "asd@asd.asd"
            ]
          },
          {
            "id": 103,
            "name": "asdasdasdasd",
            "institutionName": "asdasdasdasdasdasd",
            "description": "asdasdasdasd",
            "abilities": [
              "Ambition",
              "Work-rate",
              "Knowledge"
            ],
            "relatedGroups": [],
            "relatedUsers": [
              "asd@asd.asd"
            ]
          }
        ]
      }, () => {
        this.updateSelections()
      });

    })
    const URL = 'http://localhost:8080/users/' + getSessionCookie().email

    xhr.open('GET', URL);
    xhr.send(URL);
  }

  componentDidMount() {
    this.getUserData();
  }

  updateUserAbilityValue = () => {

  }

  updateAbilityValue = (value, abilityName) => {

    const updatedAbilityMap = this.state.abilityScoresUpdated
    updatedAbilityMap[abilityName] = value
    this.setState({
      abilityScoresUpdated : updatedAbilityMap
    });

  }

  renderAbilitySlider = (abilityName) => {
    return(
        <Form>
          <Form.Group controlId="formBasicRange">
            <Form.Label>Range</Form.Label>
            <Form.Control
                type="range"
                onChange={ e => (this.updateAbilityValue(e.target.value, abilityName))}
            />
          </Form.Group>
        </Form>
    );
  }

  sendAbilityUpdates = () => {
    for (const [key, value] of Object.entries(this.state.abilityScoresUpdated)) {
      this.sendAbilityUpdate(key,value)
      console.log(key, value);
    }
  }

  sendAbilityUpdate = (abilityName, abilityValue) => {
    toast.success("Successfully sent the ability update request for ", abilityName)
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

    const URL = 'http://localhost:8080/users/' + getSessionCookie().email
    xhr.open('PUT',URL)

    xhr.setRequestHeader('Content-Type', 'application/json');

    const jsonString = JSON.stringify( {
      "abilityScore": abilityValue,
      "abilityName": abilityName,
      "abilityGroupName": this.state.selectedCourse,
    })

    xhr.send(jsonString)

  }

 getCurrentCourseAbilities = () => {
    if (this.state.courses !== "")
      for (const course of this.state.courses)
        if (course.name === this.state.selectedCourse)
          return course.abilities


 }

  renderAbilities = () => {
    if (this.state.selectedCourse !== "Course") {
      return (
          <Table  responsive="xl">
            <thead>
            <tr>
              <th >Ability Score </th>
              <th>Ability name</th>

            </tr>
            </thead>
            <tbody>
            {
              Object.entries(this.getCurrentCourseAbilities()).map(([key, ability]) => {
                  return(
                      <tr>

                        <td> {this.renderAbilitySlider(ability)}</td>
                        <td>{ability}</td>

                      </tr>
                  );
              })
            }
            </tbody>
          </Table>
      );
    }
  }



  setSelectedCourse = (courseName) => {
    console.log("courseName: ", courseName)
    this.setState({
      selectedCourse: courseName
    });
  }



  render() {
    return (
      <div className="profile">
        <div class="container">
          <div class="row align-items-center my-5">
            <div class="col-lg-5">
              <h1 class="font-weight-light">Profile Page</h1>
              <p>
                {this.state.userName}
              </p>

              <InputGroup className="mb-3">

                <Form.Control
                    as="select"
                    onChange={(e) => {this.setSelectedCourse(e.target.value)}}
                >
                  {this.state.courseSelection.map((value, index) => {
                    return <option>{value}</option>
                  })}
                </Form.Control>
                <Button
                    variant="primary"
                    size="sm"
                    onClick = {e => {this.sendAbilityUpdates()}} // send HTTP request here

                >
                  Update abilities
                </Button>{' '}
              </InputGroup>
              <div>
                {this.renderAbilities()}
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default Profile;