import React, { Component } from "react";
import "../css/form.css";
import { graphql, compose } from "react-apollo";
import { signUpMutation, loginMutation } from "../queries/queries";
import FacebookLogin from 'react-facebook-login';
import GoogleLogin from 'react-google-login';

class signIn extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      password: ""
    };
  }

  signUpClicked(e) {
    e.preventDefault();
    this.props
      .signUpMutation({
        variables: {
          username: this.state.username,
          firstName: "Random",
          lastName: "Random",
          password: this.state.password
        }
      })
      .then(function(res) {
        console.log(res);
      })
      .catch(function(err) {
        if (err.toString().includes("E11000")) {
          let errorBox = document.getElementById("error-box");
          errorBox.innerHTML = "Error - Username already taken.";
        }
      });
  }

  loginClicked(e) {
    e.preventDefault();
    this.props
      .loginMutation({
        variables: {
          username: this.state.username,
          password: this.state.password
        }
      })
      .then(function(res) {
        window.location.href = "game";
      })
      .catch(function(err) {
        if (err.toString().includes("Password incorrect")) {
          let errorBox = document.getElementById("error-box");
          errorBox.innerHTML = "Error - Incorrect username or password";
        }
      });
  }


  render() {


     const responseFacebook = (response) => {
        console.log(response);
    }

      const responseGoogle = (response) => {
          console.log(response);
      }
    return (
      <div className="box">
        <p> Username </p>
        <input
          className="box-input"
          type="text"
          name="username"
          onChange={e => this.setState({ username: e.target.value })}
        />
        <p> Password </p>
        <input
          className="box-input"
          type="password"
          name="password"
          onChange={e => this.setState({ password: e.target.value })}
        />
        <div className="flex-container">
          <input
            type="submit"
            value="Login"
            onClick={this.loginClicked.bind(this)}
          />
          <input
            type="submit"
            value="Sign Up"
            onClick={this.signUpClicked.bind(this)}
          />
        </div>
        <br />
        <span id="error-box" />
        <hr />
        <br />
        <p id="description-text">Or, authenticate with -</p>
        <div className="flex-container">
          {/* TODO: Change this from a random link to redirect after Signup above */}
          <GoogleLogin
              clientId="1062529729199-1fl5lo42dlgfg8fuv5kjo6ulrhq3brbf.apps.googleusercontent.com" 
              buttonText="LOGIN WITH GOOGLE"
              onSuccess={responseGoogle}
              onFailure={responseGoogle}
              render={renderProps => (
                  <button onClick={renderProps.onClick}>This is my custom Google button</button>
              )}
          />
          <FacebookLogin
            appId="807440372963277"
            fields="name,email,picture"
            callback={responseFacebook}
            render={renderProps => (
               <button onClick={renderProps.onClick}>This is my custom FB button</button>
            )}
          />
      
          <a href="highscore">
            <i className="fab fa-twitter fa-2x" />
          </a>
        </div>
      </div>
    );
  }
}

export default compose(
  graphql(signUpMutation, { name: "signUpMutation" }),
  graphql(loginMutation, { name: "loginMutation" })
)(signIn);
