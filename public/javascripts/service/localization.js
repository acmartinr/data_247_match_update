angular.module( 'consumer_data_base' )
.factory( 'localization', function() {
    var locale = 'en_US';

    localizationObject = document.localization;
    /*for ( var prop in localizationObject[ 'en_US' ] ) {
        if ( !localizationObject[ 'ru_RU' ] ) {
            console.log( prop );
        }
    }*/

    return {
        localize: function( key ) {
            var value = document.localization[ locale ][ key ];
            return value ? value : key;
        },
        getLocale: function() { return locale; }
    }
} )
.directive( 'localized', function( localization ) {
    return {
        restrict: 'A',
        link: function( $scope, element, attrs ) {
            element.html( localization.localize( element.html() ) );
        }
    }
 } )
.directive( 'localizedPlaceholder', function( localization ) {
    return {
        restrict: 'A',
        link: function( $scope, element, attrs ) {
           element.attr( 'placeholder', localization.localize( element.attr( 'localized-placeholder' ) ) );
        }
    }
} )
.directive( 'localizedTitle', function( localization ) {
    return {
        restrict: 'A',
        link: function( $scope, element, attrs ) {
            element.attr( 'title', localization.localize( element.attr( 'localized-title' ) ) );
        }
    }
} )
.directive( 'localizedValue', function( localization ) {
    return {
        restrict: 'A',
        link: function( $scope, element, attrs ) {
            element.attr( 'value', localization.localize( element.attr( 'localized-value' ) ) );
        }
    }
} );
