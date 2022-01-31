angular.module( 'consumer_data_base' ).
controller( 'CommentsController',
[ '$scope', 'BASE_URL', '$modal', '$filter', '$state', '$cookies', '$window', 'credentialsService', 'confirm', 'administrationService', 'toasty', 'localization',
function( $scope, BASE_URL, $modal, $filter, $state, $cookies, $window, credentialsService, confirm, administrationService, toasty, localization ) {
    $scope.config = { sortValue: 'c.date', sortDesc: false, page: 1, limit: 10 };

    var user = credentialsService.getUser();
    $scope.search = function() {
        administrationService.protected( user.id, user.password ).
            getCommentsList( $scope.config, function( response ) {
                $scope.comments = response.comments;
                $scope.total = response.total;
            } );
    }
    $scope.search();

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

    $scope.editComment = function( comment ) {
        var modalInstance = $modal.open( {
             templateUrl: BASE_URL + '/assets/partials/modal/edit.comment.html',
             controller: 'EditCommentController',
             resolve: {
                 comment: function () { return comment; }
             } } );

        modalInstance.result.then( function() { $scope.search(); } );
    }

    $scope.removeComment = function( comment ) {
        confirm.getUserConfirmation( 'Do you really want to delete this comment?', function() {
            administrationService.protected(user.id, user.password).deleteComment( comment, function() {
                $scope.search();
            } );
        } );
    }
} ] )
.controller( 'EditCommentController',
[ '$scope', '$modalInstance', 'comment', 'credentialsService', 'administrationService', 'toasty',
function( $scope, $modalInstance, comment, credentialsService, administrationService, toasty ) {
    $scope.comment = {};
    for (var prop in comment) {
        $scope.comment[prop] = comment[prop];
    }

    var user = credentialsService.getUser();

    $scope.save = function() {
        $scope.errorMessage = '';
        administrationService.protected(user.id, user.password).updateComment( $scope.comment, function( response ) {
            if ( response.status === 'OK' ) {
                toasty.success( {
                    title: 'Comment update status',
                    msg: 'Comment has been updated successfully',
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