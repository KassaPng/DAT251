import React from "react";
import { getSessionCookie } from "../Cookies/Session.js";
import {Table, Form, InputGroup, FormControl} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import { ToastContainer, toast } from 'react-toastify';
import RangeSlider from 'react-bootstrap-range-slider';

class Profile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userAbilities : {},
      courseSelection: ["Courses"],
      selectedCourse: "Course",
      courses: ["Course"],
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

  componentDidMount() {
    this.getUserData();
    this.getUserAbilityValues();
  }


  getUserAbilityValues = () => {
    const xhr = new XMLHttpRequest();

    xhr.addEventListener('load', () => {
      const data = xhr.responseText;
      const jsonResponse = JSON.parse(data)

      this.setState({
        userAbilities: jsonResponse.abilities
      }, () => {this.renderAbilities()});

    })
    const URL = 'http://localhost:8080/users/' + getSessionCookie().email

    xhr.open('GET', URL);
    xhr.send(URL);

  }


  sendAbilityUpdates = () => {
    for (const [key, value] of Object.entries(this.state.abilityScoresUpdated)) {
      this.sendAbilityUpdate(key,value)
    }
  }

  sendAbilityUpdate = (abilityName, abilityValue) => {
    const str = "Successfully sent the ability update request for " + abilityName
    toast.success(str)

    const xhr = new XMLHttpRequest()

    xhr.addEventListener('load', () => {

      const data = xhr.responseText;
      const jsonResponse = JSON.parse(data)
      this.setState({
        userAbilities : jsonResponse.abilities
      })
      const str = "Successfully updated " +  abilityName
      toast.success(str)
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
                        <td >{ability}</td>
                      </tr>
                  );
              })
            }
            </tbody>
          </Table>
      );
    }
  }

  renderAbilitySlider = (abilityName) => {
    return(
        <Form>
          <Form.Group controlId="formBasicRange">
            <Form.Label></Form.Label>
            <RangeSlider
                min = "1"
                max = "10"
                step = {1}
                type="range"
                tooltipLabel={currentValue => `${currentValue}`}
                tooltip='auto'
                defaultValue = {() => {this.getSpecificUserAbility(abilityName)}}
                value = {this.getAbilityValue(abilityName)}
                onChange={ e => {this.updateAbilityValue(e.target.value, abilityName); } }
            />
          </Form.Group>
        </Form>
    );
  }

  getSpecificUserAbility = (abilityName) => {

    if (this.state.userAbilities !== ""){
      for (const course of this.state.courses){
        if ( this.state.selectedCourse === course.name) {
          for (const [key, value] of Object.entries(this.state.userAbilities)){
            if (value['abilities'] !== undefined && parseInt(key) === parseInt(course.id)){
              const scr = this.state.abilityScoresUpdated
              scr[abilityName] = value['abilities'][abilityName]
              this.setState({
                abilityScoresUpdated : scr
              });
              return value['abilities'][abilityName]
            }
          }
        }

      }
    }
  }

  updateAllAbilities = () => {
    for (const [key,value] of Object.entries(this.getCurrentCourseAbilities())) {
      this.updateSpecificUserAbility(value)
    }


  }
  updateSpecificUserAbility = (abilityName) => {
    if (this.state.userAbilities !== ""){
      for (const course of this.state.courses){
        if ( this.state.selectedCourse === course.name) {
          for (const [key, value] of Object.entries(this.state.userAbilities)){
            if (value['abilities'] !== undefined && parseInt(key) === parseInt(course.id)){
              const scr = this.state.abilityScoresUpdated
              scr[abilityName] = value['abilities'][abilityName]
              this.setState({
                abilityScoresUpdated : scr
              });
            }
          }
        }

      }
    }

  }

  updateAbilityValue = (value, abilityName) => {

    const updatedAbilityMap = this.state.abilityScoresUpdated
    updatedAbilityMap[abilityName] = value
    this.setState({
      abilityScoresUpdated : updatedAbilityMap
    });

  }

  getAbilityValue = (abilityName) => {
    if (this.state.abilityScoresUpdated !== undefined)
      return this.state.abilityScoresUpdated[abilityName]

  }

  setSelectedCourse = (courseName) => {
    this.setState({
      selectedCourse: courseName
    }, () => {
      this.getCurrentCourseAbilities(courseName)
      this.renderAbilities()
      this.updateAllAbilities()

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

              </InputGroup>
              <div>
                <Button
                    variant="primary"
                    size="sm"
                    onClick = {e => {this.sendAbilityUpdates()}} // send HTTP request here

                >
                  Update abilities
                </Button>{' '}
                <Button
                    variant="primary"
                    size="sm"
                >
                  Find a group
                </Button>{' '}
              </div>
              <div>
                {this.renderAbilities()}
              </div>
            </div>
          </div>
        </div>
        <ToastContainer />

      </div>
    );
  }
}

export default Profile;