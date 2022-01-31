angular.module('consumer_data_base').
controller('DashboardController',
    [ '$scope', '$document', '$stateParams', 'BASE_URL', 'systemService', 'cancelableDataService', 'localization', 'dataService', 'credentialsService', 'administrationService', 'toasty', '$timeout', '$modal', 'listService', 'alert', 'authService',
        function($scope, $document, $stateParams, BASE_URL, systemService, cancelableDataService, localization, dataService, credentialsService, administrationService, toasty, $timeout, $modal, listService, alert, authService) {

            $timeout(function () {
                /*if (!credentialsService.getUser().verified) {
                    toasty.info({
                        title: localization.localize('profile.user.not.verified'),
                        msg: localization.localize('profile.user.not.verified.message'),
                        sound: false
                    });
                }*/
            }, 500);

            $scope.getAccauntName = function () {
                return credentialsService.getUserName();
            }

            $scope.getDatesRangeTitle = function (array) {
                if (array[0] && array[1]) {
                    return array[0].title + ' - ' + array[1].title;
                } else if (array[0]) {
                    return array[0].title;
                } else if (array[1]) {
                    return array[1].title;
                }
            }

            dataService.dataSources({userId: credentialsService.getUser().id}, function (response) {
                $scope.dataSources = response.data;
                /*
                $scope.matchTable = new Map();
                for(datasource in $scope.dataSources){
                $scope.matchTable[response.data[datasource].name] = false;
                console.log("datasource"+response.data[datasource].name);
                }
                */
                if (response.data.length > 0) {
                    if (!$scope.dataType) {
                        $scope.dataType = response.data[0].name;
                    } else {
                        $scope.updateActiveTab();
                    }

                    initTables($scope.dataType, function () {
                        $scope.handleParams();
                    });
                }
            });

            $scope.BASE_URL = BASE_URL;

            $scope.isFilterDNCEnabled = false;
            $scope.isFilterEmptyPhonesEnabled = false;
            $scope.isFilterEmailEnabled = false;
            $scope.isMultipleGeographicParametersEnabled = false;
            $scope.isCustomersKeywordEnabled = false;
            $scope.isCarriersSearchEnabled = false;
            $scope.isMatchResponderEnabled = false;
            $scope.isMatchCraigslistEnabled = false;
            $scope.isBusinessFilterEmailEnabled = false;
            $scope.isMatchDirectoryEnabled = false;
            $scope.isMatchBusinessDetailedEnabled = false;
            $scope.isMatchOptInEnabled = false;
            $scope.isMatchFacebookEnable = false;
            $scope.isMatchConsumersEnabled = false;
            $scope.isMatchCallLeadsEnabled = false;
            $scope.isFilterWebsiteEnabled = false;
            //new matches
            $scope.isMatchHealthinsuranceleadsEnabled = false;
            $scope.isMatchHealthbuyersEnabled = false;
            $scope.isMatchInstagramEnabled = false;
            $scope.isMatchWhoisEnabled = false;
            $scope.isMatchBussiness2Enabled = false;
            $scope.isMatchEverydataEnabled = false;
            $scope.isMatchBussinessEnabled = false;


            $scope.isAdmin = function () {
                return credentialsService.getUser().admin > 0;
            }

            $scope.config = {
                filterDNC: false,
                filterEmptyPhone: false,
                filterEmail: false,
                filterBusinessEmail: false,
                filterWebsite: false,
                uniqueBusinessName: false,
                uniqueEmails: false,
                mobile: false,
                landlines: false,
                blackListMatch: false,
                consumerMatch: false,
                callLeadsMatch: false,
                craigslistMatch: false,
                directoryMatch: false,
                businessDetailedMatch: false,
                confirmed: false,
                optinMatch: false,
                facebookMatch: false
            };

            systemService.getUserDetails({userId: credentialsService.getUser().id}, function (response) {
                if (response.status == 'OK') {
                    $scope.isFilterDNCEnabled = response.data.filterDNC;
                    $scope.isFilterEmptyPhonesEnabled = response.data.filterEmptyPhone;
                    $scope.isFilterEmailEnabled = response.data.filterEmail;
                    $scope.isMultipleGeographicParametersEnabled = response.data.multipleGeographicParametersEnabled;
                    $scope.isCustomersKeywordEnabled = response.data.allowCustomersKeyword;
                    $scope.isBusinessKeywordEnabled = response.data.allowBusinessKeyword;
                    $scope.isCarriersSearchEnabled = response.data.allowCarriersSearch;
                    $scope.isMatchResponderEnabled = response.data.allowMatchResponder;
                    $scope.isMatchCallLeadsEnabled = $scope.isAdmin();
                    $scope.isMatchCraigslistEnabled = response.data.allowMatchCraigslist;
                    $scope.isBusinessFilterEmailEnabled = response.data.allowBusinessEmailFilter;
                    $scope.isMatchConsumersEnabled = response.data.allowMatchConsumers;
                    $scope.isMatchDirectoryEnabled = response.data.allowMatchDirectory;
                    $scope.isMatchBusinessDetailedEnabled = response.data.allowMatchBusinessDetailed;
                    $scope.isDetailedBusinessKeywordsEnabled = response.data.allowDetailedBusinessKeywords;
                    $scope.isMatchOptInEnabled = response.data.allowMatchOptIn;
                    $scope.isFilterWebsiteEnabled = credentialsService.getUser().id === document.permittedUserId;

                    if (response.data.allowedMatchesList) {
                        let allowedMatchesArray = response.data.allowedMatchesList.split(",");

                        allowedMatchesArray.forEach((item) => {
                            switch (item) {
                                case 'matchHealthinsuranceleadsEnabled':
                                    $scope.isMatchHealthinsuranceleadsEnabled = true;
                                    break;
                                case 'matchHealthbuyersEnabled':
                                    $scope.isMatchHealthbuyersEnabled = true;
                                    break;
                                case 'matchInstagramEnabled':
                                    $scope.isMatchInstagramEnabled = true;
                                    break;
                                case 'matchWhoisEnabled':
                                    $scope.isMatchWhoisEnabled = true;
                                    break;
                                case 'matchBussiness2Enabled':
                                    $scope.isMatchBussiness2Enabled = true;
                                    break;
                                case 'matchBussinessEnabled':
                                    $scope.isMatchBussinessEnabled = true;
                                    break;
                                case 'matchFacebookEnabled':
                                    $scope.isMatchFacebookEnable = true;
                                    break;
                                case 'matchEverydataEnabled':
                                    $scope.isMatchEverydataEnabled = true;
                                    break;
                            }
                        })
                    }
                }
            });

            $scope.ifKeywordsEnable = function () {
                if (!$scope.dataType) {
                    return false;
                }

                var result = ($scope.isCustomersKeywordEnabled && $scope.dataType == "consumer") ||
                    ($scope.isBusinessKeywordEnabled && $scope.dataType == "business") ||
                    ($scope.isDetailedBusinessKeywordsEnabled && $scope.dataType == "business detailed") ||
                    ($scope.isAdmin() && $scope.dataType == "OPT IN") ||
                    ($scope.dataType != "blacklist" &&
                        $scope.dataType != "consumer" && $scope.dataType != "business" &&
                        $scope.dataType != "business detailed");

                return result;
            }

            $scope.$watch('unique', function () {
                if ($scope.unique) {
                    $scope.config.filterEmptyPhone = false;
                    $scope.config.uniqueBusinessName = false;
                    $scope.config.uniqueEmails = false;
                    $scope.config.removeCorps = false;
                }
            });

            $scope.$watch('config.removeCorps', function () {
                if ($scope.config.removeCorps) {
                    $scope.config.uniqueBusinessName = false;
                    $scope.config.uniqueEmails = false;
                    $scope.unique = false;
                }
            });

            $scope.$watch('config.filterEmptyPhone', function () {
                if ($scope.config.filterEmptyPhone) {
                    $scope.unique = false;
                }
            });

            $scope.$watch('config.uniqueBusinessName', function () {
                if ($scope.config.uniqueBusinessName) {
                    $scope.unique = false;
                    $scope.config.removeCorps = false;
                }
            });

            $scope.$watch('config.mobile', function () {
                if ($scope.config.mobile) {
                    $scope.config.landlines = false;
                }
            });

            $scope.$watch('config.uniqueEmails', function () {
                if ($scope.config.uniqueEmails) {
                    $scope.unique = false;
                    $scope.config.removeCorps = false;
                }
            });

            $scope.$watch('config.landlines', function () {
                if ($scope.config.landlines) {
                    $scope.config.mobile = false;
                }
            });

            $scope.cleanSelectedMortgageRateValue = function (value) {
                $scope.selectedMortgageRate[value] = undefined;
                $scope.$broadcast('mortgageRateChanged');
            }

            var initTables = function (type, callback) {
                var numType = $scope.$root.types[type];

                var userId = credentialsService.getUser().id;
                var userPassword = credentialsService.getUser().password;

                administrationService.protected(userId, userPassword).tables({'type': numType}, function (response) {
                    $scope.tableName = response.data[0];
                    $scope.tables = response.data;
                    /* deete this art*/
                    var tablesList = [];

                    for(var i =0;i<$scope.tables.length;i++){
                        if($scope.tables[i].name.indexOf("_Apr") == -1){
                            tablesList.push($scope.tables[i]);
                        }
                        else{
                            if ($scope.getAccauntName() == 'admin' || $scope.getAccauntName() == 'phil') {
                                tablesList.push($scope.tables[i]);
                            }
                        }
                    }
                    $scope.tables = tablesList;
                    $scope.tableName = tablesList[0];
                    /* deete this art*/
                    if (callback) {
                        callback();
                    }
                });
            };

            $scope.resetKeyword = function () {
                $scope.keywords = [{keywords: '', selectedColumns: {}}];
            }

            $scope.addKeyword = function () {
                $scope.keywords.push({keyword: '', selectedColumns: {}});
            }

            $scope.removeKeyword = function (keyword) {
                $scope.keywords.splice($scope.keywords.indexOf(keyword), 1);

                if ($scope.keywords.length == 0) {
                    $scope.addKeyword();
                }
            }

            $scope.generateColumnsString = function (keyword) {
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

            $scope.tabConfig = {statesActiveTab: true};
            $scope.menu = {expanded: false};

            var documentClickBind = function (element) {
                if ($scope.menu.expanded && event.target.id !== 'toggle' &&
                    event.target.parentElement.id !== 'toggle') {
                    $scope.$apply(function () {
                        $scope.menu.expanded = false;
                    });
                }
            }
            $scope.$watch('menu.expanded', function (value) {
                if (value) {
                    $document.bind('click', documentClickBind);
                } else {
                    $document.unbind('click', documentClickBind);
                }
            });

            $scope.isTabEnabled = function (tab) {
                if ($scope.isMultipleGeographicParametersEnabled) {
                    return true;
                }

                var enableObject = {
                    'state': [$scope.selectedCounties, $scope.selectedCities, $scope.selectedTimeZones, $scope.selectedZipCodes, $scope.selectedAreaCodes, $scope.selectedCarriers],
                    'county': [$scope.selectedCities, $scope.selectedStates, $scope.selectedTimeZones, $scope.selectedZipCodes, $scope.selectedAreaCodes, $scope.selectedCarriers],
                    'city': [$scope.selectedCounties, $scope.selectedStates, $scope.selectedTimeZones, $scope.selectedZipCodes, $scope.selectedAreaCodes, $scope.selectedCarriers],
                    'timeZone': [$scope.selectedCounties, $scope.selectedCities, $scope.selectedStates, $scope.selectedZipCodes, $scope.selectedAreaCodes, $scope.selectedCarriers],
                    'zip': [$scope.selectedCounties, $scope.selectedCities, $scope.selectedStates, $scope.selectedTimeZones, $scope.selectedAreaCodes, $scope.selectedCarriers],
                    'areaCode': [$scope.selectedCounties, $scope.selectedCities, $scope.selectedStates, $scope.selectedTimeZones, $scope.selectedZipCodes, $scope.selectedCarriers],
                    'carrier': [$scope.selectedCounties, $scope.selectedCities, $scope.selectedStates, $scope.selectedTimeZones, $scope.selectedZipCodes, $scope.selectedAreaCodes]
                };

                var conditions = enableObject[tab];
                var enabled = true;

                for (var i = 0; i < conditions.length; i++) {
                    var condition = conditions[i];

                    if (getValues($scope.asArray(condition)).length > 0) {
                        return false;
                    }
                }

                return enabled;
            }

            $scope.resetParams = function () {
                $scope.lockSave = false;
                $scope.selectedStates = {};
                $scope.selectedCities = {};
                $scope.selectedZipCodes = {};
                $scope.selectedAreaCodes = {};
                $scope.$root.areaState = "AK";
                $scope.selectedCounties = {};
                $scope.selectedCarriers = {};
                $scope.omittedStates = {};
                $scope.omittedCities = {};
                $scope.omittedZipCodes = {};
                $scope.omittedAreaCodes = {};

                $scope.selectedAges = {};
                $scope.agesRange = {};
                $scope.dobs = {};
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
                //  $scope.config.matchTable = new Map();
                $scope.selectedSections = {};
                $scope.selectedDates = {};

                $scope.selectedTimeZones = {};

                $scope.selectedMaritalStatuses = {};
                $scope.selectedEthnicCodes = {};
                $scope.selectedLanguageCodes = {};
                $scope.selectedEthnicGroups = {};
                $scope.selectedReligionCodes = {};
                $scope.selectedProperties = {};
                $scope.selectedCompanyTypes = {};
                $scope.selectedCreditScores = {};
                $scope.selectedHispanicCountryCodes = {};

                $scope.selectedPropertyType = {};
                $scope.selectedOwnerType = {};
                $scope.selectedLengthOfResidence = {};
                $scope.selectedNumberOfPersonInLivingUnit = {};
                $scope.selectedNumberOfChildren = {};
                $scope.selectedInferredHouseHoldRank = {};
                $scope.selectedDomainSource = {};
                $scope.selectedFacebookJob = {};
                $scope.selectedFacebookHLastName = {};
                $scope.selectedFacebookGender = {};
                $scope.selectedFacebookStatus = {};
                $scope.selectedCarriersBrands = {};
                $scope.selectedConsumerCarrierName = {};
                $scope.searchConsumerCarrierName = {};
                $scope.searchFacebookHLastName = {};
                $scope.selectedFileDates = {};
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
                $scope.insertedNote = "";

                $scope.selectedRating = {};
                $scope.selectedActiveLines = {};
                $scope.selectedRange = {};
                $scope.selectedCategory = {};

                $scope.selectedYears = [];
                $scope.selectedModels = {};
                $scope.selectedMakes = {};

                $scope.count = 0;
                $scope.countNote = "";

                $scope.searchType = 'geographic';

                $scope.keywords = [{keywords: '', selectedColumns: {}}];
                $scope.unique = true;
                $scope.config.uniqueEmails = false;
                var userRole = $scope.getAccauntName();
                if (userRole == 'admin' || userRole == 'phil') {
                    $scope.config.showMatch = false;
                } else {
                    $scope.config.showMatch = true;
                }

                $scope.config.removeCorps = false;
                $scope.config.localNumbers = false;
                $scope.config.fbHispanicLNames = false;
                $scope.config.uniqueBusinessName = false;
                $scope.config.filterDNC = false;
                $scope.config.filterEmptyPhone = false;
                $scope.config.filterEmail = false;
                $scope.config.businessMatch = false;
                $scope.config.businessMatch2 = false;
                $scope.config.everydataMatch = false;
                $scope.config.whoisMatch = false;
                $scope.config.Consumer2019Match = false;
                $scope.config.healthBuyersMatch = false;
                $scope.config.consumer2018Match = false;
                $scope.config.healthInsuranceMatch = false;
                $scope.config.instagramMatch = false;
                $scope.config.filterBusinessEmail = false;
                $scope.config.filterWebsite = false;
                $scope.config.mobile = false;
                $scope.config.landlines = false;
                $scope.config.blackListMatch = false;
                $scope.config.consumerMatch = false;
                $scope.config.craigslistMatch = false;
                $scope.config.directoryMatch = false;
                $scope.config.businessDetailedMatch = false;
                $scope.config.confirmed = false;
                $scope.config.optinMatch = false;
                $scope.config.facebookMatch = false;
                $scope.config.callLeadsMatch = false;
            }

            $scope.resetParams();
            if ($stateParams.searchRequest) {
                $scope.searchRequest = $stateParams.searchRequest;

                $scope.dataType = $scope.$root.reversedTypes[$stateParams.searchRequest.dataType]
                if ($scope.dataType == "consumers") {
                    $scope.dataType = "consumer";
                }
            }

            var asObject = function (array) {
                var result = {};
                for (var i = 0; i < array.length; i++) {
                    result[array[i].split("_")[0]] = true;
                }

                return result;
            }

            $scope.isPhoneTypeEnabled = function () {
                if ($scope.tableName && $scope.tableName.name.indexOf("Original") != -1) {
                    return credentialsService.getUser().admin;
                }

                return false;
            }

            $scope.applySearchRequest = function (configObject) {
                $scope.count = 0;
                $scope.searchType = 'geographic';
                $scope.unique = configObject.unique;
                $scope.config.confirmed = configObject.confirmed;
                $scope.config.uniqueEmails = configObject.uniqueEmails;
                $scope.config.uniqueBusinessName = configObject.uniqueBusinessName;
                $scope.config.filterDNC = configObject.filterDNC;
                $scope.config.removeCorps = configObject.removeCorps;
                $scope.config.localNumbers = configObject.localNumbers;
                $scope.config.fbHispanicLNames = configObject.fbHispanicLNames;
                $scope.config.filterEmptyPhone = configObject.filterEmptyPhone;
                $scope.config.filterEmail = configObject.filterEmail;
                $scope.config.businessMatch = configObject.businessMatch;
                $scope.config.businessMatch2 = configObject.businessMatch2;
                $scope.config.everydataMatch = configObject.everydataMatch;
                $scope.config.healthBuyersMatch = configObject.healthBuyersMatch;
                $scope.config.consumer2018Match = configObject.consumer2018Match;
                $scope.config.healthInsuranceMatch = configObject.healthInsuranceMatch;
                $scope.config.whoisMatch = configObject.whoisMatch;
                $scope.config.Consumer2019Match = configObject.Consumer2019Match;
                $scope.config.instagramMatch = configObject.instagramMatch;
                $scope.config.filterBusinessEmail = configObject.filterBusinessEmail;
                $scope.config.callLeadsMatch = configObject.callLeadsMatch;
                $scope.config.filterWebsite = configObject.filterWebsite;
                $scope.config.blackListMatch = configObject.blackListMatch;
                $scope.config.consumerMatch = configObject.consumerMatch;
                $scope.config.craigslistMatch = configObject.craigslistMatch;
                $scope.config.directoryMatch = configObject.directoryMatch;
                $scope.config.businessDetailedMatch = configObject.businessDetailedMatch;
                $scope.config.optinMatch = configObject.optinMatch;
                $scope.config.facebookMatch = configObject.facebookMatch;

                $scope.config.mobile = false;
                $scope.config.landlines = false;

                for (var i = 0; i < $scope.tables.length; i++) {
                    if (configObject.tableName === $scope.tables[i].name) {
                        $scope.tableName = $scope.tables[i];
                        break;
                    }
                }

                $scope.keywords = [{keywords: '', selectedColumns: {}}];
                var keywords = configObject.keywords;
                if (keywords && keywords.length > 0 && keywords[0]) {
                    $scope.keywords = [];
                    for (var i = 0; i < keywords.length; i++) {
                        if (keywords[i]) {
                            var selectedColumns = {};
                            for (var j = 0; j < configObject.keywordsColumns[i].length; j++) {
                                var column = configObject.keywordsColumns[i][j];
                                var formattedColumn = column.toLowerCase().replace("_", " ") + "|" + column;
                                selectedColumns[formattedColumn] = true;
                            }

                            $scope.keywords.push({keyword: keywords[i], 'selectedColumns': selectedColumns});
                        }
                    }
                }
            }

            $scope.handleParams = function () {
                if ($stateParams.searchRequest) {
                    $scope.applySearchRequest($stateParams.searchRequest);
                }
            }

            $scope.localize = function (key) {
                return localization.localize(key);
            }

            $scope.clearPage = function () {
                $scope.menu.expanded = false;
                $scope.resetParams();

                $scope.loading = false;
                cancelableDataService.cancelRequest();
            }

            $scope.stopRequest = function () {
                $scope.menu.expanded = false;

                $scope.loading = false;
                cancelableDataService.cancelRequest();
            }

            $scope.getCount = function () {

                $scope.lockSave = false;
                console.log('reset lock')
                console.log($scope.lockSave)

                if($scope.getAccauntName() == "admin" || $scope.getAccauntName() == "phil"){
                    var modalInstance = $modal.open({
                        templateUrl: BASE_URL + '/assets/partials/modal/insert.note.html',
                        controller: 'InsertNoteModalController',
                    });
                    modalInstance.result.then(function (textNote) {


                        $scope.insertedNote = textNote;
                        $scope.countNote = textNote;
                        var request = getRequest();

                        if ($scope.isRestricted && !$scope.checkRequest(request)) {
                            alert.getUserConfirmation("Please select at least one geographic parameter for your list");
                        } else {
                            $scope.loading = true;
                            cancelableDataService.geographic(request, function (response) {
                                $scope.count = response.data.count;
                                $scope.countNote = response.data.textNote;
                                $scope.loading = false;
                            }, function (response) {
                                $scope.loading = false;
                            });
                        }
                    });
                }
                else{
                    var request = getRequest();

                    if ($scope.isRestricted && !$scope.checkRequest(request)) {
                        alert.getUserConfirmation("Please select at least one geographic parameter for your list");
                    } else {
                        $scope.loading = true;
                        cancelableDataService.geographic(request, function (response) {
                            $scope.count = response.data.count;
                            $scope.loading = false;
                        }, function (response) {
                            $scope.loading = false;
                        });
                    }

                }

            }

            $scope.checkRequest = function (request) {
                if (request.dataType !== $scope.types.consumer) {
                    return true;
                }

                var count = 0;

                if (request.states.length > 0) count++;
                if (request.counties.length > 0) count++;
                if (request.cities.length > 0) count++;
                if (request.timeZones.length > 0) count++;
                if (request.carriers.length > 0) count++;
                if (request.zipCodes.length > 0) count++;
                if (request.areaCodes.length > 0) count++;

                return count > 0;
            }

            $scope.isRestricted = true;
            authService.checkIfRestricted({id: credentialsService.getUser().id}, function (response) {
                $scope.isRestricted = response.data;
            });

            $scope.formatZipCode = function (value) {
                value = value + '';
                if (value.length === 4) {
                    return '0' + value;
                } else {
                    return value;
                }
            }

            $scope.dataTypeChanged = function () {
                $scope.searchType = 'geographic';
                $scope.resetKeyword();

                $scope.searchRequest = undefined;
                $scope.resetParams();

                $scope.updateActiveTab();
                initTables($scope.dataType);
            }

            $scope.clearDobs = function () {
                $scope.dobs = {};
            }

            $scope.formatDob = function () {
                var dobs = $scope.asArray($scope.dobs);
                if (dobs.length == 1) {
                    return dobs[0].split('|')[0];
                } else if (dobs.length == 2) {
                    var leftDob = dobs[0].split('|')[0];
                    var rightDob = dobs[1].split('|')[0];

                    var leftDate = new Date(leftDob).getTime();
                    var rightDate = new Date(rightDob).getTime();

                    if (leftDate > rightDate) {
                        var tmp = leftDob;
                        leftDob = rightDob;
                        rightDob = tmp;
                    }

                    return leftDob + ' - ' + rightDob;
                } else {
                    return '';
                }
            }

            $scope.formatSourceName = function (name) {
                name = name.toLowerCase();

                if (name == 'bing2019') {
                    return 'bing';
                } else if (name == 'angieslist2019' || name == 'angieslist2020') {
                    return 'angieslist';
                } else if (name == 'superpages2019') {
                    return 'superpages';
                } else if (name == 'yelp2020') {
                    return 'yelp';
                } else if (name == 'google2020') {
                    return 'google';
                } else {
                    return name;
                }
            }

            $scope.updateActiveTab = function () {
                $scope.tabConfig.statesActiveTab = true;
            }

            var getRequest = function () {
                /*
                var userRole = $scope.getAccauntName();
                if(userRole != "phil"){
                    $scope.config.localNumbers = false;
                }

               // console.log($scope.matchTable['business']);
                var arrMatch = [];
                for(matchtable in $scope.matchTable){
                if($scope.matchTable[matchtable] == true){
                arrMatch.push($scope.$root.types[matchtable]);
                }

             //   console.log($scope.matchTable[matchtable]);
                }
            */
                var dataType = $scope.$root.types[$scope.dataType];
                if(dataType != 6 && dataType != 0 && dataType != 9) {
                    $scope.config.localNumbers = false;
                }
                //  console.log("code"+$scope.config.fbHispanicLNames);
                $scope.currentJson =JSON.stringify( {
                    states: getValues($scope.asArray($scope.selectedStates)),
                    carriers: $scope.asArray($scope.selectedCarriers),
                    carriersPhones: getSelectedCarriersPhones(),
                    consumerCarriers:$scope.asArray($scope.selectedConsumerCarrierName),
                    domainSources: $scope.asArray($scope.selectedDomainSource),
                    facebookGenders:getValues($scope.asArray($scope.selectedFacebookGender)),
                    facebookStatus:getValues($scope.asArray($scope.selectedFacebookStatus)),
                    facebookJobs: $scope.asArray($scope.selectedFacebookJob),
                    facebookHLName:$scope.asArray($scope.selectedFacebookHLastName),
                    carriersBrands: $scope.asArray($scope.selectedCarriersBrands),
                    'datime': getValues($scope.asArray($scope.selectedFileDates)),
                    countNote:$scope.insertedNote,
                    omittedStates: getValues($scope.asArray($scope.omittedStates)),
                    cities: $scope.asArray($scope.selectedCities),
                    omittedCities: $scope.asArray($scope.omittedCities),
                    zipCodes: $scope.asArray($scope.selectedZipCodes),
                    omittedZipCodes: $scope.asArray($scope.omittedZipCodes),
                    counties: $scope.asArray($scope.selectedCounties),
                    areaCodes: $scope.asArray($scope.selectedAreaCodes),
                    stateAreacode:$scope.$root.areaState,
                    omittedAreaCodes: $scope.asArray($scope.omittedAreaCodes),
                    ages: getValues($scope.asArray($scope.selectedAges)),
                    agesRange: getValues($scope.asArray($scope.agesRange)),
                    dobs: getValues($scope.asArray($scope.dobs)),
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
                    'dataType': dataType,
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
                    phoneTypes: getSelectedPhonesTypes(),
                    keywords: getKeywordsArray(),
                    keywordsColumns: getKeywordsColumnsArray(),
                    tableName: $scope.tableName.name,
                    unique: $scope.unique,
                    localNumbers: $scope.config.localNumbers,
                    fbHispanicLName: $scope.config.fbHispanicLNames,
                    confirmed: $scope.config.confirmed,
                    uniqueEmails: $scope.config.uniqueEmails,
                    uniqueBusinessName: $scope.config.uniqueBusinessName,
                    filterDNC: $scope.config.filterDNC,
                    removeCorps: $scope.config.removeCorps,
                    filterEmptyPhone: $scope.config.filterEmptyPhone,
                    filterEmail: $scope.config.filterEmail,
                    businessMatch: $scope.config.businessMatch,
                    businessMatch2: $scope.config.businessMatch2,
                    everydataMatch: $scope.config.everydataMatch,
                    consumerMatch2018: $scope.config.consumer2018Match,
                    whoisMatch: $scope.config.whoisMatch,
                    consumerMatch2019: $scope.config.Consumer2019Match,
                    instagramMatch: $scope.config.instagramMatch,
                    healthBuyersMatch: $scope.config.healthBuyersMatch,
                    healthInsuranceMatch: $scope.config.healthInsuranceMatch,
                    filterBusinessEmail: $scope.config.filterBusinessEmail,
                    filterWebsite: $scope.config.filterWebsite,
                    blackListMatch: $scope.config.blackListMatch,
                    consumerMatch: $scope.config.consumerMatch,
                    callLeadsMatch: $scope.config.callLeadsMatch,
                    facebookMatch: $scope.config.facebookMatch,
                    craigslistMatch: $scope.config.craigslistMatch,
                    directoryMatch: $scope.config.directoryMatch,
                    businessDetailedMatch: $scope.config.businessDetailedMatch,
                    optinMatch: $scope.config.optinMatch,
                    timeZones: getValues($scope.asArray($scope.selectedTimeZones)),
                    maritalStatuses: getValues($scope.asArray($scope.selectedMaritalStatuses)),
                    ethnicCodes: getValues($scope.asArray($scope.selectedEthnicCodes)),
                    languageCodes: getValues($scope.asArray($scope.selectedLanguageCodes)),
                    ethnicGroups: getValues($scope.asArray($scope.selectedEthnicGroups)),
                    religionCodes: getValues($scope.asArray($scope.selectedReligionCodes)),
                    hispanicCountryCodes: getValues($scope.asArray($scope.selectedHispanicCountryCodes)),
                    properties: getValues($scope.asArray($scope.selectedProperties)),
                    companyTypes: getValues($scope.asArray($scope.selectedCompanyTypes)),
                    creditScores: $scope.asArray($scope.selectedCreditScores),

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
                });

                console.log($scope.currentJson);
                return {
                    states: getValues($scope.asArray($scope.selectedStates)),
                    carriers: $scope.asArray($scope.selectedCarriers),
                    carriersPhones: getSelectedCarriersPhones(),
                    consumerCarriers:$scope.asArray($scope.selectedConsumerCarrierName),
                    domainSources: $scope.asArray($scope.selectedDomainSource),
                    facebookGenders:getValues($scope.asArray($scope.selectedFacebookGender)),
                    facebookStatus:getValues($scope.asArray($scope.selectedFacebookStatus)),
                    facebookJobs: $scope.asArray($scope.selectedFacebookJob),
                    facebookHLName:$scope.asArray($scope.selectedFacebookHLastName),
                    carriersBrands: $scope.asArray($scope.selectedCarriersBrands),
                    'datime': getValues($scope.asArray($scope.selectedFileDates)),
                    countNote:$scope.insertedNote,
                    omittedStates: getValues($scope.asArray($scope.omittedStates)),
                    cities: $scope.asArray($scope.selectedCities),
                    omittedCities: $scope.asArray($scope.omittedCities),
                    zipCodes: $scope.asArray($scope.selectedZipCodes),
                    omittedZipCodes: $scope.asArray($scope.omittedZipCodes),
                    counties: $scope.asArray($scope.selectedCounties),
                    areaCodes: $scope.asArray($scope.selectedAreaCodes),
                    stateAreacode:$scope.$root.areaState,
                    omittedAreaCodes: $scope.asArray($scope.omittedAreaCodes),
                    ages: getValues($scope.asArray($scope.selectedAges)),
                    agesRange: getValues($scope.asArray($scope.agesRange)),
                    dobs: getValues($scope.asArray($scope.dobs)),
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
                    'dataType': dataType,
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
                    phoneTypes: getSelectedPhonesTypes(),
                    keywords: getKeywordsArray(),
                    keywordsColumns: getKeywordsColumnsArray(),
                    tableName: $scope.tableName.name,
                    unique: $scope.unique,
                    localNumbers: $scope.config.localNumbers,
                    fbHispanicLName: $scope.config.fbHispanicLNames,
                    confirmed: $scope.config.confirmed,
                    uniqueEmails: $scope.config.uniqueEmails,
                    uniqueBusinessName: $scope.config.uniqueBusinessName,
                    filterDNC: $scope.config.filterDNC,
                    removeCorps: $scope.config.removeCorps,
                    filterEmptyPhone: $scope.config.filterEmptyPhone,
                    filterEmail: $scope.config.filterEmail,
                    businessMatch: $scope.config.businessMatch,
                    businessMatch2: $scope.config.businessMatch2,
                    everydataMatch: $scope.config.everydataMatch,
                    consumerMatch2018: $scope.config.consumer2018Match,
                    whoisMatch: $scope.config.whoisMatch,
                    consumerMatch2019: $scope.config.Consumer2019Match,
                    instagramMatch: $scope.config.instagramMatch,
                    healthBuyersMatch: $scope.config.healthBuyersMatch,
                    healthInsuranceMatch: $scope.config.healthInsuranceMatch,
                    filterBusinessEmail: $scope.config.filterBusinessEmail,
                    filterWebsite: $scope.config.filterWebsite,
                    blackListMatch: $scope.config.blackListMatch,
                    consumerMatch: $scope.config.consumerMatch,
                    callLeadsMatch: $scope.config.callLeadsMatch,
                    facebookMatch: $scope.config.facebookMatch,
                    craigslistMatch: $scope.config.craigslistMatch,
                    directoryMatch: $scope.config.directoryMatch,
                    businessDetailedMatch: $scope.config.businessDetailedMatch,
                    optinMatch: $scope.config.optinMatch,
                    timeZones: getValues($scope.asArray($scope.selectedTimeZones)),
                    maritalStatuses: getValues($scope.asArray($scope.selectedMaritalStatuses)),
                    ethnicCodes: getValues($scope.asArray($scope.selectedEthnicCodes)),
                    languageCodes: getValues($scope.asArray($scope.selectedLanguageCodes)),
                    ethnicGroups: getValues($scope.asArray($scope.selectedEthnicGroups)),
                    religionCodes: getValues($scope.asArray($scope.selectedReligionCodes)),
                    hispanicCountryCodes: getValues($scope.asArray($scope.selectedHispanicCountryCodes)),
                    properties: getValues($scope.asArray($scope.selectedProperties)),
                    companyTypes: getValues($scope.asArray($scope.selectedCompanyTypes)),
                    creditScores: $scope.asArray($scope.selectedCreditScores),

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

            var getSelectedCarriersPhones = function () {
                var result = [];
                var values = $scope.asArray($scope.selectedCarriers);

                for (var i = 0; i < values.length; i++) {
                    var value = values[i];

                    var phones = document.carriersData[value];
                    if (phones) {
                        for (var j = 0; j < phones.length; j++) {
                            result.push(phones[j]);
                        }
                    }
                }

                return result;
            }

            var getSelectedPhonesTypes = function () {
                if ($scope.config.mobile) {
                    return [1];
                } else if ($scope.config.landlines) {
                    return [0];
                } else {
                    return [];
                }
            }

            var getKeywordsArray = function () {
                var result = [];
                for (var i = 0; i < $scope.keywords.length; i++) {
                    result.push($scope.keywords[i].keyword);
                }

                return result;
            }

            var getKeywordsColumnsArray = function () {
                var result = [];
                for (var i = 0; i < $scope.keywords.length; i++) {
                    result.push(getValues($scope.asArray($scope.keywords[i].selectedColumns)));
                }

                return result;
            }

            var getValues = function (states) {
                var result = [];
                for (var i = 0; i < states.length; i++) {
                    result.push(states[i].split('|')[1]);
                }
                return result;
            }

            var getDateValues = function (dates) {
                if (!dates[0] && !dates[1]) {
                    return [];
                }

                var results = [-1, -1];
                for (var i = 0; i < 2; i++) {
                    if (dates[i]) {
                        results[i] = dates[i].value;
                    }
                }

                return results
            }

            $scope.asArray = function (object) {
                var result = [];
                for (var prop in object) {
                    if (object[prop]) {
                        result.push(prop);
                    }
                }

                return result;
            }

            $scope.asSortedArray = function (object) {
                var result = $scope.asArray(object);
                result.sort()

                return result;
            }

            $scope.selectColumns = function (keyword) {
                var modalInstance = $modal.open({
                    templateUrl: BASE_URL + '/assets/partials/modal/select.columns.html',
                    controller: 'SelectKeywordColumnsController',
                    resolve: {
                        selectedColumns: function () {
                            return copyObject(keyword.selectedColumns);
                        },
                        type: function () {
                            return $scope.dataType;
                        }
                    }
                });

                modalInstance.result.then(function (columns) {
                    for (var prop in keyword.selectedColumns) {
                        keyword.selectedColumns[prop] = false;
                    }

                    for (var prop in columns) {
                        keyword.selectedColumns[prop] = columns[prop];
                    }
                });
            }

            var copyObject = function (src) {
                var result = {}
                for (var prop in src) {
                    result[prop] = src[prop];
                }

                return result;
            }

            $scope.formattedAmount = function (value) {
                if (value) {
                    return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                } else {
                    return "0";
                }
            }

            $scope.getCountDetails = function () {
                if ($scope.count > 0) {
                    var modalInstance = $modal.open({
                        templateUrl: BASE_URL + '/assets/partials/modal/count.details.html',
                        controller: 'RequestCountDetailsModalController',
                        resolve: {
                            request: function () {
                                return getRequest();
                            },
                            list: function () {
                                return undefined;
                            }
                        }
                    });
                }
            }
            $scope.saveTxtNoteList = function (countNote) {

                var modalInstance = $modal.open({
                    templateUrl: BASE_URL + '/assets/partials/modal/save.list.html',
                    controller: 'SaveListTxtCountModalController',
                    resolve: {
                        txtCount: function () {
                            return countNote;
                        }
                    }
                });
                modalInstance.result.then(function (name) {
                    var type = $scope.$root.types[$scope.dataType];
                    var sameJson = isEqualsJson($scope.currentJson, JSON.stringify(getRequest()) );

                    if(sameJson == true && $scope.lockSave == false) {
                        console.log("Json is the same");
                        $scope.lockSave = false;
                        var request = {
                            'name': name,
                            userId: credentialsService.getUser().id,
                            cnt: $scope.count,
                            date: new Date().getTime(),
                            'type': type,
                            tableName: $scope.tableName.name,
                            'request': JSON.stringify(getRequest())
                        };

                        toasty.info({
                            title: localization.localize('lists.saving.in.progress'),
                            msg: localization.localize('lists.saving.in.progress.message'),
                            timeout: 10000,
                            sound: false
                        });

                        listService.saveList(request, function (response) {
                            if (response.status === 'OK') {
                                toasty.success({
                                    title: localization.localize('lists.saved.successfully'),
                                    msg: localization.localize('lists.saved.successfully.message'),
                                    timeout: 10000,
                                    sound: false
                                });
                            }
                        })
                    }else{
                        $scope.lockSave = true;
                        toasty.error({
                            title: "Make get count again before save list",
                            msg: "Filters param was changed",
                            timeout: 10000,
                            sound: false
                        });
                    }

                });
            }

            var isEqualsJson = (obj1,obj2)=>{
                keys1 = Object.keys(obj1);
                keys2 = Object.keys(obj2);

                //return true when the two json has same length and all the properties has same value key by key
                return keys1.length === keys2.length && Object.keys(obj1).every(key=>obj1[key]==obj2[key]);
            }

            $scope.saveList = function () {
                var modalInstance = $modal.open({
                    templateUrl: BASE_URL + '/assets/partials/modal/save.list.html',
                    controller: 'SaveListModalController',
                });

                modalInstance.result.then(function (name) {
                    var type = $scope.$root.types[$scope.dataType];

                    var sameJson = isEqualsJson($scope.currentJson, JSON.stringify(getRequest()) );

                    if(sameJson == true && $scope.lockSave == false){
                        $scope.lockSave = false;
                        console.log("Json is the same");
                        var request = {
                            'name': name,
                            userId: credentialsService.getUser().id,
                            cnt: $scope.count,
                            date: new Date().getTime(),
                            'type': type,
                            tableName: $scope.tableName.name,
                            'request': JSON.stringify(getRequest())
                        };

                        toasty.info({
                            title: localization.localize('lists.saving.in.progress'),
                            msg: localization.localize('lists.saving.in.progress.message'),
                            timeout: 10000,
                            sound: false
                        });

                        listService.saveList(request, function (response) {
                            if (response.status === 'OK') {
                                toasty.success({
                                    title: localization.localize('lists.saved.successfully'),
                                    msg: localization.localize('lists.saved.successfully.message'),
                                    timeout: 10000,
                                    sound: false
                                });
                            }
                        })
                    }else{
                        $scope.lockSave = true;
                        toasty.error({
                            title: "Make get count again before save list",
                            msg: "Filters param was changed",
                            timeout: 10000,
                            sound: false
                        });
                    }



                });
            }
        }]).controller('RequestCountDetailsModalController',
    ['$scope', '$modalInstance', 'request', 'dataService', 'list',
        function ($scope, $modalInstance, request, dataService, list) {
            if (list) {
                request = JSON.parse(list.request);

                if (list.request == '{}') {
                    request.states = [];
                    request.timeZones = [];
                    request.cities = [];
                    request.zipCodes = [];
                    request.counties = [];
                    request.areaCodes = [];
                }
            }
            $scope.searchRequest = request;

            $scope.selectedStates = {};
            $scope.selectedTimeZones = {};
            $scope.selectedCities = request.cities;
            $scope.selectedZipCodes = request.zipCodes;
            $scope.selectedCounties = request.counties;
            $scope.selectedAreaCodes = request.areaCodes;

            $scope.states = document.states;
            for (var i = 0; i < $scope.states.length; i++) {
                for (var j = 0; j < $scope.searchRequest.states.length; j++) {
                    if ($scope.searchRequest.states[j] == $scope.states[i].code) {
                        var state = $scope.states[i];
                        var key = state.name + "|" + state.code;
                        $scope.selectedStates[key] = true;
                        break;
                    }
                }
            }

            $scope.timeZones = document.timeZones;
            if ($scope.searchRequest && $scope.searchRequest.timeZones) {
                for (var i = 0; i < $scope.searchRequest.timeZones.length; i++) {
                    for (var j = 0; j < $scope.timeZones.length; j++) {
                        if ($scope.searchRequest.timeZones[i] == $scope.timeZones[j].abbreviation) {
                            var timeZone = $scope.timeZones[j];
                            $scope.selectedTimeZones[timeZone.name + "|" + timeZone.abbreviation] = true;

                            break;
                        }
                    }
                }
            }

            $scope.formattedAmount = function (value) {
                return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            }

            $scope.asArray = function (object) {
                if (Array.isArray(object)) {
                    return object;
                }

                var result = [];
                for (var prop in object) {
                    if (object[prop]) {
                        result.push(prop);
                    }
                }

                return result;
            }

            var getValue = function (value) {
                return value.split('|')[1];
            }

            $scope.ready = function () {
                return $scope.requests.length > 0 && $scope.requests.length === $scope.handled.length;
            }

            $scope.download = function () {
                var element = document.createElement('a');
                element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent($scope.generateTextContent()));
                element.setAttribute('download', "detailed_count.txt");

                element.style.display = 'none';
                document.body.appendChild(element);

                element.click();
                document.body.removeChild(element);
            }

            $scope.generateTextContent = function () {
                var result = "";

                var parameters = [
                    {name: "timeZone", title: "Time Zone", values: $scope.asArray($scope.selectedTimeZones)},
                    {name: "state", title: "State", values: $scope.asArray($scope.selectedStates)},
                    {name: "county", title: "County", values: $scope.selectedCounties},
                    {name: "city", title: "City", values: $scope.selectedCities},
                    {name: "zip", title: "Zip", values: $scope.selectedZipCodes},
                    {name: "areaCode", title: "Area Code", values: $scope.selectedAreaCodes}
                ];

                parameters.forEach(function (parameter) {
                    if (parameter.values.length > 0) {
                        result = result + parameter.title + "\n";

                        parameter.values.forEach(function (value) {
                            var handledValue = $scope.handled[$scope.requestIndex(parameter.name, value)];

                            result = result + value.split("|")[0].split("_")[0] + ": " +
                                handledValue + "\n";
                        })

                        result = result + "\n";
                    }
                });

                return result;
            }

            $scope.handled = [];
            $scope.requests = [];
            for (var i = 0; i < $scope.asArray($scope.selectedTimeZones).length; i++) {
                $scope.requests.push({type: "timeZone", value: $scope.asArray($scope.selectedTimeZones)[i]});
            }

            for (var i = 0; i < $scope.asArray($scope.selectedStates).length; i++) {
                $scope.requests.push({type: "state", value: $scope.asArray($scope.selectedStates)[i]});
            }

            for (var i = 0; i < $scope.selectedCounties.length; i++) {
                $scope.requests.push({type: "county", value: $scope.selectedCounties[i]});
            }

            for (var i = 0; i < $scope.selectedCities.length; i++) {
                $scope.requests.push({type: "city", value: $scope.selectedCities[i]});
            }

            for (var i = 0; i < $scope.selectedZipCodes.length; i++) {
                $scope.requests.push({type: "zip", value: $scope.selectedZipCodes[i]});
            }

            for (var i = 0; i < $scope.selectedAreaCodes.length; i++) {
                $scope.requests.push({type: "areaCode", value: $scope.selectedAreaCodes[i]});
            }

            $scope.requestIndex = function (type, value) {
                for (var i = 0; i < $scope.requests.length; i++) {
                    if ($scope.requests[i].type == type && $scope.requests[i].value == value) {
                        return i;
                    }
                }

                return 0;
            }

            $scope.isEmpty = function () {
                return $scope.asArray($scope.selectedTimeZones).length == 0 &&
                    $scope.asArray($scope.selectedStates).length == 0 &&
                    $scope.asArray($scope.selectedCounties).length == 0 &&
                    $scope.asArray($scope.selectedCities).length == 0 &&
                    $scope.asArray($scope.selectedZipCodes).length == 0 &&
                    $scope.asArray($scope.selectedAreaCodes).length == 0;
            }

            $scope.dismiss = function () {
                $modalInstance.dismiss();
            }

            var index = 0;
            var handleNextRequest = function () {
                if ($scope.requests.length > index) {
                    var request = $scope.requests[index];
                    var requestForSend = {type: request.type, value: request.value};
                    index = index + 1;

                    if (requestForSend.type == 'state' || requestForSend.type == "timeZone") {
                        requestForSend.value = getValue(requestForSend.value);
                    }

                    var data = {'request': requestForSend, 'searchRequest': $scope.searchRequest};
                    if (list) {
                        data.listId = list.id;
                    }

                    dataService.detailedCount(data, function (response) {
                        $scope.handled.push(response.data);
                        handleNextRequest();
                    });
                }
            }
            handleNextRequest();
        }]).controller('SaveListTxtCountModalController',
    ['$scope', '$modalInstance','txtCount',
        function ($scope, $modalInstance,txtCount) {
            $scope.name = txtCount;
            $scope.close = function () {
                $modalInstance.dismiss();
            }

            $scope.save = function () {
                $modalInstance.close($scope.name);
            }
        }]).controller('SaveListModalController',
    ['$scope', '$modalInstance',
        function ($scope, $modalInstance) {
            $scope.close = function () {
                $modalInstance.dismiss();
            }

            $scope.save = function () {
                $modalInstance.close($scope.name);
            }
        }]).controller('InsertNoteModalController',
    ['$scope', '$modalInstance',
        function ($scope, $modalInstance) {
            $scope.close = function () {
                $modalInstance.dismiss();
            }

            $scope.save = function () {
                $modalInstance.close($scope.textNote);
            }
        }]).controller('SelectKeywordColumnsController',
    ['$scope', '$modalInstance', 'selectedColumns', 'type',
        function ($scope, $modalInstance, selectedColumns, type) {
            $scope.selectedColumns = selectedColumns;
            $scope.config = {all: false};
            if (type === 'consumers') {
                $scope.columns = ["first name|FN", "last name|LN", "address|ADDR", "apartment|APT", "phone|phone"];
            } else if (type === 'business') {
                $scope.columns = ["title|TITLE", "industry|INDUSTRY", "address|ADDRESS", "company name|COMPANY_NAME",
                    "contact name|CONTACT_NAME", "SIC code|SIC_CODE", "phone|phone", "website|WWW"];
            } else if (type === 'business2') {
                $scope.columns = ["address|ADDRESS", "company name|COMPANY_NAME", "contact name|CONTACT_NAME", "SIC code|SIC_CODE", "phone|phone", "website|WWW"];
            } else if (type === 'directory') {
                $scope.columns = ["industry|INDUSTRY", "address|ADDRESS", "company name|COMPANY_NAME", "phone|phone", "website|websites"];
            } else if (type === 'craigslist') {
                $scope.columns = ["industry|INDUSTRY", "phone|PHONE", "website|website"];
            } else if (type === 'WHOIS') {
                $scope.columns = ["website|WEBSITE", "email|email", "phone|phone", "name|NAME", "business|BUSINESS", "address|ADDRESS"];
            } else if (type === 'search engine') {
                $scope.columns = ["website|WEBSITE", "phone|PHONE"];
            } else if (type === 'consumer2018' || type === 'consumer') {
                $scope.columns = ["first name|PERSONFIRSTNAME", "last name|PERSONLASTNAME", "address|PRIMARYADDRESS", "phone|phone", "email|EMAIL", "website|website"];
            } else if (type === 'instagram' || type === 'instagram2020') {
                $scope.columns = ["full name|fullname", "username|username", "category|category", "email|email", "phone|phone", "website|website"];
            } else if (type === 'OPT IN') {
                $scope.columns = ["first name|firstname", "last name|lastname", "address|address", "email|email", "phone|phone", "source|source"];
            } else if (type === 'AUTO') {
                $scope.columns = ["first name|first_name", "last name|last_name", "address|address", "phone|phone", "make|make", "model|model", "VIN|vin"];
            } else if (type === 'blacklist' || type === 'callleads') {
                $scope.columns = ['phone|phone'];
            } else if (type === 'healthbuyers') {
                $scope.columns = ['email|email', 'name|name', 'phone|phone'];
            } else if (type === 'healthinsuranceleads') {
                $scope.columns = ["first name|firstName", "second name|lastName", "address|address", "phone|phone", "email|email", "IP|ip"];
            } else if (type === 'linked in') {
                $scope.columns = ["email|email", "company name|companyName", "first name|firstName", "last name|lastName", "title|title", "address|address", "fax|fax", "website|website", "linked in id|linkedInId"]
            } else if (type === 'business detailed') {
                $scope.columns = ["title|title", "address|address", "company name|company", "contact name|name", "SIC code|SIC_CODE", "phone|phone", "website|WWW"];
            } else if (type === 'student') {
                $scope.columns = ["first name|firstName", "second name|secondName", "address|address", "phone|phone", "email|email", "IP|ip"];
            }

            $scope.changeAll = function () {
                $scope.config.all = !$scope.config.all;
                $scope.selectAll();
            }

            $scope.selectAll = function () {
                if ($scope.config.all) {
                    for (var i = 0; i < $scope.columns.length; i++) {
                        $scope.selectedColumns[$scope.columns[i]] = true;
                    }
                } else {
                    for (var prop in $scope.selectedColumns) {
                        $scope.selectedColumns[prop] = false;
                    }
                }
            }

            $scope.close = function () {
                $modalInstance.dismiss();
            }

            $scope.done = function () {
                $modalInstance.close($scope.selectedColumns);
            }
        }]);
