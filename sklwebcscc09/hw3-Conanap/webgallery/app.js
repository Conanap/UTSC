const express = require('express');
const app = express();

const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

let Datastore = require('nedb');
let photos = new Datastore({ filename: 'db/photos.db', autoload: true, timestampData: true });
let comments = new Datastore({ filename: 'db/comments.db', autoload: true, timestampData: true });

let users = new Datastore({ filename: 'db/users.db', autoload: true });

let multer = require('multer');
let upload = multer({ dest: 'uploads/' });

const crypto = require('crypto');

let hashPw = function (pw, salt = null) {
    salt = salt || crypto.randomBytes(16).toString('base64');
    let hash = crypto.createHmac('sha512', salt);
    hash.update(pw);
    let saltedHash = hash.digest('base64');

    return { salt: salt, saltedHash: saltedHash };
};

const session = require('express-session');
app.use(session({
    secret: 'waterdose boi',
    resave: false,
    saveUninitialized: true
}));

var isAuthenticated = function (req) {
    // return true;
    return !!req.username;
};

const cookie = require('cookie');

app.use(express.static('frontend'));

app.use(function (req, res, next) {
    req.username = ('user' in req.session) ? req.session.user : null;
    var username = (req.username) ? req.username : '';
    res.setHeader('Set-Cookie', cookie.serialize('username', username, {
        path: '/',
        maxAge: 60 * 60 * 24 * 7 // 1 week in number of seconds
    }));
    console.log("HTTP request", username, req.method, req.url, req.body);
    next();
});

let photoId = 0;
let newPost = function (post) {
    let ret = {
        author: post.author,
        photo: post.photo,
        title: post.title,
        mimetype: post.photo.mimetype,
        date: post.date,
        id: photoId.toString()
    };

    photoId++;
    return ret;
};

let cmtId = 0;
let newComment = function (cmt) {
    let ret = {
        imgAuth: cmt.imgAuth,
        author: cmt.author,
        content: cmt.content,
        date: cmt.date,
        id: cmtId.toString(),
        photoId: cmt.photoId.toString()
    };

    cmtId++;
    return ret;
};

let getDate = function () {
    let date = new Date();
    let ret = date.getDate() + '/' + (date.getMonth() + 1) + '/';
    ret += date.getFullYear();
    return ret;
};

// login, logout, etc
// sign up
app.post('/api/signup/', function (req, res, next) {
    var username = req.body.username;
    var password = req.body.password;
    users.findOne({ _id: username }, function (err, user) {
        if (err) return res.status(500).end(err);
        if (user) return res.status(409).end("username " + username + " already exists");
        var hashed = hashPw(password);
        users.update({ _id: username }, { _id: username, saltedHash: hashed.saltedHash, salt: hashed.salt }, { upsert: true }, function (err) {
            if (err) return res.status(500).end(err);
            // initialize cookie
            req.session.user = username;
            res.setHeader('Set-Cookie', cookie.serialize('username', username, {
                path: '/',
                maxAge: 60 * 60 * 24 * 7
            }));
            return res.json("User " + username + " signed up.");
        });
    });
});

// sign in
app.post('/api/signin/', function (req, res, next) {
    var username = req.body.username;
    var password = req.body.password;
    // retrieve user from the database
    users.findOne({ _id: username }, function (err, user) {
        if (err) return res.status(500).end(err);
        if (!user) return res.status(401).end("access denied");
        var salt = user.salt;
        var hashed = hashPw(password, salt);
        if (user.saltedHash !== hashed.saltedHash) return res.status(401).end("access denied");
        // initialize cookie
        req.session.user = user._id;
        res.setHeader('Set-Cookie', cookie.serialize('username', username, {
            path: '/',
            maxAge: 60 * 60 * 24 * 7
        }));
        return res.json("user " + username + " signed in");
    });
});

// sign out
app.get('/api/signout/', function (req, res, next) {
    req.session.destroy();
    req.username = '';
    res.setHeader('Set-Cookie', cookie.serialize('username', '', {
        path: '/',
        maxAge: 60 * 60 * 24 * 7 // 1 week in number of seconds
    }));
    res.redirect('/');
});

// create
app.post('/api/posts/', upload.single('file'), function (req, res, next) {
    if (!isAuthenticated(req)) return res.status(401).end("Access denied.");
    let post = newPost({
        author: req.body.author,
        title: req.body.title,
        photo: req.file,
        mimetype: req.file.mimetype,
        date: getDate()
    });

    photos.insert(post, function (err, item) {
        if (err) return res.status(500).end(err);
        photos.count({}, function (err, count) {
            if (err) return res.status(500).end(err);
            res.setHeader('numPosts', count);
            return res.json({ author: post.author, title: post.title, date: post.date, id: post.id });
        });
    });
});

