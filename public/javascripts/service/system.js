angular.module( 'consumer_data_base' )
.factory( 'systemService', function( $resource, BASE_URL ) {
    return $resource( '', {}, {
        sendFeedback:    { url: BASE_URL + '/rest/public/feedback', method: 'POST' },
        getSupportPhone: { url: BASE_URL + '/rest/public/support/phone/:userId', method: 'GET' },
        getUserDetails:  {url: BASE_URL + '/rest/public/user/details/:userId', method: 'GET'},
        getComments:     {url: BASE_URL + '/rest/public/comments/approved', method: 'GET'},
    } );
} );
