let api = (function(){
    let module = {};
    
    /*  ******* Data types *******
        message objects must have at least the following attributes:
            - (String) messageId 
            - (String) author
            - (String) content
            - (Int) upvote
            - (Int) downvote 
    
    ****************************** */ 
    
    // add a message
    module.addMessage = function(author, content){

    }
    
    // delete a message given its messageId
    module.deleteMessage = function(messageId){
        
    }
    
    // return a set of 5 messages using pagination
    // page=0 returns the 5 latest messages
    // page=1 the 5 following ones and so on
    module.getMessages = function(page=0){
        
    }
    
    // upvote a message given its messageId
    module.upvoteMessage = function(messageId){
        
    }
    
    
    // downvote a message given its messageId
    module.downvoteMessage = function(messageId){
        
    }
    
    // register a message listener 
    // to be notified when a message is added or deleted
    module.onMessageUpdate = function(listener){
        
    }
    
    // register a vote listener
    // to be notified when a message is upvoted or downvoted
    module.onVoteUpdate = function(listener){
        
    }
    
    return module;
})();