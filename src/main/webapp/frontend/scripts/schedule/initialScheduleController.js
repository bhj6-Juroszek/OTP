var app = angular.module('myApp');
app.controller('initialScheduleController', function ($scope, userService, $http, $sce, $window) {
    $scope.currentDate = new Date();
    $scope.showModal = false;
    var halfHourBasic = 96.25;
    var dayWidth = 14.28;
    var secondBasic = (halfHourBasic / 30) / 60;
    $scope.scheduleWeek = [];
    $scope.scheduleWeekHtml = [];
    $scope.scheduleTrainings = [];
    $scope.getSchedule = function () {
        $http({
            method: 'GET',
            url: userService.getHost() + 'schedule/getUserSchedule',
            params: {
                "uuid": userService.getUUID(),
                "trainerId": userService.getUserContext().user.id,
                "date": Date.parse($scope.currentDate.toString())
            },
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
                    trainingInstance.capacity = responseTraining.capacity;
                    trainingInstance.lat = responseTraining.place.lat;
                    trainingInstance.lng = responseTraining.place.lng;
                    trainingInstance.ownerId = responseTraining.owner.id;
                    trainingInstance.size = responseTraining.capacity;
                    trainingInstance.dateEnd = new Date(trainingInstance.dateEnd);
                    trainingInstance.dateStart = new Date(trainingInstance.dateStart);
                    trainingInstance.length = (trainingInstance.dateEnd - trainingInstance.dateStart) / 1000;
                    $scope.scheduleTrainings.push(trainingInstance);
                }
            }
        }, function errorCallback() {
        });

    };
    $scope.getSchedule();
    $scope.getTrainingsFromDate = function (day) {
        var result = [];
        for (var i = 0; i < $scope.scheduleTrainings.length; i++) {
            var scheduleTraining =$scope.scheduleTrainings[i];
            if (scheduleTraining.dateStart.getUTCDate() == day.day && (scheduleTraining.dateStart.getMonth()+1) == day.month) {
                result.push(scheduleTraining);
            }
        }
        return result;
    };

    $scope.getTrainingStyle = function (training) {
        var topPercentage = (((training.dateStart.getHours()-6) * 3600 + training.dateStart.getMinutes() * 60) * secondBasic) + halfHourBasic;
        var height = training.length *secondBasic;
        var topString = Math.round(topPercentage * 100) / 100+"%";
        var heightString = Math.round(height * 100) / 100+"%";
        var widthString = dayWidth+"%";
        return {
            "position": 'absolute',
            "top": topString,
            "height":heightString,
            "width":widthString
        };
    };
    $scope.pickedTraining = null;
    $scope.mapSource = "";
    $scope.modalTitle = 'Put here your header';
    $scope.modalBody = 'Put here your body';
    $scope.footer = 'Put here your footer';
    $scope.owner = false;

    $scope.toggleModal = function(training){
        $scope.pickedTraining = training;
        $scope.buttonClicked = training.description;
        var lat = training.lat;
        var lng = training.lng;
        $scope.owner = training.ownerId === userService.getUserContext().user.id;
        $scope.mapSource = $sce.trustAsResourceUrl("http://maps.google.com/maps?q="+lat+","+lng+"&z=15&output=embed");
        $scope.showModal = true;

    };

    $scope.delete = function () {
        if($scope.pickedTraining.trainingReservations.length !== 0) {

        } else {
            $http({
                method: 'DELETE',
                url: userService.getHost() + 'schedule/removeInstance',
                params: {
                    "uuid": userService.getUUID(),
                    "instanceId": $scope.pickedTraining.id
                },
                headers: {'Content-Type': 'application/json'}
            }).then(function successCallback(response) {
                if(response.data) {
                    $window.location.reload();
                    $scope.showModal = false;
                }
            }, function errorCallback() {
            });
        }
    };

    $scope.reserve = function () {
        if($scope.pickedTraining.trainingReservations.length > $scope.pickedTraining.size) {
            $scope.showModal = false;
        } else {

        }
    };
    $scope.cancel = function () {
        $scope.showModal = false;
    };

});

app.directive('modal', function () {
    return {
        templateUrl: '../frontend/trainingModal.html',
        restrict: 'E',
        transclude: true,
        replace:true,
        scope:true,
        link: function postLink(scope, element, attrs) {
            scope.$watch(attrs.visible, function(value){
                if(value == true)
                    $(element).modal('show');
                else
                    $(element).modal('hide');
            });

            $(element).on('shown.bs.modal', function(){
                scope.$apply(function(){
                    scope.$parent[attrs.visible] = true;
                });
            });

            $(element).on('hidden.bs.modal', function(){
                scope.$apply(function(){
                    scope.$parent[attrs.visible] = false;
                });
            });
        }
    };
});
