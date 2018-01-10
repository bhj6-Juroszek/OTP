app.controller('trainingInstanceController', function ($scope, $http, $window, userService) {
    $scope.date;
    $scope.hours;
    $scope.duration;
    $scope.foundMatchingTrainings = false;


    var prepareDate = function () {
        var b = Date.parse($scope.date.toString());
        var c = Date.parse($scope.hours.toString());
        var trDate = Date.parse(new Date(b+c));
        return trDate;
    };

    $scope.saveTrainingInstance = function () {
        if ($scope.confirm()) {

            $http({
                method: 'POST',
                url: userService.getHost() + 'saveTrainingInstance',
                params: {
                    "uuid": userService.getUUID(),
                    "date": prepareDate(),
                    "duration": $scope.duration,
                    "trainingTemplate":$scope.userTrainingSelect.id
                },
                headers: {'Content-Type': 'application/json'}
            }).then(function successCallback(response) {
                if(response.data === 1) {
                    $.notify('Training instance successfully saved', "success");
                    window.location.href = "settings.html";
                } else {
                    setTimeout(function () {
                        window.location.href = userService.getMainAdress()
                    },1000);
                }

            }, function errorCallback(response) {
                $.notify('Could not connect to server. Please try again later', "error");
            });
        }
    }
});