app.controller('editTrainingsController', function ($scope, $http, $window, userService) {

    $scope.userTrainings = [];

    var hideEverything = function () {
        for(var i = 0; i < $scope.userTrainings.length; i++) {
            $scope.userTrainings[i].visibility = false;
        }
    };

    $scope.setVisibleDetailsChange = function (training) {
       var visibility = !training.visibility;
       hideEverything();
       training.visibility = visibility;
    };


    $scope.updateTemplate = function (trainingTemplate) {
        $http({
            method: 'POST',
            url: userService.getHost() + 'trainingTemplate',
            params: {"uuid": userService.getUUID(),
            priceForHour: trainingTemplate.price,
            description: trainingTemplate.description,
            details: trainingTemplate.details,
            trainingId: trainingTemplate.id}
        }).then(function successCallback(response) {
            if(response.data === 1) {
                $.notify('Changes has been saved', "success");
                setTimeout(function () {
                    $window.location.reload();
                }, 1000);
            } else {
                $.notify('Session expired', "error");
                setTimeout(function () {
                    window.location.href = userService.getMainAdress();
                }, 1000);
            }
        }, function errorCallback(response) {
            $.notify('Could not connect to server. Please try again later', "error");
        });
    };

    $http({
        method: 'GET',
        url: userService.getHost() + 'trainings',
        params: {"uuid": userService.getUUID()}
    }).then(function successCallback(response) {
        if(response.data.responseCode === 1) {
            $scope.userTrainings = response.data.userTrainings;
        } else {
            $.notify('Session expired', "error");
            setTimeout(function () {
                window.location.href = userService.getMainAdress();
            }, 1000);
        }
    }, function errorCallback(response) {
        $.notify('Could not connect to server. Please try again later', "error");
    });
});