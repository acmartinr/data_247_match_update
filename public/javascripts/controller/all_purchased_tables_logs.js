angular.module( 'consumer_data_base' ).
controller( 'AllPurchasedTablesLogsController',
    [ '$scope', '$state', 'BASE_URL', 'listService', 'systemService', 'credentialsService', 'confirm', 'localization', '$modal', 'toasty', '$state', '$interval', 'administrationService',
        function( $scope, $state, BASE_URL, listService, systemService, credentialsService, confirm, localization, $modal, toasty, $state, $interval, administrationService ) {
            $scope.purchased = $state.current.name === 'purchased';
            $scope.allPurchased = $state.current.name === 'all_purchased';
            $scope.allowTransferToSuppression = false;

            systemService.getUserDetails({userId: credentialsService.getUser().id}, function(response) {
                if (response.status == 'OK') {
                    $scope.allowTransferToSuppression = response.data.allowTransferToSuppression;
                }
            });

            $scope.isReseller = function() {
                return credentialsService.getUser().resellerNumber;
            }

            $scope.isAdmin = function() {
                return credentialsService.getUser().admin > 0;
            }

            $scope.userId = credentialsService.getUser().id;
            $scope.userPassword = credentialsService.getUser().password;

            $scope.loginAsUser = function( list ) {
                administrationService.protected( $scope.userId, $scope.userPassword ).loginAsUser( {id: list.userId}, function( response ) {
                    if ( response.status == "OK" ) {
                        user = response.data;
                        credentialsService.setUser(user);

                        $state.go( 'main' );
                        setTimeout(function() { location.reload(true); }, 100);
                    }
                } );
            }

            $scope.hideDownloadButton = function(list) {
                if (list.userId == credentialsService.getUser().id) {
                    return false;
                }

                /*if ($scope.isReseller()) {
                    return true;
                }*/

                return false;
            }

            $scope.needToHideDownloadButton = function() {
                if (!$scope.lists) { return true; }

                for (var i = 0; i < $scope.lists.length; i++) {
                    if (!$scope.hideDownloadButton($scope.lists[i])) {
                        return false;
                    }
                }

                return true;
            }

            $scope.config = { searchValue: '',
                sortValue: 'date',
                sortDesc: true,
                page: 1,
                limit: 10,
                userId: credentialsService.getUser().id };

            $scope.getDate = function( time ) {
                return new Date( time );
            }

            $scope.exportLists = function() {
                administrationService.
                protected( $scope.userId, $scope.userPassword ).
                exportLists( $scope.config, function( response ) {
                    if ( response.status === 'OK' ) {
                        var path = BASE_URL + '/rest/public/administration/download/' + response.message;
                        var frame = angular.element('<iframe src="' + path + '" style="display: none;" ></iframe>' );
                        angular.element( document.getElementById( 'hidden_frame' ) ).append( frame );
                    }
                } );
            }

            var updateLists = function() {
                var handler = function( response ) {
                    if ( response.status === 'OK' ) {
                        $scope.lists = response.data.lists;
                        $scope.total = response.data.total;
                    }
                };

                if ($scope.isReseller()) {
                    $scope.config.resellerId = $scope.config.userId;
                }

                administrationService.protected( $scope.userId, $scope.userPassword ).
                purchasedlistslogs( $scope.config, handler );

                //listService.getPagedUsersPurchased( $scope.config, handler );

            }
            updateLists();

            $scope.getLocalizedListType = function( list ) {
                return localization.localize( 'lists.type.' + list.type );
            }
            $scope.searchWithParams = function(list) {
                $state.transitionTo('main', {searchRequest: JSON.parse( list.request )});
            }
            $scope.getFormattedTableName = function(name) {
                var parts = name.split('_archived_');
                if (parts.length > 1) {
                    return parts[0] + "[a]"
                } else {
                    return name;
                }
            }

            $scope.getCountDetails = function(list) {
                var modalInstance = $modal.open({
                    templateUrl: BASE_URL + '/assets/partials/modal/count.details.html',
                    controller: 'RequestCountDetailsModalController',
                    resolve: {
                        list: function() { return list; },
                        request: function() { return undefined; }
                    }
                });
            }

            $scope.sortByField = function( field ) {
                if ( field === $scope.config.sortValue ) {
                    $scope.config.sortDesc = !$scope.config.sortDesc;
                } else {
                    $scope.config.sortValue = field;
                    $scope.config.sortDesc = false;
                }

                updateLists();
            }

            $scope.searchByValue = function() {
                $scope.config.page = 1;
                updateLists();
            }

            $scope.search = function() {
                updateLists();
            }

            $scope.$on( '$destroy', function() {
                if ( $scope.interval ) $interval.cancel( $scope.interval );
            } );

            $scope.isOriginallyReseller = function() {
                return false;//credentialsService.getUser().originalRole == 1;
            }
        }
    ] )