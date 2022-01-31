angular.module( 'consumer_data_base' ).
controller( 'RegistrationRequestsController',
[ '$scope', 'BASE_URL', '$modal', '$filter', '$modal', '$state', '$cookies', '$window', 'credentialsService', 'confirm', 'administrationService', 'toasty', 'localization', 'authService',
function( $scope, BASE_URL, $modal, $filter, $modal, $state, $cookies, $window, credentialsService, confirm, administrationService, toasty, localization, authService ) {
    $scope.config = { sortValue: 'rr.date', sortDesc: true, page: 1, limit: 10 };

    $scope.userId = credentialsService.getUser().id;
    $scope.userPassword = credentialsService.getUser().password;
    $scope.isAdmin = credentialsService.getUser().admin > 0;
    $scope.isManager = credentialsService.getUser().role === 2;
    $scope.resellerNumber = credentialsService.getUser().resellerNumber;

    $scope.accessGranted = false;
    administrationService.protected($scope.userId, $scope.userPassword).
        getValidateRegistrationRequestsData(function(response) {
             $scope.accessGranted = response.data;
    });

    $scope.search = function() {
        administrationService.protected($scope.userId, $scope.userPassword).
            getRegistrationRequestList($scope.config, function(response) {
                $scope.registrationRequests = response.requests;
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
        } else if (value == 'rr.date') {
            $scope.config.sortDesc = true;
        } else {
            $scope.config.sortDesc = false;
        }

        $scope.config.sortValue = value;
        $scope.config.page = 1;

        $scope.search();
    }

    $scope.verifyRegistrationRequest = function(registrationRequest) {
        authService.validateRegistration(registrationRequest, function(response) {
            if (response.status == 'OK') {
                $scope.sendManualVerificationRequest(registrationRequest)
            } else {
                toasty.error( {
                    title: localization.localize( 'registration.error' ),
                    msg: localization.localize( response.message ),
                    sound: false
                } );
            }
        });
    }

    $scope.sendManualVerificationRequest = function(registrationRequest) {
        administrationService.protected($scope.userId, $scope.userPassword).
            manuallyVerifyRegistrationRequest({id: registrationRequest.id}, function(response) {
            if (response.status == 'OK') {
                $scope.search();
            }
        });
    }

    $scope.cancelRegistrationRequest = function(registrationRequest) {
        confirm.getUserConfirmation( 'Do you really want to delete this registration request?', function() {
            administrationService.protected($scope.userId, $scope.userPassword).
                cancelRegistrationRequest({id: registrationRequest.id}, function(response) {
                if (response.status == 'OK') {
                    $scope.search();
                }
            });
        });
    }

    $scope.getFormattedDate = function(date) {
        if (date) {
            return $filter('date')(date, 'MM/dd/yyyy HH:mm');
        } else {
            return 'no data';
        }
    }

    $scope.exportRegistrationRequests = function() {
        administrationService.
            protected($scope.userId, $scope.userPassword).
            exportRegistrationRequests($scope.config, function(response) {
                if (response.status === 'OK') {
                    var path = BASE_URL + '/rest/public/administration/download/' + response.message;
                    var frame = angular.element('<iframe src="' + path + '" style="display: none;" ></iframe>' );
                    angular.element(document.getElementById('hidden_frame')).append(frame);
                }
        } );
    }
} ] );
