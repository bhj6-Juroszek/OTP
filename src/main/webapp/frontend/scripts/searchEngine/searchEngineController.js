app.controller('searchEngineController', function ($scope, $http, $window, userService) {


    var defaultImageString = "data:image/jpeg;base64,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";
    var pagesOnView = 8;
    $scope.testV = false;
    $scope.pages = [];
    $scope.currentPage = 0;
    $scope.name = "";
    $scope.parents = [];
    $scope.citiesList = [];
    $scope.countriesList = [];
    $scope.selectedCountry = null;
    $scope.selectedCity = null;
    $scope.selectedCategory = null;
    $scope.pickableCategories = [];
    $scope.dateFrom = new Date();
    $scope.dateTo = new Date();
    $scope.trainingsFound = [];
    $scope.trainingsFoundOnPage = [];
    $scope.showOnline = false;
    $scope.getCategories = function () {
        $http({
            method: 'GET',
            url: userService.getHost() + 'categoryList'
        }).then(function successCallback(response) {
            $scope.categories = [];
            $scope.allCategories = response.data;
            for (var i = 0, l = response.data.length; i < l; i++) {
                var category = response.data[i];
                if (category.parent == 0) {
                    category.fullName = category.name;
                    $scope.categories.push(category);
                } else {
                    for (var y = 0; y < $scope.allCategories.length; y++) {
                        if (category.parent === $scope.allCategories[y].id) {
                            category.fullName = $scope.allCategories[y].name + "/" + category.name;
                        }
                    }
                    $scope.pickableCategories.push(category);
                }


                $scope.parents.push(JSON.parse("{\"id\":\"" + category.parent + "\", \"visible\":false}"));
            }
            userService.setPickableCategories($scope.pickableCategories);
        }, function errorCallback() {
            $scope.categories = [];
        });

    };

    $scope.getSubCategories = function (argumentCat) {
        var result = [];
        for (var i = 0, l = $scope.allCategories.length; i < l; i++) {
            var value = $scope.allCategories[i];
            if (value.parent === argumentCat.id) {
                result.push(value);
            }
        }
        return result;
    };

    $scope.getPages = function () {
        $scope.trainingsFoundOnPage = [];
        var maxPages = Math.ceil($scope.trainingsFound.length / pagesOnView);
        var result = [];
        var i = $scope.currentPage - 1;
        if (i < 0) {
            i = 0;
            var max = i + 2;
            for (; i < max && i < maxPages; i++) {
                result.push({
                    name: i + 1,
                    value: i
                });
            }
        }
        $scope.pages = result;

    };

    $scope.setSelectedCategory = function (category) {
        $scope.selectedCategory = category;
        $scope.search();
    };

    $scope.getVisibility = function (value) {
        for (var i = 0, l = $scope.parents.length; i < l; i++) {
            if ($scope.parents[i].id === value) {
                return $scope.parents[i].visible;
            }
        }
    };

    $scope.selectMainCategory = function (category) {
        if (category !== null) {
            setVisible(category.id);
        }
        $scope.setSelectedCategory(category);
    };
    $scope.initSearcher = function () {
        $scope.getCategories();
        getCountries();
    };

    $scope.validateDate = function () {
        if ($scope.dateFrom > $scope.dateTo) {
            $scope.dateTo = $scope.dateFrom;
        }
    };

    $scope.selectPage = function (pageNumber) {
        $scope.currentPage = pageNumber;
        setTrainingsOnPage();
        $scope.getPages();
    };
    $scope.goToSchedule = function (training) {
        var scheduleViewData = {
            training: training,
            owner: training.owner,
            date: new Date(training.dateStart)
        };
        userService.setScheduleViewData(scheduleViewData);
        window.location.href = 'schedule.html';
    };
    $scope.search = function () {
        $scope.trainingsFound = [];
        var selectedcategoryId;
        if ($scope.selectedCategory === null) {
            selectedcategoryId = null;
        } else {
            selectedcategoryId = $scope.selectedCategory.id
        }
        if ($scope.dateFrom === undefined || $scope.dateTo === undefined) {
            $scope.dateFrom = $scope.dateTo = "";
        }
        var city = null;
        for (var i = 0; i < $scope.citiesList.length; i++) {
            if ($scope.citiesList[i].name === $scope.selectedCity) {
                city = $scope.citiesList[i];
            }
        }
        var dist = 0;
        for (var i = 0; i < $scope.distancesList.length; i++) {
            if ($scope.distancesList[i].name === $scope.distance) {
                dist = $scope.distancesList[i].value;
            }
        }
        $http({
            method: 'POST',
            url: userService.getHost() + 'trainingsWithFilter',
            data: {
                uuid: userService.getUUID(),
                city: city,
                range: dist,
                categoryId: selectedcategoryId,
                dateFirst: Date.parse($scope.dateFrom.toString()),
                dateLast: Date.parse($scope.dateTo.toString()),
                maxPrice: 500,
                sortBy: null,
                showOnline: $scope.showOnline
            }
        }).then(function successCallback(response) {
            var trainings = response.data;
            for (var i = 0; i < trainings.length; i++) {
                var training = trainings[i];
                var trainingInstances = training.instances;
                for (var y = 0; y < trainingInstances.length; y++) {
                    var trainingInstance = trainingInstances[y];
                    trainingInstance.place = training.place;
                    trainingInstance.owner = training.owner;
                    trainingInstance.price = training.price;
                    trainingInstance.size = training.capacity;
                    if (training.owner.imageUrl === null) {
                        training.owner.imageUrl = defaultImageString;
                    }
                    trainingInstance.description = training.description;
                    $scope.trainingsFound.push(trainingInstance);
                }
            }
            if ($scope.trainingsFound.length === 0) {
                $.notify('Not found any training matching criteria!', "warn");
            }
            setTrainingsOnPage();

        }, function errorCallback() {
            $.notify('Cannot connect to server. Try again later', "error");
            $scope.trainingsFound = [];
        });
        $scope.getPages();
    };

    var setTrainingsOnPage = function () {

        var x = ($scope.currentPage) * pagesOnView;
        var delimiter = x + pagesOnView;
        $scope.trainingsFoundOnPage = [];
        for (; x < delimiter && x < $scope.trainingsFound.length; x++) {
            $scope.trainingsFoundOnPage.push($scope.trainingsFound[x]);
        }
    };

    var setVisible = function (value) {
        for (var i = 0, l = $scope.parents.length; i < l; i++) {
            if ($scope.parents[i].id === value) {
                $scope.parents[i].visible = !$scope.parents[i].visible;
            } else {
                $scope.parents[i].visible = false;
            }
        }
    };


    var initializeDistances = function () {
        var result = [];
        var zero = {
            "name": "0",
            "value": 0
        };
        var lessThan2 = {
            "name": "<2 km",
            "value": 2
        };
        var lessThan5 = {
            "name": "<5 km",
            "value": 5
        };
        var lessThan10 = {
            "name": "<10 km",
            "value": 10
        };
        var lessThan25 = {
            "name": "<25 km",
            "value": 25
        };
        var lessThan100 = {
            "name": "<100 km",
            "value": 100
        };
        result.push(zero);
        result.push(lessThan2);
        result.push(lessThan5);
        result.push(lessThan10);
        result.push(lessThan25);
        result.push(lessThan100);
        return result;
    };

    $scope.distancesList = initializeDistances();
    var getCountries = function () {
        $scope.citiesList = [];
        $http({
            method: 'GET',
            url: userService.getHost() + 'countries'
        }).then(function successCallback(response) {
                var countries = response.data;
                for (var i = 0; i < countries.length; i++) {
                    $scope.countriesList.push(countries[i]);
                }
            }
            , function errorCallback() {
            });
    };
    $scope.distance = $scope.distancesList[0];

    var setNotFound = function () {
        $scope.citiesList = [];
        $scope.selectedCity = "";
        $.notify('No cities found', "warn");
    };

    $scope.getCities = function () {
        $scope.citiesList = [];
        var countryCode = null;
        if ($scope.selectedCountry !== null) {
            for (var i = 0; i < $scope.countriesList.length; i++) {
                if ($scope.countriesList[i].countryName === $scope.selectedCountry) {
                    countryCode = $scope.countriesList[i].countryId;
                    break;
                }
            }
            if (countryCode === null) {
                return;
            }
            if ($scope.selectedCity === null || $scope.selectedCity === "" || ($scope.citiesList.length < 15 && $scope.citiesList.length !== 0)) {
                return;
            }
            $http({
                method: 'GET',
                url: userService.getHost() + 'cities',
                params: {
                    "countryCode": countryCode,
                    "prefix": $scope.selectedCity
                }
            }).then(function successCallback(response) {

                    if (response.data.length === 0) {
                        setNotFound();
                    }

                    for (var i = 0; (i < response.data.length && i < 15); i++) {
                        $scope.citiesList.push(response.data[i])
                    }
                }
                , function errorCallback() {
                    var a = 0;
                });

        } else {
            setNotFound();
        }
    }
});