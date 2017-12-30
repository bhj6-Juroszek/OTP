try {
    var controller = $controller('resetPasswordController')
} catch (e) {
    var app = angular.module('myApp');
    app.controller('resetPasswordController', function ($scope, $http, userService, $window) {
        var mainAdress = userService.getMainAdress();
        $scope.mail = "jurbar369@gmail.com";
        $scope.resetPassword = function () {
            $http({
                method: 'GET',
                url: userService.getHost() + 'resetPassword',
                params: {"mail": $scope.mail}
            }).then(function successCallback() {
                userService.setUUID(null);
                userService.setUserContext(null);
                alert('Password successfully changed ');
                $window.location.href = mainAdress;
            }, function errorCallback() {
                alert('Something went wrong');
                $window.location.href = mainAdress;
            });
        };
    });
}

