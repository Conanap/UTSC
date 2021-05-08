const express = require('express');
const app = express();

const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

let Datastore = require('nedb');
let messages = new Datastore({filename: 'db/messages.db', autoload: true, timestampData : true});
let users = new Datastore({filename: 'db/users.db', autoload: true})

let multer = require('multer');
let upload = multer({dest: 'uploads/'});

app.use(express.static('static'));

app.use(function (req, res, next){
    console.log("HTTP request", req.method, req.url, req.body);
    next();
});

var Message = (function(){
    var id = 0;
    return function item(message){
        this._id = id.toString();
        id++;
        this.content = message.content;
        this.username = message.username;
        this.upvote = 0;
        this.downvote = 0;
    }
}());

// Create

app.post('/api/users/', upload.single('picture'), function (req, res, next) {
    if (req.body.username in users) return res.status(409).end("Username:" + req.body.username + " already exists");
    users.insert({username: req.body.username, picture: req.file, mimetype: req.file.mimetype}, function(err, item) {
        if(err) return res.status(500).end(err);
        return res.redirect('/');
    });
});

app.post('/api/messages/', function (req, res, next) {
    var message = new Message(req.body);
    messages.insert(message, function(err, item) {
        if(err) return res.status(500).end(err);
        return res.json(message);
    });
});

// Read

app.get('/api/messages/', function (req, res, next) {
    messages.find({}).sort({createdAt: -1}).limit(5).exec(function(err, items) {
        if(err) return res.status(500).end(err);
        return res.json(items.reverse());
    });
});

app.get('/api/users/', function (req, res, next) {
    users.find({}).exec(function(err, items) {
        if(err) return res.status(500).end(err);
        let usrs = [];
        for(let item in items) {
            usrs.push(items[item].username);
        }
        return res.json(usrs);
    });
});

app.get('/api/users/:username/profile/picture/', function (req, res, next) {
    users.findOne({username: req.params.username}).exec(function(err, item) {
        if(err) return res.status(500).end(err);
        if(!item) res.status(404).end('username ' + req.params.username + ' does not exists');
        let profile = item.picture;
        res.setHeader('Content-Type', item.mimetype);
        res.sendFile(profile.path, {root: './'});
    });
});

// Update

app.patch('/api/messages/:id/', function (req, res, next) {
    messages.findOne({_id: req.params.id}, function(err, item) {
        if(err) return res.status(500).end(err);
        if(!item) return res.status(404).end("Message id:" + req.params.id + " does not exists");
        let numVote = 0;
        switch (req.body.action){
            case ("upvote"):
                numvote = item.upvote+1;
                break
            case ("downvote"):
                numVote = item.downvote+=1;
                break;
        }

        let votiBoi = req.body.action;
        messages.update({_id: item._id}, {$set: {[votiBoi]: numvote}}, {multi: false}, function(err, numReplaced) {
            if(err) return res.status(500).end(err);
            if(!numReplaced) return res.status(500).end(err);
            return res.json(item);
        });
    });
});

// Delete

app.delete('/api/messages/:id/', function (req, res, next) {
    messages.findOne({_id: req.params.id}, function(err, item) {
        if(err) return res.status(500).end(err);
        if(!item) return res.status(404).end("Message id:" + req.params.id + " does not exists");
        messages.remove({_id: item._id}, {multi: false}, function(err, num) {
            res.json(item);
        });
    });
});

const http = require('http');
const PORT = 3000;

http.createServer(app).listen(PORT, function (err) {
    if (err) console.log(err);
    else console.log("HTTP server on http://localhost:%s", PORT);
});