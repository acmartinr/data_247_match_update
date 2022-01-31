angular.module( 'consumer_data_base' ).
controller( 'PrefixesController',
[ '$scope', 'BASE_URL', '$modal', '$filter', '$state', '$cookies', '$window', 'credentialsService', 'confirm', 'administrationService', 'toasty', 'localization',
function( $scope, BASE_URL, $modal, $filter, $state, $cookies, $window, credentialsService, confirm, administrationService, toasty, localization ) {
    $scope.config = { sortValue: 'prefix', sortDesc: false, page: 1, limit: 10 };

    $scope.userId = credentialsService.getUser().id;
    $scope.userPassword = credentialsService.getUser().password;

    $scope.search = function() {
        administrationService.protected( $scope.userId, $scope.userPassword ).
            getPrefixesList( $scope.config, function( response ) {
                for ( var i = 0; i < response.prefixes.length; i++ ) {
                    response.prefixes[ i ].prefix = response.prefixes[ i ].prefix.replace( '%', '' );
                }

                $scope.prefixes = response.prefixes;
                $scope.total = response.total;
            } );
    }
    $scope.search();

    $scope.searchByValue = function() {
        $scope.config.page = 1;
        $scope.search();
    }

    $scope.sortByField = function( value ) {
        if ( value === $scope.config.sortValue ) {
            $scope.config.sortDesc = !$scope.config.sortDesc;
        } else {
            $scope.config.sortDesc = false;
        }

        $scope.config.sortValue = value;
        $scope.config.page = 1;

        $scope.search();
    }

    $scope.importPrefixes = function() {
        var modalInstance = $modal.open( {
             templateUrl: BASE_URL + '/assets/partials/modal/upload.prefixes.html',
             controller: 'UploadPrefixesModalController'
         } );

        modalInstance.result.then( function() {
            $scope.search();
        } );
    }

    $scope.editPrefix = function( prefix ) {
        var modalInstance = $modal.open( {
             templateUrl: BASE_URL + '/assets/partials/modal/phone.prefix.html',
             controller: 'PrefixController',
             resolve: {
                 prefix: function () { return prefix; }
             } } );

        modalInstance.result.then( function() { $scope.search(); } );
    }

    $scope.removePrefix = function( prefix ) {
        confirm.getUserConfirmation( 'Do you really want to delete this prefix?', function() {
            administrationService.protected( $scope.userId, $scope.userPassword ).deletePhonePrefix( prefix, function() {
                $scope.search();
            } );
        } );
    }
} ] )
.controller( 'PrefixController',
[ '$scope', '$modalInstance', 'prefix', 'credentialsService', 'administrationService', 'toasty', 'localization',
function( $scope, $modalInstance, prefix, credentialsService, administrationService, toasty, localization ) {
    $scope.userId = credentialsService.getUser().id;
    $scope.userPassword = credentialsService.getUser().password;

    $scope.prefix = {};
    for ( var prop in prefix ) {
        $scope.prefix[ prop ] = prefix[ prop ];
    }

    $scope.save = function() {
        $scope.errorMessage = '';

        if ( !$scope.prefix.prefix ) {
            toasty.error( {
                title: 'Updating phone prefix',
                msg: 'Please enter phone prefix',
                sound: false
            } );

            return;
        }

        if ( $scope.prefix.prefix.length > 9 ) {
            toasty.error( {
                title: 'Updating phone prefix',
                msg: 'Wrong phone prefix length',
                sound: false
            } );

            return;
        }

        administrationService.protected( $scope.userId, $scope.userPassword ).updatePhonePrefix( $scope.prefix, function( response ) {
            if ( response.status === 'OK' ) {
                toasty.success( {
                    title: 'Updating phone prefix',
                    msg: 'Phone prefix has been updated successfully',
                    sound: false
                } );

                $modalInstance.close();
            } else {
                toasty.error( {
                    title: 'Updating phone prefix',
                    msg: 'Entered phone prefix already exists',
                    sound: false
                } );
            }
        } );
    }

    $scope.close = function() {
        $modalInstance.dismiss();
    }
} ] )
.controller( 'UploadPrefixesModalController',
[ '$scope', 'BASE_URL', '$modalInstance', 'administrationService', '$http', 'toasty',
function( $scope, BASE_URL, $modalInstance, administrationService, $http, toasty ) {
    $scope.params = {};

    $scope.import = function() {
        var fd = new FormData();
        fd.append( 'file', $scope.params.file );

        $scope.loading = true;

        $http.post( BASE_URL + '/rest/public/administration/prefixes/uploaded/file', fd, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        } ).success( function( response ) {
            $scope.loading = false;

            if ( response.status === 'OK' ) {
                toasty.success( {
                    title: 'Importing prefixes',
                    msg: 'Prefixes have been imported from file successfully',
                    sound: false
                } );

                $modalInstance.close();
            } else {
                toasty.error( {
                    title: 'Importing prefixes',
                    msg: 'Uploaded file has wrong format',
                    sound: false
                } );
            }
        } );
    }

    $scope.close = function() {
        $modalInstance.dismiss();
    }
} ] );
