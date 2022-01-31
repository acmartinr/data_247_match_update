angular.module( 'consumer_data_base' ).
controller( 'UsersController',
[ '$scope', 'BASE_URL', '$modal', '$filter', '$state', '$cookies', '$window', 'credentialsService', 'confirm', 'administrationService', 'toasty', 'localization',
function( $scope, BASE_URL, $modal, $filter, $state, $cookies, $window, credentialsService, confirm, administrationService, toasty, localization ) {
    $scope.config = { sortValue: 'date', sortDesc: true, page: 1, limit: 10 };
    $scope.ivoSite = document.URL.indexOf('reidatalist') != -1;// || document.URL.indexOf('localhost') != -1;

    $scope.userId = credentialsService.getUser().id;
    $scope.userPassword = credentialsService.getUser().password;
    $scope.isAdmin = credentialsService.getUser().admin > 0;
    $scope.isManager = credentialsService.getUser().role === 2;

    $scope.resellerNumber = credentialsService.getUser().resellerNumber;

    $scope.search = function() {
        administrationService.protected( $scope.userId, $scope.userPassword ).
            getUserList( $scope.config, function( response ) {
                for ( var i = 0; i < response.users.length; i++ ) {
                    response.users[ i ].balance = response.users[ i ].balance.toFixed( 2 );
                }

                $scope.users = response.users;
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

    $scope.getFormattedDate = function( date ) {
        if ( date ) {
            return $filter( 'date' )( date, 'MM/dd/yyyy HH:mm' );
        } else {
            return 'no data';
        }
    }

    $scope.getLocalizedStatus = function(status) {
        return localization.localize('note.status.' + status);
    }

    $scope.exportUsers = function() {
        administrationService.
            protected( $scope.userId, $scope.userPassword ).
            exportUsers( $scope.config, function( response ) {
                if ( response.status === 'OK' ) {
                    var path = BASE_URL + '/rest/public/administration/download/' + response.message;
                    var frame = angular.element('<iframe src="' + path + '" style="display: none;" ></iframe>' );
                    angular.element( document.getElementById( 'hidden_frame' ) ).append( frame );
                }
        } );
    }

    $scope.editUser = function( user ) {
        var modalInstance = $modal.open( {
             templateUrl: BASE_URL + '/assets/partials/modal/user.html',
             controller: 'UserController',
             windowClass: 'user-details ',
             resolve: {
                 user: function () { return user; }
             } } );

        modalInstance.result.then( function() { $scope.search(); } );
    }

    $scope.showUserNote = function(user) {
        var modalInstance = $modal.open( {
             templateUrl: BASE_URL + '/assets/partials/modal/user.note.html',
             controller: 'UserNoteController',
             resolve: {
                 user: function () { return user; }
             } } );

        modalInstance.result.then( function() { $scope.search(); } );
    }

    $scope.loginAsUser = function( user ) {
        administrationService.protected( $scope.userId, $scope.userPassword ).loginAsUser( user, function( response ) {
            if ( response.status == "OK" ) {
                user = response.data;
                credentialsService.setUser(user);

                $state.go( 'main' );
                setTimeout(function() { location.reload(true); }, 100);
            }
        } );
    }

    $scope.showPayments = function( user ) {
        $state.go( 'payments', { username: user.username } );
    }

    $scope.removeUser = function( user ) {
        confirm.getUserConfirmation( 'Do you really want to delete this user?', function() {
            administrationService.protected( $scope.userId, $scope.userPassword ).deleteUser( user, function() {
                $scope.search();
            } );
        } );
    }

    $scope.emailUsers = function() {
        var modalInstance = $modal.open( {
             templateUrl: BASE_URL + '/assets/partials/modal/send.email.html',
             controller: 'SendEmailController' } );
    }
} ] )
.controller( 'UserController',
[ '$scope', '$modalInstance', 'user', 'dataService', 'systemService', 'credentialsService', 'administrationService', 'toasty', 'localization',
function( $scope, $modalInstance, user, dataService, systemService, credentialsService, administrationService, toasty, localization ) {
    $scope.userId = credentialsService.getUser().id;
    $scope.userPassword = credentialsService.getUser().password;

    $scope.resellers = [];
    administrationService.protected( $scope.userId, $scope.userPassword ).resellers(function(response) {
        $scope.resellers = response.data;
    });

    $scope.allowManageMoney = function() {
        return credentialsService.getUser().allowManageMoney;
    }

    $scope.config = {dataType: -1};
    $scope.dataSources = [];
    $scope.filteredPrices = [];
    $scope.dataSourceItemPriceAllowed = false;

    $scope.options = {};

    systemService.getUserDetails({userId: credentialsService.getUser().id}, function(response) {
        if (response.status == 'OK') {
            $scope.dataSourceItemPriceAllowed = response.data.allowDataSourceItemsPrices;

            $scope.options = response.data;
        }
    });

    $scope.resellerHasAccess = function(field) {
        return $scope.options[field];
    }

    dataService.dataSources({userId: user.id}, function(response) {
        $scope.dataSources = response.data;
        if (response.data.length > 0) {
            $scope.config.dataType = (response.data[0].id - 1);
            $scope.dataSources = response.data;

            $scope.filterPrices();
        }
    });

    $scope.requestPricesList = function() {
        administrationService.protected( $scope.userId, $scope.userPassword ).
            getUserPricesList( {id: user.id}, function( response ) {
                for ( var i = 0; i < response.data.length; i++ ) {
                    response.data[ i ].price = response.data[ i ].price.toFixed(3);
                }

                $scope.prices = response.data;
                $scope.filterPrices();
            } );
    }
    $scope.requestPricesList();

    $scope.onDataTypeChanged = function() {
        $scope.filterPrices();
    }

    $scope.getLocalizedPrice = function(price) {
        return localization.localize( 'prices.type.' + price.type );
    }

    $scope.filterPrices = function() {
        var result = [];

        if ($scope.prices) {
            for (var i = 0; i < $scope.prices.length; i++) {
                if ($scope.prices[i].dataSource == $scope.config.dataType) {
                    result.push($scope.prices[i]);
                }
            }
        }

        $scope.filteredPrices = result;
    }

    $scope.priceMatch = function() {
        return function(item) {
            return item.dataSource == $scope.dataType;
        };
    }

    $scope.inited = false;
    $scope.setInited = function() {
        $scope.inited = true;
    }

    $scope.profile = {};
    $scope.profile.allowedMatchesList = "";
     $scope.matches = {};
    for ( var prop in user ) {
    if(prop.toString() == 'allowedMatchesList' && user[ prop ]){
    console.log(user[ prop ]);
    let allowedMatchesArray = user[ prop ].split(",");
    allowedMatchesArray.forEach((item) => {
                                switch (item) {
                                    case 'matchHealthinsuranceleadsEnabled':
                                        $scope.matches['matchHealthinsuranceleadsEnabled'] = true;
                                        break;
                                    case 'matchHealthbuyersEnabled':
                                        $scope.matches['matchHealthbuyersEnabled'] = true;
                                        break;
                                    case 'matchInstagramEnabled':
                                        $scope.matches['matchInstagramEnabled'] = true;
                                        break;
                                    case 'matchWhoisEnabled':
                                        $scope.matches['matchWhoisEnabled'] = true;
                                        break;
                                    case 'matchBussiness2Enabled':
                                        $scope.matches['matchBussiness2Enabled'] = true;
                                        break;
                                    case 'matchBussinessEnabled':
                                        $scope.matches['matchBussinessEnabled']= true;
                                        break;
                                    case 'matchFacebookEnabled':
                                        $scope.matches['matchFacebookEnabled'] = true;
                                        break;
                                    case 'matchEverydataEnabled':
                                        $scope.matches['matchEverydataEnabled'] = true;
                                        break;
                                }
                            })
    }
        $scope.profile[ prop ] = user[ prop ];
    }

    $scope.profile.becomeReseller = user.resellerNumber > 0;
    $scope.profile.multipleGeographicParametersDisabled = !user.multipleGeographicParametersEnabled;

    $scope.block = function() {
        administrationService.protected( $scope.userId, $scope.userPassword ).blockUser( user, function() {
            $modalInstance.close();
        } );
    }

    $scope.unblock = function() {
        administrationService.protected( $scope.userId, $scope.userPassword ).unBlockUser( user, function() {
            $modalInstance.close();
        } );
    }

    $scope.isAddress = function() {
        return $scope.profile.address && $scope.profile.address.split(" ").length > 3;
    }

    $scope.isAdmin = function() {
        return credentialsService.getUser().admin > 0;
    }

    $scope.isReseller = function() {
        return credentialsService.getUser().resellerNumber;
    }

    $scope.isDataSourceItemPriceAllowed = function() {
        return $scope.dataSourceItemPriceAllowed || $scope.isAdmin();
    }

    $scope.isEnableFilterEmail = function() {
        return $scope.isAdmin() || credentialsService.getUser().id === 362 || credentialsService.getUser().id === 405;
    }

    $scope.save = function() {
    $scope.profile.allowedMatchesList = '';
        $scope.errorMessage = '';
           for ( var prop in $scope.matches ) {
            console.log("profile value"+$scope.matches[prop]);
           if($scope.matches[prop] == true){
           $scope.profile.allowedMatchesList = $scope.profile.allowedMatchesList + prop.toString()+",";
           }
           }
        $scope.profile.multipleGeographicParametersEnabled = !$scope.profile.multipleGeographicParametersDisabled;
        if ($scope.profile.password) {
            $scope.profile.newPassword = md5($scope.profile.password);
        }

        administrationService.protected( $scope.userId, $scope.userPassword ).saveUser( $scope.profile, function( response ) {
            if ( response.status === 'OK' ) {
                toasty.success( {
                    title: 'User update status',
                    msg: 'User has been updated successfully',
                    sound: false
                } );

                $modalInstance.close();
            } else {
                var errorMessage;
                if ( response.message === 'email' ) {
                    errorMessage = 'User with this email already exists';
                } else if ( response.message === 'username' ) {
                    errorMessage = 'User with this username already exists';
                } else if ( response.message === 'phone' ) {
                    errorMessage = 'User with this phone already exists';
                }

                toasty.error( {
                    title: 'User update error',
                    msg: errorMessage,
                    sound: false
                } );
            }
        } );
    }

    $scope.savePrice = function(price) {
        price.userId = user.id;
        administrationService.
            protected( $scope.userId, $scope.userPassword ).
            savePrice( price, function( response ) {
                if ( response.status === 'OK' ) {
                    toasty.success( {
                        title: 'Users source type price saving',
                        msg: 'Users source type price has been saved successfully',
                        sound: false
                    } );
                }
        } );
    }

    $scope.close = function() {
        $modalInstance.dismiss();
    }

} ] )
.controller( 'UserNoteController',
[ '$scope', '$modalInstance', 'user', 'credentialsService', 'administrationService', 'toasty',
function( $scope, $modalInstance, user, credentialsService, administrationService, toasty ) {
    $scope.userId = credentialsService.getUser().id;
    $scope.userPassword = credentialsService.getUser().password;
    $scope.note = user.note;
    $scope.noteStatus = user.noteStatus;

    $scope.save = function() {
        var request = {userId: user.id, note: $scope.note, noteStatus: $scope.noteStatus};
        administrationService.protected($scope.userId, $scope.userPassword).updateUserNote(request, function(response) {
            if (response.status === 'OK') {
                toasty.success({
                    title: 'User note update status',
                    msg: 'User note has been updated successfully',
                    sound: false
                });

                $modalInstance.close();
            }
        } );
    }

    $scope.close = function() {
        $modalInstance.dismiss();
    }

} ] )
.controller( 'SendEmailController',
[ '$scope', '$modalInstance', 'administrationService', 'credentialsService', 'toasty',
function($scope, $modalInstance, administrationService, credentialsService, toasty) {
    $scope.model = {};
    $scope.loading = false;

    $scope.userId = credentialsService.getUser().id;
    $scope.userPassword = credentialsService.getUser().password;

    $scope.sendEmail = function() {
        if ($scope.model.subject && $scope.model.body && $scope.model.from && $scope.model.fromName) {
            $scope.loading = true;

            if ($scope.model.recipients) {
                $scope.model.emails = $scope.model.recipients.split('\n');
            }

            administrationService.protected($scope.userId, $scope.userPassword).sendEmails($scope.model, function(response) {
                $scope.loading = false;

                if (response.status == 'OK') {
                    toasty.success( {
                        title: 'Bulk email sending',
                        msg: 'Emails have been sent successfully',
                        sound: false
                    } );

                    $scope.close();
                }
            });
        }
    }

    $scope.close = function() {
        $modalInstance.dismiss();
    }

}]);
