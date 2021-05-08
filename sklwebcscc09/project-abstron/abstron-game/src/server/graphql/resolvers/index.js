let User = require("../../models/User");
let Score = require("../../models/Score");

const validator = require("validator");
const crypto = require("crypto");
const cookie = require("cookie");

//Functions to validate the input
const checkString = function(s) {
  return validator.isAlphanumeric(s);
};

const checkScore = function(score) {
  return validator.isDecimal(score);
};

//Functions to hash the password
function generateSalt() {
  return crypto.randomBytes(16).toString("base64");
}

function generateHash(password, salt) {
  var hash = crypto.createHmac("sha512", salt);
  hash.update(password);
  return hash.digest("base64");
}

const users = function() {
  return new Promise(function(resolve, reject) {
    User.find({})
      .populate()
      .exec(function(err, res) {
        err ? reject(err) : resolve(res);
      });
  });
};

const user = function(root, args) {
  return new Promise(function(resolve, reject) {
    User.findOne(args).exec(function(err, res) {
      err ? reject(err) : resolve(res);
    });
  });
};

const createUser = function(
  root,
  { username, password, firstName, lastName },
  context
) {
  //Making sure the fields entered are alphanumeric
  if (
    !checkString(username) ||
    !checkString(password) ||
    !checkString(firstName) ||
    !checkString(lastName)
  ) {
    return new Promise(function(resolve, reject) {
      reject("A field enterned is not alphanumeric.");
    });
  }

  user(root, { username: username }).then(function(err) {
    return new Promise(function(resolve, reject) {
      reject("Username already exists.");
    });
  });

  let salt = generateSalt();
  let hashed = generateHash(password, salt);
  const newUser = new User({ username, hashed, salt, firstName, lastName });

  return new Promise(function(resolve, reject) {
    newUser.save(function(err, res) {
      err ? reject(err) : resolve(res);
    });
  });
};

const isAuthenticated = function(context) {
  return context.req.session.user;
};

const currentUser = function(root,args,context){
    return new Promise(function(resolve, reject) {
    let user = isAuthenticated(context);
    //Not sure if it's necessary
    if (user) {
      return reject("User is not logged in. Unathorized access.");
    } 
    else{
      return resolve(user);
    }


  });


}

const setUserHighScore = function(root, { username, score }, context) {
  const newScore = new Score({ username, score });

  //Probably not necessary here, but doing it anyway
  if (!checkString(username)) {
    return new Promise(function(resolve, reject) {
      reject("Username is not alphanumeric.");
    });
  }
  /*
    if(!checkScore(score)) {
        return reject("Score is not a number.");
        
    }*/
  if (!isAuthenticated(context)) {
    return new Promise(function(resolve, reject) {
      reject("User not authorized to complete this action.");
    });
  }
  return new Promise(function(resolve, reject) {
    newScore.save(function(err, res) {
      err ? reject(err) : resolve(res);
    });
  });
};

const getHighScores = function(root, { num }, context) {
  const numScore = num || 10;

  return new Promise(function(resolve, reject) {
    Score.find({})
         .populate()
         .sort({score: -1})
         .limit(numScore)
         .exec(function (err, res) {
           err ? reject(err) : resolve(res);
         });
  });
};

const login = function(root, { username, password }, context) {
  return new Promise(function(resolve, reject) {
    //Making sure the fields entered are alphanumeric
    if (!checkString(username) || !checkString(password)) {
      return reject("A field enterned is not alphanumeric.");
    }

    //Not sure if it's necessary
    if (isAuthenticated(context)) {
      return reject("User already logged in. Log out first.");
    }

    user(root, { username: username })
      .catch(function(err) {
        return reject("Username does not exist.");
      })
      .then(function(res) {
        if (res.hashed !== generateHash(password, res.salt)) {
          return reject("Password incorrect.");
        }
        context.req.session.user = username;
        context.res.setHeader(
          "Set-Cookie",
          cookie.serialize("username", username, {
            path: context.req.path
          })
        );

        return resolve({ username });
      });
  });
};
const logout = function(root, { username }, context) {
  return new Promise(function(resolve, reject) {
    //Making sure username entered is alphanumeric
    if (!checkString(username)) {
      return reject("Username is not alphanumeric.");
    }

    //Making sure the user is already logged in
    if (!isAuthenticated(context)) {
      return reject("User is not logged in. Log in first.");
    }

    user(root, { username: username })
      .catch(function(err) {
        return reject("Username does not exist.");
      })
      .then(function(res) {
        context.req.session.destroy();
        context.res.setHeader(
          "Set-Cookie",
          cookie.serialize("username", "", {
            path: context.req.path
          })
        );

        return resolve({ username });
      });
  });
};

const restrictPW = function(_, _) {
  return "Redacted";
};

const output = {
  User: {
    hashed: restrictPW,
    salt: restrictPW
  },

  Query: {
    user: user,
    users: users,
    highScores: getHighScores,
    currentUser: currentUser
  },

  Mutation: {
    createUser: createUser,
    setUserHighScore: setUserHighScore,
    login: login,
    logout: logout
  }
};
module.exports = output;