app.post('/api/comments/', function (req, res, next) {
    if (!isAuthenticated(req)) return res.status(401).end("Access denied.");
    photos.findOne({ id: req.body.photoId }).exec(function (err, item) {
        if (err) return res.status(500).end(err);

        let cmt = newComment({
            imgAuth: item.author,
            photoId: req.body.photoId,
            author: req.body.author,
            content: req.body.content,
            date: getDate()
        });

        comments.insert(cmt, function (err, item) {
            if (err) return res.status(500).end(err);
            comments.count({}, function (err, count) {
                if (err) return res.status(500).end(err);
                res.setHeader('numCmts', count);
                return res.json(cmt);
            });
        });
    });
});

// get
app.get('/api/users/userList/', function (req, res, next) {
    if (!isAuthenticated(req)) return res.status(401).end("Access denied.");
    users.find({}).exec(function (err, items) {
        if (err) return res.status(500).end(err);

        let ret = [];
        for(i in items) {
            ret.push(items[i]._id);
        }
        return res.json(ret);
    });
});

app.get('/api/posts/:author/postIds/', function (req, res, next) {
    if (!isAuthenticated(req)) return res.status(401).end("Access denied.");
    photos.find({ author: req.params.author }).exec(function (err, items) {
        if (err) return res.status(500).end(err);
        let ret = [];
        for (let index in items) {
            ret.push(items[index].id);
        }
        res.setHeader('numPosts', ret.length);
        return res.json(ret);
    });
});

app.get('/api/posts/:author/:id/', function (req, res, next) {
    if (!isAuthenticated(req)) return res.status(401).end("Access denied.");
    photos.findOne({ author: req.params.author, id: req.params.id }).exec(function (err, item) {
        if (err) return res.status(500).end(err);
        if (!item) return res.status(404).end("Post id: " + req.params.id + " does not exist with author " + req.params.author + ".");
        delete item.photo;
        delete item.mimetype;
        res.json(item);
    });
});

app.get('/api/posts/:author/', function (req, res, next) {
    if (!isAuthenticated(req)) return res.status(401).end("Access denied.");
    photos.find({ author: req.params.author }).exec(function (err, item) {
        if (err) return res.status(500).end(err);
        if (!item) return res.status(404).end("Post from " + req.params.author + " does not exist.");
        delete item.photo;
        delete item.mimetype;
        res.json(item);
    });
});

app.get('/api/photos/:id/', function (req, res, next) {
    if (!isAuthenticated(req)) return res.status(401).end("Access denied.");
    photos.findOne({ id: req.params.id }).exec(function (err, item) {
        if (err) return res.status(500).end(err);
        if (!item) return res.status(404).end("Photo id: " + req.params.id + " does not exist.");
        res.setHeader('Content-Type', item.mimetype);
        return res.sendFile(item.photo.path, { root: './' });
    });
});

app.get('/api/comments/:author/:id/', function (req, res, next) {
    if (!isAuthenticated(req)) return res.status(401).end("Access denied.");
    let limit = req.query.limit || 5;
    let page = req.query.page || 0;
    let photoId = req.params.id;
    let imgAuth = req.params.author;

    comments.find({ imgAuth: imgAuth, photoId: photoId }).sort({ createdAt: -1 }).limit(limit + page * 5).exec(function (err, items) {
        if (err) return res.status(500).end(err);
        if (!items) return res.json([]);
        return res.json(items.slice(Math.max(items.length - 5, 0), items.length).reverse());
    });
});

// delete
app.delete('/api/posts/:author/:id/', function (req, res, next) {
    if (!isAuthenticated(req)) return res.status(401).end("Access denied.");
    photos.findOne({ author: author, id: req.params.id }, function (err, item) {
        if (err) return res.status(500).end(err);
        if (!item) return res.status(404).end("Post id: " + req.params.id + " does not exist with author " + req.params.author + ".");

        if (item.author != req.username) return res.status(401).end("Access denied.");
        photos.remove({ id: item.id }, { multi: false }, function (err, num) {
            if (err) return res.status(500).end(err);
            comments.remove({ photoId: item.id }, { multi: true }, function (err, num) {
                if (err) return res.status(500).end(err);
                return res.json(item);
            });
        });
    });
});

app.delete('/api/comments/:id', function (req, res, next) {
    if (!isAuthenticated(req)) return res.status(401).end("Access denied.");
    comments.findOne({ id: req.params.id }, function (err, item) {
        if (err) return res.status(500).end(err);
        if (!item) return res.status(404).end("Comment id: " + req.params.id + " does not exist.");

        if (item.author != req.username) return res.status(401).end("Access denied.");
        comments.remove({ id: item.id }, { multi: false }, function (err, num) {
            if (err) return res.status(500).end(err);
            return res.json(item);
        });
    });
});

const http = require('http');
const PORT = 3000;

http.createServer(app).listen(PORT, function (err) {
    if (err) console.log(err);
    else console.log("HTTP server on http://localhost:%s", PORT);
});