angular.module( 'consumer_data_base' ).
factory( 'administrationService', function( $resource, BASE_URL ) {
    return { protected:
        function( userId, userPassword ) {
            var header = userId + ':' + userPassword;
            return $resource( '', {}, {
                consumersMessage:     { url: BASE_URL + '/rest/public/import/consumers/message', method: 'GET' },
                businessMessage:      { url: BASE_URL + '/rest/public/import/business/message', method: 'GET' },

                preparedItemsCount:   { url: BASE_URL + '/rest/public/lists/prepared/:id', method: 'GET' },

                updateCounties:       { url: BASE_URL + '/rest/public/update/counties/:tableName', method: 'GET' },
                updateZipCodes:       { url: BASE_URL + '/rest/public/update/zip/:tableName', method: 'GET' },
                updateAgeCategories:  { url: BASE_URL + '/rest/public/update/age/:tableName', method: 'GET' },
                updatePhoneTypes:     { url: BASE_URL + '/rest/public/update/phone/:tableName', method: 'GET' },
                createMobileDataTable:{ url: BASE_URL + '/rest/public/update/mobile/:tableName', method: 'GET' },
                createLandLineDataTable:{ url: BASE_URL + '/rest/public/update/landline/:tableName', method: 'GET' },

                message:              { url: BASE_URL + '/rest/public/status/message', method: 'GET' },

                getPrefixesList:     { url: BASE_URL + '/rest/public/administration/prefixes', method: 'POST', headers: { 'auth': header } },
                purchasedlistslogs:     { url: BASE_URL + '/rest/public/administration/purchasedlistslogs', method: 'POST', headers: { 'auth': header } },
                allmatchlists:     { url: BASE_URL + '/rest/public/administration/allmatchlists', method: 'POST', headers: { 'auth': header } },
                emailSents:     { url: BASE_URL + '/rest/public/administration/emailSents', method: 'POST', headers: { 'auth': header } },

                deletePhonePrefix:   { url: BASE_URL + '/rest/public/administration/prefixes/:id', method: 'DELETE', headers: { 'auth': header } },
                updatePhonePrefix:   { url: BASE_URL + '/rest/public/administration/prefixes', method: 'PUT', headers: { 'auth': header } },

                dataSources:                  { url: BASE_URL + '/rest/public/administration/datasources', method: 'GET', headers: { 'auth': header }},
                updateDataSourceVisible:      { url: BASE_URL + '/rest/public/administration/datasource/:id/visibility/:visible', method: 'GET', headers: { 'auth': header }},
                updateDataSourceBlockedUsers: { url: BASE_URL + '/rest/public/administration/datasource/blocked', method: 'POST', headers: { 'auth': header }},
                updateDataSourceBlockedState: { url: BASE_URL + '/rest/public/administration/datasource/blockedState', method: 'POST', headers: { 'auth': header }},
                getDataSourceBlockedUsers:    { url: BASE_URL + '/rest/public/administration/datasource/blocked/:id/:state', method: 'GET', headers: { 'auth': header }},

                getRegistrationRequestList:        { url: BASE_URL + '/rest/public/administration/registration/requests', method: 'POST', headers: { 'auth': header }},
                manuallyVerifyRegistrationRequest: { url: BASE_URL + '/rest/public/administration/registration/requests/verify/:id', method: 'GET', headers: { 'auth': header }},
                cancelRegistrationRequest:         { url: BASE_URL + '/rest/public/administration/registration/requests/cancel/:id', method: 'GET', headers: { 'auth': header }},

                getPaymentRequestsList:  { url: BASE_URL + '/rest/public/administration/payment/requests', method: 'POST', headers: { 'auth': header }},
                deletePaymentRequest:    { url: BASE_URL + '/rest/public/administration/payment/requests/:id', method: 'DELETE', headers: { 'auth': header }},
                savePaymentRequest:      { url: BASE_URL + '/rest/public/administration/payment/requests', method: 'PUT', headers: { 'auth': header }},

                tables:          { url: BASE_URL + '/rest/public/import/tables/:type', method: 'GET', headers: { 'auth': header } },
                allTables:       { url: BASE_URL + '/rest/public/import/all/tables', method: 'GET' },
                allTablesByType: { url: BASE_URL + '/rest/public/import/all/tables/:type', method: 'GET' },
                removeTable:     { url: BASE_URL + '/rest/public/import/tables/:id', method: 'DELETE' },
                saveTable:       { url: BASE_URL + '/rest/public/import/tables',     method: 'POST' },
                importData:      { url: BASE_URL + '/rest/public/import/:type/:path/:name/:skipRecords', method: 'GET'},
                importConsumers: { url: BASE_URL + '/rest/public/import/consumers/:path/:name/:skipRecords', method: 'GET' },
                importBusiness:  { url: BASE_URL + '/rest/public/import/business/:path/:name/:skipRecords', method: 'GET' },
                importDirectory: { url: BASE_URL + '/rest/public/import/directory/:path/:name/:skipRecords', method: 'GET' },
                importCraigslist:{ url: BASE_URL + '/rest/public/import/craigslist/:path/:name/:skipRecords', method: 'GET' },

                getUserList:     { url: BASE_URL + '/rest/public/administration/users', method: 'POST', headers: { 'auth': header } },
                blockUser:       { url: BASE_URL + '/rest/public/administration/users/block/:id', method: 'GET', headers: { 'auth': header } },
                unBlockUser:     { url: BASE_URL + '/rest/public/administration/users/unblock/:id', method: 'GET', headers: { 'auth': header } },
                saveUser:        { url: BASE_URL + '/rest/public/administration/user', method: 'POST', headers: { 'auth': header } },
                updateUserNote:  { url: BASE_URL + '/rest/public/administration/user/note', method: 'POST', headers: { 'auth': header } },
                deleteUser:      { url: BASE_URL + '/rest/public/administration/user/:id', method: 'DELETE', headers: { 'auth': header } },
                loginAsUser:     { url: BASE_URL + '/rest/public/administration/user/login', method: 'POST', headers: { 'auth': header } },
                exportUsers:     { url: BASE_URL + '/rest/public/administration/user/export', method: 'POST', headers: { 'auth': header } },
                exportLists:     { url: BASE_URL + '/rest/public/administration/lists/export', method: 'POST', headers: { 'auth': header } },
                resellers:       { url: BASE_URL + '/rest/public/administration/resellers', method: 'GET', headers: { 'auth': header } },

                getCommentsList: { url: BASE_URL + '/rest/public/administration/comments', method: 'POST', headers: { 'auth': header } },
                deleteComment:   { url: BASE_URL + '/rest/public/administration/comments/:id', method: 'DELETE', headers: { 'auth': header } },
                updateComment:   { url: BASE_URL + '/rest/public/administration/comments', method: 'PUT', headers: { 'auth': header } },

                exportRegistrationRequests: { url: BASE_URL + '/rest/public/administration/regrequest/export', method: 'POST', headers: { 'auth': header } },
                getPaymentsList: { url: BASE_URL + '/rest/public/administration/payments', method: 'POST', headers: { 'auth': header } },
                getUserAutoComplete: { url: BASE_URL + '/rest/public/administration/user/autocomplete', method: 'GET', isArray: true, headers: { 'auth': header } },
                getUserPaymentAutoComplete: { url: BASE_URL + '/rest/public/administration/user/payment/autocomplete', method: 'GET', isArray: true, headers: { 'auth': header } },
                addPayment:      { url: BASE_URL + '/rest/public/administration/payment', method: 'POST', headers: { 'auth': header } },
                getPricesList:   { url: BASE_URL + '/rest/public/administration/prices', method: 'GET', headers: { 'auth': header } },
                getUserPricesList: { url: BASE_URL + '/rest/public/administration/prices/:id', method: 'GET', headers: { 'auth': header } },
                savePrice:       { url: BASE_URL + '/rest/public/administration/prices', method: 'POST', headers: { 'auth': header } },
                sendEmails:      { url: BASE_URL + '/rest/public/administration/email', method: 'POST', headers: { 'auth': header } },

                backupMessage:   { url: BASE_URL + '/rest/public/administration/backup/message', method: 'GET', headers: { 'auth': header } },
                backupListsData: { url: BASE_URL + '/rest/public/administration/backup', method: 'GET', headers: { 'auth': header } },

                getMatchingPrice:  { url: BASE_URL + '/rest/public/administration/settings/matched_price', method: 'GET', headers: { 'auth': header }},
                getMatchingPrices: { url: BASE_URL + '/rest/public/administration/settings/matched_price_', method: 'PUT', headers: { 'auth': header }},
                updateSettings:    { url: BASE_URL + '/rest/public/administration/settings', method: 'POST', headers: { 'auth': header } },

                getValidateRegistrationRequestsData:    { url: BASE_URL + '/rest/public/administration/validate/registration/requests', method: 'GET', headers: { 'auth': header } }
            } );
        }
    }
} );