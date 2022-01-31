angular.module( 'consumer_data_base' ).
controller( 'PricesController',
[ '$scope', 'administrationService', 'dataService', 'credentialsService', 'toasty', 'localization',
function( $scope, administrationService, dataService, credentialsService, toasty, localization ) {
    $scope.userId = credentialsService.getUser().id;
    $scope.userPassword = credentialsService.getUser().password;
    $scope.dataType = 0;

    dataService.dataSources({userId: credentialsService.getUser().id}, function(response) {
        $scope.dataSources = response.data;
        if (response.data.length > 0) {
            $scope.dataType = (response.data[0].id - 1);
            $scope.dataSources = response.data;
        }
    });

    $scope.requestPricesList = function() {
        administrationService.protected( $scope.userId, $scope.userPassword ).
            getPricesList( $scope.config, function( response ) {
                for ( var i = 0; i < response.data.length; i++ ) {
                    response.data[ i ].price = response.data[ i ].price.toFixed(3);
                }

                $scope.prices = response.data;
            } );
    }
    $scope.requestPricesList();

    $scope.getLocalizedPrice = function(price) {
        return localization.localize( 'prices.type.' + price.type );
    }

    $scope.priceMatch = function() {
        return function(item) {
            return item.dataSource == $scope.dataType;
        };
    }

    $scope.savePrice = function(price) {
        administrationService.
            protected( $scope.userId, $scope.userPassword ).
            savePrice( price, function( response ) {
                if ( response.status === 'OK' ) {
                    toasty.success( {
                        title: 'Source type price saving',
                        msg: 'Source type price has been saved successfully',
                        sound: false
                    } );
                }
        } );
    }

    $scope.matchedPrices = {'0': 0.5};
    administrationService.
        protected( $scope.userId, $scope.userPassword ).
        getMatchingPrices(function(response) {
            for (var i = 0; i < response.data.length; i++) {
                $scope.matchedPrices[i + ''] = response.data[i].toFixed(3);
            }
        });

    $scope.saveMatchedPrice = function() {
        administrationService.
            protected( $scope.userId, $scope.userPassword ).
            updateSettings({key: 'matched_price_' + $scope.dataType, value: $scope.matchedPrices[$scope.dataType] + ''}, function() {
                toasty.success( {
                    title: 'Matched price saving',
                    msg: 'Matched price has been saved successfully',
                    sound: false
                } );
            });
    }
} ] )