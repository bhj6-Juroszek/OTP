app.controller('manageReservationsController', function ($scope, $http, $window, userService) {

    $scope.reservationsUnconfirmed = [];
        $http({
            method: 'GET',
            url: userService.getHost() + 'unconfirmedReservations',
            params: {"uuid": userService.getUUID()}
        }).then(function successCallback(response) {
            if(response.data.responseCode === 1) {
                var trainingsList = response.data.userTrainings;
                for(var i = 0; i< trainingsList.length; i++) {
                    var training = trainingsList[i];
                    var trainingsInstancesList = training.instances;
                    for(var y = 0; y< trainingsInstancesList.length; y++) {
                        var trainingInstance = trainingsInstancesList[y];
                        var trainingReservations = trainingInstance.trainingReservations;
                        for(var x = 0; x< trainingReservations.length; x++) {
                            var unconfirmedReservation = trainingReservations[x];
                            trainingInstance.dateStart = new Date(trainingInstance.dateStart);
                            unconfirmedReservation.startDate = trainingInstance.dateStart.toISOString().substring(0, 10) + " " + trainingInstance.dateStart.toISOString().substring(11, 19);
                            unconfirmedReservation.category = training.category;
                            $scope.reservationsUnconfirmed.push(unconfirmedReservation);
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

    var confirm = function () {
        return $window.confirm('Are you sure ?');
    };

   $scope.confirmReservation = function (reservation) {
    if(confirm()) {
        $http({
            method: 'POST',
            url: userService.getHost() + '/reservation/confirmation',
            params: {"uuid": userService.getUUID(),
                reservationId: reservation.id}
        }).then(function successCallback(response) {
            if(response.data === 1) {
                $.notify('Reservation confirmed', "success");
                setTimeout(function () {
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
}

});