angular.module('consumer_data_base').
controller('MobileDashboardController',
[ '$scope', '$document', '$state', 'BASE_URL', 'cancelableDataService', 'dataService', 'credentialsService', 'toasty', '$timeout', '$modal', 'listService', 'authService',
function($scope, $document, $state, BASE_URL, cancelableDataService, dataService, credentialsService, toasty, $timeout, $modal, listService, authService) {
    $scope.currentStep = 'selectTable';
    $scope.steps = [$scope.currentStep];

    $scope.params = {};
    $scope.keywords = [{keywords: '', selectedColumns: {'industry': true}}];
    $scope.states = document.states;

    var fixHeight = function() {
        var elements = document.getElementsByClassName('main-container');
        for (var i = 0; i < elements.length; i++) {
            var element = elements[i];

            element.style.minHeight = (window.innerHeight - 162) + 'px';
        }
    }
    fixHeight();

    var requestPurchasedLists = function() {
        if (credentialsService.getUser().isGuest) {
            $scope.lists = [];
        } else {
            listService.getAllPurchased({ id: credentialsService.getUser().id, dataType: -1 }, function(response) {
                if (response.status === 'OK') {
                    $scope.lists = response.data;
                }
            });
        }
    }
    requestPurchasedLists();

    var isCalienteLeads = function() {
        return document.URL.indexOf('axiomleads.com') != -1;
    }

    var isMultimediaListsCom = function() {
        return document.URL.indexOf('multimedialists.com') != -1;
    }

    var isMultimediaListsNet = function() {
        return document.URL.indexOf('multimedialists.net') != -1;
    }

    $scope.logout = function() {
        credentialsService.logout();

        if (isCalienteLeads()) {
            document.location.href = 'http://axiomleads.com';
        } else if (isMultimediaListsCom()) {
            document.location.href = 'https://multimedialists.com';
        } else if (isMultimediaListsNet()) {
            document.location.href = 'https://multimedialists.net';
        } else {
            $state.transitionTo( 'login' );
        }
    }

    $scope.isDisabledForCurrentStep = function() {
        if ($scope.currentStep === 'selectTable') {
            return !$scope.params.businessOwners && !$scope.params.homeOwners;
        } else if ($scope.currentStep === 'selectBusinessOwners') {
            return (!$scope.params.allBusinessOwners && !$scope.params.specialIndustry) ||
                ($scope.params.specialIndustry && $scope.keywords.length == 1 && !$scope.keywords[0].keyword);
        } else if ($scope.currentStep === 'selectHomeOwners') {
            return !$scope.params.allHomeOwners && !$scope.params.hispanicHomeOwners;
        } else if ($scope.currentStep === 'selectGeographic') {
            return !$scope.params.allUSA && !$scope.params.byState && !$scope.params.zipCodeRadius;
        } else if ($scope.currentStep === 'selectZipRadius') {
            return !$scope.params.zipCodeCenter || !$scope.params.zipCodeDistance;
        } else if ($scope.currentStep === 'selectStates') {
            return $scope.asArray($scope.selectedStates).length == 0;
        } else if ($scope.currentStep === 'selectDNCAndLists') {
            return false;
        } else if ($scope.currentStep === 'gettingCount') {
            return $scope.loading || $scope.count == 0;
        }
    }

    $scope.onPreviousStepTapped = function() {
        if ($scope.currentStep == 'selectBusinessOwners') {
            $scope.params.allBusinessOwners = false;
            $scope.params.specialIndustry = false;
            $scope.keywords = [];
        } else if ($scope.currentStep === 'selectHomeOwners') {
            $scope.params.allHomeOwners = false;
            $scope.params.hispanicHomeOwners = false;
        } else if ($scope.currentStep === 'selectGeographic') {
            $scope.params.allUSA = false;
            $scope.params.byState = false;
            $scope.params.zipCodeRadius = false;
        } else if ($scope.currentStep === 'selectZipRadius') {
            $scope.params.zipCodeCenter = false;
            $scope.params.zipCodeDistance = false;
        } else if ($scope.currentStep === 'selectStates') {
            $scope.selectedStates = {};
        } else if ($scope.currentStep === 'selectDNCAndLists') {
            $scope.params.removeDNC = false;
            $scope.selectedLists = {};
        }

        $scope.currentStep = $scope.steps.pop();
    }

    $scope.onNextStepTapped = function() {
        if ($scope.currentStep == 'selectTable') {
            if ($scope.params.businessOwners) {
                $scope.currentStep = 'selectBusinessOwners';
            } else if ($scope.params.homeOwners) {
                $scope.currentStep = 'selectHomeOwners';
            }

            $scope.steps.push('selectTable');
        } else if ($scope.currentStep == 'selectBusinessOwners') {
            $scope.currentStep = 'selectGeographic';
            $scope.steps.push('selectBusinessOwners');
        } else if ($scope.currentStep == 'selectHomeOwners') {
            $scope.currentStep = 'selectGeographic';
            $scope.steps.push('selectHomeOwners');
        } else if ($scope.currentStep == 'selectGeographic') {
            if ($scope.params.allUSA) {
                $scope.currentStep = 'selectDNCAndLists';
            } else if ($scope.params.byState) {
                $scope.currentStep = 'selectStates';
            } else if ($scope.params.zipCodeRadius) {
                $scope.currentStep = 'selectZipRadius';
            }

            $scope.steps.push('selectGeographic');
        } else if ($scope.currentStep == 'selectZipRadius') {
            $scope.currentStep = 'selectDNCAndLists';
            $scope.steps.push('selectZipRadius');
        } else if ($scope.currentStep == 'selectStates') {
            $scope.currentStep = 'selectDNCAndLists';
            $scope.steps.push('selectStates');
        } else if ($scope.currentStep == 'selectDNCAndLists') {
            $scope.currentStep = 'gettingCount';
            $scope.steps.push('selectDNCAndLists');
            $scope.requestCount();
        } else if ($scope.currentStep == 'gettingCount') {
            $scope.buyItems();
        }
    }

    $scope.buyItems = function() {
        var dataRequest = getRequest();
        var request = { 'name': 'APP_list_' + new Date().toLocaleString(),
                        userId: credentialsService.getUser().id,
                        cnt: $scope.count,
                        date: new Date().getTime(),
                        'type': dataRequest.dataType,
                        tableName: dataRequest.tableName,
                        'request': JSON.stringify(getRequest()) };

        var modalInstance = $modal.open({
            templateUrl: BASE_URL + '/assets/partials/mobile/buy.html',
            controller: 'MobileBuyListModalController',
            resolve: {
                list: function () { return request; }
            }
        });

        modalInstance.result.then(function() {
            toasty.success({
                title: 'Download your list',
                msg: 'Your list is ready. Open "Purchases" screen and download it.',
                timeout: 10000,
                sound: false
            });
        });
    }

    $scope.requestCount = function() {
        var request = getRequest();

        $scope.loading = true;
        cancelableDataService.geographic(request, function(response) {
            $scope.count = response.data.count;
            $scope.loading = false;
        });
    }

    $scope.onAllListsTapped = function() {
        for (var i = 0; i < $scope.lists.length; i++) {
            var list = $scope.lists[i];
            $scope.selectedLists[list.name + "|" + list.id] = $scope.params.allLists;
        }
    }

    // select table
    $scope.$watch('params.businessOwners', function() {
        if ($scope.params.businessOwners) {
            $scope.params.homeOwners = false;
        }
    });

    $scope.$watch('params.homeOwners', function() {
        if ($scope.params.homeOwners) {
            $scope.params.businessOwners = false;
        }
    });

    // select business owners
    $scope.$watch('params.allBusinessOwners', function() {
        if ($scope.params.allBusinessOwners) {
            $scope.params.specialIndustry = false;
        }
    });

    $scope.$watch('params.specialIndustry', function() {
        if ($scope.params.specialIndustry) {
            $scope.params.allBusinessOwners = false;
        }
    });

    // select home owners
    $scope.$watch('params.allHomeOwners', function() {
        if ($scope.params.allHomeOwners) {
            $scope.params.hispanicHomeOwners = false;
        }
    });

    $scope.$watch('params.hispanicHomeOwners', function() {
        if ($scope.params.hispanicHomeOwners) {
            $scope.params.allHomeOwners = false;
        }
    });

    // select geographic
    $scope.$watch('params.allUSA', function() {
        if ($scope.params.allUSA) {
            $scope.params.byState = false;
            $scope.params.zipCodeRadius = false;
        }
    });

    $scope.$watch('params.byState', function() {
        if ($scope.params.byState) {
            $scope.params.allUSA = false;
            $scope.params.zipCodeRadius = false;
        }
    });

    $scope.$watch('params.zipCodeRadius', function() {
        if ($scope.params.zipCodeRadius) {
            $scope.params.byState = false;
            $scope.params.allUSA = false;
        }
    });



    $scope.config = {filterDNC: false, filterEmptyPhone: false, filterEmail: false, uniqueBusinessName: false, mobile: false, landlines: false};


    $scope.resetKeyword = function() {
        $scope.keywords = [{keywords: '', selectedColumns: {}}];
    }

    $scope.addKeyword = function() {
        $scope.keywords.push({keyword: '', selectedColumns: {}});
    }

    $scope.removeKeyword = function(keyword) {
        $scope.keywords.splice($scope.keywords.indexOf(keyword), 1);

        if ($scope.keywords.length == 0) {
            $scope.addKeyword();
        }
    }

    $scope.generateColumnsString = function(keyword) {
        var result = "";
        for (prop in keyword.selectedColumns) {
            if (result.length > 0) {
                result = result + ", ";
            }

            result = result + prop.split("|")[0];
        }

        if (result.length > 0) {
            return " [" + result + "]";
        } else {
            return result;
        }
    }

    $scope.resetParams = function() {
        $scope.selectedStates = {};
        $scope.omittedStates = {};
        $scope.selectedCities = {};
        $scope.omittedCities = {};
        $scope.selectedZipCodes = {};
        $scope.omittedZipCodes = {};
        $scope.selectedAreaCodes = {};
        $scope.omittedAreaCodes = {};
        $scope.selectedCounties = {};

        $scope.selectedAges = {};
        $scope.agesRange = {};
        $scope.selectedGenders = {};
        $scope.selectedEducations = {};

        $scope.selectedNetWorth = {};
        $scope.selectedRating = {};
        $scope.selectedActiveLines = {};
        $scope.selectedRange = {};

        $scope.selectedEthnicityGroup = {};
        $scope.selectedEthnicityLanguage = {};
        $scope.selectedEthnicityReligion = {};

        $scope.selectedHouseholdSize = {};
        $scope.selectedHouseholdIncome = {};

        $scope.selectedResidenceType = {};
        $scope.selectedResidenceOwnerShip = {};
        $scope.selectedResidenceVeteran = {};
        $scope.selectedResidenceLength = {};
        $scope.selectedResidenceMarital = {};
        $scope.selectedResidenceChildren = {};

        $scope.selectedInterests = {};

        $scope.selectedLists = {};
        $scope.selectedUploadedLists = {};

        $scope.selectedSales = {};
        $scope.selectedEmployeeCount = {};
        $scope.selectedTitles = {};
        $scope.selectedIndustries = {};

        $scope.selectedSics = {};
        $scope.sicRange = {};

        $scope.selectedSources = {};

        $scope.selectedSections = {};
        $scope.selectedDates = {};

        $scope.selectedTimeZones = {};

        $scope.selectedMaritalStatuses = {};
        $scope.selectedEthnicCodes = {};
        $scope.selectedLanguageCodes = {};
        $scope.selectedEthnicGroups = {};
        $scope.selectedReligionCodes = {};
        $scope.selectedProperties = {};
        $scope.selectedHispanicCountryCodes = {};

        $scope.selectedPropertyType = {};
        $scope.selectedOwnerType = {};
        $scope.selectedLengthOfResidence = {};
        $scope.selectedNumberOfPersonInLivingUnit = {};
        $scope.selectedNumberOfChildren = {};
        $scope.selectedInferredHouseHoldRank = {};
        $scope.selectedNumberOfAdults = {};
        $scope.selectedGenerationsInHouseHold = {};
        $scope.selectedSewer = {};
        $scope.selectedWater = {};

        $scope.selectedOccupationGroups = {};
        $scope.selectedPersonEducations = {};
        $scope.selectedPersonOccupations = {};
        $scope.selectedBusinessOwners = {};
        $scope.selectedEstimatedIncome = {};
        $scope.selectedNetWorthes = {};
        $scope.selectedPropertyOwned = {};

        $scope.selectedHomePurchasePrices = {};
        $scope.selectedHomePurchasedDates = [];
        $scope.selectedHomeYearBuilt = [];
        $scope.selectedEstimatedCurrentHomeValueCodes = {};
        $scope.selectedMortgageAmountInThousands = {};
        $scope.selectedMortgageLenderNames = {};
        $scope.selectedMortgageRate = {};
        $scope.selectedMortgageRateTypes = {};
        $scope.selectedMortgageLoanTypes = {};
        $scope.selectedTransactionTypes = {};
        $scope.selectedRefinanceAmountInThousands = {};
        $scope.selectedRefinanceLeaderNames = {};
        $scope.selectedDeedDatesOfRefinance = [];
        $scope.selectedRefinanceRateTypes = {};
        $scope.selectedRefinanceLoanTypes = {};
        $scope.selectedCensusMedianHouseHoldIncome = {};
        $scope.selectedCensusMedianHomeValue = {};
        $scope.selectedCraIncomeClassificationCodes = {};
        $scope.selectedPurchaseMortgageDates = [];
        $scope.selectedMostRecentLenderCodes = {};
        $scope.selectedPurchaseLenderNames = {};
        $scope.selectedMostRecentMortgageInterestRates = {};
        $scope.selectedLoanToValues = {};

        $scope.selectedNumberOfSources = {};
        $scope.selectedDPV = {};

        $scope.selectedChildrenAgeGender = {};

        $scope.selectedRating = {};
        $scope.selectedActiveLines = {};
        $scope.selectedRange = {};
        $scope.selectedCategory = {};

        $scope.selectedYears = [];
        $scope.selectedModels = {};
        $scope.selectedMakes = {};

        $scope.count = 0;

        $scope.searchType = 'geographic';

        $scope.keywords = [{keywords: '', selectedColumns: {}}];
        $scope.unique = true;
        $scope.config.uniqueBusinessName = false;
        $scope.config.filterDNC = false;
        $scope.config.filterEmptyPhone = false;
        $scope.config.filterEmail = false;
        $scope.config.mobile = false;
        $scope.config.landlines = false;
    }
    $scope.resetParams();

    var asObject = function(array) {
        var result = {};
        for (var i = 0; i < array.length; i++) {
            result[array[i].split("_")[0]] = true;
        }

        return result;
    }

    $scope.clearPage = function() {
        $scope.menu.expanded = false;
        $scope.resetParams();

        $scope.loading = false;
        cancelableDataService.cancelRequest();
    }

    $scope.checkRequest = function(request) {
        return true;
    }

    $scope.formatZipCode = function(value) {
        value = value + '';
        if (value.length === 4) {
            return '0' + value;
        } else {
            return value;
        }
    }

    var initDefaultRequest = function() {
        return { states: getValues($scope.asArray($scope.selectedStates)),
                 omittedStates: getValues($scope.asArray($scope.omittedStates)),
                 cities: $scope.asArray($scope.selectedCities),
                 omittedCities: $scope.asArray($scope.omittedCities),
                 zipCodes: $scope.asArray($scope.selectedZipCodes),
                 omittedZipCodes: $scope.asArray($scope.omittedZipCodes),
                 counties: $scope.asArray($scope.selectedCounties),
                 areaCodes: $scope.asArray($scope.selectedAreaCodes),
                 omittedAreaCodes: $scope.asArray($scope.omittedAreaCodes),
                 ages: getValues($scope.asArray($scope.selectedAges)),
                 agesRange: getValues($scope.asArray($scope.agesRange)),
                 genders: getValues($scope.asArray($scope.selectedGenders)),
                 educations: getValues($scope.asArray($scope.selectedEducations)),
                 netWorth: getValues($scope.asArray($scope.selectedNetWorth)),
                 creditRating: getValues($scope.asArray($scope.selectedRating)),
                 creditLines: getValues($scope.asArray($scope.selectedActiveLines)),
                 creditRanges: getValues($scope.asArray($scope.selectedRange)),
                 ethnicityGroups: getValues($scope.asArray($scope.selectedEthnicityGroup)),
                 ethnicityLanguages: getValues($scope.asArray($scope.selectedEthnicityLanguage)),
                 ethnicityReligions: getValues($scope.asArray($scope.selectedEthnicityReligion)),
                 householdSize: getValues($scope.asArray($scope.selectedHouseholdSize)),
                 householdIncome: getValues($scope.asArray($scope.selectedHouseholdIncome)),
                 residenceType: getValues($scope.asArray($scope.selectedResidenceType)),
                 residenceOwnership: getValues($scope.asArray($scope.selectedResidenceOwnerShip)),
                 residenceVeteran: getValues($scope.asArray($scope.selectedResidenceVeteran)),
                 residenceLength: getValues($scope.asArray($scope.selectedResidenceLength)),
                 residenceMarital: getValues($scope.asArray($scope.selectedResidenceMarital)),
                 residenceChildren: getValues($scope.asArray($scope.selectedResidenceChildren)),
                 interests: getValues($scope.asArray($scope.selectedInterests)),
                 selectedLists: getValues($scope.asArray($scope.selectedLists)),
                 uploadedLists: getValues($scope.asArray($scope.selectedUploadedLists)),
                 sales: $scope.asArray($scope.selectedSales),
                 sections: $scope.asArray($scope.selectedSections),
                 dates: getValues($scope.asArray($scope.selectedDates)),
                 employeeCount: $scope.asArray($scope.selectedEmployeeCount),
                 titles: $scope.asArray($scope.selectedTitles),
                 industries: getValues($scope.asArray($scope.selectedIndustries)),
                 sics: $scope.asArray($scope.selectedSics),
                 fromSic: $scope.sicRange.fromSic,
                 toSic: $scope.sicRange.toSic,
                 sources: getValues($scope.asArray($scope.selectedSources)),
                 phoneTypes: [],
                 keywords: getKeywordsArray(),
                 keywordsColumns: getKeywordsColumnsArray(),
                 timeZones: getValues($scope.asArray($scope.selectedTimeZones)),
                 maritalStatuses: getValues($scope.asArray($scope.selectedMaritalStatuses)),
                 ethnicCodes: getValues($scope.asArray($scope.selectedEthnicCodes)),
                 languageCodes: getValues($scope.asArray($scope.selectedLanguageCodes)),
                 ethnicGroups: getValues($scope.asArray($scope.selectedEthnicGroups)),
                 religionCodes: getValues($scope.asArray($scope.selectedReligionCodes)),
                 hispanicCountryCodes: getValues($scope.asArray($scope.selectedHispanicCountryCodes)),
                 properties: getValues($scope.asArray($scope.selectedProperties)),

                 propertyType: getValues($scope.asArray($scope.selectedPropertyType)),
                 ownerType: getValues($scope.asArray($scope.selectedOwnerType)),
                 lengthOfResidence: getValues($scope.asArray($scope.selectedLengthOfResidence)),
                 numberOfPersonInLivingUnit: getValues($scope.asArray($scope.selectedNumberOfPersonInLivingUnit)),
                 numberOfChildren: getValues($scope.asArray($scope.selectedNumberOfChildren)),
                 inferredHouseHoldRank: getValues($scope.asArray($scope.selectedInferredHouseHoldRank)),
                 numberOfAdults: getValues($scope.asArray($scope.selectedNumberOfAdults)),
                 generationsInHouseHold: getValues($scope.asArray($scope.selectedGenerationsInHouseHold)),
                 sewer: getValues($scope.asArray($scope.selectedSewer)),
                 water: getValues($scope.asArray($scope.selectedWater)),

                 occupationGroups: getValues($scope.asArray($scope.selectedOccupationGroups)),
                 personEducations: getValues($scope.asArray($scope.selectedPersonEducations)),
                 personOccupations: getValues($scope.asArray($scope.selectedPersonOccupations)),
                 businessOwners: getValues($scope.asArray($scope.selectedBusinessOwners)),
                 estimatedIncome: getValues($scope.asArray($scope.selectedEstimatedIncome)),
                 netWorthes: getValues($scope.asArray($scope.selectedNetWorthes)),
                 propertyOwned: getValues($scope.asArray($scope.selectedPropertyOwned)),

                 homePurchasePrices: getValues($scope.asArray($scope.selectedHomePurchasePrices)),
                 homePurchasedDates: getDateValues($scope.selectedHomePurchasedDates),
                 homeYearBuilt: getDateValues($scope.selectedHomeYearBuilt),
                 estimatedCurrentHomeValueCodes: getValues($scope.asArray($scope.selectedEstimatedCurrentHomeValueCodes)),
                 mortgageAmountInThousands: getValues($scope.asArray($scope.selectedMortgageAmountInThousands)),
                 mortgageLenderNames: getValues($scope.asArray($scope.selectedMortgageLenderNames)),
                 mortgageRate: getValues($scope.asArray($scope.selectedMortgageRate)),
                 mortgageRateTypes: getValues($scope.asArray($scope.selectedMortgageRateTypes)),
                 mortgageLoanTypes: getValues($scope.asArray($scope.selectedMortgageLoanTypes)),
                 transactionTypes: getValues($scope.asArray($scope.selectedTransactionTypes)),
                 refinanceAmountInThousands: getValues($scope.asArray($scope.selectedRefinanceAmountInThousands)),
                 refinanceLeaderNames: getValues($scope.asArray($scope.selectedRefinanceLeaderNames)),
                 deedDatesOfRefinance: getDateValues($scope.selectedDeedDatesOfRefinance),
                 refinanceRateTypes: getValues($scope.asArray($scope.selectedRefinanceRateTypes)),
                 refinanceLoanTypes: getValues($scope.asArray($scope.selectedRefinanceLoanTypes)),
                 censusMedianHouseHoldIncome: getValues($scope.asArray($scope.selectedCensusMedianHouseHoldIncome)),
                 censusMedianHomeValue: getValues($scope.asArray($scope.selectedCensusMedianHomeValue)),
                 craIncomeClassificationCodes: getValues($scope.asArray($scope.selectedCraIncomeClassificationCodes)),
                 purchaseMortgageDates: getDateValues($scope.selectedPurchaseMortgageDates),
                 mostRecentLenderCodes: getValues($scope.asArray($scope.selectedMostRecentLenderCodes)),
                 purchaseLenderNames: getValues($scope.asArray($scope.selectedPurchaseLenderNames)),
                 mostRecentMortgageInterestRates: getValues($scope.asArray($scope.selectedMostRecentMortgageInterestRates)),
                 loanToValues: getValues($scope.asArray($scope.selectedLoanToValues)),
                 rating: getValues($scope.asArray($scope.selectedRating)),
                 activeLines: getValues($scope.asArray($scope.selectedActiveLines)),
                 range: getValues($scope.asArray($scope.selectedRange)),
                 categories: getValues($scope.asArray($scope.selectedCategory)),
                 models: getValues($scope.asArray($scope.selectedModels)),
                 makes: getValues($scope.asArray($scope.selectedMakes)),
                 yearsRange: getDateValues($scope.selectedYears),
                 numberOfSources: getValues($scope.asArray($scope.selectedNumberOfSources)),
                 dpv: getValues($scope.asArray($scope.selectedDPV)),
                 childrenAgeGender: getValues($scope.asArray($scope.selectedChildrenAgeGender))
             };
    }

    var getRequest = function() {
        var request = initDefaultRequest();

        request.dataType = $scope.params.businessOwners ? 2 : 9;
        request.tableName = $scope.params.businessOwners ? 'DirectoryMobile' : 'ConsumersMobile';
        request.unique = true;
        request.selectedLists = getValues($scope.asArray($scope.selectedLists));

        if ($scope.params.specialIndustry) {
            request.keywords = getKeywordsArray();
            request.keywordsColumns = getKeywordsColumnsArray();
        }

        if ($scope.params.hispanicHomeOwners) {
            request.ethnicCodes = ["T9"];
        }

        if ($scope.params.ageFrom && $scope.params.ageTo) {
            request.agesRange = [$scope.params.ageFrom, $scope.params.ageTo];
        }

        if ($scope.params.byState) {
            request.states = getValues($scope.asArray($scope.selectedStates));
        }

        if ($scope.params.zipCodeRadius) {
            request.zipCodes = getZipCodes()
        }

        if ($scope.params.removeDNC) {
            request.filterDNC = true;
        }

        if (!$scope.businessOwners) {
            request.propertyType = ["S"];
            request.ownerType = ["H", "9"];
        }

        return request;
    }

    var getZipCodes = function() {
        var zipCoordinatesObject = {};
        for (var i = 0; i < document.zipCoordinates.length; i++) {
            zipCoordinatesObject[ document.zipCoordinates[ i ].zipCode ] = document.zipCoordinates[ i ];
        }

        var searchedZipCode = [$scope.params.zipCodeCenter];
        var zipCode = zipCoordinatesObject[$scope.params.zipCodeCenter];

        for (var i = 0; i < document.zipCoordinates.length; i++) {
            if (getDistanceFromLatLonInKm(zipCode.latitude, zipCode.longitude,
                    document.zipCoordinates[ i ].latitude, document.zipCoordinates[ i ].longitude) <= $scope.params.zipCodeDistance) {
                searchedZipCode.push(document.zipCoordinates[ i ].zipCode);
            }
        }

        return searchedZipCode;
    }

    function getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2) {
        var R = 6371;
        var dLat = deg2rad(lat2-lat1);
        var dLon = deg2rad(lon2-lon1);
        var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                Math.sin(dLon/2) * Math.sin(dLon/2);

        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        var d = R * c / 1.6;
        return d;
    }

    function deg2rad(deg) {
        return deg * (Math.PI/180)
    }

    var getKeywordsArray = function() {
        var result = [];
        for (var i = 0; i < $scope.keywords.length; i++) {
            result.push($scope.keywords[i].keyword);
        }

        return result;
    }

    var getKeywordsColumnsArray = function() {
        var result = [];
        for (var i = 0; i < $scope.keywords.length; i++) {
            result.push(getValues($scope.asArray($scope.keywords[i].selectedColumns)));
        }

        return result;
    }

    var getValues = function(states) {
        var result = [];
        for (var i = 0; i < states.length; i++) {
            result.push(states[ i ].split('|')[ 1 ]);
        }
        return result;
    }

    var getDateValues = function(dates) {
        if (!dates[0] && !dates[1]) { return []; }

        var results = [-1, -1];
        for (var i = 0; i < 2; i++) {
            if (dates[i]) {
                results[i] = dates[i].value;
            }
        }

        return results
    }

    $scope.asArray = function(object) {
        var result = [];
        for (var prop in object) {
            if (object[ prop ]) {
                result.push(prop);
            }
        }

        return result;
    }

    var copyObject = function(src) {
        var result = {}
        for (var prop in src) {
            result[ prop ] = src[ prop ];
        }

        return result;
    }

    $scope.formattedAmount = function(value) {
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
} ])
.controller('MobileBuyListModalController',
[ '$scope', '$modalInstance', 'listService', 'profileService', 'credentialsService', 'toasty', 'list',
function($scope, $modalInstance, listService, profileService, credentialsService, toasty, list) {
    $scope.count = list.cnt;

    $scope.close = function() {
        $modalInstance.dismiss();
    }

    var init = function() {
        $scope.total = 0;
        $scope.amount = 0;
        $scope.totalChanged();

        profileService.protected(
            credentialsService.getUser().id,
            credentialsService.getUser().password).
                getUserBalance({ id: credentialsService.getUser().id }, function(response) {
                    $scope.balance = response.data.toFixed(2);
                    $scope.commonBalance = response.data.toFixed(2);
                });

        listService.getTableItemPrice({ 'table': list.tableName, 'userId': credentialsService.getUser().id }, function(response) {
            $scope.pricePerItem = response.data;
        });
    }

    $scope.filterValue = function($event) {
        if(isNaN(String.fromCharCode($event.charCode)) &&
                $event.keyCode != 8 && $event.keyCode != 46 &&
                $event.keyCode != 37 && $event.keyCode != 39) {
            $event.preventDefault();
        }
    };

    $scope.pricePerItem = 0.001;
    $scope.totalChanged = function() {
        if ($scope.total > list.cnt) {
            $scope.total = list.cnt;
        }

        $scope.amount = ($scope.total * $scope.pricePerItem).toFixed(2);
        $scope.balance = ($scope.commonBalance - $scope.amount).toFixed(2);
        $scope.showErrorMessage = false;

        if ($scope.balance < 0) {
            $scope.balance = 0;
            $scope.total = $scope.commonBalance / $scope.pricePerItem;
            $scope.amount = ($scope.total * $scope.pricePerItem).toFixed(2);
        }
    }

    $scope.buy = function() {
        list.total = $scope.total;
        $scope.loading = true;

        listService.saveAndBuyList(list, function(response) {
            profileService.protected(
                credentialsService.getUser().id,
                credentialsService.getUser().password).
                    getUserBalance({ id: credentialsService.getUser().id }, function(response) {
                        var balance = response.data.toFixed(2);
                        var user = credentialsService.getUser();
                        user.balance = balance;
                        credentialsService.setUser(user);
                    });

            if (response.status == 'OK') {
                toasty.success({
                    title: 'Bought successfully',
                    msg: 'Records have been bought successfully.',
                    sound: false
                });
                $modalInstance.close();
            } else if (response.message == "reseller balance") {
                toasty.error({
                    title: 'Bought failed',
                    msg: 'Parent Account Suspended',
                    sound: false
                });
            } else {
                toasty.error({
                    title: 'Bought failed',
                    msg: 'Your balance changed before you bought this records. Try again.',
                    sound: false
                });
                init();
            }
            $scope.loading = false;
            $modalInstance.close();
        });
    }

    init();
} ]);
