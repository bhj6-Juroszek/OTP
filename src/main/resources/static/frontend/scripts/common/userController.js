app.controller('userController', function ($scope, $http, userService) {
    var start = $scope.init = function () {
        $scope.authData = false;
        if (userService.getUserContext() !== null) {
            $scope.userInfoText = userService.getUserContext().user.mail;
            $scope.authData = true;
        } else {
            $scope.authData = false;
        }

    };
    start();
    $scope.goToScheduleAsOwner = userService.goToScheduleAsOwner;
    $scope.profile = function () {
        window.location.href = "userProfile.html?userId="+userService.getUserContext().user.id;
    };
});