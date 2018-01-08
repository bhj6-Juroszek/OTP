app.controller("userProfileController", function ($scope, $http, userService) {
    var defaultImageString = "data:image/jpeg;base64,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";
    var paramsObject = {};
    $scope.generalRate = "No rates available";
    $scope.newLink ="";
    window.location.search.replace(/\?/, '').split('&').map(function (o) {
        paramsObject[o.split('=')[0]] = o.split('=')[1]
    });
    if(paramsObject.userId === undefined) {
        paramsObject.userId = userService.getUserContext().user.id;
    }
    $scope.removeLink = function (link) {
        var links =  $scope.profile.socialMediaLinks;
        for(var i = 0; i<links.length; i++) {
            if(links[i] === link) {
                $scope.profile.socialMediaLinks.splice(i,1);
            }
        }

    };

    $scope.pushNewSocialMediaLink = function (link) {
        $scope.newLink ="";
        if(link === "" || link.indexOf(" ") !== -1) {
            $.notify("Non valid url", "warn");
            return;
        }
        var links = $scope.profile.socialMediaLinks;
        for(var i = 0; i < links.length; i++) {
          if(link === links[i]) {
              return;
          }
      }
        $scope.profile.socialMediaLinks.push(link);
    };
    var getProfile = function () {

        $http({
            method: 'GET',
            url: userService.getHost() + 'profile/' + paramsObject.userId,
            headers: {'Content-Type': 'application/json'}
        }).then(function successCallback(response) {
            $scope.profile = response.data.profile;
            $scope.rates = $scope.profile.rates;
            if ($scope.rates.length > 0) {
                var sum = 0;
                for (var i = 0; i < $scope.rates.length; i++) {
                    sum += $scope.rates.rateValue;
                }
                $scope.generalRate = Math.round(sum / $scope.rates.length);
            }
            $scope.user = response.data.user;
            if ($scope.user.imageUrl === null || $scope.user.imageUrl === "") {
                $scope.user.imageUrl = defaultImageString;
            }
            if ($scope.profile === null) {
                $.notify('User not found. You will be redirected to main page', "error");
                setTimeout(function () {
                    window.location.href = userService.getMainAdress();
                }, 3000)
            }
        }, function errorCallback(response) {
            $.notify('Could not connect to server. Please try again later', "error");
            setTimeout(function () {
                window.location.href = userService.getMainAdress();
            }, 3000)
        });
    };
    getProfile();
    $scope.test = function () {
        var a = $scope.profile.phoneNumber;
    };
    $scope.updateProfile = function () {
        if($scope.profile.phoneNumber === undefined) {
            $.notify("Phone number too short", "warn")
            return;
        }
        $http({
            method: 'POST',
            url: userService.getHost() + 'profile/' + paramsObject.userId,
            data: $scope.profile,
            params: {
              "uuid": userService.getUUID()
            },
            headers: {'Content-Type': 'application/json'}
        }).then(function successCallback(response) {
            if(!response.data) {
                $.notify("You have no right to perform this action", "error");
                userService.clearImportantData();
                window.location.href = userService.getMainAdress();
            } else {
                $.notify("Changes saved", "success");
            }
        }, function errorCallback(response) {
            $.notify('Could not connect to server. Please try again later', "error");
            setTimeout(function () {
                window.location.href = userService.getMainAdress();
            }, 3000)
        });
    }
});