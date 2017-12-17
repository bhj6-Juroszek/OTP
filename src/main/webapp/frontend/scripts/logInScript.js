try {
    var controller = $controller('signInController', [])
} catch (e) {
    var app = angular.module('myApp');
    app.controller('signInController', function ($scope, $http, userService, $window) {
        var mainAdress = 'index.html';
        $scope.logIn = function () {
            $http({
                method: 'POST',
                url: userService.getHost() + '/login',
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
                alert('Could not connect to server. Please try again later');
            });
        };
        $scope.init = function () {
            $scope.email = "";
            $scope.password = "";
            $scope.validPassword = true;
        }
    });
}

