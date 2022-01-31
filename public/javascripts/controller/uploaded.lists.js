angular.module( 'consumer_data_base' ).
controller( 'UploadedListsController',
[ '$scope', '$state', 'BASE_URL', 'listService', 'credentialsService', 'confirm', 'localization', '$modal', 'toasty', '$state', '$interval',
function( $scope, $state, BASE_URL, listService, credentialsService, confirm, localization, $modal, toasty, $state, $interval ) {
    $scope.purchased = $state.current.name === 'purchased';

    $scope.config = { searchValue: '',
                      sortValue: 'date',
                      sortDesc: true };

    $scope.getDate = function( time ) {
        return new Date( time );
    }

    var updateLists = function() {
        var handler = function( response ) {
            if ( response.status === 'OK' ) {
                $scope.lists = response.data;
            }
        };

        listService.getUploadedLists( { id: credentialsService.getUser().id }, handler );
    }
    updateLists();

    $scope.interval = $interval(updateLists, 10000);
    $scope.$on('$destroy', function() {
        if ($scope.interval) $interval.cancel($scope.interval);
    });

    $scope.isAdmin = function() {
        return credentialsService.getUser().admin;
    }

    $scope.isManager = function() {
        return credentialsService.getUser().role === 2;
    }

    $scope.editList = function( list ) {
        var modalInstance = $modal.open( {
             templateUrl: BASE_URL + '/assets/partials/modal/edit.list.html',
             controller: 'EditListModalController',
             resolve: { list: function() { return list; },
                        uploaded: function() { return true; } }
         } );

        modalInstance.result.then( function( name ) {
            list.name = name;

            listService.updateUploadedList( list, function( response ) {
                if ( response.status === 'OK' ) {
                    toasty.success( {
                        title: localization.localize( 'lists.updated.successfully' ),
                        msg: localization.localize( 'lists.updated.successfully.message' ),
                        sound: false
                    } );

                    updateLists();
                }
            } )
        } );
    }

    $scope.removeList = function( list ) {
        confirm.getUserConfirmation( localization.localize( 'lists.delete.confirm') , function() {
            listService.deleteUploadedList( { id: list.id }, function( response ) {
                if ( response.status === 'OK' ) {
                    toasty.success( {
                        title: localization.localize( 'lists.deleted.successfully' ),
                        msg: localization.localize( 'lists.deleted.successfully.message' ),
                        sound: false
                    } );

                    updateLists();
                }
            } )
        } );
    }

    $scope.uploadList = function() {
        var modalInstance = $modal.open( {
            templateUrl: BASE_URL + '/assets/partials/modal/upload.list.html',
            controller: 'UploadListModalController',
            resolve: {more: function(){ return false; }}
        } );

        modalInstance.result.then( function( list ) {
            list.userId = credentialsService.getUser().id;
            listService.saveUploadedList( list, function( response ) {
                if ( response.status === 'OK' ) {
                    toasty.success( {
                        title: 'Uploading list started',
                        msg: 'You will able to use new list when it is ready',
                        sound: false
                    } );

                    updateLists();
                }
            } );
        } );
    }

    $scope.uploadMore = function(list) {
        var modalInstance = $modal.open({
            templateUrl: BASE_URL + '/assets/partials/modal/upload.list.html',
            controller: 'UploadListModalController',
            resolve: {more: function(){ return true; }}
        });

        modalInstance.result.then( function( outList ) {
            outList.userId = credentialsService.getUser().id;
            outList.listId = list.id;

            listService.saveUploadedList( outList, function( response ) {
                if ( response.status === 'OK' ) {
                    toasty.success( {
                        title: 'Uploading list started',
                        msg: 'You will able to use new records when it is ready',
                        sound: false
                    } );

                    updateLists();
                }
            } );
        } );

    }

    $scope.sortByField = function( field ) {
        if ( field === $scope.config.sortValue ) {
            $scope.config.sortDesc = !$scope.config.sortDesc;
        } else {
            $scope.config.sortValue = field;
            $scope.config.sortDesc = false;
        }
    }
}
] ).
controller('UploadListModalController',
['$scope', 'BASE_URL', '$modalInstance', 'listService', '$http', 'toasty', 'more',
function($scope, BASE_URL, $modalInstance, listService, $http, toasty, more) {
    $scope.params = {};
    $scope.more = more;

    $scope.check = function() {
        var fd = new FormData();
        fd.append( 'file', $scope.params.file );
        $scope.loading = true

        $http.post( BASE_URL + '/rest/public/lists/uploaded/file', fd, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        } ).success( function( response ) {
            if ( response.status === 'OK' ) {
                $scope.loading = false

                $scope.columns = response.data;
                $scope.params.filePath = response.message;

                if ( $scope.columns.length > 0 ) {
                    $scope.params.column = $scope.columns[ 0 ];
                }
            } else {
                toasty.error( {
                    title: 'Uploading file',
                    msg: 'Uploaded file has wrong format',
                    sound: false
                } );
            }
        } );
    }

    $scope.close = function() {
        $modalInstance.dismiss();
    }

    $scope.save = function() {
        $modalInstance.close( { column: $scope.columns.indexOf( $scope.params.column ),
                                filePath: $scope.params.filePath,
                                name: $scope.params.name } );
    }
} ] ).
directive( 'fileModel', [ '$parse', function ( $parse ) {
    return {
        restrict: 'A',
        link: function( scope, element, attrs ) {
            element.bind( 'change', function() {
                scope.$apply( function() {
                    scope.params.file = element[ 0 ].files[ 0 ];
                } );
            } );
        }
    };
} ] )
