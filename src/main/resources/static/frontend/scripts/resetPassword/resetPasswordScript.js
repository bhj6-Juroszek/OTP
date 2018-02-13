app.controller('resetPasswordController', function ($scope, $http, userService, $window) {
        var mainAdress = userService.getMainAdress();
        $scope.mail = "jurbar369@gmail.com";
        $scope.resetPassword = function () {
            $http({
                method: 'POST',
                url: userService.getHost() + 'password',
                params: {"mail": $scope.mail}
            }).then(function successCallback() {
                userService.setUUID(null);
                userService.setUserContext(null);
                $.notify('Password successfully changed ', "success");
                setTimeout(function () {
                    $window.location.href = mainAdress;
                },1000)

            }, function errorCallback() {
                $.notify('Something went wrong', "error");
                setTimeout(function () {
                    $window.location.href = mainAdress;
                },1000)
            });
        };
    });

