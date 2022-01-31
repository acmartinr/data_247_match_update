angular.module( 'consumer_data_base' )
.factory( 'credentialsService', function( $cookies, authService ) {
    var user;
    if ( $cookies.user ) {
        user = JSON.parse( $cookies.user );
    }

    return {
        login: function( login, password, prepareCallback, successCallback ) {
            authService.login( { name: login, password: password }, function( response ) {
                if ( response.status == "OK" ) {
                    prepareCallback();

                    user = response.data;
                    $cookies.user = JSON.stringify( user );
                }

                successCallback( response );
            } );
        },

        setUser: function( outUser ) {
            user = outUser;

            delete $cookies.user;
            $cookies.user = JSON.stringify( outUser );
        },

        logout: function() {
            authService.logout();

            user = undefined;
            delete $cookies.user;
        },

        update: function( newUser ) {
            user = newUser;
            $cookies.user = JSON.stringify( newUser )
        },

        isLoggedIn: function() { return user !== undefined; },

        getUserName: function() { return user ? user.username : undefined; },
        getUserLogin: function() { return user ? user.login : undefined; },
        getId: function() { return user ? user.id : undefined; },
        getUserBalance: function() {return user ? user.balance : 0},

        getUser : function() {
            var result = {};
            for ( var p in user )
                result[ p ] = user[ p ]

            return result;
        }
    }
} )
.factory( 'authService', function( $resource, BASE_URL ) {
    return $resource( '', {}, {
        login: { url: BASE_URL + '/rest/public/auth/login', method: 'POST' },
        loginWithToken: { url: BASE_URL + '/rest/public/auth/login/:token', method: 'GET' },
        logout: { url: BASE_URL + '/rest/public/auth/logout', method: 'POST' },
        registration: { url: BASE_URL + '/rest/public/auth/register', method: 'POST' },
        validateRegistration: { url: BASE_URL + '/rest/public/auth/register/validate', method: 'POST' },
        completeRegistration: { url: BASE_URL + '/rest/public/auth/register/complete', method: 'POST' },
        recoverPassword: { url: BASE_URL + '/rest/public/auth/password/recovery/:email', method: 'GET' },
        checkPasswordRecoveryToken: { url: BASE_URL + '/rest/public/auth/password/check/:token', method: 'GET' },
        changePassword: { url: BASE_URL + '/rest/public/auth/password/change', method: 'POST' },
        checkIfRestricted: { url: BASE_URL + '/rest/public/auth/restricted/:id', method: 'GET' }
    } );
} );
