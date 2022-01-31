angular.module( 'consumer_data_base' ).
factory( 'listService', function( $resource, BASE_URL ) {
    return $resource( '', {}, {
        getAllPurchased:      { url: BASE_URL + '/rest/public/lists/purchased/:id/:dataType', method: 'GET' },
        getAllUsersPurchased: { url: BASE_URL + '/rest/public/lists/all/purchased/:id/:dataType', method: 'GET' },
        getAllNonPurchased:   { url: BASE_URL + '/rest/public/lists/non/purchased/:id/:dataType', method: 'GET' },

        getPagedPurchased:      { url: BASE_URL + '/rest/public/lists/purchased', method: 'POST' },
        getPagedUsersPurchased: { url: BASE_URL + '/rest/public/lists/all/purchased', method: 'POST' },
        getPagedNonPurchased:   { url: BASE_URL + '/rest/public/lists/non/purchased', method: 'POST' },

        saveList:             { url: BASE_URL + '/rest/public/lists', method: 'POST' },
        prepareDownloading:   { url: BASE_URL + '/rest/public/lists/prepare/download', method: 'POST' },
        prepareEmail:         { url: BASE_URL + '/rest/public/lists/prepare/email', method: 'POST' },
        buyList:              { url: BASE_URL + '/rest/public/lists/buy', method: 'POST' },
        updateList:           { url: BASE_URL + '/rest/public/lists', method: 'PUT' },
        deleteList:           { url: BASE_URL + '/rest/public/lists/:id', method: 'DELETE' },
        getAllUploadedLists:  { url: BASE_URL + '/rest/public/lists/uploaded/all/:id', method: 'GET' },
        getUploadedLists:     { url: BASE_URL + '/rest/public/lists/uploaded/:id', method: 'GET' },
        updateUploadedList:   { url: BASE_URL + '/rest/public/lists/uploaded', method: 'PUT' },
        saveUploadedList:     { url: BASE_URL + '/rest/public/lists/uploaded', method: 'POST' },
        deleteUploadedList:   { url: BASE_URL + '/rest/public/lists/uploaded/:id', method: 'DELETE' },
        getListItemPrice:     { url: BASE_URL + '/rest/public/lists/price/:id', method: 'GET' },
        transferToSuppression: { url: BASE_URL + '/rest/public/lists/transfer/:id', method: 'GET' },
        getTableItemPrice:    { url: BASE_URL + '/rest/public/lists/price/table/:table/:userId', method: 'GET'},
        saveAndBuyList:       { url: BASE_URL + '/rest/public/lists/save/buy', method: 'POST' }
    } );
} );