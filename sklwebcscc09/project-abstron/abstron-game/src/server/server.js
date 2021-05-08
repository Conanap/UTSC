/**
 * source 1: https://graphql.org/graphql-js/running-an-express-graphql-server/
 * source 2: https://zellwk.com/blog/crud-express-mongodb/
 * source 3: https://medium.freecodecamp.org/a-beginners-guide-to-graphql-60e43b0a41f5
 * source 4: https://medium.freecodecamp.org/how-to-set-up-a-graphql-server-using-node-js-express-mongodb-52421b73f474
 */
const express = require("express");
const session = require("express-session");
const cookie = require("cookie");
const app = express();
const cors = require('cors');
app.use(express.static("../abstron-game/public"));


//Allow cross-origin requests
app.use(cors());

app.use(
  session({
    secret: "HyBfhs23Ar046732",
    resave: false,
    saveUninitialized: false,
    maxAge: 60 * 60 * 24 * 7, // 1 week in number of seconds
    cookie: { httpOnly: false, sameSite: false, secure: false }
  })
);

const MongoClient = require("mongoose");
// mongoose internal bug requires this
MongoClient.set("useCreateIndex", true);
let db;

// local testing repo
let temp = [{ username: "wow", id: 1 }];
let temp2 = [];

// old code
let graphqlHTTP = require("express-graphql");
let schema = require("./graphql/");
// let bodyParser = require('body-parser');

app.use(function(req, res, next) {
  var username = req.session.user ? req.session.user : "";
  res.setHeader(
    "Set-Cookie",
    cookie.serialize("username", username, {
      path: req.path
    })
  );
  console.log("HTTP request", username, req.method, req.url, req.body);
  //console.log("HTTP request", req.method, req.url, req.body);
  next();
});

// mongodb stuff

const collectionNames = ["Users"];
// running func
let serverRunFunc = function(client) {
  console.log("Mongoose initialization sucessful.");

  // run server
  app.listen(3030);
  console.log("Server running on localhost:3030/graphql");
};

app.use(
  "/graphql",
  graphqlHTTP((req, res) => ({
    schema,
    graphiql: true,
    context: { req: req, res: res }
  }))
);
// connect to mongo db
MongoClient.connect(
  "mongodb+srv://admin:admin@cluster0-hzjkz.mongodb.net/test?retryWrites=true",
  { useNewUrlParser: true }
)
  .then(serverRunFunc)
  .catch(function(err) {
    console.log("Mongoose initialization failed.");
    console.log(err);
  });

// Brian's WebRTC code
// REFER TO HERE: https://www.twilio.com/blog/react-app-with-node-js-server-proxy
// let SimplePeer = require("simple-peer");
// var wrtc = require("wrtc");

// let peer = new SimplePeer({
//   // Sets to true/false if this is first peer
//   initiator: true,
//   trickle: false,
//   wrtc: wrtc
// });

// peer.on("signal", function(data) {
//   document.getElementById("yourId").value = JSON.stringify(data);
// });
