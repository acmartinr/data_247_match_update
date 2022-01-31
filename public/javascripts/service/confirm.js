angular.module( 'consumer_data_base' )
.factory( 'confirm', [ '$modal', 'BASE_URL', function( $modal, BASE_URL ) {
    return {
        getUserConfirmation: function( message, callback ) {
             var modalInstance = $modal.open( {
                 templateUrl: BASE_URL + '/assets/partials/confirm.html',
                 controller: 'ConfirmController',
                 resolve: {
                     message: function () { return message; }
                 } } );

             modalInstance.result.then( function() { if ( callback ) callback(); } );
        }
    }
} ] )
.controller( 'ConfirmController', [ '$scope', '$modalInstance', 'message',
    function( $scope, $modalInstance, message ){
        $scope.message = message;

        $scope.OK = function() { $modalInstance.close(); }
        $scope.cancel = function() { $modalInstance.dismiss() }
    }
] );
