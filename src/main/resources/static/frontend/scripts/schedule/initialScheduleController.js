var app = angular.module('myApp');
app.controller('initialScheduleController', function ($scope, userService, $http, $sce, $window) {
    $scope.currentDate = new Date();
    $scope.showModal = false;
    var halfHourBasic = 96.25;
    var dayWidth = 14.28;
    var secondBasic = (halfHourBasic / 30) / 60;
    var params = {};
    $scope.scheduleWeek = [];
    $scope.scheduleWeekHtml = [];
    $scope.scheduleTrainings = [];
    $scope.scheduleTrainingsToShow = [];
    $scope.trainingMaterials = [];
    $scope.owner = false;
    $scope.showOwned = true;
    $scope.showReserved = false;

    $scope.isOwnerOfView = userService.getScheduleViewData().owner.id === userService.getUserContext().user.id;

    var getParams = function (isInitial) {
        params = {
            "uuid": userService.getUUID(),
            "trainerId": userService.getUserContext().user.id,
            "date": Date.parse($scope.currentDate.toString())
        };
        var scheduleInitialData = userService.getScheduleViewData();
        if (scheduleInitialData !== null) {
            params.trainerId = scheduleInitialData.owner.id;
            if(isInitial) {
                if(scheduleInitialData.date !== undefined) {
                    params.date = Date.parse(scheduleInitialData.date.toString());
                }
                if(scheduleInitialData.training !== undefined) {
                    $scope.toggleModal(scheduleInitialData.training);
                    scheduleInitialData.training = undefined;
                    userService.setScheduleViewData(scheduleInitialData);
                }
            }
        }
    };
    // Opens training window with correct parameters
    $scope.toggleModal = function (training) {
        $scope.pickedTraining = training;
        $scope.buttonClicked = training.description;
        $scope.isOnline = training.place.name === 'online';
        var lat = training.place.lat;
        var lng = training.place.lng;
        var bookedBy = training.trainingReservations.length;
        $scope.alreadyBooked = false;
        for(var i=0; i<training.trainingReservations.length; i++) {
            if(training.trainingReservations[i].customer.id === userService.getUserContext().user.id) {
                $scope.alreadyBooked = true;
            }
        }
        $scope.modalTitle = training.description;
        $scope.modalBody = "Already registered: "+bookedBy + "/" + training.size;
        $scope.trainingDetails = training.details;
        $scope.owner = training.owner.id === userService.getUserContext().user.id;
        if(!$scope.owner && $scope.alreadyBooked) {
            $scope.modalFooter = "You have already booked this training";
        }
        else if (!$scope.owner && bookedBy >= training.size) {
            $scope.modalFooter = "This training is already taken.";
            $scope.full = true;
        }
        else if (bookedBy > 0 && $scope.owner) {
            $scope.modalFooter = "You can't delete training that was already booked.";
            $scope.taken = true;
        }

        $scope.mapSource = $sce.trustAsResourceUrl("http://maps.google.com/maps?q=" + lat + "," + lng + "&z=15&output=embed");
        $scope.showModal = true;

        if(training.category.theoretical) {
            $scope.mapSource = "";
            $scope.modalBody = "This training is theoretical. It contains only raw materials for you to download";
        }

    };

    var initializeModalData = function () {
        $scope.pickedTraining = null;
        $scope.mapSource = "";
        $scope.modalTitle = 'Put here your header';
        $scope.modalBody = 'Put here your body';
        $scope.modalFooter = '';
        $scope.full = false;
        $scope.taken = false;
        getParams(false);
    };

    $scope.getSchedule = function () {
        getParams(false);
        $http({
            method: 'GET',
            url: userService.getHost() + 'schedule/userSchedule',
            params: params,
            headers: {'Content-Type': 'application/json'}
        }).then(function successCallback(response) {
            $scope.scheduleWeek = response.data.scheduleWeek.days;
            var responseTrainings = response.data.trainings;
            for (var i = 0; i < responseTrainings.length; i++) {
                var responseTraining = responseTrainings[i];
                var trainingInstances = responseTraining.instances;
                for (var y = 0; y < trainingInstances.length; y++) {
                    var trainingInstance = trainingInstances[y];
                    trainingInstance.description = responseTraining.description;
                    trainingInstance.category = responseTraining.category;
                    trainingInstance.details = responseTraining.details;
                    trainingInstance.capacity = responseTraining.capacity;
                    trainingInstance.place = responseTraining.place;
                    trainingInstance.price = responseTraining.price;
                    trainingInstance.owner = responseTraining.owner;
                    trainingInstance.size = responseTraining.capacity;
                    trainingInstance.dateEnd = new Date(trainingInstance.dateEnd);
                    var hours = trainingInstance.hoursEnd = trainingInstance.dateEnd.getHours();
                    var minutes = trainingInstance.minutesEnd = trainingInstance.dateEnd.getMinutes();
                    if (hours < 10) {
                        trainingInstance.hoursEnd = "0" + hours;
                    }
                    if (minutes < 10) {
                        trainingInstance.minutesEnd = "0" + minutes;
                    }
                    trainingInstance.dateStart = new Date(trainingInstance.dateStart);
                    hours = trainingInstance.hoursStart = trainingInstance.dateStart.getHours();
                    minutes = trainingInstance.minutesStart = trainingInstance.dateStart.getMinutes();
                    if (hours < 10) {
                        trainingInstance.hoursStart = "0" + hours;
                    }
                    if (minutes < 10) {
                        trainingInstance.minutesStart = "0" + minutes;
                    }
                    trainingInstance.length = (trainingInstance.dateEnd - trainingInstance.dateStart) / 1000;
                    $scope.scheduleTrainings.push(trainingInstance);
                }
            }
            $scope.visibilityChange();
        }, function errorCallback() {
        });

    };
    $scope.getSchedule();
// Sorts out training out of week range on schedule view
    $scope.getTrainingsFromDate = function (day) {
        var result = [];
        for (var i = 0; i < $scope.scheduleTrainingsToShow.length; i++) {
            var scheduleTraining = $scope.scheduleTrainingsToShow[i];
            if (scheduleTraining.dateStart.getUTCDate() === day.day && (scheduleTraining.dateStart.getMonth() + 1) === day.month) {
                result.push(scheduleTraining);
            }
        }
        return result;
    };

    $scope.goToProfile = function () {
        window.location.href = "userProfile.html?userId="+userService.getScheduleViewData().owner.id;
    };

    // Gives style to training button (placement, color)
    $scope.getTrainingStyle = function (training) {
        var topPercentage = (((training.dateStart.getHours() - 6) * 3600 + training.dateStart.getMinutes() * 60) * secondBasic) + halfHourBasic;
        var height = training.length * secondBasic;
        var topString = Math.round(topPercentage * 100) / 100 + "%";
        var heightString = Math.round(height * 100) / 100 + "%";
        var widthString = dayWidth + "%";
        var params =  {
            "position": 'absolute',
            "top": topString,
            "height": heightString,
            "width": widthString
        };
        if(training.owner.id === userService.getUserContext().user.id) {
            params.background = "var(--blues)";
        }
        return params;

    };
    // Allows owner to delete his own training (instead of reserve button on training window)
    $scope.delete = function () {
        if ($scope.pickedTraining.trainingReservations.length !== 0) {

        } else {
            $http({
                method: 'DELETE',
                url: userService.getHost() + 'schedule/trainingInstance',
                params: {
                    "uuid": userService.getUUID(),
                    "instanceId": $scope.pickedTraining.id
                },
                headers: {'Content-Type': 'application/json'}
            }).then(function successCallback(response) {
                if (response.data) {
                    $window.location.reload();
                    $scope.showModal = false;
                }
            }, function errorCallback() {
            });
        }
    };
// Checks if user already reserved training
    var checkIfReserved = function (training) {
        var loggedUser = userService.getUserContext().user;
        for(var i = 0; i < training.trainingReservations.length; i++) {
            var reservationToCheck = training.trainingReservations[i];
            if(reservationToCheck.customer.id === loggedUser.id) {
                return true
            }
        }
        return false;
    };
// Checks for visibility flags
    $scope.visibilityChange = function () {
        var loggedUser = userService.getUserContext().user;
        $scope.scheduleTrainingsToShow = [];
        for (var i = 0; i < $scope.scheduleTrainings.length; i++) {
            var scheduleTraining = $scope.scheduleTrainings[i];
            if ($scope.showOwned && scheduleTraining.owner.id === loggedUser.id) {
                $scope.scheduleTrainingsToShow.push(scheduleTraining);
            } else if ($scope.showReserved && checkIfReserved(scheduleTraining)) {
                $scope.scheduleTrainingsToShow.push(scheduleTraining);
            } else if (!$scope.isOwnerOfView) {
                $scope.scheduleTrainingsToShow.push(scheduleTraining);
            }
        }
    };
// Allows trainee to sign up for picked training (on training window)
    $scope.reserve = function () {
        if ($scope.pickedTraining.trainingReservations.length > $scope.pickedTraining.size) {
            $scope.showModal = false;
        } else {

            var requestData = {
                uuid: userService.getUUID(),
                customerId: userService.getUserContext().user.id,
                trainingId: $scope.pickedTraining.id
            };
            $http({
                method: 'POST',
                url: userService.getHost() + 'schedule/training/book',
                data: requestData,
                headers: {'Content-Type': 'application/json'}
            }).then(function successCallback(response) {
                if (response.data) {
                    $window.location.href = "schedule.html";
                    $scope.showModal = false;
                }
            }, function errorCallback() {
            });
        }
    };
    // Close Training window
    $scope.cancel = function () {
        $scope.showModal = false;
        initializeModalData();
    };

    // Request for all available trainings
    initializeModalData();

    getParams(true);

});

app.directive('modal', function () {
    return {
        templateUrl: '../frontend/trainingModal.html',
        restrict: 'E',
        transclude: true,
        replace: true,
        scope: true,
        link: function postLink(scope, element, attrs) {
            scope.$watch(attrs.visible, function (value) {
                if (value === true)
                    $(element).modal('show');
                else
                    $(element).modal('hide');
            });

            $(element).on('shown.bs.modal', function () {
                scope.$apply(function () {
                    scope.$parent[attrs.visible] = true;
                });
            });

            $(element).on('hidden.bs.modal', function () {
                scope.$apply(function () {
                    scope.$parent[attrs.visible] = false;
                });
            });
        }
    };
});
