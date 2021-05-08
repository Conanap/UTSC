import React, { Component } from "react";
import { loadGame } from "../client/client_game";
import { getIsConnected, connect, send } from "../client/localserver";

class game extends Component {
  componentDidMount() {
    // Runs the client sided game located in scripts/core_game.js
    connect();
  }

  render() {
    let gameCanvas = (
      <div id="game-content">
        Current Score: <span id="current-score">0</span>
        <br />
        Lives Remaining: <span id="current-lives">...</span>
        <br />
        <br />
        <canvas id="game-canvas" height="520" width="1400" />
      </div>
    );
    return gameCanvas;
  }
}

export default game;
