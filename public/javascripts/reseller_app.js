//import {mainServerAddress} from "../util/constants";

angular.module( 'consumer_data_base', [ 'ngResource', 'ngCookies', 'ui.bootstrap', 'ui.router', 'ui.utils.masks', 'angular-toasty', 'angularAwesomeSlider' ] )
.config( [ 'toastyConfigProvider', function( toastyConfigProvider ) {
	toastyConfigProvider.setConfig( {
		limit: 1
	} );
} ] )
.provider('BASE_URL', [function() {
    this.$get = function() {
        if (document.URL.indexOf('localhost') != -1) {
            return ''; 
        } else {
           return "https://www.makemydata.com";
         //    return "http://test.wsdevworld.com:9000";
        }
    }
}]);

