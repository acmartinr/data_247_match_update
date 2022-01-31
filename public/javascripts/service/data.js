angular.module( 'consumer_data_base' ).
factory( 'dataService', function( $resource, BASE_URL ) {
    return $resource( '', {}, {
        dataSources: { url: BASE_URL + '/rest/public/data/sources/:userId', method: 'GET'},
        matching:    { url: BASE_URL + '/rest/public/data/matching', method: 'POST' },
        reset:       { url: BASE_URL + '/rest/public/data/matching/reset/:userId', method: 'GET' },
        message:     { url: BASE_URL + '/rest/public/data/message/:userId', method: 'GET' },
        saveMatchingList: { url: BASE_URL + '/rest/public/data/matching/save', method: 'POST' },
        detailedCount: { url: BASE_URL + '/rest/public/data/detailed/count', method: 'POST' }
    } );
} ).
factory('cancelableDataService',function($timeout, BASE_URL) {
    var xhr;
    var geographic = function(request, callback,errorCallBack){
        xhr = $.ajax({
            type: 'POST',
            url: BASE_URL + '/rest/public/data/geographic',
            data: JSON.stringify(request),
            dataType: 'json',
            contentType: 'application/json',
            success: function(data) { $timeout(function() { callback(data); }, 0); },
            timeout : 3600000,
            error: function() { $timeout(function() { errorCallBack("TIME_OUT"); }, 0); }
        });
    };

    var cancelRequest = function(){
        if (xhr) { xhr.abort(); }
    };

    return {
        geographic: geographic,
        cancelRequest: cancelRequest
    };
}).factory('cancelableCarriersDataService',function($timeout, BASE_URL) {
       var xhr;
       var carriers = function(request, callback){
           xhr = $.ajax({
               type: 'GET',
               url: BASE_URL + '/rest/public/dictionary/domainSources/'+request.domainSource+'/'+request.tableName,
               dataType: 'json',
               contentType: 'application/json',
               success: function(data) { $timeout(function() { callback(data); }, 0); }
           });
       };

       var cancelRequest = function(){
           if (xhr) { xhr.abort(); }
       };

       return {
           carriers: carriers,
           cancelRequest: cancelRequest
       };
   });

