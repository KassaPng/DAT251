import React from "react";
import { getSessionCookie } from "../Cookies/Session.js";
import {Table, Form, InputGroup, FormControl} from "react-bootstrap";
import Button from "react-bootstrap/Button";

class Profile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userAbilities : "",
    }
  }


  getUserData = () => {
    const xhr = new XMLHttpRequest();

    xhr.addEventListener('load', () => {
      const data = xhr.responseText;
      const jsonResponse = JSON.parse(data)

      this.setState({
        name: jsonResponse["name"],
        userName: jsonResponse["userName"],
        groups: jsonResponse["groups"]
      });

    })
    const URL = 'http://localhost:8080/users/' + this.state.email;

    xhr.open('GET', URL);
    xhr.send(URL);
  }
  componentDidMount() {
    this.setState({"email": getSessionCookie().email
    }, () => {
      this.getUserData();
    });
  }
  addAbility = () => {


  }

  renderAbilitySlider = () => {
    return(
        <Form>
          <Form.Group controlId="formBasicRange">
            <Form.Label>Range</Form.Label>
            <Form.Control type="range" />
          </Form.Group>
        </Form>
    );
  }

  renderAbilities = () => {
    console.log(this.state.searchInput)
    if (this.state.userAbilities !== "placeholder") {
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
              Object.entries(this.state.userAbilities).map(([key, ability]) => {
                  return(
                      <tr>
                        {this.renderAbilitySlider()}
                        <td> </td>
                        <td>{ability.abilityName}</td>

                      </tr>
                  );
              })
            }
            </tbody>
          </Table>
      );
    }
  }
  updateAbilityToAdd = (event) => {
    this.setState({
      abilityToAdd: event.target.value
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
                <FormControl
                    placeholder="Ability name"
                    aria-label="Ability name"
                    aria-describedby="basic-addon2"
                    onChange = {this.updateAbilityToAdd.bind(this) }
                />
                <InputGroup.Append>
                  <Button
                      variant="outline-secondary"
                      onClick={ e=> {this.addAbility();}}
                  >
                    Add
                  </Button>
                </InputGroup.Append>
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