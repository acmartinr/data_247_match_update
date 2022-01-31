angular.module( 'consumer_data_base' ).
controller( 'UpdateDataController',
[ '$scope', 'administrationService', '$timeout', 'toasty', 'localization', '$interval', 'confirm', '$modal',
function( $scope, administrationService, $timeout, toasty, localization, $interval, confirm, $modal ) {
    $scope.table = { name: '' };
    var updateMessage = function() {
        administrationService.protected().message(
        function( response ) {
            if ( response.message ) {
                toasty.success( {
                    title: 'Updating data',
                    msg: response.message,
                    sound: false,
                    timeout: 15000
                } );

                $scope.loading = true;
            } else {
                $scope.loading = false;
            }
        } );
    }

    updateMessage();
    $scope.interval = $interval(updateMessage, 10000);
    $scope.$on( '$destroy', function() {
        if ( $scope.interval ) $interval.cancel( $scope.interval );
    } );

    var initTables = function() {
        administrationService.protected().allTables( function( response ) {
            $scope.tables = [];
            for ( var i = 0; i < response.data.length; i++ ) {
                $scope.tables.push( response.data[ i ] );
                if ( !$scope.table.name ) {
                    $scope.table = response.data[ i ];
                }
            }
        } );
    }
    initTables();

    var checkIfConsumerTable = function() {
        for ( var i = 0; i < $scope.tables.length; i++ ) {
            if ( $scope.tables[ i ].name === $scope.table.name &&
                 $scope.tables[ i ].type === 0 ) {
                return true;
            }
        }

        return false;
    }

    $scope.updateCounties = function() {
        /*if ( !checkIfConsumerTable() ) {
            toasty.error( {
                title: 'Updating counties data',
                msg: 'Select consumers table',
                sound: false
            } );
            return;
        }*/

        $scope.loading = true;
        administrationService.protected().updateCounties( { tableName: $scope.table.name }, function( response ) {
            $scope.loading = false;
            if ( response.status == 'OK' ) {
                toasty.success( {
                    title: 'Updating counties data',
                    msg: 'Data has been updated successfully',
                    sound: false,
                    timeout: false
                } );
            } else {
                toasty.error( {
                    title: 'Updating counties data',
                    msg: 'Data updating failed',
                    sound: false,
                    timeout: false
                } );
            }
        } );
    }

    $scope.updateZipCodes = function() {
        $scope.loading = true;
        administrationService.protected().updateZipCodes( { tableName: $scope.table.name }, function( response ) {
          $scope.loading = false;
          if ( response.status == 'OK' ) {
              toasty.success( {
                  title: 'Updating zip codes data',
                  msg: 'Data has been updated successfully',
                  sound: false,
                  timeout: false
              } );
          } else {
              toasty.error( {
                  title: 'Updating zip codes data',
                  msg: 'Data updating failed',
                  sound: false,
                  timeout: false
              } );
          }
        } );
    }

    $scope.updateAgeCategories = function() {
        if ( !checkIfConsumerTable() ) {
            toasty.error( {
                title: 'Updating age categories data',
                msg: 'Select consumers table',
                sound: false
            } );
            return;
        }

        $scope.loading = true;
        administrationService.protected().updateAgeCategories( { tableName: $scope.table.name }, function( response ) {
            $scope.loading = false;
            if ( response.status == 'OK' ) {
                toasty.success( {
                    title: 'Updating age categories data',
                    msg: 'Data has been updated successfully',
                    sound: false,
                    timeout: false
                } );
            } else {
                toasty.error( {
                    title: 'Updating age categories data',
                    msg: 'Data updating failed',
                    sound: false,
                    timeout: false
                } );
            }
        } );
    }

    $scope.createMobileDataTable = function() {
        $scope.loading = true;
        administrationService.protected().createMobileDataTable( { tableName: $scope.table.name }, function( response ) {
            $scope.loading = false;
            if ( response.status == 'OK' ) {
                /*toasty.success( {
                    title: 'Creating mobile data table',
                    msg: 'Table has been created successfully',
                    sound: false,
                    timeout: false
                } );*/
            } else {
                toasty.error( {
                    title: 'Creating mobile data table',
                    msg: 'Table creating failed',
                    sound: false,
                    timeout: false
                } );
            }
        } );
    }

    $scope.createLandLineDataTable = function() {
            $scope.loading = true;
            administrationService.protected().createLandLineDataTable( { tableName: $scope.table.name }, function( response ) {
                $scope.loading = false;
                if ( response.status == 'OK' ) {
                    /*toasty.success( {
                        title: 'Creating land line data table',
                        msg: 'Table has been created successfully',
                        sound: false,
                        timeout: false
                    } );*/
                } else {
                    toasty.error( {
                        title: 'Creating land line data table',
                        msg: 'Table creating failed',
                        sound: false,
                        timeout: false
                    } );
                }
            } );
        }

    $scope.updatePhoneType = function() {
        $scope.loading = true;
        administrationService.protected().updatePhoneTypes( { tableName: $scope.table.name }, function( response ) {
            $scope.loading = false;
            if ( response.status == 'OK' ) {
                toasty.success( {
                    title: 'Updating phone type data',
                    msg: 'Data has been updated successfully',
                    sound: false,
                    timeout: false
                } );
            } else {
                toasty.error( {
                    title: 'Updating phone type data',
                    msg: 'Data updating failed',
                    sound: false,
                    timeout: false
                } );
            }
        } );
    }

} ] );