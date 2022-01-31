angular.module( 'consumer_data_base' ).
controller( 'RecoveryPasswordController',
[ '$scope', '$modalInstance', 'toasty', 'authService',
function( $scope, $modalInstance, toasty, authService ) {
    $scope.email = '';

    $scope.close = function() {
        $modalInstance.dismiss();
    }

    $scope.recoverPassword = function() {
        authService.recoverPassword( { email: $scope.email }, function() {
            toasty.success( {
                title: 'Password recovery',
                msg: 'Email with password recovery instructions has been sent to your email',
                sound: false
            } );
            $scope.close();
        } );
    }
}
] ).
controller( 'ChangePasswordModalController',
[ '$scope', '$modalInstance', 'toasty', 'authService', 'token',
function( $scope, $modalInstance, toasty, authService, token ) {
    $scope.password = '';
    $scope.confirm = '';

    $scope.close = function() {
        $modalInstance.dismiss();
    }

    $scope.changePassword = function() {
        authService.changePassword( { 'token': token, password: md5( $scope.password ) }, function( response ) {
            toasty.success( {
                title: 'Password changed',
                msg: 'Your password has been changed successfully',
                sound: false
            } );
            $modalInstance.close( { username: response.message, password: md5( $scope.password ) } );
        } );
    }
}
] )

