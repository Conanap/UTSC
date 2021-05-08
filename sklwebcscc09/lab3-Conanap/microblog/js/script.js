(function() {
    "use strict";

    // these are defs, don't require window to finish loading first
    var msgC = 0;

    var updateUserStorage = function(key, obj) {
        if(msgC <= obj.msgnum) {
            localStorage.setItem('msgC', obj.msgnum);
        }

        localStorage.setItem('msg' + obj.msgnum, JSON.stringify(obj));
    };

    var addMsg = function(msgnum, content, username, updoots, downdoots) {
        var ilike = function(ele) {
            ele.innerHTML = ++updoots;
        };

        var ihate = function (ele) {
            ele.innerHTML = ++downdoots;
        };

        var rm = function (ele) {
            ele.remove();
        };

        var wrapper = function(id, f) {
            var obj = document.getElementById(id);
            return function(e) {
                e.preventDefault();
                f(obj);
            };
        };

        // create a new message element
        var elmt = document.createElement('div');
        elmt.className = "message";
        elmt.id = "msg" + msgnum;
        elmt.innerHTML=`
            <div class="message_user">
                <img class="message_picture" src="media/user.png" alt="${username}">
                <div class="message_username">${username}</div>
            </div>
            <div class="message_content">${content}</div>
            <div class="upvote-icon icon" id="up${msgnum}">${updoots}</div>
            <div class="downvote-icon icon" id="down${msgnum}">${downdoots}</div>
            <div class="delete-icon icon" id="del${msgnum}"></div>
        `;
        // add this element to the document
        document.getElementById("messages").prepend(elmt);

        document.getElementById("up" + msgnum).addEventListener('click', wrapper("up" + msgnum, ilike));
        document.getElementById("down" + msgnum).addEventListener('click', wrapper("down" + msgnum, ihate));
        document.getElementById("del" + msgnum).addEventListener('click', wrapper("msg" + msgnum, rm));
    };

    var loadLocMsg = function() {
        msgC = localStorage.getItem('msgC') || 0;
        var msg;

        for(var msgnum in msgC) { // for ea key
            msg = JSON.parse(localStorage.getItem('msg' + msgnum));
            addMsg(msg.msgnum, msg.msg, msg.username, msg.updoots, msg.downdoots);
        }

        return msgC;
    };

    window.onload = function() {
        var msgnum = loadLocMsg();

        // message posting
        document.getElementById('create_message_form').addEventListener('submit', function(e){
            // prevent from refreshing the page on submit
            e.preventDefault();

            // read form elements
            var username = document.getElementById("post_name").value;
            var content = document.getElementById("post_content").value;
            var updoots = 0;
            var downdoots = 0;
            var saveobj = {};

            // clean form
            document.getElementById("create_message_form").reset();
            // add it
            addMsg(msgnum, content, username, updoots, downdoots);

            saveobj = {
                'msgnum': msgnum,
                'msg': content,
                'user': username,
                'updoots': updoots,
                'downdoots': downdoots
            };

            updateUserStorage(username, saveobj);

            msgnum++;
        });
    };
}());
