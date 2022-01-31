angular.module( 'consumer_data_base' ).
controller( 'StatesController',
[ '$scope',
function( $scope ) {
    $scope.states = document.states;
    $scope.config = { all: false };

    $scope.onAllStatesChanged = function() {
        for ( var i = 0; i < $scope.states.length; i++ ) {
            var state = $scope.states[ i ];
            $scope.selectedStates[ state.name + "|" + state.code ] = $scope.config.all;
        }
    }

    $scope.onSelectedChanged = function(state) {
        var key = state.name + "|" + state.code;
        var selected = $scope.selectedStates[ key ];
        if (selected == true && $scope.isRestricted && $scope.dataType == 'consumers') {
            for (var prop in $scope.selectedStates) {
                if ( prop != key ) {
                    $scope.selectedStates[ prop ] = false;
                }
            }
        }

        $scope.selectedStates[ key ] = selected;
    }

    if ($scope.searchRequest && $scope.searchRequest.states) {
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
    }

    if ($scope.searchRequest && $scope.searchRequest.omittedStates) {
        for (var i = 0; i < $scope.states.length; i++) {
            for (var j = 0; j < $scope.searchRequest.omittedStates.length; j++) {
                if ($scope.searchRequest.omittedStates[j] == $scope.states[i].code) {
                    var state = $scope.states[i];
                    var key = state.name + "|" + state.code;
                    $scope.omittedStates[key] = true;
                    break;
                }
            }
        }
    }
}
] )
.controller('TimeZonesController',
['$scope', function($scope) {
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
}
])
.controller( 'CitiesController',
[ '$scope', 'dictionaryService',
function( $scope, dictionaryService ) {
    $scope.states = document.states;
    $scope.selectedState = { code: 'AK', limit: 90 };

    $scope.updateCities = function() {
        $scope.loading = true;
        dictionaryService.cities( { state: $scope.selectedState.code },
        function( response ) {
            $scope.cities = response.data;
            $scope.loading = false;
        } );
    }
    $scope.updateCities();

    if ($scope.searchRequest && $scope.searchRequest.cities) {
        var cities = $scope.searchRequest.cities;
        for (var i = 0; i < cities.length; i++) {
            $scope.selectedCities[cities[i]] = true;
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.omittedCities) {
        var cities = $scope.searchRequest.omittedCities;
        for (var i = 0; i < cities.length; i++) {
            $scope.omittedCities[cities[i]] = true;
        }
    }

    $scope.loadMore = function() {
        $scope.selectedState.limit = $scope.selectedState.limit + 90;
    }
}
] )

.controller( 'CarriersController',
[ '$scope',
function($scope) {
    $scope.carriers = document.carriersKeys;
    $scope.params = { limit: 90, filter: '' };

    if ($scope.searchRequest && $scope.searchRequest.carriers) {
        var carriers = $scope.searchRequest.carriers;
        for (var i = 0; i < carriers.length; i++) {
            var carrier = carriers[i];
            $scope.selectedCarriers[carrier] = true;
        }
    }

    $scope.loadMore = function() {
        $scope.params.limit = $scope.params.limit + 90;
    }
}
] )
.controller( 'ZipController',
[ '$scope', 'dictionaryService', '$timeout',
function( $scope, dictionaryService, $timeout ) {
    $scope.states = document.states;
    $scope.selectedState = { code: 'AK', limit: 40 };
    $scope.params = {};

    $scope.updateZipCodes = function() {
        $scope.loading = true;
        dictionaryService.zipCodes( { state: $scope.selectedState.code },
        function( response ) {
            $scope.zipCodes = response.data;
            $scope.loading = false;
        } );
    }
    $scope.updateZipCodes();

    $scope.loadMore = function() {
        $scope.selectedState.limit = $scope.selectedState.limit + 40;
    }

    $scope.$watch("params.file", function() {
        var reader = new FileReader();
        if ($scope.params.file) {
            reader.readAsText($scope.params.file, "UTF-8");
            reader.onload = function (evt) {
                $timeout(function() {
                    var fileContent = reader.result;
                    var values = [];
                    if (fileContent.indexOf(",") != -1) {
                        values = fileContent.split(",");
                    } else if (fileContent.indexOf(" ") != -1) {
                        values = fileContent.split(" ");
                    } else {
                        values = fileContent.split("\n");
                    }

                    updateSelectedZipCodes();
                    for ( var i = 0; i < values.length; i++ ) {
                        selectedZipCodes[values[ i ].trim()] = true;
                        if ( i + 1 === 100 ) {
                            break;
                        }
                    }
                }, 1);
            }
        }
    });

    var selectedZipCodes = []
    var updateSelectedZipCodes = function() {
        selectedZipCodes = $scope.selectedZipCodes;
        if ($scope.searchType === 'geographicOmit') {
            selectedZipCodes = $scope.omittedZipCodes;
        }
    }

    $scope.addRawZipCodes = function() {
        updateSelectedZipCodes();

        var values = $scope.selectedState.rawZipCodes.split( "," );
        for ( var i = 0; i < values.length; i++ ) {
            selectedZipCodes[ values[ i ].trim() ] = true;
            if ( i + 1 === 100 ) {
                break;
            }
        }
        $scope.selectedState.rawZipCodes = '';
    }

    if ($scope.searchRequest && $scope.searchRequest.zipCodes) {
        var zipCodes = $scope.searchRequest.zipCodes;
        for (var i = 0; i < zipCodes.length; i++) {
            $scope.selectedZipCodes[zipCodes[i]] = true;
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.omittedZipCodes) {
        var zipCodes = $scope.searchRequest.omittedZipCodes;
        for (var i = 0; i < zipCodes.length; i++) {
            $scope.omittedZipCodes[zipCodes[i]] = true;
        }
    }
}
] )
.controller( 'RingRadiusController',
[ '$scope',
function( $scope ) {
    var zipCoordinatesObject = {};
    for ( var i = 0; i < document.zipCoordinates.length; i++ ) {
        zipCoordinatesObject[ document.zipCoordinates[ i ].zipCode ] = document.zipCoordinates[ i ];
    }

    $scope.selectedState = { limit: 30 };

    $scope.searchZipCodes = function() {
        $scope.emptyError = false;
        $scope.loading = true;

        $scope.searchedZipCode = [];
        if ( !zipCoordinatesObject[ $scope.selectedState.code ] ) {
            $scope.emptyError = true;
        } else {
            var zipCode = zipCoordinatesObject[ $scope.selectedState.code ];

            for ( var i = 0; i < document.zipCoordinates.length; i++ ) {
                if ( getDistanceFromLatLonInKm( zipCode.latitude, zipCode.longitude,
                        document.zipCoordinates[ i ].latitude, document.zipCoordinates[ i ].longitude ) <= $scope.selectedState.radius ) {
                    $scope.searchedZipCode.push( document.zipCoordinates[ i ].zipCode );
                }
            }
            $scope.loading = false;
        }
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

    $scope.searchedZipCode = [];
    $scope.addToCommonSearch = function() {
        for ( var i = 0; i < $scope.searchedZipCode.length; i++ ) {
            $scope.selectedZipCodes[ $scope.searchedZipCode[ i ] ] = true;
        }

        $scope.selectedState.radius = undefined;
        $scope.selectedState.code = undefined;
        $scope.searchedZipCode = [];
    }

    $scope.removeCodeFromSearched = function( zipCode ) {
        for ( var i = 0; i < $scope.searchedZipCode; i++ ) {
            if ( $scope.searchedZipCode[ i ] === zipCode ) {
                $scope.searchedZipCode.splice( i, 1 );
                return;
            }
        }
    }

    $scope.loadMore = function() {
        $scope.selectedState.limit = $scope.selectedState.limit + 30;
    }
}
] )
.controller( 'CountyController',
[ '$scope', 'dictionaryService',
function( $scope, dictionaryService ) {
$scope.states = document.states;
    $scope.selectedState = { code: 'AK', limit: 90 };
    $scope.states = document.states;

    $scope.updateCounties = function() {
        $scope.loading = true;
        dictionaryService.counties( { state: $scope.selectedState.code },
        function( response ) {
            $scope.counties = response.data;
            $scope.loading = false;
        } );
    }
    $scope.updateCounties();

    $scope.loadMore = function() {
        $scope.selectedState.limit = $scope.selectedState.limit + 90;
    }

    if ($scope.searchRequest && $scope.searchRequest.counties) {
        for (var i = 0; i < $scope.searchRequest.counties.length; i++) {
            $scope.selectedCounties[$scope.searchRequest.counties[i]] = true;
        }
    }
}
] )
.controller( 'PhonesController',
[ '$scope', 'dictionaryService',
function( $scope, dictionaryService ) {
$scope.states = document.states;
    $scope.selectedState = { code: 'AK', limit: 40 };
    $scope.states = document.states;
   // $scope.areaCodeState = $scope.selectedState.code;

    $scope.updateAreaCodes = function() {
        $scope.areaCodes = document.areaCodes[ $scope.selectedState.code ];
        $scope.areaCodeState = document.areaCodes[ $scope.selectedState.code ];
        $scope.$root.areaState =  $scope.selectedState.code;
    }
    $scope.updateAreaCodes();

    $scope.loadMore = function() {
        $scope.selectedState.limit = $scope.selectedState.limit + 40;
    }

    if ($scope.searchRequest && $scope.searchRequest.areaCodes) {
        for (var i = 0; i < $scope.searchRequest.areaCodes.length; i++) {
            $scope.selectedAreaCodes[$scope.searchRequest.areaCodes[i]] = true;

        }
    }

    if ($scope.searchRequest && $scope.searchRequest.omittedAreaCodes) {
        for (var i = 0; i < $scope.searchRequest.omittedAreaCodes.length; i++) {
            $scope.omittedAreaCodes[$scope.searchRequest.omittedAreaCodes[i]] = true;
        }
    }
}
] )