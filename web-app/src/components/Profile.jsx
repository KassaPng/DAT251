import React from "react";
import { getSessionCookie } from "../Cookies/Session.js";

class Profile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
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
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default Profile;