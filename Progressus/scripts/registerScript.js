var app = angular.module('myApp', []);
app.controller('myAppController', function($scope, $http) {
    $scope.email = "jo";
    $scope.password = "";
    $scope.register = function() {
       var result= $http({
            method: 'POST',
            url: 'http://localhost:8181/register',
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
