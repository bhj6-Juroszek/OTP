var app = angular.module('myApp');
app.controller('trainingViewController', function ($scope, userService) {
    $scope.vvv = false;

    $scope.show = function () {
        $scope.vvv = !$scope.vvv;
    }
});