app.controller('ratesController', function ($scope, $http, $window, userService) {
    $scope.trainingsToRate = [];
    $scope.rateComment = "";
    var hideRateFields = function () {
        $scope.rateValue = $scope.allowedRates[0].name;
        $scope.rateValue = 0;
        for(var i = 0; i< $scope.trainingsToRate.length; i++ ) {
            $scope.trainingsToRate[i].visibility = false;
        }
    };
    var initializeRates = function () {
        var result = [];
        var one = {
            "name": "1/5",
            "value": 1
        };
        var two = {
            "name": "2/5",
            "value": 2
        };
        var three = {
            "name": "3/5",
            "value": 3
        };
        var four = {
            "name": "4/5",
            "value": 4
        };
        var five = {
            "name": "5/5",
            "value": 5
        };
        result.push(one);
        result.push(two);
        result.push(three);
        result.push(four);
        result.push(five);
        return result;
    };
    $scope.allowedRates = initializeRates();
    hideRateFields();
    $http({
        method: 'GET',
        url: userService.getHost() + 'trainee/getTrainingsToRate',
        params: {"uuid": userService.getUUID()}
    }).then(function successCallback(response) {
        $scope.returnCode = response.data.responseCode;
        if ($scope.returnCode === 1) {
            $scope.trainingsToRate = response.data.userTrainings;
            hideRateFields();
        } else {
            $.notify('Session expired', "error");
            setTimeout(function () {
                window.location.href = userService.getMainAdress();
            }, 1000);
        }
    }, function errorCallback(response) {
        $.notify('Could not connect to server. Please try again later', "error");
    });

    $scope.setRate = function (rating) {
        $scope.selectedRate = rating;
    };

    $scope.setVisibleRateField = function (training) {
        var finalVisibility = !training.visibility;
        hideRateFields();
        training.visibility = finalVisibility;
    };

    $scope.rate = function (training) {
        var ratingValue = 1;
        for(var i = 0; i< $scope.allowedRates.length; i++) {
            var allowedRate = $scope.allowedRates[i];
            if($scope.selectedRate === allowedRate.name) {
                ratingValue = allowedRate.value;
                break;
            }
        }
        $http({
            method: 'POST',
            url: userService.getHost() + 'trainee/rateTrainer',
            params: {"uuid": userService.getUUID()},
            data: {
                comment: $scope.ratesCtrl.rateComment,
                value: ratingValue,
                toId: training.owner.id,
                fromId: userService.getUserContext().user.id,
                date: new Date()
            }
        }).then(function successCallback(response) {
           if(response.data === 1) {
               $.notify('Your rate has been saved. Thank you for your opinion', "success");
               setTimeout(function () {
                   hideRateFields();
                   $window.location.reload();
               }, 1000);
           } else {
               $.notify('Session expired', "error");
               setTimeout(function () {
                   window.location.href = userService.getMainAdress();
               }, 1000);
           }
        }, function errorCallback(response) {
            $.notify('Could not connect to server. Please try again later', "error");
        });
    }

});