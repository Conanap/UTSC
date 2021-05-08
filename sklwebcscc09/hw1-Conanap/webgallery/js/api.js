let api = (function(){
    let module = {};

    let imageListeners = [];
    let commentListeners = [];
    
    /*  ******* Data types *******
        image objects must have at least the following attributes:
            - (String) imageId 
            - (String) title
            - (String) author
            - (String) url
            - (Date) date
    
        comment objects must have the following attributes
            - (String) commentId
            - (String) imageId
            - (String) author
            - (String) content
            - (Date) date
    
    ****************************** */ 

    function getItemIndexById(items, id, isCmt) {
        for(let i = 0; i < items.length; i++) {
            if((isCmt ? items[i].commentId : items[i].imageId) === id) {
                return i;
            }
        }

        return -1;
    };

    function refreshComments(imgs, cmtPage=0) {
        for(let i = 0; i < imgs.length; i++) {
            notifyListeners(commentListeners, module.getComments(imgs[i].imageId, cmtPage), imgs[i].imageId);
        }
    };

    // add an image to the gallery
    module.addImage = function(title, author, url){
        let imgs = JSON.parse(localStorage.getItem('imgs')) || [];
        let dateObj = new Date();
        let date = dateObj.getDate() + "-" + (dateObj.getMonth() + 1) + "-" + dateObj.getFullYear();
        let obj = {
            'imageId': imgs.length ? parseInt(imgs[imgs.length - 1].imageId) + 1 : 0,
            'title': title,
            'author': author,
            'url': url,
            'date': date
        };

        imgs.push(obj);
        localStorage.setItem('imgs', JSON.stringify(imgs));
        notifyListeners(imageListeners, imgs);
        refreshComments(imgs);
    };
    
    // delete an image from the gallery given its imageId
    module.deleteImage = function(imageId){
        let imgs = JSON.parse(localStorage.getItem('imgs')) || [];
        let index = getItemIndexById(imgs, imageId);
        if(index === -1)
            return;

        imgs.splice(index, 1);
        // delete corresponding comments
        let cmtMap = JSON.parse(localStorage.getItem('cmtMap')) || {};
        for(let i in cmtMap) {
            if(cmtMap[i] == imageId) {
                module.deleteComment(i);
            }
        }

        localStorage.setItem('imgs', JSON.stringify(imgs));
        notifyListeners(imageListeners, imgs);
        refreshComments(imgs);
    };
    
    // get an image from the gallery given its imageId
    module.egetImage = function(imageId){
        let imgs = JSON.parse(localStorage.getItem('imgs')) || [];
        let index = getItemIndexById(imgs, imageId);

        if(index === -1)
            return null;

        return imgs[i];
    };
    
    // add a comment to an image
    module.addComment = function(imageId, author, content){
        let cmts = JSON.parse(localStorage.getItem('cmts')) || {};
        let cmtMap = JSON.parse(localStorage.getItem('cmtMap')) || {};
        let dateObj = new Date();
        let date = dateObj.getDate() + "-" + (dateObj.getMonth() + 1) + "-" + dateObj.getYear();

        if(!cmts[imageId]) {
            cmts[imageId] = [];
        }

        let obj = {
            'imageId': imageId,
            'commentId': imageId + 'cmt' + cmts[imageId].length,
            'content': content,
            'author': author,
            'date': date
        };

        cmts[imageId].push(obj);
        cmtMap[obj.commentId] = imageId;

        localStorage.setItem('cmts', JSON.stringify(cmts));
        localStorage.setItem('cmtMap', JSON.stringify(cmtMap));
        notifyListeners(commentListeners, module.getComments(imageId), imageId);
    };
    
    // delete a comment to an image
    module.deleteComment = function(commentId){
        let cmts = JSON.parse(localStorage.getItem('cmts')) || {};
        let cmtMap = JSON.parse(localStorage.getItem('cmtMap')) || {};
        let imageId = cmtMap[commentId];
        if(imageId === undefined)
            return;
        let index = getItemIndexById(cmts[imageId], commentId, true);
        if(index === -1)
            return;

        cmts[imageId].splice(index, 1);
        delete cmtMap[commentId];
        localStorage.setItem('cmts', JSON.stringify(cmts));
        localStorage.setItem('cmtMap', JSON.stringify(cmtMap));
        notifyListeners(commentListeners, module.getComments(imageId), imageId);
    };
    
    // return a set of 10 comments using pagination
    // page=0 returns the 10 latest messages
    // page=1 the 10 following ones and so on
    module.getComments = function(imageId, page=0){
        let cmts = (JSON.parse(localStorage.getItem('cmts')) || {})[imageId];
        if(!cmts)
            return;

        let imgCmts = [];
        let maxIndex = cmts.length - 1;

        for(let i = 0; i < page && maxIndex - 10 >= 0; i++) {
            maxIndex -= 10;
        }

        for(let i = maxIndex; i >= 0 && imgCmts.length < 10; i--) {
            if(parseInt(cmts[i].imageId) === parseInt(imageId)) {
                imgCmts.unshift(cmts[i]);
            }
        }
        return imgCmts;
    };
    
    // register an image listener
    // to be notified when an image is added or deleted from the gallery
    module.onImageUpdate = function(listener){
        imageListeners.push(listener);
    };
    
    // register an comment listener
    // to be notified when a comment is added or deleted to an image
    module.onCommentUpdate = function(listener){
        commentListeners.push(listener);
    };

    function notifyListeners(listeners, items, imageId=undefined) {
        for(let i = 0; i < listeners.length; i++) {
            if(imageId != undefined)
                listeners[i](items, imageId);
            else
                listeners[i](items);
        }
    };

    module.onLoad = function(cmtPage=0) {
        let imgs = JSON.parse(localStorage.getItem('imgs')) || [];
        if(imgs.length > 0) {
            notifyListeners(imageListeners, imgs);
            refreshComments(imgs, cmtPage);
        }
    };

    module.getTotNumCmtPages = function (imageId) {
        let cmts = JSON.parse(localStorage.getItem('cmts') || {})[imageId];
        if(!cmts)
            return 0;

        return Math.ceil((cmts.length + 1)/10);
    }
    
    return module;
})();