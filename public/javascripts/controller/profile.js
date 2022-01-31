angular.module( 'consumer_data_base' ).
controller( 'ProfileController',
[ '$scope', 'BASE_URL', 'credentialsService', 'profileService', 'systemService', 'localization', 'toasty', '$modal',
function( $scope, BASE_URL, credentialsService, profileService, systemService, localization, toasty, $modal ) {
    $scope.profile = credentialsService.getUser();
    $scope.balance = undefined;
    $scope.allowPayments = false;

    $scope.inited = false;
    $scope.setInited = function() {
        $scope.inited = true;
    }

    profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).getUserBalance( { id: credentialsService.getUser().id }, function( response ) {
        $scope.profile.balance = parseFloat( response.data ).toFixed( 2 );
    } );

    systemService.getUserDetails({userId: credentialsService.getUser().id}, function(response) {
        if (response.status == 'OK') {
            $scope.allowPayments = response.data.allowPayments;
        }
    });

    $scope.updateProfile = function() {
        var request = { address: $scope.profile.address,
                        companyName: $scope.profile.companyName,
                        phone: $scope.profile.phone,
                        verified: $scope.profile.verified,
                        id: $scope.profile.id };

        profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).updateDetails( request, function( response ) {
            if ( response.status === 'OK' ) {
                $scope.profile.verified = response.data.verified;

                toasty.success( {
                    title: localization.localize( 'profile.saved.successfully' ),
                    msg: localization.localize( 'profile.saved.successfully.message' ),
                    sound: false
                } );

                credentialsService.setUser( $scope.profile );
            }
        } );
    }

    $scope.isAddress = function() {
        return $scope.profile.address && $scope.profile.address.split(" ").length > 3;
    }

    $scope.passwordConfig = {};
    $scope.changePassword = function() {
        var request = { oldPassword: md5( $scope.passwordConfig.oldPassword ),
                        newPassword: md5( $scope.passwordConfig.password ),
                        id: $scope.profile.id };

        profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).updatePassword( request, function( response ) {
            if ( response.status === 'OK' ) {
                toasty.success( {
                    title: localization.localize( 'profile.changed.successfully' ),
                    msg: localization.localize( 'profile.changed.successfully.message' ),
                    sound: false
                } );
            } else {
                toasty.error( {
                    title: localization.localize( 'profile.changed.error' ),
                    msg: localization.localize( 'profile.changed.error.message' ),
                    sound: false
                } );
            }
        } );
    }

    $scope.verifyPhoneNumber = function() {
        var modalInstance = $modal.open( {
             templateUrl: BASE_URL + '/assets/partials/modal/phone.verification.html',
             controller: 'PhoneVerificationController',
             resolve: {
                 userId: function () { return $scope.profile.id; }
             } } );

        modalInstance.result.then( function() {
            $scope.profile.verified = 1;
            credentialsService.setUser( $scope.profile );

            toasty.success( {
                title: localization.localize( 'profile.verified.successfully' ),
                msg: localization.localize( 'profile.verified.successfully.message' ),
                sound: false
            } );
        } );
    }

    $scope.isPaymentAvailable = function() {
        return document.URL.indexOf("mytargetdata.com") == -1 && $scope.allowPayments;
    }

    $scope.doPayment = function() {
        var modalInstance = $modal.open( {
             templateUrl: BASE_URL + '/assets/partials/modal/payment.html',
             controller: 'PaymentController',
             resolve: {
                 userId: function () { return $scope.profile.id; }
             } } );

        modalInstance.result.then( function( user ) {
            credentialsService.setUser( user );
            $scope.profile = user;

            toasty.success( {
                title: localization.localize( 'profile.payment.successfully' ),
                msg: localization.localize( 'profile.payment.successfully.message' ),
                sound: false
            } );
        } );
    }
}
] ).
controller( 'PaymentController',
[ '$scope', 'userId', 'credentialsService', 'profileService', 'localization', '$modalInstance', 'toasty', '$timeout',
function( $scope, userId, credentialsService, profileService, localization, $modalInstance, toasty, $timeout ) {
    $scope.payment = { amount: 100 };

    $scope.setAmount = function( amount ) {
        $scope.payment.amount = amount;
    }

    profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).getCardHolderInfo( { id: userId }, function( response ) {
        $scope.person = response.data;
    } );

    $scope.close = function() { $modalInstance.dismiss(); }

    $scope.onGetCardNonce = function() {
        $scope.loading = true;
        $scope.paymentForm.requestCardNonce();
    }

    var initSquareUp = function() {
        $scope.paymentForm = new SqPaymentForm({
            applicationId: "sq0idp-RmXAtG0xi79tPjYVMkPf7g",
            inputClass: 'sq-input',
            autoBuild: false,

            inputStyles: [{
               fontSize: '16px',
               lineHeight: '24px',
               padding: '4px 8px 4px 8px',
               placeholderColor: '#a0a0a0',
               backgroundColor: 'transparent',
            }],

            cardNumber: {
               elementId: 'sq-card-number',
               placeholder: 'Card Number'
            },
            cvv: {
               elementId: 'sq-cvv',
               placeholder: 'CVV'
            },
            expirationDate: {
               elementId: 'sq-expiration-date',
               placeholder: 'MM/YY'
            },
            postalCode: {
               elementId: 'sq-postal-code',
               placeholder: 'Zip Code'
            },
            callbacks: {
               cardNonceResponseReceived: function (errors, nonce, cardData) {
                  $timeout(function() {
                      $scope.processPaymentOnBackend(nonce);
                  });
               }
            }
        });
        $scope.paymentForm.build();
    }

    $scope.processPaymentOnBackend = function(token) {
        var request = { token: token,
                        amount: $scope.payment.amount,
                        'userId': userId,
                        type: 'stripe' };

        profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).doPayment( request, function( response ) {
            if ( response.status === 'OK' ) {
                $scope.loading = false;
                $modalInstance.close( response.data );
            } else {
                toasty.error( {
                    title: localization.localize( 'profile.payment.error.title' ),
                    msg: response.message ? response.message : localization.localize( 'profile.payment.error' ),
                    sound: false
                } );
                $scope.loading = false;
            }
        } );
    }

    var initStripe = function(pkKey) {
        var stripe = Stripe(pkKey);
        var elements = stripe.elements();
        var style = {
          base: {
            color: '#32325d',
            lineHeight: '24px',
            fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
            fontSmoothing: 'antialiased',
            fontSize: '16px',
            '::placeholder': {
              color: '#aab7c4'
            }
          },
          invalid: {
            color: '#fa755a',
            iconColor: '#fa755a'
          }
        };

        var card = elements.create('card', {style: style});
        card.mount('#card-element');
        card.addEventListener('change', function(event) {
          var displayError = document.getElementById('card-errors');
          if (event.error) {
            displayError.textContent = event.error.message;
          } else {
            displayError.textContent = '';
          }
        });

        $scope.doPayment = function() {
            $scope.loading = true;

            stripe.createToken( card ).then( function( result ) {
                if ( result.error ) {
                    $timeout( function() {
                        var errorElement = document.getElementById( 'card-errors' );
                        errorElement.textContent = result.error.message;
                        $scope.loading = false;
                    }, 100 )
                } else {
                    $scope.processPaymentOnBackend(result.token.id);
                }
            } );
        }
    }

    $scope.filterValue = function( $event ) {
        if( isNaN( String.fromCharCode( $event.charCode ) ) &&
                $event.keyCode != 8 && $event.keyCode != 46 &&
                $event.keyCode != 37 && $event.keyCode != 39 ) {
            $event.preventDefault();
        }
    };

    var delayedStripeInit = function() {
        if ( window.Stripe ) {
            profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).getStripePublicKey(function(response) {
                initStripe(response.message);
            });
        } else {
            setTimeout( delayedStripeInit, 500 );
        }
    }

    var delayedSquareUpInit = function() {
        if (window.SqPaymentForm) {
            initSquareUp();
        } else {
            setTimeout(delayedSquareUpInit, 500);
        }
    }

    setTimeout(delayedStripeInit, 500);
    //setTimeout(delayedSquareUpInit, 500);
} ] ).
controller( 'PhoneVerificationController',
[ '$scope', 'userId', 'credentialsService', 'profileService', 'localization', '$modalInstance', 'toasty',
function( $scope, userId, credentialsService, profileService, localization, $modalInstance, toasty ) {
    profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).sendVerificationCode( { id: userId }, function( response ) {
        if ( response.status === 'OK' ) {
            $scope.sent = true;
        }
    } );

    $scope.code = '';
    $scope.close = function() { $modalInstance.dismiss(); }
    $scope.checkCode = function() {
        var request = { id: userId,
                        code: $scope.code };
        profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).verifyPhone( request, function( response ) {
            if ( response.status === 'OK' ) {
                $modalInstance.close();
            } else {
                toasty.error( {
                    title: localization.localize( 'profile.phone.code.error.title' ),
                    msg: localization.localize( 'profile.phone.code.error' ),
                    sound: false
                } );
            }
        } );
    }
} ] ).
directive( 'stripe', function() {
    var injectScript = function(element) {
        if ( !window.Stripe ) {
            var scriptTag = angular.element(document.createElement('script'));
            scriptTag.attr('charset', 'utf-8');
            scriptTag.attr('src', 'https://js.stripe.com/v3/');
            element.append( scriptTag );
        }
    };

    return {
        link: function(scope, element) {
            injectScript(element);
        }
    };
}).
directive('squareup', function() {
    var injectScript = function(element) {
        if ( !window.SqPaymentForm ) {
            var scriptTag = angular.element(document.createElement('script'));
            scriptTag.attr('charset', 'utf-8');
            scriptTag.attr('src', 'https://js.squareup.com/v2/paymentform');
            element.append( scriptTag );
        }
    };

    return {
        link: function(scope, element) {
            injectScript(element);
        }
    };
})
.directive('focusOn',function($timeout) {
     return {
         restrict : 'A',
         link : function($scope,$element,$attr) {
             $scope.$watch($attr.focusOn,function(_focusVal) {
                 $timeout(function() {
                     if ($scope.inited === false) {
                        $scope.setInited();
                        return;
                     }

                     _focusVal ? $element[0].focus() :
                         $element[0].blur();
                 });
             });
         }
     }
 });