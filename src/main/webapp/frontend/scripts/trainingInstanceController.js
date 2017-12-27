var app = angular.module('myApp');
app.controller('trainingInstanceController', function ($scope, $http, $window, userService) {
    var mainAdress = userService.getMainAdress();
    $scope.date;
    $scope.hours;
    $scope.duration;
    $scope.userTrainingSelect;


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
                var a = 0;
            }, function errorCallback(response) {
                alert('Could not connect to server. Please try again later');
            });
        }
    }
});