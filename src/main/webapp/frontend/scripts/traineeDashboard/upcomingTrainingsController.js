app.controller('upcomingTrainingsController', function ($scope, $http, $window, userService) {

    $scope.upcomingTrainings = [];
    $http({
        method: 'GET',
        url: userService.getHost() + 'trainee/getUpcomingTrainings',
        params: {"uuid": userService.getUUID()}
    }).then(function successCallback(response) {
        if(response.data.responseCode === 1) {
            var responseTrainings = response.data.userTrainings;
            for(var i = 0; i< responseTrainings.length; i++) {
                var responseTraining = responseTrainings[i];
                var responseTrainingInstances = responseTraining.instances;
                for(var y = 0; y< responseTrainingInstances.length; y++) {
                    var responseTrainingInstance = responseTrainingInstances[y];
                    responseTrainingInstance.description = responseTraining.description;
                    responseTrainingInstance.owner = responseTraining.owner;
                    responseTrainingInstance.category = responseTraining.category;
                    responseTrainingInstance.dateStart = new Date(responseTrainingInstance.dateStart);
                    responseTrainingInstance.dateStartHumanForm = responseTrainingInstance.dateStart.toISOString().substring(0, 10) + " " + responseTrainingInstance.dateStart.toISOString().substring(11, 19);
                    responseTrainingInstance.dateEnd = new Date(responseTrainingInstance.dateEnd);
                    responseTrainingInstance.place = responseTraining.place;
                    if(!responseTrainingInstance.category.theoretical) {
                        $scope.upcomingTrainings.push(responseTrainingInstance);
                    }
                }
            }
        } else {
            $.notify('Session expired', "error");
            setTimeout(function () {
                window.location.href = userService.getMainAdress();
            }, 1000);
        }
    }, function errorCallback(response) {
        $.notify('Could not connect to server. Please try again later', "error");
    });
});