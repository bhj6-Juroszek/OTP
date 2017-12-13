try {
    var controller = $controller('logOutController', [])
} catch (e) {
    var app = angular.module('myApp');
    app.controller('logOutController', function ($scope, $http, userService, $window) {
        var mainAdress = userService.getMainAdress();
        var uuid = userService.getUUID();
        $scope.logOut = function () {
            $http({
                method: 'GET',
                url: userService.getHost() + '/logout',
                params: {"uuid": uuid}
            }).then(function successCallback() {
                userService.setUUID(null);
                userService.setUserContext(null);
                alert('You have been loged out');
                window.location.href = mainAdress;
            }, function errorCallback() {
                userService.setUUID(null);
                userService.setUserContext(null);
                alert('Session expired');
                window.location.href = mainAdress;
            });
        };
    });
}

