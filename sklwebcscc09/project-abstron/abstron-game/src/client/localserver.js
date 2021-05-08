import { deflateRaw } from "zlib";

//our username
var name;
var connectedUser;

let server = null;
let state = false;
let is_connected = false;
let player_id = null;

// Canvas items for drawing to the page
let canvas_context = null;
let canvas = null;
const grid_size = 10;
const GOAL_COLOR = "#44ff60";

export function connect() {
  // connecting to our signaling server
  server = new WebSocket("ws://localhost:9090");

  server.onopen = function() {
    is_connected = true;
    // Gets the WebGL drawing components
    canvas = document.getElementById("game-canvas");
    canvas_context = canvas.getContext("2d");
    // Adds a keylistener for tracking keyboard input
    document.addEventListener("keydown", keyListenerHandler);
    console.log("Connected to the signaling server");
  };

  // when we got a message from a signaling server - something has changed
  server.onmessage = function(msg) {
    console.log(msg.data);
    if (player_id == null) {
      player_id = msg.data;
    } else {
      let data = JSON.parse(msg.data);
      // Sees if the game has started yet
      state = data.start;
      updateGame(data);
    }
  };
}

export function getIsConnected() {
  return is_connected;
}

function lifeGenerator(n) {
  if (n === 0) {
    return "Game Over!";
  }
  let x = "";
  for (let i = 0; i < n; i++) {
    x += "❤";
  }
  return x;
}

function updateGame(data) {
  let player_one = data.p1;
  let player_two = data.p2;
  let goal = data.apple;

  // Background of program
  canvas_context.fillStyle = "black";
  canvas_context.fillRect(0, 0, canvas.width, canvas.height);

  drawPlayer(player_one);
  drawPlayer(player_two);
  // Draws the goal
  canvas_context.fillStyle = GOAL_COLOR;
  canvas_context.fillRect(
    goal.x * grid_size,
    goal.y * grid_size,
    grid_size - 2,
    grid_size - 2
  );
}

function drawPlayer(player) {
  let position_x = player.x;
  let position_y = player.y;
  let trail = player.trail;

  let score = document.getElementById("current-score");
  score.innerHTML = player.score;

  let num_lives = player.lives;
  let lives = document.getElementById("current-lives");
  let life_element = lifeGenerator(num_lives);
  lives.innerHTML = life_element;

  // Draws the trail of the snake
  canvas_context.fillStyle = player.color;
  for (var i = 0; i < trail.length; i++) {
    canvas_context.fillRect(
      trail[i].x * grid_size,
      trail[i].y * grid_size,
      grid_size - 2,
      grid_size - 2
    );
  }

  // Draws the current position of the snake
  canvas_context.fillStyle = "white";
  canvas_context.fillRect(
    position_x * grid_size,
    position_y * grid_size,
    grid_size - 2,
    grid_size - 2
  );
}

function keyListenerHandler(evt) {
  console.log("Sending event");
  let data = { player: player_id, event: evt.key };
  server.send(JSON.stringify(data));
}
