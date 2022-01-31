//import {mainServerAddress} from "../util/constants";

angular.module( 'consumer_data_base_invoice', [ 'ngResource', 'ui.bootstrap', 'angular-toasty' ] )
.config( [ 'toastyConfigProvider', function( toastyConfigProvider ) {
	toastyConfigProvider.setConfig( {
		limit: 1
	} );
} ] )
.provider('BASE_URL', [function() {
    this.$get = function() {
        if (document.URL.indexOf('localhost') != -1) {
            return '';
        }  else {
          //  return "http://dev.wsdevworld.com:9000";
           return "https://www.makemydata.com";
        }
    }
}])
.controller('InvoiceController',
[ '$scope', 'invoiceService', 'toasty', '$timeout', '$filter','$modal','BASE_URL',
function( $scope, invoiceService, toasty, $timeout, $filter,$modal,BASE_URL) {
    $scope.lines = [];

    invoiceService.getInvoiceDetails({id: document.invoiceNumber}, function(response) {
        if (response.data && response.data.id) {
            if (response.data.paid) {
                $scope.errorMessage = 'This invoice is already paid!';
            } else {
                $scope.paymentRequest = response.data;
                $scope.lines = response.data.note.split('\n');
            }
        } else {
            $scope.errorMessage = 'There is no such invoice!';
        }
    });

    $scope.formatDate = function(date) {
        if (date) {
            return $filter( 'date' )( date, 'MM/dd/yyyy' );
        } else {
            return 'no data';
        }
    }

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

    $scope.processPaymentOnBackend = function(token) {
        var request = { token: token,
                        invoiceId: document.invoiceNumber };

        invoiceService.payInvoice( request, function( response ) {
            if ( response.status === 'OK' ) {
                $scope.loading = false;
                toasty.success( {
                    title: 'Invoice payment status',
                    msg: 'Your invoice has been paid successfully',
                    sound: false
                } );
                var modalInstanceSucess = $modal.open( {
                    templateUrl: BASE_URL + '/assets/partials/modal/success.payment.html',
                    controller: 'PaymentSuccessControllerSuccess',

                } );
            } else {
                toasty.error( {
                    title: 'Invoice payment error',
                    msg: response.message,
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

    var delayedStripeInit = function() {
        if ( window.Stripe ) {
            initStripe('pk_live_qR9Vw0Aaoe97gbj2hgxbSlA8');
        } else {
            setTimeout( delayedStripeInit, 500 );
        }
    }

    setTimeout(delayedStripeInit, 500);

}]).
controller( 'PaymentSuccessControllerSuccess',
    [ '$scope', '$modalInstance', '$timeout',
        function( $scope, $modalInstance, $timeout ) {
            $scope.goToDashBoard = function (){
                window.location.href = '/'
            }

        } ] )
.directive( 'stripe', function() {
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
})
.factory( 'invoiceService', function( $resource, BASE_URL ) {
    return $resource( '', {}, {
        getInvoiceDetails:  { url: BASE_URL + '/rest/public/invoice/:id', method: 'GET' },
        payInvoice:         { url: BASE_URL + '/rest/public/invoice/pay', method: 'POST' }
    } );
} );




