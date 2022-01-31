angular.module('consumer_data_base').controller('FacebookController',
    ['$scope', 'dictionaryService', 'BASE_URL', 'cancelableCarriersDataService',
        function ($scope, dictionaryService, BASE_URL, cancelableCarriersDataService) {

            $scope.selectedJob = {limit: 30};
            $scope.searchConfig = {option: "gender", limit: 30};
            $scope.values = {};

            $scope.tabs = [
                {value: "gender", title: "Gender"},
                {value: "status", title: "Status"},
                {value: "job", title: "Job"}
            ];
            $scope.updateJobs = function () {
                dictionaryService.facebookJobs({tableName: $scope.tableName.name},
                    function (response) {

                        $scope.facebookJobs = response.data;
                    });
            }
            $scope.updateLastName = function () {
                if($scope.searchFacebookHLastName["lastHLNameValue"]  == undefined || $scope.searchFacebookHLastName["lastHLNameValue"]  == ''){
                    $scope.searchFacebookHLastName["lastHLNameValue"] = ' '
                }

                dictionaryService.facebookHLName({strSearch: $scope.searchFacebookHLastName["lastHLNameValue"]},
                    function (response) {
                        $scope.facebookHLastName = response.data;
                    });
            }

            $scope.updateActions = function (value) {
                $scope.searchConfig.option = value;
                if (value == "job") {
                    $scope.updateJobs()
                } else if (value == "last_name") {
                    $scope.updateLastName()
                }
            }

            $scope.loadMore = function () {
                $scope.selectedJob.limit = $scope.selectedJob.limit + 30;
            }

        }])