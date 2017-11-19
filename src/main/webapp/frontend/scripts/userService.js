try {
    angular.module('myApp');
}
catch(e) {
    var app = angular.module('myApp', []);
    app.factory('commonService', [function ($scope, $http) {

        var userContext;

        return {
            getUserContext: getUserContext,
            setUserContext: setUserContext
        };

        // .................

        function getUserContext() {
            return userContext;
        }

        function setUserContext(value) {
            userContext = value;
        }
    }]);
}