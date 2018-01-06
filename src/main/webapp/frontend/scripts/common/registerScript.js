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
                    $.notify('Your account has been succesfully registered. Check your email to confirm registration.', "success");
                    setTimeout(function () {
                        $window.location.href = mainAdress;
                    },500)

                }
                else if ($scope.returnCode === 5) {
                    $.notify('This mail is already registered!', "error");
                }
                else {
                    $.notify('Something went wrong. Try again later', "error");
                }
            }, function errorCallback(response) {
                $.notify('Could not connect to server. Please try again later', "error");
            });

        };

        $scope.init = function () {
            $scope.email = "";
            $scope.password = "";
            $scope.passwordConfirm = "";
            $scope.mailExists = false;
        }
    });
