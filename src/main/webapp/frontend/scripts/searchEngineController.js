try {
    var controller = $controller('searchEngineController', [])
} catch (e) {
    var app = angular.module('myApp');
    app.controller('searchEngineController', function ($scope, $http, $window, userService) {
        $scope.testV = false;
        $scope.name = "";
        $scope.parents = [];
        $scope.selectedCategory = null;
        $scope.getCategories = function () {
            $http({
                method: 'GET',
                url: 'http://localhost:8181/categoryList',
            }).then(function successCallback(response) {
                $scope.categories = [];
                $scope.allCategories = response.data;
                for (var i = 0, l = response.data.length; i < l; i++) {
                    var category = response.data[i];
                    if (category.parent === 0) {
                        $scope.categories.push(category);
                    }
                    $scope.parents.push(JSON.parse("{\"id\":" + category.parent + ", \"visible\":false}"
                    ));
                }
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

        $scope.getVisibility = function (value) {
            for (var i = 0, l = $scope.parents.length; i < l; i++) {
                if ($scope.parents[i].id === value) {
                    return $scope.parents[i].visible;
                }
            }
        };
        $scope.test = function (category) {
            setVisible(category.id);
            $scope.setSelectedCategory(category);
        };

    });
}