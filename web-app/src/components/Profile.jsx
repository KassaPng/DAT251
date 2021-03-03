import React from "react";

class Profile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: 'Ola Nordmann',
    }
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