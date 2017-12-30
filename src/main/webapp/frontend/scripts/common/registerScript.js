var app = angular.module('myApp');
    app.controller('myAppController', function ($scope, $http, $window, userService) {
        $scope.email = "jo";
        $scope.password = "";
        $scope.passwordConfirm = "";
        $scope.error = "";
        $scope.returnCode = -100;
        var mainAdress = userService.getMainAdress();
        $scope.register = function () {
            $http({
                method: 'POST',
                url: userService.getHost() + '/register',
                data: {
                    email: this.email,
                    password: this.password
                },
                headers: {'Content-Type': 'application/json'}
            }).then(function successCallback(response) {
                $scope.returnCode = response.data;
                if ($scope.returnCode === 1) {
                    alert('Your account has been succesfully registered. Check your email to confirm registration.');
                    $window.location.href = mainAdress;
                }
                else if ($scope.returnCode === 5) {
                    alert('This mail is already registered!');
                }
                else {
                    alert('Something went wrong. Try again later');
                }
            }, function errorCallback(response) {
                alert('Could not connect to server. Please try again later');
            });

        };

        $scope.init = function () {
            $scope.email = "";
            $scope.password = "";
            $scope.passwordConfirm = "";
            $scope.mailExists = false;
        }
    });
