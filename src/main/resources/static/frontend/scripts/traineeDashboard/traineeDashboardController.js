app.controller('traineeDashboardController', function ($scope) {

    $scope.rateComment = "Your Comment here.";
    var hideEverything = function () {
        $scope.ratesDashboardShow = false;
        $scope.upcomingTrainingsShow = false;
        $scope.materialsShow = false;
    };

    hideEverything();

    $scope.rates = function () {
        hideEverything();
        $scope.ratesDashboardShow = true;
    };

    $scope.upcomingTrainings = function () {
      hideEverything();
        $scope.upcomingTrainingsShow = true;
    };

    $scope.materials = function () {
        hideEverything();
        $scope.materialsShow = true;
    };

});