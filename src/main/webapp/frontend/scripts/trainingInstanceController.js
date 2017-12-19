var app = angular.module('myApp');
app.controller('trainingInstanceController', function ($scope, $http, $window, userService) {
    var mainAdress = userService.getMainAdress();
    $scope.saveTrainingInstance = function () {
        if ($scope.confirm()) {

            $http({
                method: 'POST',
                url: userService.getHost() + 'saveTrainingTemplate',
                params: {
                    "uuid": userService.getUUID(),
                    "placeName": $scope.place,
                    "categoryId": $scope.categorySelect.id,
                    "price": $scope.price,
                    "description": $scope.description,
                    "capacity": $scope.capacity
                },
                headers: {'Content-Type': 'application/json'}
            }).then(function successCallback(response) {
            }, function errorCallback(response) {
                alert('Could not connect to server. Please try again later');
            });
        }
    }
});