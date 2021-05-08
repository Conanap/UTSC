// Server sided game client

// Constants for gameplay
const GAME_SPEED = 1000 / 20;
const tile_count_x = 140;
const tile_count_y = 52;
// Spawns the first goal near-middle

function Player(locX, locY, color) {
  this.x = locX;
  this.y = locY;
  this.vx = 0;
  this.vy = 0;
  this.color = color;
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
let goal = null;

let player_num = 0;
let random = 0;

function joinGame() {
  let player_id = player_num;
  player_num++;
  // Starting pos for player 1
  player_one = new Player(20, 10);
  player_two = new Player(120, 10);
  spawnNewGoal();
  startGame();
  return random;
}

function leaveGame() {}

function startGame() {
  setInterval(runGame, GAME_SPEED);
}

function runGame() {
  update(player_one);
  update(player_two);
}

function update(player) {
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

function getPlayer() {
  return player_one;
}

function getGoal() {
  return goal;
}

function getScore() {
  return player_one.score;
}

function getLives() {
  return player_one.lives;
}

// Handles keyevents for each player
function keyInput(evt) {
  player_one.isMoving = true;
  switch (evt) {
    default:
      break;
    case 37:
      player_one.vx = -1;
      player_one.vy = 0;
      break;
    case 38:
      player_one.vx = 0;
      player_one.vy = -1;
      break;
    case 39:
      player_one.vx = 1;
      player_one.vy = 0;
      break;
    case 40:
      player_one.vx = 0;
      player_one.vy = 1;
      break;
  }
}

function spawnNewGoal() {
  let goal_x = Math.floor(Math.random() * tile_count_x);
  let goal_y = Math.floor(Math.random() * tile_count_y);
  goal = new Goal(goal_x, goal_y);
}

module.exports.joinGame = joinGame;
module.exports.getPlayer = getPlayer;
module.exports.getGoal = getGoal;
module.exports.getScore = getScore;
module.exports.getLives = getLives;
module.exports.keyInput = keyInput;
