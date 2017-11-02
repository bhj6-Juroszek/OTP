var app = angular.module('signIn', []);
app.controller('signInController', function($scope, $http) {
    $scope.email = "bhj6";
    $scope.password = "asd";
    $scope.logIn = function() {
        var result= $http({
            method: 'POST',
            url: 'http://localhost:8181/login',
            data: {
                email:this.email,
                password:this.password
            },
            headers: {'Content-Type': 'application/json'}
        }).then(function successCallback(response) {
            $scope.email = response.data;
        }, function errorCallback(response) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });
    };
});
