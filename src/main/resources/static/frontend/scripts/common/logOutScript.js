app.controller('logOutController', function ($scope, $http, userService) {
        var mainAdress = userService.getMainAdress();
        var uuid = userService.getUUID();
        $scope.logOut = function () {
            $http({
                method: 'POST',
                url: userService.getHost() + '/logout',
                params: {"uuid": uuid}
            }).then(function successCallback() {
                userService.setUUID(null);
                userService.setUserContext(null);
                $.notify('You have been loged out', "info");
                setTimeout(function () {
                    window.location.href = mainAdress;
                }, 500);

            }, function errorCallback() {
                userService.clearImportantData();
                $.notify('Session expired', "error");
                setTimeout(function () {
                    window.location.href = mainAdress;
                }, 500);
            });
        };
    });


