try {
    var controller = $controller('signInController')
} catch (e) {
    var app = angular.module('myApp');
    app.controller('signInController', function ($scope, $http, commonService) {

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
                    commonService.setUserContext($scope.loginResponse.userContext);
                    alert('Logged In succesfully on : ' + $scope.loginResponse.userContext.user.mail);
                    window.location.href = '/INZ/executable/frontend/index.html';
                } else {
                    $scope.email = "";
                    $scope.password = "";
                    $scope.validPassword = false;
                    $window.location.href = '/index.html';
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

