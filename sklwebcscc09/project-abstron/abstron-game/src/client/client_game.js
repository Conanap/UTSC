import {
  joinGame,
  getPlayer,
  getGoal,
  getScore,
  getLives,
  keyInput
} from "../server/server_game";

// Canvas items for drawing to the page
let canvas_context = null;
let canvas = null;

// User colors to disambiguate between two players
const USER_1_COLOR = "#4298f4";
// const USER_2_COLOR = "#f48341";
const GOAL_COLOR = "#44ff60";

// Constants for gameplay
const GAME_SPEED = 1000 / 20;
const grid_size = 10;
let game_running = true;

export function loadGame() {
  // Gets the WebGL drawing components
  canvas = document.getElementById("game-canvas");
  canvas_context = canvas.getContext("2d");

  // Joins the game
  joinGame();
  // Adds a keylistener for tracking keyboard input
  document.addEventListener("keydown", keyListenerHandler);
  // The game loop is a continuous call every few MS of game function
  setInterval(gameHandler, GAME_SPEED);
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

function gameHandler() {
  if (game_running) {
    let player = getPlayer();
    let position_x = player.x;
    let position_y = player.y;
    let trail = player.trail;
    let goal = getGoal();
    let goal_x = goal.x;
    let goal_y = goal.y;

    let score = document.getElementById("current-score");
    score.innerHTML = getScore();

    let num_lives = getLives();
    let lives = document.getElementById("current-lives");
    let life_element = lifeGenerator(num_lives);
    lives.innerHTML = life_element;

    // Background of program
    canvas_context.fillStyle = "black";
    canvas_context.fillRect(0, 0, canvas.width, canvas.height);

    // Draws the trail of the snake
    canvas_context.fillStyle = USER_1_COLOR;
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

    // Draws the goal
    canvas_context.fillStyle = GOAL_COLOR;
    canvas_context.fillRect(
      goal_x * grid_size,
      goal_y * grid_size,
      grid_size - 2,
      grid_size - 2
    );

    if (num_lives === 0) {
      game_running = false;
      // TODO - Saba please update the backend with this highscore of the user
      // alert("GAME OVER! Your highscore was:" + getScore());
    }
  } else {
    return;
  }
}

function keyListenerHandler(evt) {
  keyInput(evt.keyCode);
}
