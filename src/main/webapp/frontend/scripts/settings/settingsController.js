app.controller('settingsController', function ($scope, $http, $window, userService) {
    var user = ((ctx = userService.getUserContext()) === null) ? null: ctx.user;
    if(user === null) {
        $window.location.href = userService.getMainAdress();
    }
    $scope.details = false;
    $scope.editingProfile = false;
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
    $scope.foundMatchingTrainings = false;


    $scope.accountDetails = function () {
        hideEverything();
        $scope.details = true;
    };
    $scope.editProfile = function () {
        hideEverything();
        $scope.editingProfile = true;
    };
    $scope.trainingTemplate = function () {
        hideEverything();
        $scope.trainingsTemplate = true;
    };
    $scope.addTrainings = function () {
        hideEverything();
        $scope.addTrInstanceVisibility = true;
    };

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
                    $.notify('Your account has been updated', "success");
                } else if($scope.returnCode === 5)  {
                    $.notify('Login already taken', "error");
                }
                else {
                    $.notify('Session expired', "error");
                    setTimeout(function () {
                       window.location.href = userService.getMainAdress();
                    }, 1000);
                }
            }, function errorCallback(response) {
                $.notify('Could not connect to server. Please try again later', "error");
            });
            clearDetails();
        }
    };

    $scope.profile = function () {
      window.location.href = "userProfile.html?userId="+userService.getUserContext().user.id;
    };

    $scope.confirm = function () {
        return $window.confirm('Are you sure ?');
    };

    var hideEverything = function () {
        $scope.details = false;
        $scope.trainingsTemplate = false;
        $scope.addTrInstanceVisibility = false;
        $scope.schedule = false;
        $scope.editingProfile = false;
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
                    if(!uTraining.category.theoretical) {
                        $scope.userTrainings.push(uTraining);
                    }
                }
                if(!($scope.userTrainings.length === 0)) {
                    $scope.foundMatchingTrainings = true;
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

    var clearDetails = function () {
        $scope.userNameChange = user.name;
        $scope.loginChange = user.login;
        $scope.passwordChange = "";
        $scope.adressChange = user.adress;
        $scope.imageUrl = user.imageUrl;
    };
    loadTrainings();
});
