var app = angular.module('myApp', []).factory('userService', function () {
    return {
        getUserContext: getUserContext,
        setUserContext: setUserContext,
        getPickableCategories: getPickableCategories,
        setPickableCategories: setPickableCategories,
        getUUID: getUUID,
        setUUID: setUUID,
        getHost: getHost,
        getMainAdress: getMainAdress,
        clearImportantData: clearImportantData,
        getScheduleViewData: getScheduleViewData,
        setScheduleViewData: setScheduleViewData,
        getProfileData: getProfileData,
        setProfileData: setProfileData,
        goToScheduleAsOwner: goToScheduleAsOwner
    };

    // .................

    function getUserContext() {
        if (!sessionStorage['user']) {
            return null;
        }
        return JSON.parse(sessionStorage.getItem("user"));
    }

    function setUserContext(value) {
        sessionStorage.setItem("user", JSON.stringify(value))
    }

    function getUUID() {
        if (!sessionStorage['UUID']) {
            return null;
        }
        return sessionStorage.getItem("UUID");
    }

    function setUUID(value) {
        sessionStorage.setItem("UUID", value)
    }

    function getHost() {
        return 'http://localhost:8181/';
    }

    function getMainAdress() {
        return 'index.html';
    }

    function getPickableCategories() {
        if (!sessionStorage['categories']) {
            return [];
        }
        return JSON.parse(sessionStorage.getItem("categories"));
    }

    function setPickableCategories(categories) {
        sessionStorage.setItem("categories", JSON.stringify(categories))
    }

    function clearImportantData() {
        setUUID(null);
        setUserContext(null);
        setScheduleViewData(null);
    }

    function getScheduleViewData() {
        if (!sessionStorage['scheduleViewData']) {
            return null;
        }
        return JSON.parse(sessionStorage.getItem("scheduleViewData"));
    }

    function setScheduleViewData(data) {
        sessionStorage.setItem("scheduleViewData", JSON.stringify(data))
    }

    function goToScheduleAsOwner() {
        if (sessionStorage['user']) {

            var data = {
                owner: getUserContext().user
            };
            sessionStorage.setItem("scheduleViewData", JSON.stringify(data));
            window.location.href = "schedule.html"
        }
    }
    function getProfileData() {
        if (!sessionStorage['profileData']) {
            return null;
        }
        return JSON.parse(sessionStorage.getItem("profileData"));
    }

    function setProfileData(data) {
        sessionStorage.setItem("profileData", JSON.stringify(data))
    }

});
app.config(function($locationProvider) {
    $locationProvider.html5Mode(true);
});


