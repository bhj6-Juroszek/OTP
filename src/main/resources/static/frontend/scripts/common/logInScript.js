app.controller('signInController', function ($scope, $http, userService, $window) {
    var mainAdress = userService.getMainAdress();
    var holdLoginResponse = function (response) {
        $scope.loginResponse = response.data;
        if ($scope.loginResponse.responseCode === 1) {
            userService.setUserContext($scope.loginResponse.userContext);
            userService.setUUID($scope.loginResponse.uuid);
            var message = 'Logged In succesfully on : ' + $scope.loginResponse.userContext.user.mail;
            $.notify(message, "success");
            setTimeout(function () {
                $window.location.href = mainAdress;
            }, 500);

        } else if ($scope.loginResponse.responseCode === 2) {
            $.notify('Wrong credentials! ', "warn");
            init();
        } else if ($scope.loginResponse.responseCode === 3) {
            $.notify('Account doesn\'t exist!', "error");
            init();
        } else {
            init();
            $window.location.href = mainAdress;
        }
    };

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
            holdLoginResponse(response)
        }, function errorCallback(response) {
            $.notify('Could not connect to server. Please try again later', "error");
        });
    };

    $scope.init = function () {
        $scope.email = "";
        $scope.password = "";
        $scope.validPassword = true;
    };
});

