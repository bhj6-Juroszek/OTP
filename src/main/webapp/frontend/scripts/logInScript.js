try {
    var controller = $controller('signInController', [])
} catch (e) {
    var app = angular.module('myApp');
    app.controller('signInController', function ($scope, $http, userService, $window) {
        var mainAdress = '/executable/frontend/index.html';
        $scope.logIn = function () {
            $http({
                method: 'POST',
                url: 'http://localhost:8181/login',
                data: {
                    email: this.email,
                    password: this.password
                },
                headers: {'Content-Type': 'application/json'}
            }).then(function successCallback(response) {
                $scope.loginResponse = response.data;
                if ($scope.loginResponse.responseCode === 1) {
                    userService.setUserContext($scope.loginResponse.userContext);
                    userService.setUUID($scope.loginResponse.uuid);
                    alert('Logged In succesfully on : ' + $scope.loginResponse.userContext.user.mail);
                    $window.location.href = mainAdress;
                } else if ($scope.loginResponse.responseCode === 2) {
                    alert('Wrong credentials! ');
                    init();
                }else if ($scope.loginResponse.responseCode === 3) {
                    alert('Account doesn\'t exist!');
                    init();
                } else {
                    $scope.email = "";
                    $scope.password = "";
                    $scope.validPassword = false;
                    $window.location.href = mainAdress;
                }
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
        };
        $scope.init = function () {
            $scope.email = "";
            $scope.password = "";
            $scope.validPassword = true;
        }
    });
}

