angular.module( 'consumer_data_base' ).
controller( 'SalesController',
    [ '$scope',
        function( $scope ) {
            $scope.sales = [ 'LESS THAN $500,000',
                '$500,000 TO $1 MILLION',
                '$1 TO 2.5 MILLION',
                '$2.5 TO 5 MILLION',
                '$5 TO 10 MILLION',
                '$10 TO 20 MILLION',
                '$20 TO 50 MILLION',
                '$50 TO 100 MILLION',
                '$100 TO 500 MILLION',
                '$500 MILLION TO $1 BILLION' ];

            if ($scope.searchRequest && $scope.searchRequest.sales) {
                for (var i = 0; i < $scope.searchRequest.sales.length; i++) {
                    $scope.selectedSales[$scope.searchRequest.sales[i]] = true
                }
            }
        }
    ] )
    .controller( 'EmployeesController',
        [ '$scope',
            function( $scope ) {
                $scope.counts = [ '1 TO 4',
                    '5 TO 9',
                    '10 TO 19',
                    '20 TO 49',
                    '50 TO 99',
                    '100 TO 249',
                    '250 TO 499',
                    '500 TO 999',
                    '1,000 TO 4,999',
                    '5,000 TO 9,999',
                    'OVER 10,000' ];

                if ($scope.searchRequest && $scope.searchRequest.employeeCount) {
                    for (var i = 0; i < $scope.searchRequest.employeeCount.length; i++) {
                        $scope.selectedEmployeeCount[$scope.searchRequest.employeeCount[i]] = true
                    }
                }
            }
        ] )
    .controller('YearFoundedController',
        ['$scope',
            function($scope) {
                $scope.values = {};

                $scope.onStartDateChanged = function() {
                    $scope.addDate(true);

                    if ($scope.values.endDate) {
                        $scope.values.before = false;
                        $scope.values.after = false;

                        $scope.addDate(false);
                    }
                }

                $scope.onEndDateChanged = function() {
                    $scope.addDate(false);

                    if ($scope.values.startDate) {
                        $scope.values.before = false;
                        $scope.values.after = false;

                        $scope.addDate(true);
                    }
                }

                $scope.onStartDateIntervalParamChanged = function() {
                    if ($scope.values.before) {
                        $scope.values.endDate = undefined;
                        $scope.updateDashBoardObject(false)
                    }

                    $scope.addDate(true, undefined);
                }

                $scope.onEndDateIntervalParamChanged = function() {
                    if ($scope.values.after) {
                        $scope.values.startDate = undefined;
                        $scope.updateDashBoardObject(true)
                    }

                    $scope.addDate(false, undefined);
                }

                $scope.addDate = function(startDate) {
                    if (startDate && !$scope.values.startDate) { return; }
                    if (!startDate && !$scope.values.endDate) { return; }

                    var title = document.getElementById("startYear").value;
                    if (!startDate) {
                        var title = document.getElementById("endYear").value;
                    }

                    var value = 0;
                    if (startDate) {
                        value = $scope.values.startDate.getTime();
                    } else {
                        value = $scope.values.endDate.getTime();
                    }

                    if (startDate && $scope.values.before) {
                        title = title + " and earlier";
                        value = value + (value > 0 ? 999 : -999);
                    }

                    if (!startDate && $scope.values.after) {
                        title = title + " and later";
                        value = value + (value > 0 ? 999 : -999);
                    }

                    $scope.updateDashBoardObject(startDate, {'title': title, 'value': value })
                }

                $scope.updateDashBoardObject = function(startDate, value) {
                    var index = startDate ? 0 : 1;
                    $scope.selectedYears[index] = value;
                }

                $scope.getDateValue = function(time) {
                    var date = new Date(parseInt(time));
                    var month = (date.getMonth() + 1) + "";
                    if (month.length == 1) {
                        month = "0" + month;
                    }

                    return month + "." + date.getFullYear();
                }

                $scope.getYearValue = function(time) {
                    var date = new Date(parseInt(time));
                    return date.getFullYear() + "";
                }

                $scope.fillDates = function(selectedArray, dates, yearOnly) {
                    if (dates[0] && dates[0] != -1) {
                        var title = yearOnly ? $scope.getYearValue(dates[0]) : $scope.getDateValue(dates[0]);
                        if (parseInt(dates[0]) % 1000 == 999 || parseInt(dates[0]) % 1000 == -999) {
                            title = title + " and earlier";
                        }

                        selectedArray[0] = {'title': title, value: parseInt(dates[0])};
                    }

                    if (dates[1] && dates[1] != -1) {
                        var title = yearOnly ? $scope.getYearValue(dates[1]) : $scope.getDateValue(dates[1]);
                        if (parseInt(dates[1]) % 1000 == 999 || parseInt(dates[1]) % 1000 == -999) {
                            title = title + " and later";
                        }

                        selectedArray[1] = {'title': title, value: parseInt(dates[1])};
                    }
                }

                if ($scope.searchRequest && $scope.searchRequest.yearsRange) {
                    $scope.fillDates($scope.selectedYears, $scope.searchRequest.yearsRange, true);
                }
            }
        ])
    .controller('CreditScoreController',
        ['$scope',
            function($scope) {
                $scope.creditScores = ['A+', 'A', 'B+', 'B', 'C+', 'C', 'D+', 'D'];

                if ($scope.searchRequest && $scope.searchRequest.creditScores) {
                    for (var i = 0; i < $scope.searchRequest.creditScores.length; i++) {
                        $scope.selectedCreditScores[$scope.searchRequest.creditScores[i]] = true
                    }
                }
            }
        ])
    .controller('CompanyTypeController',
        ['$scope',
            function($scope) {
                $scope.types = [
                    {name: "Minority Owned", value: "minorityOwned"},
                    {name: "Small Business", value: "smallBusiness"},
                    {name: "Large Business", value: "largeBusiness"},
                    {name: "Home Business", value: "homeBusiness"},
                    {name: "Import Export", value: "importExport"},
                    {name: "Public Company", value: "publicCompany"},
                    {name: "Headquarters Branch", value: "headquartersBranch"},
                    {name: "Stock Exchange", value: "stockExchange"},
                    {name: "Franchise ", value: "franchiseFlag"}
                ];

                if ($scope.searchRequest && $scope.searchRequest.companyTypes) {
                    for (var i = 0; i < $scope.types.length; i++) {
                        for (var j = 0; j < $scope.searchRequest.companyTypes.length; j++) {
                            if ($scope.types[i].value == $scope.searchRequest.companyTypes[j]) {
                                var type = $scope.types[i];
                                $scope.selectedCompanyTypes[type.name + "|" + type.value] = true;
                                break;
                            }
                        }
                    }
                }
            }
        ])
    .controller( 'TitlesController',
        [ '$scope',
            function( $scope ) {
                $scope.titles = [ 'administration executive',
                    'administrator',
                    'board member',
                    'business development',
                    'ceo',
                    'chairman',
                    'chief administrative officer',
                    'chief executive officer',
                    'chief financial officer',
                    'chief marketing officer',
                    'chief operating officer',
                    'cio/cto',
                    'controller',
                    'corporate communications executive',
                    'director',
                    'educator',
                    'engineering/technical',
                    'executive',
                    'executive director',
                    'executive officer',
                    'executive vice president',
                    'facilities',
                    'finance',
                    'finance executive',
                    'general manager',
                    'human resources',
                    'human resources executive',
                    'information technology',
                    'international',
                    'it executive',
                    'legal',
                    'manager',
                    'manufacturing',
                    'manufacturing executive',
                    'marketing',
                    'marketing executive',
                    'office manager',
                    'operations',
                    'operations executive',
                    'other',
                    'owner',
                    'partner',
                    'president',
                    'principal',
                    'publisher/editor',
                    'purchasing executive',
                    'regional manager',
                    'religious leader',
                    'sales',
                    'sales executive',
                    'senior vice president',
                    'site manager',
                    'telecommunications executive',
                    'treasurer',
                    'vice president' ];

                if ($scope.searchRequest && $scope.searchRequest.titles) {
                    for (var i = 0; i < $scope.searchRequest.titles.length; i++) {
                        $scope.selectedTitles[$scope.searchRequest.titles[i]] = true
                    }
                }
            }])
    .controller( 'BusinessDetailedTitlesController',
        [ '$scope', 'dictionaryService',
            function( $scope, dictionaryService ) {
                $scope.titles = [];
                $scope.params = {filter: '', limit: 30, count: 0, all: false}

                $scope.loadMore = function() {
                    $scope.params.limit = $scope.params.limit + 30;
                    $scope.updateTitles();
                }

                $scope.onSearchChanged = function() {
                    $scope.params.limit = 30;
                    $scope.updateTitles();
                }

                $scope.onAllStatesChanged = function() {
                    for (var i = 0; i < $scope.titles.length; i++) {
                        var title = $scope.titles[i];
                        $scope.selectedTitles[title] = $scope.config.all;
                    }
                }

                $scope.updateTitles = function() {
                    var request = {searchValue: $scope.params.filter,
                        limit: $scope.params.limit};

                    dictionaryService.businessTitles(request, function(response) {
                        if (response.status == 'OK') {
                            $scope.titles = response.data.data;
                            $scope.params.count = response.data.total;
                        }
                    });
                }
                $scope.updateTitles();

                if ($scope.searchRequest && $scope.searchRequest.titles) {
                    for (var i = 0; i < $scope.searchRequest.titles.length; i++) {
                        $scope.selectedTitles[$scope.searchRequest.titles[i]] = true
                    }
                }
            }
        ] ).controller( 'DatesOptInController',
    [ '$scope',
        function( $scope ) {
            $scope.config = {};

            $scope.onStartDateChanged = function(date) {

                for (var prop in $scope.selectedFileDates) {
                    if (prop.indexOf("Start date") == 0) {
                        $scope.selectedFileDates[prop] = false;
                    }
                }

                var value = document.getElementById("datimeOptInStartDate").value;
                $scope.selectedFileDates["Start date: " + value + "|" + date.getTime()] = true;
            }

            $scope.onEndDateChanged = function(date) {
                for (var prop in $scope.selectedFileDates) {
                    if (prop.indexOf("End date") == 0) {
                        $scope.selectedFileDates[prop] = false;
                    }
                }

                var value = document.getElementById("datimeOptInEndDate").value;
                $scope.selectedFileDates["End date: " + value + "|" + date.getTime()] = true;
            }

            $scope.getDateValue = function(time) {
                var date = new Date(parseInt(time));
                var month = (date.getMonth() + 1) + "";
                if (month.length == 1) {
                    month = "0" + month;
                }

                return month + "." + date.getFullYear();
            }

            if ($scope.searchRequest && $scope.searchRequest.dates) {
                if ($scope.searchRequest && $scope.searchRequest.dates[0]) {
                    var value = $scope.getDateValue($scope.searchRequest.dates[0]);
                    $scope.selectedFileDates["Start date: " + value + "|" + $scope.searchRequest.dates[0]] = true;
                }

                if ($scope.searchRequest && $scope.searchRequest.dates[1]) {
                    var value = $scope.getDateValue($scope.searchRequest.dates[1]);
                    $scope.selectedFileDates["End date: " + value + "|" + $scope.searchRequest.dates[1]] = true;
                }
            }
        }
    ] )
    .controller( 'LinkedInTitlesController',
        [ '$scope', 'dictionaryService',
            function( $scope, dictionaryService ) {
                $scope.titles = [];
                $scope.params = {filter: '', limit: 30, count: 0, all: false}

                $scope.loadMore = function() {
                    $scope.params.limit = $scope.params.limit + 30;
                    $scope.updateTitles();
                }

                $scope.onSearchChanged = function() {
                    $scope.params.limit = 30;
                    $scope.updateTitles();
                }

                $scope.onAllStatesChanged = function() {
                    for (var i = 0; i < $scope.titles.length; i++) {
                        var title = $scope.titles[i];
                        $scope.selectedTitles[title] = $scope.config.all;
                    }
                }

                $scope.updateTitles = function() {
                    var request = {searchValue: $scope.params.filter,
                        limit: $scope.params.limit};

                    dictionaryService.titles(request, function(response) {
                        if (response.status == 'OK') {
                            $scope.titles = response.data.data;
                            $scope.params.count = response.data.total;
                        }
                    });
                }
                $scope.updateTitles();

                if ($scope.searchRequest && $scope.searchRequest.titles) {
                    for (var i = 0; i < $scope.searchRequest.titles.length; i++) {
                        $scope.selectedTitles[$scope.searchRequest.titles[i]] = true
                    }
                }
            }
        ] )
    .controller( 'IndustriesController',
        [ '$scope', '$filter', 'dictionaryService',
            function( $scope, $filter, dictionaryService ) {
                $scope.industries = [];
                $scope.selectedIndustry = { limit: 30, all: false };
                $scope.loading = true;

                $scope.selectAll = function() {
                    if ( $scope.selectedIndustry.all ) {
                        var filtered = $filter( 'filter' )( $scope.industries, $scope.selectedIndustry.filter );
                        for ( var i = 0; i < filtered.length; i++ ) {
                            var industry = filtered[ i ];
                            $scope.selectedIndustries[ industry.toLowerCase() + "|" + industry ] = true;
                        }
                    } else {
                        for ( var prop in $scope.selectedIndustries ) {
                            $scope.selectedIndustries[ prop ] = undefined;
                        }
                    }
                }

                dictionaryService.industries( function( response ) {
                    $scope.loading = false;
                    if ( response.status === 'OK' ) {
                        $scope.industries = response.data;

                        if ($scope.searchRequest && $scope.searchRequest.industries) {
                            for (var i = 0; i < $scope.searchRequest.industries.length; i++) {
                                var industry = $scope.searchRequest.industries[i];
                                $scope.selectedIndustries[industry.toLowerCase() + "|" + industry] = true;
                            }
                        }
                    } else {
                        console.error( response );
                    }
                } );

                $scope.loadMore = function() {
                    $scope.selectedIndustry.limit = $scope.selectedIndustry.limit + 30;
                }
            }
        ] )
    .controller( 'DetailedIndustriesController',
        [ '$scope', '$filter', 'dictionaryService',
            function( $scope, $filter, dictionaryService ) {
                $scope.detailedBusinessIndustries = [];
                $scope.selectedIndustry = {limit: 30, all: false, filter: ''};
                $scope.loading = true;
                $scope.total = 0;

                $scope.onFilterChanged = function() {
                    $scope.selectedIndustry.limit = 30;
                    $scope.search();
                }

                $scope.selectAll = function() {
                    if ( $scope.selectedIndustry.all ) {
                        var filtered = $scope.detailedBusinessIndustries;
                        for ( var i = 0; i < filtered.length; i++ ) {
                            var industry = filtered[ i ];
                            $scope.selectedIndustries[ industry.industry.toLowerCase() + "|" + industry.sic ] = true;
                        }
                    } else {
                        for ( var prop in $scope.selectedIndustries ) {
                            $scope.selectedIndustries[ prop ] = undefined;
                        }
                    }
                }

                $scope.search = function() {
                    var request = {searchValue: $scope.selectedIndustry.filter,
                        limit: $scope.selectedIndustry.limit};

                    $scope.loading = true;
                    dictionaryService.detailedBusinessIndustries(request, function( response ) {
                        $scope.loading = false;
                        if ( response.status === 'OK' ) {
                            $scope.detailedBusinessIndustries = response.data.data;
                            $scope.total = response.data.total;
                        }
                    } );
                }
                $scope.search();

                if ($scope.searchRequest && $scope.searchRequest.industries && $scope.searchRequest.industries.length > 0) {
                    dictionaryService.selectedDetailedBusinessIndustries({values: $scope.searchRequest.industries}, function(response) {
                        var industries = {};
                        for (var i = 0; i < response.data.length; i++) {
                            industries[response.data[i].sic] = response.data[i];
                        }

                        for (var i = 0; i < $scope.searchRequest.industries.length; i++) {
                            var industry = $scope.searchRequest.industries[i];
                            $scope.selectedIndustries[industries[industry].industry.toLowerCase() + "|" + industries[industry].sic] = true;
                        }
                    });
                }

                $scope.loadMore = function() {
                    $scope.selectedIndustry.limit = $scope.selectedIndustry.limit + 30;
                    $scope.search();
                }
            }
        ] )
    .controller( 'SourcesController',
        [ '$scope', '$filter', 'dictionaryService', 'credentialsService',
            function( $scope, $filter, dictionaryService, credentialsService ) {
                $scope.sources = [];
                $scope.selectedSource = { limit: 30, all: false };
                $scope.loading = true;

                $scope.isAdmin = function() {
                    return credentialsService.getUser().admin > 0;
                }

                $scope.isAccessGranted = function() {
                    return credentialsService.getUser().id == 1389 ||
                        credentialsService.getUser().id == 16 ||
                        credentialsService.getUser().id == 304;
                }

                $scope.selectAll = function() {
                    if ( $scope.selectedSource.all ) {
                        var filtered = $filter( 'filter' )( $scope.sources, $scope.selectedSource.filter );
                        for ( var i = 0; i < filtered.length; i++ ) {
                            var source = filtered[ i ];
                            $scope.selectedSources[ source.toLowerCase() + "|" + source ] = true;
                        }
                    } else {
                        for ( var prop in $scope.selectedSources ) {
                            $scope.selectedSources[ prop ] = undefined;
                        }
                    }
                }

                dictionaryService.sources( function( response ) {
                    $scope.loading = false;
                    if ( response.status === 'OK' ) {
                        $scope.sources = [];

                        if ($scope.searchRequest && $scope.searchRequest.industries) {
                            for (var i = 0; i < response.data.length; i++) {
                                $scope.sources.push(response.data[i]);
                            }
                            for (var i = 0; i < $scope.searchRequest.sources.length; i++) {
                                var source = $scope.searchRequest.sources[i];
                                $scope.selectedSources[source.toLowerCase() + "|" + source] = true;
                            }
                        }else{
                            for (var i = 0; i < response.data.length; i++) {
                                $scope.sources.push(response.data[i]);
                                $scope.selectedSources[response.data[i].name.toLowerCase() + "|" + response.data[i].name] = true;
                            }
                        }
                    } else {
                        console.error( response );
                    }
                } );

                $scope.loadMore = function() {
                    $scope.selectedSource.limit = $scope.selectedSource.limit + 30;
                }
            }
        ] ).controller( 'PhilSourcesController',
    [ '$scope', '$filter', 'dictionaryService', 'credentialsService',
        function( $scope, $filter, dictionaryService, credentialsService ) {
            $scope.sources = [];
            $scope.selectedSource = { limit: 30, all: false };
            $scope.loading = true;

            $scope.isAdmin = function() {
                return credentialsService.getUser().admin > 0;
            }

            $scope.isAccessGranted = function() {
                return credentialsService.getUser().id == 1389 ||
                    credentialsService.getUser().id == 16 ||
                    credentialsService.getUser().id == 304;
            }

            $scope.selectAll = function() {
                if ( $scope.selectedSource.all ) {
                    var filtered = $filter( 'filter' )( $scope.sources, $scope.selectedSource.filter );
                    for ( var i = 0; i < filtered.length; i++ ) {
                        var source = filtered[ i ];
                        $scope.selectedSources[ source.toLowerCase() + "|" + source ] = true;
                    }
                } else {
                    for ( var prop in $scope.selectedSources ) {
                        $scope.selectedSources[ prop ] = undefined;
                    }
                }
            }

            dictionaryService.sources( function( response ) {
                $scope.loading = false;
                if ( response.status === 'OK' ) {
                    $scope.sources = [];
                        $scope.sources.push({name: "debtmarch21", disabled: false});
                        $scope.sources.push({name: "debtmay21", disabled: false});
                        $scope.sources.push({name: "debtjun21", disabled: false});

                    if ($scope.searchRequest && $scope.searchRequest.industries) {
                        for (var i = 0; i < $scope.searchRequest.sources.length; i++) {
                            var source = $scope.searchRequest.sources[i];
                            $scope.selectedSources[source.toLowerCase() + "|" + source] = true;
                        }
                    }
                } else {
                    console.error( response );
                }
            } );

            $scope.loadMore = function() {
                $scope.selectedSource.limit = $scope.selectedSource.limit + 30;
            }
        }
    ] ).controller( 'EverydataSourcesController',
    [ '$scope', '$filter', 'dictionaryService', 'credentialsService',
        function( $scope, $filter, dictionaryService, credentialsService ) {
            $scope.sources = [];
            $scope.selectedSource = { limit: 30, all: false };
            $scope.loading = true;

            $scope.isAdmin = function() {
                return credentialsService.getUser().admin > 0;
            }

            $scope.isAccessGranted = function() {
                return credentialsService.getUser().id == 1389 ||
                    credentialsService.getUser().id == 16 ||
                    credentialsService.getUser().id == 304;
            }

            $scope.selectAll = function() {
                if ( $scope.selectedSource.all ) {
                    var filtered = $filter( 'filter' )( $scope.sources, $scope.selectedSource.filter );
                    for ( var i = 0; i < filtered.length; i++ ) {
                        var source = filtered[ i ];
                        $scope.selectedSources[ source.toLowerCase() + "|" + source ] = true;
                    }
                } else {
                    for ( var prop in $scope.selectedSources ) {
                        $scope.selectedSources[ prop ] = undefined;
                    }
                }
            }

            dictionaryService.everydataSources( function( response ) {
                $scope.loading = false;
                if ( response.status === 'OK' ) {
                    $scope.sources = [];
                   // $scope.sources.push({name: "debtmarch21", disabled: false});
                  //  $scope.sources.push({name: "debtmay21", disabled: false});
                   // $scope.sources.push({name: "debtjun21", disabled: false});
                    for (var i = 0; i < response.data.length; i++) {
                        $scope.sources.push(response.data[i]);
                    }

                    if ($scope.searchRequest && $scope.searchRequest.industries) {


                        for (var i = 0; i < $scope.searchRequest.sources.length; i++) {
                            var source = $scope.searchRequest.sources[i];
                            $scope.selectedSources[source.toLowerCase() + "|" + source] = true;
                        }
                    }
                } else {
                    console.error( response );
                }
            } );

            $scope.loadMore = function() {
                $scope.selectedSource.limit = $scope.selectedSource.limit + 30;
            }
        }
    ] )
    .controller( 'StudentSourcesController',
        [ '$scope', '$filter', 'dictionaryService', 'credentialsService',
            function( $scope, $filter, dictionaryService, credentialsService ) {
                $scope.sources = [];
                $scope.selectedSource = { limit: 30, all: false };
                $scope.loading = true;

                $scope.isAdmin = function() {
                    return credentialsService.getUser().admin > 0;
                }

                $scope.selectAll = function() {
                    if ( $scope.selectedSource.all ) {
                        var filtered = $filter( 'filter' )( $scope.sources, $scope.selectedSource.filter );
                        for ( var i = 0; i < filtered.length; i++ ) {
                            var source = filtered[ i ];
                            $scope.selectedSources[ source.toLowerCase() + "|" + source ] = true;
                        }
                    } else {
                        for ( var prop in $scope.selectedSources ) {
                            $scope.selectedSources[ prop ] = undefined;
                        }
                    }
                }

                dictionaryService.studentSources( function( response ) {
                    $scope.loading = false;
                    if ( response.status === 'OK' ) {
                        $scope.sources = [];
                        for (var i = 0; i < response.data.length; i++) {
                            $scope.sources.push(response.data[i]);
                        }

                        if ($scope.searchRequest && $scope.searchRequest.industries) {
                            for (var i = 0; i < $scope.searchRequest.sources.length; i++) {
                                var source = $scope.searchRequest.sources[i];
                                $scope.selectedSources[source.toLowerCase() + "|" + source] = true;
                            }
                        }
                    } else {
                        console.error( response );
                    }
                } );

                $scope.loadMore = function() {
                    $scope.selectedSource.limit = $scope.selectedSource.limit + 30;
                }
            }
        ] )
    .controller( 'OptInSourcesController',
        [ '$scope', '$filter', 'dictionaryService',
            function( $scope, $filter, dictionaryService ) {
                $scope.sources = [];
                $scope.params = {filter: '', limit: 30, count: 0, all: false}

                $scope.loadMore = function() {
                    $scope.params.limit = $scope.params.limit + 30;
                    $scope.updateSources();
                }

                $scope.onSearchChanged = function() {
                    $scope.params.limit = 30;
                    $scope.updateSources();
                }

                $scope.onAllSourcesChanged = function() {
                    for (var i = 0; i < $scope.sources.length; i++) {
                        var source = $scope.sources[i];
                        $scope.selectedSources[source.toLowerCase() + "|" + source] = $scope.config.all;
                    }
                }

                $scope.updateSources = function() {
                    var request = {searchValue: $scope.params.filter,
                        limit: $scope.params.limit};

                    dictionaryService.optinSources(request, function(response) {
                        if (response.status == 'OK') {
                            $scope.sources = response.data.data;
                            $scope.params.count = response.data.total;
                        }
                    });
                }
                $scope.updateSources();

                if ($scope.searchRequest && $scope.searchRequest.sources) {
                    for (var i = 0; i < $scope.searchRequest.sources.length; i++) {
                        var source = $scope.searchRequest.sources[i];
                        $scope.selectedSources[source.toLowerCase() + "|" + source] = true;
                    }
                }
            }
        ] )
    .controller( 'AgeRangeController',
        [ '$scope',
            function($scope,) {
                $scope.ages = {};
                $scope.onStartChanged = function() {
                    var value = $scope.ages.rangeStart;
                    for (var prop in $scope.agesRange) {
                        if (prop.indexOf("From") != -1) {
                            $scope.agesRange[prop] = false;
                        }
                    }

                    if (value == '') {
                        $scope.agesRange["From: " + value + "|" + value] = false;
                    } else {
                        $scope.agesRange["From: " + value + "|" + value] = true;
                    }
                }

                $scope.onEndChanged = function() {
                    var value = $scope.ages.rangeEnd;
                    for (var prop in $scope.agesRange) {
                        if (prop.indexOf("To") != -1) {
                            $scope.agesRange[prop] = false;
                        }
                    }

                    if (value == '') {
                        $scope.agesRange["To: " + value + "|" + value] = false;
                    } else {
                        $scope.agesRange["To: " + value + "|" + value] = true;
                    }
                }

                if ($scope.searchRequest && $scope.searchRequest.agesRange) {
                    if ($scope.searchRequest.agesRange[0]) {
                        var value = $scope.searchRequest.agesRange[0];
                        $scope.agesRange["From: " + value + "|" + value] = true;
                    }

                    if ($scope.searchRequest.agesRange[1]) {
                        var value = $scope.searchRequest.agesRange[1];
                        $scope.agesRange["To: " + value + "|" + value] = true;
                    }
                }
            }
        ])
    .controller( 'SicController',
        [ '$scope',
            function( $scope ) {
                $scope.selectedState = {};

                $scope.addRawSicCodes = function() {
                    var values = $scope.selectedState.rawSicCodes.split( "," );
                    for ( var i = 0; i < values.length; i++ ) {
                        $scope.selectedSics[ values[ i ].trim() ] = true;
                        if ( i + 1 === 50 ) {
                            break;
                        }
                    }
                    $scope.selectedState.rawSicCodes = '';
                }

                $scope.addSicCodesRange = function() {
                    $scope.sicRange.fromSic = $scope.selectedState.fromSic;
                    $scope.sicRange.toSic = $scope.selectedState.toSic;

                    $scope.selectedState.fromSic = undefined;
                    $scope.selectedState.toSic = undefined;
                }

                if ($scope.searchRequest && $scope.searchRequest.sics) {
                    for (var i = 0; i < $scope.searchRequest.sics.length; i++) {
                        $scope.selectedSics[$scope.searchRequest.sics[i]] = true;
                    }
                }

                if ($scope.searchRequest && $scope.searchRequest.fromSic && $scope.searchRequest.toSic) {
                    $scope.sicRange.fromSic = $scope.searchRequest.fromSic;
                    $scope.sicRange.toSic = $scope.searchRequest.toSic;
                }
            }
        ] )
    .controller( 'SectionsController',
        [ '$scope',
            function($scope) {
                $scope.states = document.states;
                $scope.selectedSection = {limit: 40};
                //$scope.sections = ['sof','trd','sls','etc','ofc','fns','fbh','mdc','aos','mnu','lgs','sks','hum','csr','hsd','crs','lab','fud','evs','edu','cms','hvo','hea','ptd','prk','bts','med','sec','lss','brw','bfd','pml','reb','cps','off','mar','hss','ctd','lbs','brk','trp','fgs','clk','lac','lbg','biz','chl','brx','mnh','ppd','nph','nwc','que','evl','jsy','fct','thp','atl','mas','rts','hil','lgi','pas','wet','dmg','tlg','lee','sob','spa','doc','ret','gbs','nva','mld','evg','wyn','pen','pnl','trv','pbc','oly','eat','cbd','wch','dal','nch','crg','chc','see','tac','skc','nat','trb','hvd','cpg','cph','rvd','wvl','mcd','kit','sno','est','rch','clc','rds','big','mlt','csd','fod','wsc','oah','sfc','bod','wtd','cys','mau','sfv','wst','nvn','nco','hdo','bmw','nos','snd','nwb','grq','had','msd','tch','nsd','nby','atd','van','mad','eby','sby','eld','wrg','acc','ndf','wcl','wad','rej','ftw','mdf','stn','sgv','lgb','hnp','ram','ant','sdf','sgd','jwd','tid','cwg','egr','syd','mcb','col','tfr','sat','csw','ank','dak','nwi','sox','wat','psc','okl','ssd','lgl','npo','bnc','wsh','bus','tld','mod','mss','oak','scz','sci','bpd','bid','tor','ths','ard','grg','yrk','drh','bra','esd','tad','yam','bkd','cld','wri','wan','kau','web','mpd','vgm','sad','art','bfs','avo','vgd','eng','mol','grd','zip','ele','tls','app','msg','fuo','for','tag','bab','spo','hsh','jwl','rvs','pho','clt','hab','mob','reo','mat','clo','emd','atq','bar','bks','tix','laf','kid','tro','gms','vac','snw','sub','pet','pol','com','vnn','eve','roo','muc','rid','gov','cls','bad','cto','sdp','sha','sop','pts','emq','hou','sys','bpo','wto','boa','rnr','mcy','vol','sbw','phd','swp','bik','grp','ats','mpo','avd','act','apa','mis','bop','bdp'];
                $scope.sections = [];

                $scope.sectionsValues = new Map();
                $scope.craigslists = [
                    {
                        "3digit": "AOS",
                        "value": "AUTOMOTIVE SERVICES"
                    },
                    {
                        "3digit": "BTS",
                        "value": "BEAUTY SERVICES"
                    },
                    {
                        "3digit": "CMS",
                        "value": "CELLPHONE/MOBILE SERVICES"
                    },
                    {
                        "3digit": "CPS",
                        "value": "COMPUTER SERVICES"
                    },
                    {
                        "3digit": "CRS",
                        "value": "CREATIVE SERVICES"
                    },
                    {
                        "3digit": "CYS",
                        "value": "CYCLE SERVICES"
                    },
                    {
                        "3digit": "EVS",
                        "value": "EVENT SERVICES"
                    },
                    {
                        "3digit": "FGS",
                        "value": "FARM&GARDENING SERVICES"
                    },
                    {
                        "3digit": "FNS",
                        "value": "FINANCIAL SERVICES"
                    },
                    {
                        "3digit": "HWS",
                        "value": "HEALTH/WELLNESS"
                    },
                    {
                        "3digit": "HSS",
                        "value": "HOUSEHOLD SERVICES"
                    },
                    {
                        "3digit": "LBS",
                        "value": "LABOR &MOVING"
                    },
                    {
                        "3digit": "LGS",
                        "value": "LEGAL SERVICES"
                    },
                    {
                        "3digit": "LSS",
                        "value": "LESSONS & TUTORING"
                    },
                    {
                        "3digit": "MAS",
                        "value": "MARINE SERVICES"
                    },
                    {
                        "3digit": "PAS",
                        "value": "PET SERVICES"
                    },
                    {
                        "3digit": "RTS",
                        "value": "REAL ESTATE SERVICES"
                    },
                    {
                        "3digit": "SKS",
                        "value": "SKILLED TRADE SERVICES"
                    },
                    {
                        "3digit": "BIZ",
                        "value": "SMALL BIZ ADS"
                    },
                    {
                        "3digit": "TRV",
                        "value": "TRAVEL/VACATION"
                    },
                    {
                        "3digit": "WET",
                        "value": "WRITE/EDIT/TRANS"
                    },
                    {
                        "3digit": "ATA",
                        "value": "ANTIQUES"
                    },
                    {
                        "3digit": "PPA",
                        "value": "APPLIANCES"
                    },
                    {
                        "3digit": "ARA",
                        "value": "ARTS+CRAFTS"
                    },
                    {
                        "3digit": "SNA",
                        "value": "ATV/UTVS/SNOW"
                    },
                    {
                        "3digit": "PTA",
                        "value": "AUTO PARTS"
                    },
                    {
                        "3digit": "AVA",
                        "value": "AVIATION"
                    },
                    {
                        "3digit": "BAA",
                        "value": "BABY+KIDS"
                    },
                    {
                        "3digit": "BAR",
                        "value": "BARTER"
                    },
                    {
                        "3digit": "HAA",
                        "value": "BEAUTY+HLTH"
                    },
                    {
                        "3digit": "BIP",
                        "value": "BIKE PARTS"
                    },
                    {
                        "3digit": "BIA",
                        "value": "BIKES"
                    },
                    {
                        "3digit": "BPA",
                        "value": "BOAT PARTS"
                    },
                    {
                        "3digit": "BOO",
                        "value": "BOATS"
                    },
                    {
                        "3digit": "BKA",
                        "value": "BOOKS"
                    },
                    {
                        "3digit": "BFA",
                        "value": "BUSINESS"
                    },
                    {
                        "3digit": "CTA",
                        "value": "CARS+TRUCKS"
                    },
                    {
                        "3digit": "EMA",
                        "value": "CDS/DVDS/VHS"
                    },
                    {
                        "3digit": "MOA",
                        "value": "CELLPHONES"
                    },
                    {
                        "3digit": "CLA",
                        "value": "CLOTHES+ACC"
                    },
                    {
                        "3digit": "CBA",
                        "value": "COLLECTIBLES"
                    },
                    {
                        "3digit": "SYP",
                        "value": "COMPUTER PARTS"
                    },
                    {
                        "3digit": "SYA",
                        "value": "COMPUTERS"
                    },
                    {
                        "3digit": "ELA",
                        "value": "ELECTRONICS"
                    },
                    {
                        "3digit": "GRA",
                        "value": "FARM+GARDEN"
                    },
                    {
                        "3digit": "ZIP",
                        "value": "FREE STUFF"
                    },
                    {
                        "3digit": "FUA",
                        "value": "FURNITURE"
                    },
                    {
                        "3digit": "GMS",
                        "value": "GARAGE SALES"
                    },
                    {
                        "3digit": "FOA",
                        "value": "GENERAL"
                    },
                    {
                        "3digit": "HVA",
                        "value": "HEAVY EQUIPMENT"
                    },
                    {
                        "3digit": "HAS",
                        "value": "HOUSEHOLD"
                    },
                    {
                        "3digit": "JWA",
                        "value": "JEWELRY"
                    },
                    {
                        "3digit": "MAA",
                        "value": "MATERIALS"
                    },
                    {
                        "3digit": "MPA",
                        "value": "MOTORCYCLE PARTS"
                    },
                    {
                        "3digit": "MCA",
                        "value": "MOTORCYCLES"
                    },
                    {
                        "3digit": "MSA",
                        "value": "MUSIC INSTR"
                    },
                    {
                        "3digit": "PHA",
                        "value": "PHOTO+VIDEO"
                    },
                    {
                        "3digit": "RVA",
                        "value": "RVs"
                    },
                    {
                        "3digit": "SGA",
                        "value": "SPORTING"
                    },
                    {
                        "3digit": "TIA",
                        "value": "TICKETS"
                    },
                    {
                        "3digit": "TLA",
                        "value": "TOOLS"
                    },
                    {
                        "3digit": "TAA",
                        "value": "TOYS+GAMES"
                    },
                    {
                        "3digit": "TRA",
                        "value": "TRAILERS"
                    },
                    {
                        "3digit": "VGA",
                        "value": "VIDEO GAMING"
                    },
                    {
                        "3digit": "WAA",
                        "value": "WANTED"
                    },
                    {
                        "3digit": "WTA",
                        "value": "AUTO WHEELS & TIRES"
                    },
                    {
                        "3digit": "APA",
                        "value": "APARTMENTS / HOUSING FOR RENT"
                    },
                    {
                        "3digit": "SWP",
                        "value": "HOUSING SWAP"
                    },
                    {
                        "3digit": "HSW",
                        "value": "HOUSING WANTED"
                    },
                    {
                        "3digit": "OFF",
                        "value": "OFFICE & COMMERCIAL"
                    },
                    {
                        "3digit": "PRK",
                        "value": "PARKING & STORAGE"
                    },
                    {
                        "3digit": "REA",
                        "value": "REAL ESTATE FOR SALE"
                    },
                    {
                        "3digit": "ROO",
                        "value": "ROOMS & SHARES"
                    },
                    {
                        "3digit": "SHA",
                        "value": "WANTED: ROOM/SHARE"
                    },
                    {
                        "3digit": "SUB",
                        "value": "SUBLETS & TEMPORARY"
                    },
                    {
                        "3digit": "VAC",
                        "value": "VACATION  RENTALS"
                    },
                    {
                        "3digit": "ACC",
                        "value": "FINANCE"
                    },
                    {
                        "3digit": "OFC",
                        "value": "ADMIN/OFFICE"
                    },
                    {
                        "3digit": "EGR",
                        "value": "ENGINEERING"
                    },
                    {
                        "3digit": "MED",
                        "value": "MEDIA"
                    },
                    {
                        "3digit": "SCI",
                        "value": "SCIENCE"
                    },
                    {
                        "3digit": "BUS",
                        "value": "BUSINESS"
                    },
                    {
                        "3digit": "CSR",
                        "value": "CUSTOMER SERVICE"
                    },
                    {
                        "3digit": "EDU",
                        "value": "EDUCATION"
                    },
                    {
                        "3digit": "ETC",
                        "value": "ETCETERA"
                    },
                    {
                        "3digit": "FBH",
                        "value": "FOOD/BEV/HOSP"
                    },
                    {
                        "3digit": "GOV",
                        "value": "GOVERNMENT"
                    },
                    {
                        "3digit": "HUM",
                        "value": "HUMAN RECOURCE"
                    },
                    {
                        "3digit": "LGL",
                        "value": "LEGAL "
                    },
                    {
                        "3digit": "MNU",
                        "value": "MANUFACTURING"
                    },
                    {
                        "3digit": "MAR",
                        "value": "MARKETING"
                    },
                    {
                        "3digit": "HEA",
                        "value": "HEALTHCARE"
                    },
                    {
                        "3digit": "NPO",
                        "value": "NONPROFIT"
                    },
                    {
                        "3digit": "REJ",
                        "value": "REAL ESTATE"
                    },
                    {
                        "3digit": "RET",
                        "value": "RETAIL/WHOLESALE"
                    },
                    {
                        "3digit": "SLS",
                        "value": "SALES"
                    },
                    {
                        "3digit": "SPA",
                        "value": "SALON/SPA/FITNESS"
                    },
                    {
                        "3digit": "SEC",
                        "value": "SECURITY"
                    },
                    {
                        "3digit": "TRD",
                        "value": "SKILLED TRADES"
                    },
                    {
                        "3digit": "SOF",
                        "value": "SOFTWARE"
                    },
                    {
                        "3digit": "SAD",
                        "value": "SYSTEMS/NETWORKING"
                    },
                    {
                        "3digit": "TCH",
                        "value": "TECH SUPPORT"
                    },
                    {
                        "3digit": "TRP",
                        "value": "TRANSPORT"
                    },
                    {
                        "3digit": "TFR",
                        "value": "TV VIDEO RADIO"
                    },
                    {
                        "3digit": "WEB",
                        "value": "WEB DESIGN"
                    },
                    {
                        "3digit": "WRI",
                        "value": "WRITING"
                    },
                    {
                        "3digit": "ACT",
                        "value": "activities"
                    },
                    {
                        "3digit": "ATS",
                        "value": "artists"
                    },
                    {
                        "3digit": "KID",
                        "value": "childcare"
                    },
                    {
                        "3digit": "CLS",
                        "value": "classes"
                    },
                    {
                        "3digit": "EVE",
                        "value": "events"
                    },
                    {
                        "3digit": "COM",
                        "value": "general community"
                    },
                    {
                        "3digit": "GRP",
                        "value": "groups"
                    },
                    {
                        "3digit": "VNN",
                        "value": "local news and views"
                    },
                    {
                        "3digit": "LAF",
                        "value": "lost found"
                    },
                    {
                        "3digit": "MIS",
                        "value": "missed connections"
                    },
                    {
                        "3digit": "MUC",
                        "value": "musicians"
                    },
                    {
                        "3digit": "PET",
                        "value": "pets"
                    },
                    {
                        "3digit": "POL",
                        "value": "politics"
                    },
                    {
                        "3digit": "RNR",
                        "value": "rants raves"
                    },
                    {
                        "3digit": "RID",
                        "value": "rideshare"
                    },
                    {
                        "3digit": "VOL",
                        "value": "volunteers"
                    },
                    {
                        "3digit": "CPG",
                        "value": "computer gigs"
                    },
                    {
                        "3digit": "CRG",
                        "value": "creative gigs"
                    },
                    {
                        "3digit": "CWG",
                        "value": "crew gigs"
                    },
                    {
                        "3digit": "DMG",
                        "value": "domestic gigs"
                    },
                    {
                        "3digit": "EVG",
                        "value": "event gigs"
                    },
                    {
                        "3digit": "LBG",
                        "value": "labor gigs"
                    },
                    {
                        "3digit": "TLG",
                        "value": "talent gigs"
                    },
                    {
                        "3digit": "WRG",
                        "value": "writing gigs"
                    }
                ];
                for(var i =0 ; i< $scope.craigslists.length;i++){
                    $scope.sections.push($scope.craigslists[i]["3digit"].toLowerCase());
                    $scope.sectionsValues.set($scope.craigslists[i]["3digit"], $scope.craigslists[i]["value"]);
                }
                console.log($scope.sections);

                //insertar en BD todos os keywords , salvarlos y convertirlos en json
                //$scope.sectionsValues.set('sof', "Software");

                $scope.loadMore = function() {
                    $scope.selectedSection.limit = $scope.selectedSection.limit + 40;
                }

                if ($scope.searchRequest && $scope.searchRequest.sections) {
                    for (var i = 0; i < $scope.searchRequest.sections.length; i++) {
                        $scope.selectedSections[$scope.searchRequest.sections[i]] = true;
                    }
                }

                $scope.onAllChanged = function() {
                    for (var i = 0; i < $scope.sections.length; i++) {
                        if (!$scope.selectedSection.filter || $scope.sections[i].toLowerCase().indexOf($scope.selectedSection.filter.toLowerCase()) !== -1) {
                            $scope.selectedSections[$scope.sections[i]] = $scope.selectedSection.all;
                        }
                    }
                }
            }
        ] )
    .controller( 'DatesController',
        [ '$scope',
            function( $scope ) {
                $scope.config = {};

                $scope.onStartDateChanged = function(date) {
                    for (var prop in $scope.selectedDates) {
                        if (prop.indexOf("Start date") == 0) {
                            $scope.selectedDates[prop] = false;
                        }
                    }

                    var value = document.getElementById("sourceStartDate").value;
                    $scope.selectedDates["Start date: " + value + "|" + date.getTime()] = true;
                }

                $scope.onEndDateChanged = function(date) {
                    for (var prop in $scope.selectedDates) {
                        if (prop.indexOf("End date") == 0) {
                            $scope.selectedDates[prop] = false;
                        }
                    }

                    var value = document.getElementById("sourceEndDate").value;
                    $scope.selectedDates["End date: " + value + "|" + date.getTime()] = true;
                }

                $scope.getDateValue = function(time) {
                    var date = new Date(parseInt(time));
                    var month = (date.getMonth() + 1) + "";
                    if (month.length == 1) {
                        month = "0" + month;
                    }

                    return month + "." + date.getFullYear();
                }

                if ($scope.searchRequest && $scope.searchRequest.dates) {
                    if ($scope.searchRequest && $scope.searchRequest.dates[0]) {
                        var value = $scope.getDateValue($scope.searchRequest.dates[0]);
                        $scope.selectedDates["Start date: " + value + "|" + $scope.searchRequest.dates[0]] = true;
                    }

                    if ($scope.searchRequest && $scope.searchRequest.dates[1]) {
                        var value = $scope.getDateValue($scope.searchRequest.dates[1]);
                        $scope.selectedDates["End date: " + value + "|" + $scope.searchRequest.dates[1]] = true;
                    }
                }
            }
        ] )
    .controller('CategoryController',
        ['$scope',
            function($scope) {
                $scope.sources = [];
                $scope.searchConfig = {limit: 30};
                $scope.loading = true;

                $scope.categories = [
                    "Auto Dealers",
                    "Business & Utility Services",
                    "Content & Apps",
                    "Creators & Celebrities",
                    "Entities",
                    "Food & Personal Goods",
                    "General Interest",
                    "Geography",
                    "Government Agencies",
                    "Grocery & Convenience Stores",
                    "Home & Auto",
                    "Home Goods Stores",
                    "Home Services",
                    "Lifestyle Services",
                    "Local Events",
                    "Non-Profits & Religious Organizations",
                    "Personal Goods & General Merchandise Stores",
                    "Professional Services",
                    "Publishers",
                    "Restaurants",
                    "Transportation & Accomodation Services",
                ];

                if ($scope.searchRequest && $scope.searchRequest.categories) {
                    for (var i = 0; i < $scope.searchRequest.categories.length; i++) {
                        $scope.selectedCategory[$scope.searchRequest.categories[i] + '|' + $scope.searchRequest.categories[i]] = true;
                    }
                }
            }])
    .controller('InstagramCategoryController',
        [ '$scope', 'dictionaryService',
            function( $scope, dictionaryService ) {
                $scope.categories = [];
                $scope.params = {filter: '', limit: 30, count: 0, all: false}

                $scope.loadMore = function() {
                    $scope.params.limit = $scope.params.limit + 30;
                    $scope.updateCategories();
                }

                $scope.onSearchChanged = function() {
                    $scope.params.limit = 30;
                    $scope.updateCategories();
                }

                $scope.onAllCategoriesChanged = function() {
                    for (var i = 0; i < $scope.categories.length; i++) {
                        var category = $scope.categories[i];
                        $scope.selectedCategory[category + "|" + category] = $scope.config.all;
                    }
                }

                $scope.updateCategories = function() {
                    var request = {searchValue: $scope.params.filter,
                        limit: $scope.params.limit};

                    dictionaryService.categories(request, function(response) {
                        if (response.status == 'OK') {
                            $scope.categories = response.data.data;
                            $scope.params.count = response.data.total;
                        }
                    });
                }
                $scope.updateCategories();

                if ($scope.searchRequest && $scope.searchRequest.categories) {
                    for (var i = 0; i < $scope.searchRequest.categories.length; i++) {
                        $scope.selectedCategory[$scope.searchRequest.categories[i] + '|' + $scope.searchRequest.categories[i]] = true;
                    }
                }
            }])
    .controller('MakesController',
        ['$scope', 'dictionaryService', function($scope, dictionaryService) {
            $scope.makes = [];
            $scope.total = 0;

            $scope.searchConfig = {limit: 120, search: "", all: false};
            $scope.loading = true;

            $scope.loadMore = function() {
                $scope.searchConfig.limit = $scope.searchConfig.limit + 30;
                $scope.updateData();
            }

            $scope.onAllMakesChanged = function() {
                for (var i = 0; i < $scope.makes.length; i++) {
                    var make = $scope.makes[i];
                    $scope.selectedMakes[make.toUpperCase() + "|" + make] = $scope.searchConfig.all;
                }
            }

            $scope.updateData = function() {
                $scope.loading = true;
                dictionaryService.autoMakes(
                    {
                        search: $scope.searchConfig.search,
                        limit: $scope.searchConfig.limit
                    },
                    function(response) {
                        $scope.loading = false;
                        if (response.status === 'OK') {
                            $scope.makes = response.data.makes;
                            $scope.total = response.data.total;

                            if ($scope.searchRequest && $scope.searchRequest.makes) {
                                for (var i = 0; i < $scope.searchRequest.makes.length; i++) {
                                    var make = $scope.searchRequest.makes[i];
                                    $scope.selectedMakes[make.toUpperCase() + "|" + make] = true;
                                }
                            }
                        } else {
                            console.error( response );
                        }
                    } );
            }
            $scope.updateData();
        }]).controller('CarrierC2Controller',
    ['$scope', 'dictionaryService','BASE_URL','cancelableCarriersDataService',function($scope, dictionaryService, BASE_URL, cancelableCarriersDataService) {
        $scope.selectedCarriersLimit = { limit: 30 };
        $scope.updateCarrierSources = function () {
            if($scope.searchConsumerCarrierName["carrierValue"]  == undefined || $scope.searchConsumerCarrierName["carrierValue"]  == ''){
                $scope.searchConsumerCarrierName["carrierValue"] = ' '
            }
            dictionaryService.c2CarriersName({ name: $scope.searchConsumerCarrierName["carrierValue"] },
                function (response) {

                    $scope.c2carriers = response.data;
                });
        }
        $scope.loadMoreCarriers = function () {
            $scope.selectedCarriersLimit.limit = $scope.selectedCarriersLimit.limit + 30;
        }
        $scope.updateCarrierSources()

    }])
    .controller('ModelsController',
        ['$scope', 'dictionaryService', function($scope, dictionaryService) {
            $scope.makes = [];
            $scope.total = 0;

            $scope.searchConfig = {make: "4STA", limit: 30, search: ""};
            $scope.loading = true;

            $scope.loadMore = function() {
                $scope.searchConfig.limit = $scope.searchConfig.limit + 30;
                $scope.updateData();
            }

            dictionaryService.autoMakes({limit: 1000, search: ""},
                function(response) {
                    $scope.loading = false;
                    if (response.status === 'OK') {
                        $scope.makes = response.data.makes;
                        $scope.searchConfig.make = $scope.makes[0];

                        $scope.updateData()
                    } else {
                        console.error( response );
                    }
                } );

            $scope.updateData = function() {
                $scope.loading = true;
                dictionaryService.autoModels(
                    {
                        make: $scope.searchConfig.make,
                        search: $scope.searchConfig.search,
                        limit: $scope.searchConfig.limit
                    },
                    function(response) {
                        $scope.loading = false;
                        if (response.status === 'OK') {
                            $scope.models = response.data.models;
                            $scope.total = response.data.total;

                            if ($scope.searchRequest && $scope.searchRequest.models) {
                                for (var i = 0; i < $scope.searchRequest.models.length; i++) {
                                    var model = $scope.searchRequest.models[i];
                                    $scope.selectedMakes[model + "|" + make] = true;
                                }
                            }
                        } else {
                            console.error( response );
                        }
                    });
            }
        }])
    .controller('YearsController',
        ['$scope', function($scope) {
            $scope.values = {};

            $scope.onStartDateChanged = function() {
                $scope.addDate(true);

                if ($scope.values.endDate) {
                    $scope.values.before = false;
                    $scope.values.after = false;

                    $scope.addDate(false);
                }
            }

            $scope.onEndDateChanged = function() {
                $scope.addDate(false);

                if ($scope.values.startDate) {
                    $scope.values.before = false;
                    $scope.values.after = false;

                    $scope.addDate(true);
                }
            }

            $scope.onStartDateIntervalParamChanged = function() {
                if ($scope.values.before) {
                    $scope.values.endDate = undefined;
                    $scope.updateDashBoardObject(false)
                }

                $scope.addDate(true, undefined);
            }

            $scope.onEndDateIntervalParamChanged = function() {
                if ($scope.values.after) {
                    $scope.values.startDate = undefined;
                    $scope.updateDashBoardObject(true)
                }

                $scope.addDate(false, undefined);
            }

            $scope.addDate = function(startDate) {
                if (startDate && !$scope.values.startDate) { return; }
                if (!startDate && !$scope.values.endDate) { return; }

                var title = document.getElementById("startDateAuto").value;
                if (!startDate) {
                    var title = document.getElementById("endDateAuto").value;
                }

                var value = 0;
                if (startDate) {
                    value = $scope.values.startDate.getTime();
                } else {
                    value = $scope.values.endDate.getTime();
                }

                if (startDate && $scope.values.before) {
                    title = title + " and earlier";
                    value = value + (value > 0 ? 999 : -999);
                }

                if (!startDate && $scope.values.after) {
                    title = title + " and later";
                    value = value + (value > 0 ? 999 : -999);
                }

                $scope.updateDashBoardObject(startDate, {'title': title, 'value': value })
            }

            $scope.updateDashBoardObject = function(startDate, value) {
                var index = startDate ? 0 : 1;
                $scope.selectedYears[index] = value;
            }

            $scope.getDateValue = function(time) {
                var date = new Date(parseInt(time));
                var month = (date.getMonth() + 1) + "";
                if (month.length == 1) {
                    month = "0" + month;
                }

                return month + "." + date.getFullYear();
            }

            $scope.getYearValue = function(time) {
                var date = new Date(parseInt(time));
                return date.getFullYear() + "";
            }

            $scope.fillDates = function(selectedArray, dates, yearOnly) {
                if (dates[0] && dates[0] != -1) {
                    var title = yearOnly ? $scope.getYearValue(dates[0]) : $scope.getDateValue(dates[0]);
                    if (parseInt(dates[0]) % 1000 == 999 || parseInt(dates[0]) % 1000 == -999) {
                        title = title + " and earlier";
                    }

                    selectedArray[0] = {'title': title, value: parseInt(dates[0])};
                }

                if (dates[1] && dates[1] != -1) {
                    var title = yearOnly ? $scope.getYearValue(dates[1]) : $scope.getDateValue(dates[1]);
                    if (parseInt(dates[1]) % 1000 == 999 || parseInt(dates[1]) % 1000 == -999) {
                        title = title + " and later";
                    }

                    selectedArray[1] = {'title': title, value: parseInt(dates[1])};
                }
            }

            if ($scope.searchRequest && $scope.searchRequest.yearsRange) {
                $scope.fillDates($scope.selectedYears, $scope.searchRequest.yearsRange, true);
            }

        }])
