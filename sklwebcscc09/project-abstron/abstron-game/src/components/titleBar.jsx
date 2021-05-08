import React, { Component } from "react";
import { graphql, compose } from "react-apollo";
import { logoutMutation } from "../queries/queries";

class titleBar extends Component {

  floatyVisibility(){
    console.log(document.cookie);
    if(document.cookie.includes("username")){
      let username = (document.cookie.split("username=")[1]).split(";")[0];
      if(username.length === 0){
        return "";
      }
      else{

        return(        
        <div class="floaty">
            Welcome, User!
            <br />
            <a class="navbar-link" href="/">
              Profile
            </a>
            <a class="navbar-link" href="/">
              Home
            </a>
            <a class="navbar-link" href="/highscore">
              Highscores
            </a>
            <a class="navbar-link" href="" onClick={this.logoutClicked.bind(this)}>
              Sign Out
            </a>
          </div>);
      }


    }
  }

  logoutClicked(e) {

    e.preventDefault();
    let username = (document.cookie.split("username=")[1]).split(";")[0];
    console.log(username)
    this.props
      .logoutMutation({
        variables: {
          username: username
        }
      })
      .then(function(res) {
        console.log(res);      
        window.location.href = "/";
      })
      .catch(function(err) {

        console.log(err);
          
      });
  }
  
  render() {
    return (
      // TODO - Saba please hide this div (floaty) if the user is not signed in (if there is no session)
      // TODO - Saba please replace the name User below with the session's username



      <React.Fragment>
        {this.floatyVisibility()}
        <a className="no-deco" href="/">
          <p id="cursive-title">Project-Abstron</p>
        </a>
        <p id="description-text">Outsmart. Outmaneuver. Survive.</p>
      </React.Fragment>

    );
  }
}

export default compose(
  graphql(logoutMutation, { name: "logoutMutation" })
)(titleBar);
