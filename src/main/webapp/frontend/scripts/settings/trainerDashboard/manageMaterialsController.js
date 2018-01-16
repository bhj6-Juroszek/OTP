app.controller('manageMaterialsController', function ($scope, $http, $window, userService) {

    $scope.userTrainings = [];
    $http({
        method: 'GET',
        url: userService.getHost() + 'getUserTrainings',
        params: {"uuid": userService.getUUID()}
    }).then(function successCallback(response) {
        if(response.data.responseCode === 1) {
            $scope.userTrainings = response.data.userTrainings;
        } else {
            $.notify('Session expired', "error");
            setTimeout(function () {
                window.location.href = userService.getMainAdress();
            }, 1000);
        }
    }, function errorCallback(response) {
        $.notify('Could not connect to server. Please try again later', "error");
    });

    $scope.uploadMaterial = function () {
        var f = document.getElementById('file').files[0];
            var fd = new FormData();
            fd.append("uuid", userService.getUUID());
            fd.append("file", f);
            var trainings = [];
            for(var i = 0; i<$scope.userTrainings.length; i++) {
                var training = $scope.userTrainings[i];
                if(training.addFile !== undefined && training.addFile) {
                    trainings.push(training.id);
                }
            }
            fd.append("trainings", trainings);
            $http({
                method: 'POST',
                data: fd,
                processData: false,
                url: userService.getHost() + 'fileUpload',
                headers: {
                    'Content-Type': undefined
                },
                transformRequest: angular.identity
            }).then(function (result) {
                if(result.data === 1) {
                    $.notify('Material has been added to trainings', "success");
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


});