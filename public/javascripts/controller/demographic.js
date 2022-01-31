angular.module( 'consumer_data_base' ).
controller( 'AgeController',
[ '$scope',
function( $scope ) {

}
] )
.controller( 'DashboardListsController',
[ '$scope', 'listService', 'credentialsService',
function( $scope, listService, credentialsService ) {

    $scope.config = { selectAll: false,
                      searchValue: '' };

    $scope.isEmpty = function() {
        return $scope.lists && $scope.lists.length === 0;
    }

    var updateLists = function() {
        listService.getAllPurchased( { id: credentialsService.getUser().id,
                                       dataType: -1 }, function( response ) {
            if ( response.status === 'OK' ) {
                $scope.lists = response.data;

                if ($scope.searchRequest && $scope.searchRequest.selectedLists) {
                    for (var i = 0; i < $scope.lists.length; i++) {
                        for (var j = 0; j < $scope.searchRequest.selectedLists.length; j++) {
                            if ($scope.lists[i].id == $scope.searchRequest.selectedLists[j]) {
                                var list = $scope.lists[i];
                                $scope.selectedLists[list.name + "|" + list.id] = true;

                                break;
                            }
                        }
                    }
                }
            }
        } );
    }
    updateLists();

    $scope.onSelectAllChanged = function() {
       var listFiltered = $scope.lists.filter(list => list.name.toLowerCase().search($scope.config.searchValue.toLowerCase()) !== -1);
       /*
        for ( var i = 0; i < $scope.lists.length; i++ ) {
            var list = $scope.lists[ i ];
            $scope.selectedLists[ list.name + "|" + list.id ] = $scope.config.selectAll;
        }
        */
        for ( var i = 0; i < listFiltered.length; i++ ) {
             var list = listFiltered[ i ];
             $scope.selectedLists[ list.name + "|" + list.id ] = $scope.config.selectAll;
        }
    }
}
] )
.controller( 'DashboardUploadedListsController',
[ '$scope', 'listService', 'credentialsService',
function( $scope, listService, credentialsService ) {
    $scope.config = { selectAll: false,
                      searchValue: '' };

    $scope.isEmpty = function() {
        return $scope.uploadedLists && $scope.uploadedLists.length === 0;
    }

    var updateLists = function() {
        listService.getAllUploadedLists( { id: credentialsService.getUser().id }, function( response ) {
            if ( response.status === 'OK' ) {
                $scope.uploadedLists = response.data;

                if ($scope.searchRequest && $scope.searchRequest.uploadedLists) {
                    for (var i = 0; i < $scope.uploadedLists.length; i++) {
                        for (var j = 0; j < $scope.searchRequest.uploadedLists.length; j++) {
                            if ($scope.uploadedLists[i].id == $scope.searchRequest.uploadedLists[j]) {
                                var list = $scope.uploadedLists[i];
                                $scope.selectedUploadedLists[list.name + "|" + list.id] = true;

                                break;
                            }
                        }
                    }
                }
            }
        } );
    }
    updateLists();

    $scope.onSelectAllChanged = function() {
        for ( var i = 0; i < $scope.uploadedLists.length; i++ ) {
            var list = $scope.uploadedLists[ i ];
            $scope.selectedUploadedLists[ list.name + "|" + list.id ] = $scope.config.selectAll;
        }
    }
}
] )
.controller( 'GenderController',
[ '$scope',
function( $scope ) {

}
] )
.controller( 'PhoneTypeController',
[ '$scope',
function( $scope ) {

}
] )
.controller( 'EducationController',
[ '$scope',
function( $scope ) {

}
] )
.controller( 'CreditController',
[ '$scope', 'localization',
function( $scope, localization ) {
    $scope.creditType = 'netWorth';

    $scope.netWorth = [];
    var netWorthArray = [ 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i' ];
    for ( var i = 0; i < netWorthArray.length; i++ ) {
        $scope.netWorth.push( {
            value: 'credit.net.worth.' + netWorthArray[ i ],
            title: localization.localize( 'credit.net.worth.' + netWorthArray[ i ] ) + '|' + netWorthArray[ i ] } );
    }

    $scope.rating = [];
    var ratingArray = [ 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' ];
    for ( var i = 0; i < ratingArray.length; i++ ) {
        $scope.rating.push( {
            value: 'credit.rating.' + ratingArray[ i ],
            title: localization.localize( 'credit.rating.' + ratingArray[ i ] ) + '|' + ratingArray[ i ] } );
    }

    $scope.activeLines = [];
    var activeLinesArray = [ 1, 2, 3, 4, 5, 6, 7, 8, 9 ];
    for ( var i = 0; i < activeLinesArray.length; i++ ) {
        $scope.activeLines.push( {
            value: 'credit.active.lines.' + activeLinesArray[ i ],
            title: activeLinesArray[ i ] + '|' + activeLinesArray[ i ] } );
    }

    $scope.range = [];
    var rangeArray = [ 0, 1, 2, 3, 4, 5, 6, 7 ];
    for ( var i = 0; i < rangeArray.length; i++ ) {
        $scope.range.push( {
            value: 'credit.range.new.credits.' + rangeArray[ i ],
            title: localization.localize( 'credit.range.new.credits.' + rangeArray[ i ] ) + '|' + rangeArray[ i ] } );
    }

    $scope.getLocalized = function( string ) {
        return localization.localize( string );
    }
}
] )
.controller( 'EthnicityController',
[ '$scope', 'localization',
function( $scope, localization ) {
    $scope.ethnicityType = 'group';

    $scope.groups = [];
    var groupsArray = [ 'f', 'c', 'e', 'o', 'y', 'j', 'm', 'i', 'n', 'p', 's', 'a', 'w' ];
    for ( var i = 0; i < groupsArray.length; i++ ) {
        $scope.groups.push( {
            value: 'ethnicity.group.' + groupsArray[ i ],
            title: localization.localize( 'ethnicity.group.' + groupsArray[ i ] ) + '|' + groupsArray[ i ] } );
    }


    $scope.languages = [];
    var languagesArray = [ 'a4', 'c1', 'e1', 'g2', 'h2', 'h3', 'i3', 'j1', 'k4', 'p2', 'p3', 'r2', 's8', 'v1' ];
    for ( var i = 0; i < languagesArray.length; i++ ) {
        $scope.languages.push( {
            value: 'ethnicity.language.' + languagesArray[ i ],
            title: localization.localize( 'ethnicity.language.' + languagesArray[ i ] ) + '|' + languagesArray[ i ] } );
    }

    $scope.religions = [];
    var religionsArray = [ 'b', 'c', 'o', 'g', 'i', 'j', 'h', 'm', 'p', 's' ];
    for ( var i = 0; i < religionsArray.length; i++ ) {
        $scope.religions.push( {
            value: 'ethnicity.religion.' + religionsArray[ i ],
            title: localization.localize( 'ethnicity.religion.' + religionsArray[ i ] ) + '|' + religionsArray[ i ] } );
    }

    $scope.getLocalized = function( string ) {
        return localization.localize( string );
    }
}
] )
.controller( 'HouseholdsController',
[ '$scope', 'localization',
function( $scope, localization ) {
    $scope.householdType = 'size';

    $scope.sizes = [];
    var sizesArray = [ 1, 2, 3, 4, 5, 6, 7, 8, 9 ];
    for ( var i = 0; i < sizesArray.length; i++ ) {
        $scope.sizes.push( {
            value: 'household.size.' + sizesArray[ i ],
            title: sizesArray[ i ] + '|' + sizesArray[ i ] } );
    }


    $scope.incomes = [];
    var incomesArray = [ 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's' ];
    for ( var i = 0; i < incomesArray.length; i++ ) {
        $scope.incomes.push( {
            value: 'household.income.' + incomesArray[ i ],
            title: localization.localize( 'household.income.' + incomesArray[ i ] ) + '|' + incomesArray[ i ] } );
    }

    $scope.veterans = [];
    var veteransArray = [ 'y', 'n' ];
    for ( var i = 0; i < veteransArray.length; i++ ) {
        $scope.veterans.push( {
            value: 'residence.veteran.' + veteransArray[ i ],
            title: localization.localize( 'residence.veteran.' + veteransArray[ i ] ) + '|' + veteransArray[ i ] } );
    }

    $scope.getLocalized = function( string ) {
        return localization.localize( string );
    }
}
] )
.controller( 'ResidencesController',
[ '$scope', 'localization',
function( $scope, localization ) {
    $scope.residenceType = 'type';

    $scope.types = [];
    var typesArray = [ 'a', 'b', 'd' ];
    for ( var i = 0; i < typesArray.length; i++ ) {
        $scope.types.push( {
            value: 'residence.type.' + typesArray[ i ],
            title: localization.localize( 'residence.type.' + typesArray[ i ] ) + '|' + typesArray[ i ] } );
    }

    $scope.ownerships = [];
    var ownershipsArray = [ 'h', 'r' ];
    for ( var i = 0; i < ownershipsArray.length; i++ ) {
        $scope.ownerships.push( {
            value: 'residence.ownership.' + ownershipsArray[ i ],
            title: localization.localize( 'residence.ownership.' + ownershipsArray[ i ] ) + '|' + ownershipsArray[ i ] } );
    }

    $scope.lengths = [];
    var lengthsArray = [ 14, 15 ];
    for ( var i = 0; i < lengthsArray.length; i++ ) {
        $scope.lengths.push( {
            value: 'residence.length.' + lengthsArray[ i ],
            title: localization.localize( 'residence.length.' + lengthsArray[ i ] ) + '|' + lengthsArray[ i ] } );
    }

    $scope.maritals = [];
    var maritalsArray = [ 'm', 's', 'a', 'b' ];
    for ( var i = 0; i < maritalsArray.length; i++ ) {
        $scope.maritals.push( {
            value: 'residence.marital.' + maritalsArray[ i ],
            title: localization.localize( 'residence.marital.' + maritalsArray[ i ] ) + '|' + maritalsArray[ i ] } );
    }

    $scope.children = [];
    var childrenArray = [ 0, 1, 2, 3, 4, 5, 6, 7, 8 ];
    for ( var i = 0; i < childrenArray.length; i++ ) {
        var title = localization.localize( 'residence.number.of.children.' + childrenArray[ i ] );
        title = title.replace( " Children", "" );
        title = title.replace( " Child", "" );
        $scope.children.push( {
            value: 'residence.number.of.children.' + childrenArray[ i ],
            title: title + '|' + childrenArray[ i ] } );
    }

    $scope.getLocalized = function( string ) {
        return localization.localize( string );
    }
}
] )
.controller( 'InterestsController',
[ '$scope',
function( $scope ) {

}
] )
