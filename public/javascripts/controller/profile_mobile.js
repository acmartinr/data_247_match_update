angular.module('consumer_data_base').
controller('MobileProfileController',
[ '$scope', 'BASE_URL', 'credentialsService', '$state', 'profileService', 'toasty', '$modal',
function($scope, BASE_URL, credentialsService, $state, profileService, toasty, $modal) {
    var fixHeight = function() {
        var elements = document.getElementsByClassName('main-container');
        for (var i = 0; i < elements.length; i++) {
            var element = elements[i];

            element.style.minHeight = (window.innerHeight - 187) + 'px';
        }
    }
    fixHeight();

    $scope.back = function() {
        $state.transitionTo('mobile');
    }

    $scope.profile = credentialsService.getUser();
    $scope.balance = undefined;

    $scope.inited = false;
    $scope.setInited = function() {
        $scope.inited = true;
    }

    profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).getUserBalance({ id: credentialsService.getUser().id }, function(response) {
        $scope.profile.balance = parseFloat(response.data).toFixed(2);
    });

    $scope.passwordConfig = {};
    $scope.changePassword = function() {
        var request = { oldPassword: md5($scope.passwordConfig.oldPassword),
                        newPassword: md5($scope.passwordConfig.password),
                        id: $scope.profile.id };

        profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).updatePassword(request, function(response) {
            if (response.status === 'OK') {
                toasty.success({
                    title: 'Changed successfully',
                    msg: 'Your password has been changed successfully.',
                    sound: false
                });
            } else {
                toasty.error({
                    title: 'Error on password changing',
                    msg: 'Entered password is incorrect. Try again.',
                    sound: false
                });
            }
        });
    }

    $scope.doPayment = function() {
        var modalInstance = $modal.open({
             templateUrl: BASE_URL + '/assets/partials/mobile/payment.html',
             controller: 'MobilePaymentController',
             resolve: {
                 userId: function () { return $scope.profile.id; }
             } });

        modalInstance.result.then(function(user) {
            credentialsService.setUser(user);
            $scope.profile = user;

            toasty.success({
                title: 'Paid successfully',
                msg: 'Your balance has been updated successfully.',
                sound: false
            });
        });
    }
}
]).
controller('MobilePaymentController',
[ '$scope', 'userId', 'credentialsService', 'profileService', '$modalInstance', 'toasty', '$timeout',
function($scope, userId, credentialsService, profileService, $modalInstance, toasty, $timeout) {
    $scope.payment = { amount: 100 };

    $scope.setAmount = function(amount) {
        $scope.payment.amount = amount;
    }

    profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).getCardHolderInfo({ id: userId }, function(response) {
        $scope.person = response.data;
    });

    $scope.close = function() { $modalInstance.dismiss(); }

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

            stripe.createToken(card).then(function(result) {
                if (result.error) {
                    $timeout(function() {
                        var errorElement = document.getElementById('card-errors');
                        errorElement.textContent = result.error.message;
                        $scope.loading = false;
                    }, 100)
                } else {
                    var request = { token: result.token.id,
                                    amount: $scope.payment.amount,
                                    'userId': userId,
                                    type: 'stripe',
                                    mobile: true };

                    profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).doPayment(request, function(response) {
                        if (response.status === 'OK') {
                            profileService.protected(
                                credentialsService.getUser().id,
                                credentialsService.getUser().password).
                                    getUserBalance({ id: credentialsService.getUser().id }, function(response) {
                                        var balance = response.data.toFixed(2);
                                        var user = credentialsService.getUser();
                                        user.balance = balance;
                                        credentialsService.setUser(user);
                                    });

                            $scope.loading = false;
                            $modalInstance.close(response.data);
                        } else {
                            toasty.error({
                                title: 'Payment fault',
                                msg: response.message ? response.message : 'Your payment failed. Please check again entered billing information.',
                                sound: false
                            });
                            $scope.loading = false;
                        }
                    });
                }
            });
        }
    }

    $scope.filterValue = function($event) {
        if(isNaN(String.fromCharCode($event.charCode)) &&
                $event.keyCode != 8 && $event.keyCode != 46 &&
                $event.keyCode != 37 && $event.keyCode != 39) {
            $event.preventDefault();
        }
    };

    var delayedStripeInit = function() {
        if (window.Stripe) {
            profileService.protected(credentialsService.getUser().id, credentialsService.getUser().password).getStripeMobileKey(function(response) {
                initStripe(response.message);
            });
        } else {
            setTimeout(delayedStripeInit, 500);
        }
    }

    setTimeout(delayedStripeInit, 500);
} ]);