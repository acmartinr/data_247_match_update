//import {mainServerAddress} from "../../util/constants";

angular.module( 'consumer_data_base' ).
controller( 'HeaderController',
[ '$scope', 'BASE_URL', 'credentialsService', 'systemService', '$state', '$document', '$modal', 'toasty',
function( $scope, BASE_URL, credentialsService, systemService, $state, $document, $modal, toasty ) {
    $scope.menu = { expanded: false };
    $scope.listsMenu = { expanded: false };
    $scope.administrationMenu = { expanded: false };
    $scope.allowMatchingLists = false;
    $scope.dataSourceItemPriceAllowed = false;

    systemService.getUserDetails({userId: credentialsService.getUser().id}, function(response) {
        if (response.status == 'OK') {
            $scope.dataSourceItemPriceAllowed = response.data.allowDataSourceItemsPrices;
        }
    });

    $scope.ivoSite = document.URL.indexOf('reidatalist') != -1;// || document.URL.indexOf('localhost') != -1;

    var isCalienteLeads = function() {
        return document.URL.indexOf('axiomleads.com') != -1;
    }

    var isMultimediaListsCom = function() {
        return document.URL.indexOf('multimedialists.com') != -1;
    }

    var isMultimediaListsNet = function() {
        return document.URL.indexOf('multimedialists.net') != -1;
    }

    var isAllWebReferralsLists = function() {
        return document.URL.indexOf('allwebreferrals') != -1;
    }

    var isMyTargetData = function() {
        return document.URL.indexOf('mytargetdata') != -1;
    }

    var isSalesListData = function() {
        return document.URL.indexOf('sales-list') != -1;
    }

    $scope.getUserName = function() {
        return credentialsService.getUserName() + " ($" + $scope.getUserBalance() + ")";
    }

    $scope.isMainService = function() {
        return document.URL.indexOf('makemydata.com') != -1 || document.URL.indexOf('localhost') != -1/* || document.URL.indexOf("dev.wsdevworld.com") != -1*/;
    }

    $scope.getUserBalance = function() {
        return parseFloat(credentialsService.getUserBalance()).toFixed(2);
    }

    $scope.getResellerInvitationDomain = function() {
        return 'makemydata.com';
        /*var invitationDomain = credentialsService.getUser().invitationDomain;
        if (0 == invitationDomain) {
            return 'makethedata.com';
        } else if (1 == invitationDomain) {
            return 'makemydata.com';
        } else if (2 == invitationDomain) {
            return 'makedatalist.com';
        } else {
            return 'none';
        }*/
    }

    $scope.getFormattedResellerNumber = function() {
        var result = credentialsService.getUser().resellerNumber + '';
        while (result.length < 3) {
            result = '0' + result;
        }

        return result;
    }

    $scope.registered = function() { return !credentialsService.getUser().isGuest };

    var md = new MobileDetect( window.navigator.userAgent );
    if (!md.mobile() && !$scope.ivoSite && !isCalienteLeads() && !isMyTargetData() &&
            !isMultimediaListsCom() && !isMultimediaListsNet() &&
            !document.getElementById("chatScript") && !isSalesListData()) {
        var scriptElement = document.createElement('script');
        scriptElement.id = "chatScript"
        scriptElement.setAttribute("type","text/javascript");
        scriptElement.setAttribute("async", "async");
        scriptElement.setAttribute("defer", "defer");
        scriptElement.setAttribute("data-cfasync", "false");
        scriptElement.setAttribute("src", "https://mylivechat.com/chatinline.aspx?hccid=39640536");

        document.getElementsByTagName("head")[0].appendChild(scriptElement);
    }

    $scope.supportPhone = '';
    $scope.updateSupportPhone = function() {
        systemService.getSupportPhone({userId: credentialsService.getUser().id}, function(response) {
            if (response.message) {
                $scope.supportPhone = response.message;
            } else {
                $scope.supportPhone = '714-406-0000';
            }
        });
    }
    $scope.updateSupportPhone();

    $scope.$on('LOGGED_IN', function() {
        $scope.updateSupportPhone();
        $scope.updateUserDetails();
    })

    $scope.isAdmin = function() {
        return credentialsService.getUser().admin;
    }

    $scope.isManager = function() {
        return credentialsService.getUser().role === 2;
    }

    $scope.isReseller = function() {
        return credentialsService.getUser().resellerNumber;
    }

    $scope.isDataSourceItemPriceAllowed = function() {
        return $scope.dataSourceItemPriceAllowed;
    }

    $scope.ifUploadListsISAvailable = function() {
        return $scope.allowMatchingLists;
    }

    $scope.profile = function() {
        $scope.menu.expanded = false;
        $state.transitionTo( $scope.isMobile() ? 'mobile_profile' : 'profile' );
    }

    $scope.lists = function() {
        $scope.menu.expanded = false;
        $state.transitionTo( 'mobile_purchased' );
    }

    $scope.isLoggedIn = function() {
        return credentialsService.isLoggedIn();
    }

    $scope.isMobile = function() {
        return document.URL.indexOf( '/mobile' ) != -1;
    }

    if (document.URL.indexOf('reidatalist') != -1) {
        document.title = 'reidatalist';
    } else if (document.URL.indexOf('axiomleads') != -1) {
        document.title = 'AxiomLeads';
    } else if (document.URL.indexOf('multimedialists') != -1) {
        document.title = 'Multimedia Lists';
    } else if (document.URL.indexOf('allwebreferrals') != -1 ){
        document.title = 'Data All Web Referrals';
    } else if (document.URL.indexOf('mytargetdata') != -1){
        document.title = 'My Target Data';
    } else if (document.URL.indexOf('sales-list') != -1){
        document.title = 'SalesList';
    } else {
        document.title = "Make My Data";
    }

    $scope.logout = function() {
        $scope.menu.expanded = false;
        credentialsService.logout();

        if (isCalienteLeads()) {
            document.location.href = 'http://axiomleads.com';
        } else if (isMultimediaListsCom()) {
            document.location.href = 'https://multimedialists.com';
        } else if (isMultimediaListsNet()) {
            document.location.href = 'https://multimedialists.net';
        } else {
            $state.transitionTo( 'login' );
        }
    }

    $scope.showTutorials = function() {
        var modalInstance = $modal.open( {
            templateUrl: BASE_URL + '/assets/partials/modal/tutorials.html',
            controller: 'TutorialsController'
        } );
    }

    $scope.shouldShowTutorial = function() {
        return credentialsService.getUser().resellerId != 362;
    }

    $scope.comment = function() {
        $scope.menu.expanded = false;

        var modalInstance = $modal.open( {
            templateUrl: BASE_URL + '/assets/partials/modal/comment.html',
            controller: 'CommentController'
        } );

        modalInstance.result.then( function( type ) {
            toasty.success({
                title: 'Comment has been sent successfully',
                msg: 'It will appear on the main page after administrator review. Thanks.',
                sound: false
            });
        } );
    }

    $document.on('click', function(event, scope) {
        if (event.target.className.indexOf("dropdown") == -1 &&
                (event.target.parentElement && event.target.parentElement.className.indexOf("dropdown") == -1)) {
            if ($scope.menu.expanded) {
                $scope.menu.expanded = false;
            }

            if ($scope.listsMenu.expanded) {
                $scope.listsMenu.expanded = false;
            }

            if ($scope.administrationMenu.expanded) {
                $scope.administrationMenu.expanded = false;
            }

            $scope.$apply();
        }
    });

    $scope.doPayment = function() {
        var modalInstance = $modal.open({
             templateUrl: BASE_URL + '/assets/partials/mobile/payment.html',
             controller: 'MobilePaymentController',
             resolve: {
                 userId: function () { return credentialsService.getUser().id; }
             } });

        modalInstance.result.then(function(user) {
            credentialsService.setUser(user);

            toasty.success({
                title: 'Paid successfully',
                msg: 'Your balance has been updated successfully.',
                sound: false
            });
        });
    }

    $scope.updateUserDetails = function() {
        if (credentialsService.getUser() && credentialsService.getUser().id) {
            systemService.getUserDetails({userId: credentialsService.getUser().id}, function(response) {
                if (response.status == 'OK') {
                    $scope.allowMatchingLists = response.data.allowMatchingLists;
                }
            });
        }
    }
    $scope.updateUserDetails();
} ] )
.controller( 'TutorialsController',
['$scope', '$modalInstance', 'BASE_URL',
function($scope, $modalInstance, BASE_URL) {
    $scope.videos = [
        {url: BASE_URL + "/video/0.mp4", title: "1: Welcome Video"},
        {url: BASE_URL + "/video/1.mp4", title: "2: Make A List of Homeowners"},
        {url: BASE_URL + "/video/2.mp4", title: "3: Make A List of Businesses"},
        {url: BASE_URL + "/video/3.mp4", title: "4: The Consumer Demographics"},
        {url: BASE_URL + "/video/4.mp4", title: "5: Get Only Cell Phones or Landlines"},
        {url: BASE_URL + "/video/5.mp4", title: "6: Search the Other Business Databases"},
        {url: BASE_URL + "/video/6.mp4", title: "7: Add Funds to Your Account"},
        {url: BASE_URL + "/video/7.mp4", title: "8: Avoid Buying Duplicates"}
    ];

    $scope.currentVideoIndex = -1;
    $scope.currentVideoUrl;

    $scope.playVideo = function(video) {
        $scope.currentVideoIndex = $scope.videos.indexOf(video);
        $scope.currentVideoUrl = video.url;
    }

    $scope.backToListClicked = function() {
        $scope.currentVideoIndex = -1;
        $scope.currentVideoUrl = undefined;
    }

    $scope.nextVideoClicked = function() {
        $scope.currentVideoIndex++;
        $scope.currentVideoUrl = $scope.videos[$scope.currentVideoIndex].url;
    }

    $scope.nextVideoButtonActive = function() {
        return $scope.currentVideoIndex < $scope.videos.length - 1;
    }

    $scope.previousVideoClicked = function() {
        $scope.currentVideoIndex--;
        $scope.currentVideoUrl = $scope.videos[$scope.currentVideoIndex].url;
    }

    $scope.previousVideoButtonActive = function() {
        return $scope.currentVideoIndex > 0;
    }

    $scope.cancelClicked = function() {
        $modalInstance.dismiss();
    }
} ] )
.controller( 'CommentController',
['$scope', '$modalInstance', 'credentialsService', 'profileService',
function($scope, $modalInstance, credentialsService, profileService) {
    $scope.model = {name: credentialsService.getUserName()};

    $scope.close = function() {
        $modalInstance.dismiss();
    }

    $scope.commit = function() {
        var user = credentialsService.getUser();
        profileService.protected(user.id, user.password).
            saveComment($scope.model, function(response) {
                if (response.status === 'OK') {
                    $modalInstance.close();
                }
            });
    }
}]);