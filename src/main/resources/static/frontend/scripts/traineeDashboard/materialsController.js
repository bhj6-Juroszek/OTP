app.controller('materialsController', function ($scope, $http, $window, userService) {


    var contains = function (array, filePath) {
        for (var i = 0; i < array.length; i++) {
            if (array[i].fullName === filePath.fullName) {
                return true;
            }
        }
        return false;
    };

    var getMaterials = function() {
        $http({
            method: 'GET',
            url: userService.getHost() + 'trainee/materials',
            params: {"uuid": userService.getUUID()}
        }).then(function successCallback(response) {
            if (response.data.responseCode === 1) {
                var filePaths = response.data.materialsPaths;
                for (var i = 0; i < filePaths.length; i++) {
                    var filePath = {};
                    filePath.fullName = filePaths[i];
                    var tokens = filePath.fullName.split('/');
                    filePath.fileName = tokens[tokens.length - 1];
                    if (!contains($scope.paths, filePath)) {
                        $scope.paths.push(filePath);
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
    };


    $scope.download = function (filePath) {
        $http({
            method: 'GET',
            url: userService.getHost() + 'trainee/file',
            responseType: 'arraybuffer',
            params: {
                "uuid": userService.getUUID(),
                filePath: filePath.fileName
            }
        })
            .then(function successCallback(response) {
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.style = "display: none";
                var blob = new Blob([response.data]);
                    var url = window.URL.createObjectURL(blob);
                a.href = url;
                a.download = filePath.fileName;
                a.click();
                window.URL.revokeObjectURL(url);
            }, function errorCallback(response) {
                $.notify('Could not connect to server. Please try again later', "error");
            })
    };

    $scope.paths = [];
    getMaterials();

});
