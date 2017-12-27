var app = angular.module('myApp');
app.controller('settingsController', function ($scope, $http, $window, userService) {
    var user = ((ctx = userService.getUserContext()) === null) ? null: ctx.user;
    if(user === null) {
        $window.location.href = userService.getMainAdress();
    }
    $scope.details = false;
    $scope.trainingsTemplate = false;
    $scope.addTrInstanceVisibility = false;
    $scope.schedule = false;
    $scope.userNameChange = user.name;
    $scope.loginChange = user.login;
    $scope.passwordChange;
    $scope.adressChange = user.adress;
    $scope.imageUrl = user.imageUrl;
    $scope.userTrainings = [];
    $scope.userTrainingsInstances = [];


    $scope.accountDetails = function () {
        hideEverything();
        $scope.details = true;
    };
    $scope.trainingTemplate = function () {
        hideEverything();
        $scope.trainingsTemplate = true;
    };
    $scope.addTrainings = function () {
        hideEverything();
        $scope.addTrInstanceVisibility = true;
    };

    var hideEverything = function () {
        $scope.details = false;
        $scope.trainingsTemplate = false;
        $scope.addTrInstanceVisibility = false;
        $scope.schedule = false;
    };

    var loadTrainings = function () {
        $http({
            method: 'GET',
            url: userService.getHost() + 'getUserTrainings',
            params: {"uuid": userService.getUUID()}
        }).then(function successCallback(response) {
            if (response.data.responseCode === 1) {
                    for(var i = 0; i < response.data.userTrainings.length; i++) {
                        var uTraining = response.data.userTrainings[i];
                        $scope.userTrainings.push(uTraining);
                    }
                    $scope.userTrainingsInstances = [];
            } else {
                return [];
            }
            //TODO finish
        }, function errorCallback(response) {
            return [];
        });
    };
    loadTrainings();

    $scope.changeDetailsSubmit = function () {
        if ($scope.confirm()) {
            user.name = $scope.userNameChange;
            user.login = $scope.loginChange;
            user.adress = $scope.adressChange;
            user.imageUrl = $scope.imageUrl;
            if ($scope.passwordChange !== "") {
                user.password = $scope.passwordChange;
            } else {
                user.password = "";
            }
            $http({
                method: 'POST',
                url: userService.getHost() + 'changeDetails',
                params: {"uuid": userService.getUUID()},
                data: JSON.stringify(user)
                ,
                headers: {'Content-Type': 'application/json'}
            }).then(function successCallback(response) {
                $scope.returnCode = response.data;
                if ($scope.returnCode === 1) {
                    alert('Your account has been updated');
                }
                //TODO finish
            }, function errorCallback(response) {
                alert('Could not connect to server. Please try again later');
            });
            clearDetails();
        }
    };

    var clearDetails = function () {
        $scope.userNameChange = user.name;
        $scope.loginChange = user.login;
        $scope.passwordChange = "";
        $scope.adressChange = user.adress;
        $scope.imageUrl = user.imageUrl;
    };

    $scope.confirm = function () {
        return $window.confirm('Are you sure ?');
    };
});
