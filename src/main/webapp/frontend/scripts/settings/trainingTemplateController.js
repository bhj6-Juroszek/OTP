app.controller('trainingTemplateController', function ($scope, $http, $window, userService) {
    $scope.templateCategories = userService.getPickableCategories();
    $scope.place = "";
    $scope.price = 0.0;
    $scope.description = "";
    $scope.capacity = 1;
    $scope.categorySelect = $scope.templateCategories[0];

    if($scope.categorySelect.theoretical) {
        $scope.notTheoreticalCategory = false;
    } else {
        $scope.notTheoreticalCategory = true;
    }
    $scope.setCurrentCategory = function () {
        if($scope.categorySelect.theoretical) {
            $scope.notTheoreticalCategory = false;
        } else {
            $scope.notTheoreticalCategory = true;
        }
    };
    var mainAdress = userService.getMainAdress();
    $scope.saveTrainingTemplate = function () {
        if ($scope.confirm()) {
            if ($scope.place === "") {
                $scope.place = "online";
            }
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
                $scope.returnCode = response.data;
                if ($scope.returnCode === 1) {
                    $.notify('Training successfully saved', "success");
                    window.location.href = "settings.html";
                }
                else if ($scope.returnCode === 4) {
                    alert('Session expired !');
                    userService.clearImportantData();
                    $window.location.href = mainAdress;
                }
                else {
                    alert('Couldn\'t find place, try again with something more general or without diacritic signs!');
                }
            }, function errorCallback(response) {
                alert('Could not connect to server. Please try again later');
            });
        }
    }
});