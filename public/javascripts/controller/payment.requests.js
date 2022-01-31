angular.module( 'consumer_data_base' ).
controller('PaymentRequestsController',
[ '$scope', 'BASE_URL', '$modal', '$filter', '$state', '$cookies', '$window', 'credentialsService', 'confirm', 'administrationService', 'toasty', 'localization',
function( $scope, BASE_URL, $modal, $filter, $state, $cookies, $window, credentialsService, confirm, administrationService, toasty, localization ) {
    $scope.config = { searchValue: '', page: 1, limit: 10 };
    $scope.currentTime = new Date().getTime();

    var user = credentialsService.getUser();
    $scope.search = function() {
        administrationService.protected( user.id, user.password ).
            getPaymentRequestsList( $scope.config, function( response ) {
                $scope.paymentRequests = response.data.paymentRequests;
                $scope.total = response.data.total;
            } );
    }
    $scope.search();

    $scope.searchByValue = function() {
        $scope.config.page = 1;
        $scope.search();
    }


    $scope.getFormattedDate = function(date, full) {
        if (date && full) {
            return $filter( 'date' )( date, 'MM/dd/yyyy HH:mm' );
        } else if (date && !full) {
            return $filter( 'date' )( date, 'MM/dd/yyyy' );
        } else {
            return 'no data';
        }
    }

    $scope.addNewPaymentRequest = function() {
        $scope.editPaymentRequest({});
    }

    $scope.editPaymentRequest = function(paymentRequest) {
        var modalInstance = $modal.open( {
             templateUrl: BASE_URL + '/assets/partials/modal/edit.payment.request.html',
             controller: 'EditPaymentRequestController',
             resolve: {
                 paymentRequest: function () { return paymentRequest; }
             } } );

        modalInstance.result.then( function() { $scope.search(); } );
    }

    $scope.removePaymentRequest = function( comment ) {
        confirm.getUserConfirmation( 'Do you really want to delete this payment request?', function() {
            administrationService.protected(user.id, user.password).deletePaymentRequest( comment, function() {
                $scope.search();
            } );
        } );
    }
} ] )
.controller( 'EditPaymentRequestController',
[ '$scope', '$modalInstance', 'paymentRequest', 'credentialsService', 'administrationService', 'toasty',
function( $scope, $modalInstance, paymentRequest, credentialsService, administrationService, toasty ) {
    $scope.paymentRequest = {};
    for (var prop in paymentRequest) {
        $scope.paymentRequest[prop] = paymentRequest[prop];
    }

    if (paymentRequest.dueDate) {
        $scope.paymentRequest.dueDate = new Date(paymentRequest.dueDate);
    }

    var user = credentialsService.getUser();

    $scope.save = function() {
        $scope.errorMessage = '';

        var request = {};
        for (var prop in $scope.paymentRequest) {
            request[prop] = $scope.paymentRequest[prop];
        }
        request.dueDate = request.dueDate.getTime();

        administrationService.protected(user.id, user.password).savePaymentRequest(request, function( response ) {
            if ( response.status === 'OK' ) {
                toasty.success( {
                    title: 'Payment request save status',
                    msg: 'Payment request has been updated successfully',
                    sound: false
                } );

                $modalInstance.close();
            }
        } );
    }

    $scope.close = function() {
        $modalInstance.dismiss();
    }

} ] );