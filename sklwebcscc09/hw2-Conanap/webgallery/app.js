const express = require('express');
const app = express();

const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

let Datastore = require('nedb');
let photos = new Datastore({filename: 'db/photos.db', autoload: true, timestampData : true});
let comments = new Datastore({filename: 'db/comments.db', autoload: true, timestampData : true})

let multer = require('multer');
let upload = multer({dest: 'uploads/'});

app.use(express.static('static'));

app.use(function (req, res, next){
    console.log("HTTP request", req.method, req.url, req.body);
    next();
});

let newPost = (function() {
    var id = 0;
    return function item(post) {
        this.author = post.author;
        this.photo = post.photo;
        this.mimetype = post.photo.mimetype;
        this.date = post.date;
        this.id = id.toString();
        id++;
    };
})();

let newComment = (function() {
    var id = 0;
    return function item(cmt) {
        this.author = cmt.author;
        this.content = cmt.content;
        this.date = cmt.date;
        this.id = id.toString();
        this.photoId = cmt.photoId;
        id++;
    };
})();

let getDate = function() {
    let date = new Date();
    let ret = date.getDate() + '/' + (date.getMonth() + 1) + '/';
    re += date.getFullYear();
    return ret;
};

// create
app.post('/api/photos/', upload.single('picture'), function(req, res, next) {
    let post = newPost({
        author: req.body.author,
        photo: req.file,
        mimetype: req.file.mimetype,
        date: getDate()
    });

    photos.insert(post, function(err, item) {
        if(err) return res.status(500).end(err);
        return res.json(post);
    });
});

app.post('/api/comments/', function(req, res, next) {
    let cmt = newComment({
        author: req.body.author,
        content: req.body.content,
        date: getDate()
    });

    comments.insert(cmt, function(err, item) {
        if(err) return res.status(500).end(err);
        return res.json(cmt);
    });
});

// get
app.get('/api/photos/', function(req, res, next) {
    ;
});

app.get('/api/photos/:id/picture/', function(req, res, next) {});

app.get('/api/comments/:photoId', function(req, res, next) {
    let limit = req.query.limit || 5;
    let page = req.query.page || 0;
    photos.find({id: req.params.photoId}).exec(function(err, item) {
        if(err) return res.status(500).end(err);
        if(!item) return res.status(404).end("Post id: " + req.params.photoId + " does not exist.");
    });
    comments.find({photoId: req.params.photoId}).sort({createdAt: -1}).limit(limit).exec(function(err, items) {
        if(err) return res.status(500).end(err);
        return res.json(items.reverse());
    });
});

// delete
app.delete('/api/photos/:id/', function(req, res, next) {
    photos.findOne({id: req.params.id}, function(err, item) {
        if(err) return res.status(500).end(err);
        if(!item) return res.status(404).end("Post id: " + req.params.id + " does not exist.");

        photos.remove({id: item.id}, {multi: false}, function(err, num) {
            if(err) return res.status(500).end(err);
            comments.remove({photoId: item.id}, {multi: true}, function(err, num) {
                if(err) return res.status(500).end(err);
                return res.json(item);
            });
        });
    });
});

app.delete('/api/comments/:id', function(req, res, next) {
    comments.findOne({id: req.params.id}, function(err, item) {
        if(err) return res.status(500).end(err);
        if(!item) return res.status(404).end("Comment id: " + req.params.id + " does not exist.");

        comments.remove({id: item.id}, {multi: false}, function(err, num) {
            if(err) return res.status(500).end(err);
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