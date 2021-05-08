let api = (function () {
    let module = {};

    let errorListeners = [];
    let photosListeners = [];
    let commentListeners = [];
    let userListeners = [];


    function sendFiles(method, url, data, callback) {
        let formdata = new FormData();
        Object.keys(data).forEach(function (key) {
            let value = data[key];
            formdata.append(key, value);
        });
        let xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.status !== 200) callback("[" + xhr.status + "]" + xhr.responseText, null);
            else callback(null, JSON.parse(xhr.responseText));
        };
        xhr.open(method, url, true);
        xhr.send(formdata);
    };

    function send(method, url, data, callback) {
        var xhr = new XMLHttpRequest();
        xhr.onload = function () {
            if (xhr.status !== 200) callback("[" + xhr.status + "]" + xhr.responseText, null);
            else callback(null, JSON.parse(xhr.responseText));
        };

        xhr.open(method, url, true);
        if (!data) xhr.send();
        else {
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.send(JSON.stringify(data));
        }
    };

    let notifyErrorListeners = function (err) {
        errorListeners.forEach(function (listener) {
            listener(err);
        });
    };

    let notifyCommentListeners = function (items) {
        commentListeners.forEach(function (listener) {
            listener(items);
        });
    };

    let notifyImageListeners = function (item, imgList, usrList) {
        photosListeners.forEach(function (listener) {
            listener(item, imgList, usrList);
        });
    };

    let notifyUsers = function (username) {
        userListeners.forEach(function (listener) {
            listener(username);
        });
    };

    // yes I know this is a bad design because we're letting them get a specific thing before letting them know what's there
    // but it's okay, only 1 perosn is using it anyways.
    module.updateListeners = function (author, currId, toUpdate, cmtPage = 0) {
        send('GET', '/api/posts/' + author + '/' + (currId || ''), null, function (err, res) {
            if (err) return notifyErrorListeners(err);

            let item = toUpdate || (currId ? res : res[0]);

            send('GET', '/api/posts/' + author + '/postIds/', null, function (err, res) {
                let imgList = res;

                send('GET', '/api/users/userList/', null, function (err, res) {
                    let usrList = res;
                    send('GET', '/api/comments/' + author + '/' + item.id + '?page=' + cmtPage, null, function (err, res) {
                        if (err) return notifyErrorListeners(err);
                        notifyImageListeners(item, imgList || [], usrList || []);
                        notifyCommentListeners(res);
                    });
                });
            });
        });
    };

    // register an image listener
    // to be notified when an image is added or deleted from the gallery
    module.onImageUpdate = function (listener) {
        photosListeners.push(listener);
    };

    // register an comment listener
    // to be notified when a comment is added or deleted to an image
    module.onCommentUpdate = function (listener) {
        commentListeners.push(listener);
    };

    // register error listener
    // to be notified when error occurs you feel
    module.onError = function (callback) {
        errorListeners.push(callback);
    };

    // register user update boi
    module.onUserUpdate = function (callback) {
        userListeners.push(callback);
    }

    module.onLoad = function (userId = null, id = null, cmtPage = 0) {
        if (!userId) userId = api.getUsername();
        module.updateListeners(userId, id, null, cmtPage);
    };

    let getUsername = function () {
        return document.cookie.replace(/(?:(?:^|.*;\s*)username\s*\=\s*([^;]*).*$)|^.*$/, "$1") || undefined;
    }

    module.getUsername = getUsername;

    /*  ******* Data types *******
        image objects must have at least the following attributes:
            - (String) _id 
            - (String) title
            - (String) author
            - (Date) date
    
        comment objects must have the following attributes
            - (String) _id
            - (String) imageId
            - (String) author
            - (String) content
            - (Date) date
    
    ****************************** */

    module.signup = function (username, password) {
        send('POST', '/api/signup', { username: username, password: password }, function (err, res) {
            if (err) return notifyErrorListeners(err);
            notifyUsers(getUsername());
        });
    };

    module.signin = function (username, password) {
        send('POST', '/api/signin', { username: username, password: password }, function (err, res) {
            if (err) return notifyErrorListeners(err);
            notifyUsers(getUsername());
        });
    };

    module.signout = function () {
        send('GET', '/api/signout', function (err) {
            if (err) return notifyErrorListeners(err);
            notifyUsers(getUsername());
        });
    };

    // add an image to the gallery
    module.addImage = function (title, author, file) {
        sendFiles('POST', '/api/posts/', { author: author, title: title, file: file }, function (err, res) {
            if (err) return notifyErrorListeners(err);
            module.updateListeners(author, null, res);
        });
    };

    // delete an image from the gallery given its imageId
    module.deleteImage = function (author, imageId) {
        send("DELETE", '/api/posts/' + author + '/' + imageId, null, function (err, res) {
            if (err) return notifyErrorListeners(err);
            module.updateListeners(author, null, null);
        });
    };

    // add a comment to an image
    module.addComment = function (imageId, author, content) {
        send('POST', '/api/comments/', { photoId: imageId, author: author, content: content },
            function (err, res) {
                if (err) return notifyErrorListeners(err);
                module.updateListeners(res.imgAuth, res.photoId, null);
            });
    };

    // delete a comment to an image
    module.deleteComment = function (commentId) {
        send('DELETE', '/api/comments/' + commentId, null, function (err, res) {
            if (err) return notifyErrorListeners(err);
            module.updateListeners(res.imgAuth, res.photoId, null);
        });
    };

    module.getUserList = function(callback) {
        send('GET', '/api/users/userList/', null, function (err, res) {
            if (err) return notifyErrorListeners(err);
            callback(res);
        })
    };

    return module;
})();