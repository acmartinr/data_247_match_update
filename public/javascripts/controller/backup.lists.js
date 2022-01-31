angular.module( 'consumer_data_base' ).
controller( 'BackupController',
[ '$scope', 'administrationService', '$timeout', 'toasty', 'localization', '$interval', 'confirm', '$modal',
function( $scope, administrationService, $timeout, toasty, localization, $interval, confirm, $modal ) {
    var updateMessages = function() {
        administrationService.protected().backupMessage(
        function( response ) {
            if ( response.message ) {
                toasty.success( {
                    title: 'Making backup of lists data',
                    msg: response.message,
                    sound: false,
                    timeout: 15000
                } );

                $scope.loading = true;
            }
        } );
    }

    updateMessages();
    $scope.interval = $interval( updateMessages, 10000 );
    $scope.$on( '$destroy', function() {
        if ( $scope.interval ) $interval.cancel( $scope.interval );
    } );

    $scope.backupListsData = function() {

        $scope.loading = true;
        administrationService.protected().backupListsData(function( response ) {
            $scope.loading = false;
            if ( response.status == 'OK' ) {
                toasty.success( {
                    title: 'Making backup of lists data',
                    msg: 'Backup for lists data has been created successfully',
                    sound: false,
                    timeout: false
                } );
            } else {
                toasty.error( {
                    title: 'Making backup of lists data',
                    msg: response.message,
                    sound: false,
                    timeout: false
                } );
            }
        } );
    }
} ] );