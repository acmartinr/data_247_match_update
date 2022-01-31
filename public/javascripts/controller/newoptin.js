angular.module('consumer_data_base').
    controller('OptInController',
        ['$scope', 'dictionaryService', 'BASE_URL','cancelableCarriersDataService',
            function ($scope, dictionaryService, BASE_URL, cancelableCarriersDataService) {

                $scope.selectedDomain = { limit: 30 };
                $scope.selectedCarriersLimit = { limit: 30 };
                $scope.searchConfig = { option: "source", limit: 30 };
                $scope.values = {};

                $scope.tabs = [
                    { value: "source", title: "Source" },
                    { value: "datime", title: "Datime" },
                    { value: "carrier", title: "Carrier" },
                ];

                $scope.values = {};

                $scope.updateSelectedObjectValue = function (selectedObject, fromProperty, isFrom) {
                    $scope.cleanSelectedObject(selectedObject, 'Contain');
                    if (isFrom && $scope.values[fromProperty]) {
                        $scope[selectedObject] = $scope.values[fromProperty];
                    }
                }

              $scope.cleanSelectedObject = function(selectedObject, startsWith) {
                        for (prop in $scope[selectedObject]) {
                            if (prop.indexOf(startsWith) == 0) {
                                $scope[selectedObject][prop] = undefined;
                            }
                        }
                    }

                $scope.updateSourceValue = function (isFrom) {
                    $scope.updateSelectedObjectValue(
                        'selectedSourceDomain', 'SourceFrom', isFrom);
                    $scope.selectedDomain.limit = 30;
                    $scope.updateDomainSource();
                }

                $scope.updateDomainSource = function () {
                    console.log("Updating values");
                    $scope.loading = true;
                    cancelableCarriersDataService.cancelRequest();
                    cancelableCarriersDataService.carriers({ domainSource: $scope.selectedSourceDomain, tableName: $scope.tableName.name }, function (response) {
                        $scope.sources = response.data;
                        $scope.loading = false;
                    });
                }

                $scope.updateCarrierSources = function () {
                    dictionaryService.carrierBrand({ tableName: $scope.tableName.name },
                        function (response) {
                            $scope.carriers = response.data;
                        });
                }

                $scope.loadMore = function () {
                    $scope.selectedDomain.limit = $scope.selectedDomain.limit + 30;
                }
               $scope.loadMoreCarriers = function () {
                    $scope.selectedCarriersLimit.limit = $scope.selectedCarriersLimit.limit + 30;
                }

                $scope.updateActions = function (value) {

                    $scope.searchConfig.option = value;
                    if (value == "carrier") {
                        $scope.updateCarrierSources()
                    }
                }

            }])