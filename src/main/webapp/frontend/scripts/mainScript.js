try {
    var controller = $controller('mainController')
} catch (e) {
    var app = angular.module('myApp');
    app.controller('mainController', function ($scope, $http, commonService) {

        $scope.init = function () {
            if (commonService.getUserContext() != null) {
                $scope.userInfoText = '&quot;asdYou are logged in as: &quot;asd' + commonService.getUserContext().user.email;
                $scope.userInfo = '<li><a class=&quot;asdbtn&quot;asd href=&quot;asdlogIn.html&quot;asd>' + userInfoText + '</a></li>'
            } else {
                $scope.userInfo = '<li><a class=&quot;asdbtn&quot;asd href=&quot;asdlogIn.html&quot;asd>LOG IN</a></li>'
            }

        }
    });
}