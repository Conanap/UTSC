var api = (function(){
    "use strict";
    var module = {};

    let photosListeners = [];
    let commentListeners = [];
    let errorListeners = [];

    function send(method, url, data, callbaack) {
        var xhr = new XMLHttpRequest();
        xhr.onload = function() {
            if(xhr.status !== 200) callback("[" + xhr.status + "]" + xhr.responseText, null);
            else callback(null, JSON.parse(xhr.responseText));
        };

        xhr.open(method, url, true);
        if (!data) xhr.send();
        else{
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.send(JSON.stringify(data));
        }
    };

    let notifyErrorListeners = function(err) {
        errorListeners.forEach(function(listener) {
            listener(err);
        });
    };

    let notifyCommentListeners = function() {
        send('GET', '/api/photos/', function(err, res) {
            if(err) notifyErrorListeners(err);
            photosListeners.forEach(function(listener) {
                listener(res);
            });
        });
    };

    let notifyImageListeners = function() {
        photosListeners.forEach(function(listener) {});
    };
    
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
    
    // add an image to the gallery
    module.addImage = function(title, author, file) {
        send('POST', '/api/photos/', {author: author, title: title}, function(err, res) {
            if(err) return notifyErrorListeners(err);
            notifyImageListeners();
        });
    };
    
    // delete an image from the gallery given its imageId
    module.deleteImage = function(imageId) {
        send("DELETE", '/api/photos/' + imageId, function(err, res) {
            if(err) return notifyErrorListeners(err);
            notifyImageListeners();
        });
    };
    
    // add a comment to an image
    module.addComment = function(imageId, author, content) {
        send('POST', '/api/comments/', {photoId: imageId, author: author, content: content},
        function(err, res) {
            if(err) return notifyErrorListeners(err);
            notifyCommentListeners();
        });
    };
    
    // delete a comment to an image
    module.deleteComment = function(commentId) {
        send('DELETE', '/api/comments/' + commentId, function(err, res) {
            if(err) return notifyErrorListeners(err);
            notifyCommentListeners();
        });
    };
    
    // register an image listener
    // to be notified when an image is added or deleted from the gallery
    module.onImageUpdate = function(listener) {
        photosListeners.push(listener);
    };
    
    // register an comment listener
    // to be notified when a comment is added or deleted to an image
    module.onCommentUpdate = function(listener) {
        commentListeners.push(listener);
    };
    
    return module;
})();