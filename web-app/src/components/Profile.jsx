import React from "react";

class Profile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: 'test',
    }
  }

  getUserData = () => {
    // TODO: pass email via props or cookies
    const xhr = new XMLHttpRequest();

    xhr.addEventListener('load', () => {
      const data = xhr.responseText;
    })
    const URL = 'http://localhost:8080/users/';

    xhr.open('GET', URL);
    xhr.send(URL);
  }
  componentDidMount() {

  }


  render() {
    return (
      <div className="profile">
        <div class="container">
          <div class="row align-items-center my-5">
            <div class="col-lg-5">
              <h1 class="font-weight-light">Profile Page</h1>
              <p>
                {this.state.name}
              </p>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default Profile;