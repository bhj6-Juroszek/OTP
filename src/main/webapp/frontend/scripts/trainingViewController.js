try {
    var controller = $controller('trainingViewController', [])
} catch (e) {
    try {
        var app = angular.module('myApp');
        app.controller('trainingViewController', function ($scope, userService, $window) {
            $scope.test = false;

            $scope.show = function () {
                $scope.test = true;
            }
        });
    } catch (e) {
        var a=0;
    }
}