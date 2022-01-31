angular.module( 'consumer_data_base' )
.factory( 'alert', [ '$modal', 'BASE_URL', function( $modal, BASE_URL ) {
    return {
        getUserConfirmation: function( message ) {
             var modalInstance = $modal.open( {
                 templateUrl: BASE_URL + '/assets/partials/alert.html',
                 controller: 'AlertController',
                 resolve: {
                     message: function () { return message; }
                 } } );
        }
    }
} ] )
.controller( 'AlertController', [ '$scope', '$modalInstance', 'message',
    function( $scope, $modalInstance, message ){
        $scope.message = message;
        $scope.OK = function() { $modalInstance.dismiss(); }
    }
] );
