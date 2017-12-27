try {
    var controller = $controller('searchEngineController', [])
} catch (e) {
    var app = angular.module('myApp');
    app.controller('searchEngineController', function ($scope, $http, $window, userService) {
        $scope.testV = false;
        $scope.name = "";
        $scope.parents = [];
        $scope.citiesList = [];
        $scope.selectedCategory = null;
        $scope.pickableCategories = [];
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
                        $scope.categories.push(category);
                    } else {
                        $scope.pickableCategories.push(category)
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

        var setVisible = function (value) {
            for (var i = 0, l = $scope.parents.length; i < l; i++) {
                if ($scope.parents[i].id === value) {
                    $scope.parents[i].visible = !$scope.parents[i].visible;
                } else {
                    $scope.parents[i].visible = false;
                }
            }
        };

        $scope.setSelectedCategory = function (category) {
            $scope.selectedCategory = category;
        };

        var getCities = function () {
            $http({
                method: 'GET',
                url: userService.getHost() + 'cities'
            }).then(function successCallback(response) {
                    for (var i = 0; (i < 100 && i < response.data); i++) {
                        $scope.citiesList.push(response.data[i])
                    }
                }
                , function errorCallback() {
                });
        };

        $scope.getVisibility = function (value) {
            for (var i = 0, l = $scope.parents.length; i < l; i++) {
                if ($scope.parents[i].id == value) {
                    return $scope.parents[i].visible;
                }
            }
        };
        $scope.selectMainCategory = function (category) {
            setVisible(category.id);
            $scope.setSelectedCategory(category);
        };

        $scope.initSearcher = function () {
            $scope.getCategories();
            getCities();
        };

        $scope.search = function () {
            if ($scope.selectedCategory !== null) {
                $http({
                    method: 'POST',
                    url: userService.getHost() + 'trainingsWithFilter',
                    data: {
                        "cityName": "Wisła",
                        "range": 50,
                        "categoryId": $scope.selectedCategory.id,
                        "dateFirst": new Date(2017, 1, 1, 0, 0, 0, 0),
                        "dateLast": new Date(2017, 12, 1, 0, 0, 0, 0),
                        "maxPrice": 500,
                        "sortBy": null
                    }
                }).then(function successCallback(response) {
                    var a = 1
                }, function errorCallback() {
                    var a = 1
                })
            }
        };

    });
}