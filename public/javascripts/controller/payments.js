angular.module( 'consumer_data_base' ).
controller( 'PaymentsController',
[ '$scope', 'BASE_URL', '$modal', '$stateParams', 'credentialsService', 'administrationService', 'toasty', 'localization',
function( $scope, BASE_URL, $modal, $stateParams, credentialsService, administrationService, toasty, localization ) {
    $scope.config = { sortValue: 'payments.date', searchValue: $stateParams.username, sortDesc: true, page: 1, limit: 10 };

    $scope.userId = credentialsService.getUser().id;
    $scope.userPassword = credentialsService.getUser().password;

    $scope.search = function() {
        administrationService.protected( $scope.userId, $scope.userPassword ).
            getPaymentsList( $scope.config, function( response ) {
                for ( var i = 0; i < response.payments.length; i++ ) {
                    response.payments[ i ].amount = response.payments[ i ].amount.toFixed( 2 );
                }

                $scope.payments = response.payments;
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

    $scope.getLocalizedType = function(payment) {
        if ( payment.type === 'ADD_FUNDS' ) {
            var type = 'add funds';
            if (payment.manual === true) {
                type = type + ' (by admin)';
            } else if (payment.manual === false) {
                type = type + ' (with Stripe)';
            }

            return type;
        } else {
            return 'spend';
        }
    }

    $scope.addPayment = function() {
        var modalInstance = $modal.open( {
             templateUrl: BASE_URL + '/assets/partials/modal/add.payment.html',
             controller: 'AddPaymentController' } );

        modalInstance.result.then( function() { $scope.search(); } );
    }

    $scope.isAdmin = function() {
        return credentialsService.getUser().admin;
    }

    $scope.isManager = function() {
        return credentialsService.getUser().role === 2;
    }

    $scope.allowManageMoney = function() {
        return credentialsService.getUser().allowManageMoney;
    }
} ] )
.controller( 'AddPaymentController',
[ '$scope', '$modalInstance', 'credentialsService', 'administrationService', 'toasty', 'localization',
function( $scope, $modalInstance, credentialsService, administrationService, toasty, localization ) {
    $scope.userId = credentialsService.getUser().id;
    $scope.userPassword = credentialsService.getUser().password;

    $scope.payment = {};
    $scope.userAutoCompletes = [];
    $scope.userAutoCompleteObject = {};

    administrationService.protected( $scope.userId, $scope.userPassword ).getUserPaymentAutoComplete( function( response ) {
        $scope.users = response;

        for (var i = 0; i < $scope.users.length; i++) {
            var key = $scope.users[i].username + " (" + $scope.users[i].email + ")";
            $scope.userAutoCompletes.push(key);
            $scope.userAutoCompleteObject[key] = $scope.users[i].username;
        }
    } );

    $scope.addPayment = function() {
        if ((!$scope.payment.username || !$scope.userAutoCompleteObject[$scope.payment.username]) && !$scope.payment.allUsers) {
            toasty.error( {
                title: 'Adding payment error',
                msg: 'Please select user from list',
                sound: false
            } );

            return;
        }

        if ( isNaN( parseFloat( $scope.payment.amount ) ) ) {
            toasty.error( {
                title: 'Adding payment error',
                msg: 'Please enter correct payment',
                sound: false
            } );

            return;
        }

        $scope.loading = true;
        var payment = {username: $scope.userAutoCompleteObject[$scope.payment.username],
                       allUsers: $scope.payment.allUsers,
                       amount: $scope.payment.amount};

        administrationService.protected( $scope.userId, $scope.userPassword ).addPayment( payment, function( response ) {
            $scope.loading = false;

            if (response.status === 'OK') {
                toasty.success( {
                    title: 'Payment update status',
                    msg: 'Payment has been added successfully',
                    sound: false
                } );

                $modalInstance.close();
            } else if (response.message == 'balance') {
                toasty.error( {
                    title: 'Adding payment error',
                    msg: 'Your balance is lower than the adding amount',
                    sound: false
                } );
            } else {
                toasty.error( {
                    title: 'Adding payment error',
                    msg: 'Please enter correct username',
                    sound: false
                } );
            }
        } );
    }

    $scope.close = function() {
        $modalInstance.dismiss();
    }
} ] );