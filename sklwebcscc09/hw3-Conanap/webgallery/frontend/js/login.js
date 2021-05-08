(function () {
    "use strict";
    // this file mostly taken from thierry's code
    let uhoh = function (err) {
        document.querySelector('#error_box').innerHTML = err;
    }

    window.addEventListener('load', function () {
        document.querySelector('#signin').addEventListener('click', function (e) {
            let username = document.querySelector('form [name=username]').value;
            let password = document.querySelector('form [name=password]').value;

            api.signin(username, password);
        });

        document.querySelector('#signup').addEventListener('click', function (e) {
            let username = document.querySelector('form [name=username]').value;
            let password = document.querySelector('form [name=password]').value;

            api.signup(username, password);
        });

        document.querySelector('form').addEventListener('submit', function (e) {
            e.preventDefault();
        });

        api.onError(uhoh);

        api.onUserUpdate(function (username) {
            if (username) window.location.href = '/';
        });
    });
})();