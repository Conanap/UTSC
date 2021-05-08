// Server sided game client

// Constants for gameplay
const GAME_SPEED = 1000 / 20;
const tile_count_x = 140;
const tile_count_y = 52;
// Spawns the first goal near-middle

function Player(locX, locY, cl) {
  this.x = locX;
  this.y = locY;
  this.spawnX = locX;
  this.spawnY = locY;
  this.vx = 0;
  this.vy = 0;
  this.color = cl;
  this.score = 0;
  this.trail = [];
  this.lives = 3;
  this.isMoving = false;
}

function Goal(locX, locY) {
  this.x = locX;
  this.y = locY;
}

let player_one = null;
let player_two = null;
let goal = new Goal(70, 40);

// User colors to disambiguate between two players
const USER_1_COLOR = "#4298f4";
const USER_2_COLOR = "#f48341";
// const USER_2_COLOR = "#f48341";
const GOAL_COLOR = "#44ff60";

//require our websocket library
var WebSocketServer = require("ws").Server;

//creating a websocket server at port 9090
var wss = new WebSocketServer({ port: 9090 });

//all connected to the server users
let user_id = 0;

const P1 = 0;
const P2 = 1;
let state = false;

//when a user connects to our sever
wss.on("connection", function(connection) {
  // Connects the users 1 & 2
  console.log("User " + user_id + "/2 connected!");
  connection.send(user_id);
  player_one = new Player(20, 10, USER_1_COLOR);
  player_two = new Player(120, 10, USER_2_COLOR);
  user_id++;
  if (user_id == 2) {
    // We have enough players for the game
    console.log("Two players have joined. The game begins!");
    state = true;
    spawnNewGoal();
  }
  setInterval(runGame, GAME_SPEED);
  function runGame() {
    update(player_one, player_two);
    update(player_two, player_one);
    let data = { p1: player_one, p2: player_two, apple: goal, start: state };
    connection.send(JSON.stringify(data));
  }
  // When server gets a message from a connected user - this will be key actions
  connection.on("message", function(message) {
    handleInput(JSON.parse(message));
  });
});

function update(player, other) {
  if (player.lives > 0) {
    //console.log(player);
    // Updates the location of the snake
    player.x += player.vx;
    player.y += player.vy;

    // Wraps locations (so it doesn't go offscreen)
    if (player.x < 0) {
      player.x = tile_count_x - 1;
    }
    if (player.x > tile_count_x - 1) {
      player.x = 0;
    }
    if (player.y < 0) {
      player.y = tile_count_y - 1;
    }
    if (player.y > tile_count_y - 1) {
      player.y = 0;
    }
    // Checks for collision with own trail
    for (var i = 0; i < player.trail.length; i++) {
      if (
        player.trail[i].x === player.x &&
        player.trail[i].y === player.y &&
        player.isMoving
      ) {
        // Reset trail
        player.trail = [];
        // Reduces score by a large amount
        player.score = Math.max(player.score - 500, 0);
        // Reduces player lives
        if (player.lives > 1) {
          player.lives--;
          player.x = player.spawnX;
          player.y = player.spawnY;
        } else {
          player.lives = 0;
        }
      }
    }
    // Checks for collision with other snake's trail
    for (var i = 0; i < other.trail.length; i++) {
      if (other.trail[i].x === player.x && other.trail[i].y === player.y) {
        // Reset trail
        player.trail = [];
        // Reduces score by a large amount
        player.score = Math.max(player.score - 500, 0);
        // Reduces player lives
        if (player.lives > 1) {
          player.lives--;
        } else {
          player.lives = 0;
        }
      }
    }

    // Pushes new trail and adds survival bonus
    player.trail.push({ x: player.x, y: player.y });
    if (player.isMoving) {
      player.score += 1;
    }

    if (goal.x === player.x && goal.y === player.y) {
      player.score += 1000;
      spawnNewGoal();
    }
  } else {
    // Do something when the player is dead?
    return;
  }
}

// Handles keyevents for each player
function handleInput(keyEvent) {
  if (state == true) {
    let player = keyEvent.player;
    let event = keyEvent.event;
    if (player == P1) {
      move(player_one, event);
    } else if (player == P2) {
      move(player_two, event);
    }
  }
}

function move(player, event) {
  switch (event) {
    default:
      break;
    case "ArrowLeft":
      player.vx = -1;
      player.vy = 0;
      break;
    case "ArrowUp":
      player.vx = 0;
      player.vy = -1;
      break;
    case "ArrowRight":
      player.vx = 1;
      player.vy = 0;
      break;
    case "ArrowDown":
      player.vx = 0;
      player.vy = 1;
      break;
  }
}

function spawnNewGoal() {
  let goal_x = Math.floor(Math.random() * tile_count_x);
  let goal_y = Math.floor(Math.random() * tile_count_y);
  goal = new Goal(goal_x, goal_y);
}
