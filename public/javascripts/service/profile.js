angular.module( 'consumer_data_base' ).
factory( 'profileService', function( $resource, BASE_URL ) {
    return { protected:
        function( userId, userPassword ) {
            var header = userId + ':' + userPassword;
            return $resource( '', {}, {
                updateDetails:        { url: BASE_URL + '/rest/public/profile/details', method: 'POST', headers: { 'auth': header } },
                updatePassword:       { url: BASE_URL + '/rest/public/profile/password', method: 'POST', headers: { 'auth': header } },
                sendVerificationCode: { url: BASE_URL + '/rest/public/profile/verification/:id', method: 'GET', headers: { 'auth': header } },
                verifyPhone:          { url: BASE_URL + '/rest/public/profile/verification', method: 'POST', headers: { 'auth': header } },
                doPayment:            { url: BASE_URL + '/rest/public/profile/payment', method: 'POST', headers: { 'auth': header } },
                getCardHolderInfo:    { url: BASE_URL + '/rest/public/profile/card/holder/:id', method: 'GET', headers: { 'auth': header } },
                getUserBalance:       { url: BASE_URL + '/rest/public/profile/balance/:id', method: 'GET', headers: { 'auth': header } },
                getStripePublicKey:   { url: BASE_URL + '/rest/public/profile/stripe/key/public', method: 'GET', headers: { 'auth': header } },
                getStripeMobileKey:   { url: BASE_URL + '/rest/public/profile/stripe/key', method: 'GET', headers: { 'auth': header } },
                saveComment:          { url: BASE_URL + '/rest/public/profile/comment', method: 'POST', headers: { 'auth': header } }
            } );
        }
    }
} );