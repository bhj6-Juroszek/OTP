try {
    getUserContext().user;
} catch (e) {
    var app = angular.module('myApp', []);
    app.factory('userService', function () {

        return {
            getUserContext: getUserContext,
            setUserContext: setUserContext,
            getUUID: getUUID,
            setUUID: setUUID
        };

        // .................

        function getUserContext() {
            if(!sessionStorage['user']) {
                return null;
            }
            return JSON.parse(sessionStorage.getItem("user"));
        }

        function setUserContext(value) {
            sessionStorage.setItem("user", JSON.stringify(value))
        }

        function getUUID() {
            if(!sessionStorage['UUID']) {
                return null;
            }
            return sessionStorage.getItem("UUID");
        }

        function setUUID(value) {
            sessionStorage.setItem("UUID", value)
        }
    });
}
