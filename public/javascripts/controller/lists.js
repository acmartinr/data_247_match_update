angular.module( 'consumer_data_base' ).
controller( 'ListsController',
    [ '$scope', '$state', 'BASE_URL', 'listService', 'systemService', 'credentialsService', 'confirm', 'localization', '$modal', 'toasty', '$state', '$interval', 'administrationService',
        function( $scope, $state, BASE_URL, listService, systemService, credentialsService, confirm, localization, $modal, toasty, $state, $interval, administrationService ) {
            $scope.purchased = $state.current.name === 'purchased';
            $scope.allPurchased = $state.current.name === 'all_purchased';
            $scope.allowTransferToSuppression = false;
            systemService.getUserDetails({userId: credentialsService.getUser().id}, function(response) {
                if (response.status == 'OK') {
                    $scope.allowTransferToSuppression = response.data.allowTransferToSuppression;
                }
            });

            $scope.isReseller = function() {
                return credentialsService.getUser().resellerNumber;
            }

            $scope.isAdmin = function() {
                return credentialsService.getUser().admin > 0;
            }

            $scope.userId = credentialsService.getUser().id;
            $scope.userPassword = credentialsService.getUser().password;

            $scope.loginAsUser = function( list ) {
                administrationService.protected( $scope.userId, $scope.userPassword ).loginAsUser( {id: list.userId}, function( response ) {
                    if ( response.status == "OK" ) {
                        user = response.data;
                        credentialsService.setUser(user);

                        $state.go( 'main' );
                        setTimeout(function() { location.reload(true); }, 100);
                    }
                } );
            }

            $scope.hideDownloadButton = function(list) {
                if (list.userId == credentialsService.getUser().id) {
                    return false;
                }

                /*if ($scope.isReseller()) {
                    return true;
                }*/

                return false;
            }

            $scope.needToHideDownloadButton = function() {
                if (!$scope.lists) { return true; }

                for (var i = 0; i < $scope.lists.length; i++) {
                    if (!$scope.hideDownloadButton($scope.lists[i])) {
                        return false;
                    }
                }

                return true;
            }

            $scope.config = { searchValue: '',
                sortValue: 'date',
                sortDesc: true,
                page: 1,
                limit: 10,
                userId: credentialsService.getUser().id };

            $scope.getDate = function( time ) {
                console.log(time);
                return new Date( time );
            }

            $scope.exportLists = function() {
                administrationService.
                protected( $scope.userId, $scope.userPassword ).
                exportLists( $scope.config, function( response ) {
                    if ( response.status === 'OK' ) {
                        var path = BASE_URL + '/rest/public/administration/download/' + response.message;
                        var frame = angular.element('<iframe src="' + path + '" style="display: none;" ></iframe>' );
                        angular.element( document.getElementById( 'hidden_frame' ) ).append( frame );
                    }
                } );
            }

            var updateLists = function() {
                var handler = function( response ) {
                    if ( response.status === 'OK' ) {
                        $scope.lists = response.data.lists;
                        $scope.total = response.data.total;
                    }
                };

                if ($scope.allPurchased) {
                    if ($scope.isReseller()) {
                        $scope.config.resellerId = $scope.config.userId;
                    }

                    listService.getPagedUsersPurchased( $scope.config, handler );
                } else if ( $scope.purchased ) {
                    listService.getPagedPurchased( $scope.config, handler );
                } else {
                    listService.getPagedNonPurchased( $scope.config, handler );
                }
            }
            updateLists();

            $scope.getLocalizedListType = function( list ) {
                return localization.localize( 'lists.type.' + list.type );
            }

            $scope.getFormattedTableName = function(name) {
                var parts = name.split('_archived_');
                if (parts.length > 1) {
                    return parts[0] + "[a]"
                } else {
                    return name;
                }
            }

            $scope.getCountDetails = function(list) {
                var modalInstance = $modal.open({
                    templateUrl: BASE_URL + '/assets/partials/modal/count.details.html',
                    controller: 'RequestCountDetailsModalController',
                    resolve: {
                        list: function() { return list; },
                        request: function() { return undefined; }
                    }
                });
            }

            $scope.transferInProgress = false;
            $scope.transferListId = -1;
            $scope.transferToSuppression = function(list) {
                $scope.transferListId = list.id;
                $scope.transferInProgress = true;

                listService.transferToSuppression({id: list.id}, function(response) {
                    $scope.transferListId = -1;
                    $scope.transferInProgress = false;

                    if (response.status === 'OK') {
                        toasty.success( {
                            title: 'Transfer completed',
                            msg: 'Transfer phone numbers to suppression has be completed successfully',
                            sound: false
                        } );
                    } else {
                        toasty.error( {
                            title: 'Transfer error',
                            msg: 'Transfer phone numbers to suppression failed',
                            sound: false
                        } );
                    }
                });
            }

            $scope.editList = function( list ) {
                var modalInstance = $modal.open( {
                    templateUrl: BASE_URL + '/assets/partials/modal/edit.list.html',
                    controller: 'EditListModalController',
                    resolve: { list: function() { return list; },
                        uploaded: function() { return false; } }
                } );

                modalInstance.result.then( function( name ) {
                    list.name = name;

                    listService.updateList( list, function( response ) {
                        if ( response.status === 'OK' ) {
                            toasty.success( {
                                title: localization.localize( 'lists.updated.successfully' ),
                                msg: localization.localize( 'lists.updated.successfully.message' ),
                                sound: false
                            } );

                            updateLists();
                        }
                    } )
                } );
            }

            $scope.removeList = function( list ) {
                confirm.getUserConfirmation( localization.localize( 'lists.delete.confirm') , function() {
                    listService.deleteList( { id: list.id }, function( response ) {
                        if ( response.status === 'OK' ) {
                            toasty.success( {
                                title: localization.localize( 'lists.deleted.successfully' ),
                                msg: localization.localize( 'lists.deleted.successfully.message' ),
                                sound: false
                            } );

                            updateLists();
                        }
                    } )
                } );
            }

            $scope.sortByField = function( field ) {
                if ( field === $scope.config.sortValue ) {
                    $scope.config.sortDesc = !$scope.config.sortDesc;
                } else {
                    $scope.config.sortValue = field;
                    $scope.config.sortDesc = false;
                }

                updateLists();
            }

            $scope.searchByValue = function() {
                $scope.config.page = 1;
                updateLists();
            }

            $scope.search = function() {
                updateLists();
            }

            var listId;
            var updateMessage = function() {
                if ( listId ) {
                    administrationService.protected().preparedItemsCount( { id: listId },
                        function( response ) {
                            if ( response.data != undefined ) {
                                if ( response.data === 0 ) {
                                    toasty.success( {
                                        limit: 1,
                                        title: 'Writing data in progress',
                                        msg: 'Preparing data',
                                        sound: false,
                                        timeout: 15000,
                                    } );
                                } else {
                                    toasty.success( {
                                        limit: 1,
                                        title: 'Writing data in progress',
                                        msg: response.data + ' records has been written to file',
                                        sound: false,
                                        timeout: 15000,
                                    } );
                                }
                            }
                        } );
                }
            }

            $scope.$on( '$destroy', function() {
                if ( $scope.interval ) $interval.cancel( $scope.interval );
            } );

            $scope.isOriginallyReseller = function() {
                return false;//credentialsService.getUser().originalRole == 1;
            }
            $scope.downloadEmailList = function(list, email) {
                /*if ( !credentialsService.getUser().verified ) {
                    toasty.error( {
                        title: 'List downloading error',
                        msg: 'You cannot get any data until you confirm your mobile phone number. Click here to open you profile page.',
                        sound: false,
                        clickToClose: true,
                        onClick: function() {
                            $state.transitionTo( 'profile' );
                        }
                    } );

                    return;
                }*/

                var modalInstance = $modal.open( {
                    templateUrl: BASE_URL + '/assets/partials/modal/email.download.columns.html',
                    controller: 'DownloadColumnsModalController',
                    resolve: { list: function() { return list; } }
                } );

                modalInstance.result.then( function( result ) {
                    var columns = result.fields;
                    var code = result.code;
                    var emailAddreess = result.emailAddress;

                    $scope.downloadingInProgress = true;
                    $scope.downloadListId = list.id;

                    listId = list.id;
                    updateMessage();
                    $scope.interval = $interval( updateMessage, 10000 );

                    if (email) {
                        emailList(list, columns, code,emailAddreess);
                    } else {
                        downloadList(list, columns, code);
                    }
                } );
            }

            $scope.downloadList = function(list, email) {
                /*if ( !credentialsService.getUser().verified ) {
                    toasty.error( {
                        title: 'List downloading error',
                        msg: 'You cannot get any data until you confirm your mobile phone number. Click here to open you profile page.',
                        sound: false,
                        clickToClose: true,
                        onClick: function() {
                            $state.transitionTo( 'profile' );
                        }
                    } );

                    return;
                }*/

                var modalInstance = $modal.open( {
                    templateUrl: BASE_URL + '/assets/partials/modal/download.columns.html',
                    controller: 'DownloadColumnsModalController',
                    resolve: { list: function() { return list; } }
                } );

                modalInstance.result.then( function( result ) {
                    var columns = result.fields;
                    var code = result.code;

                    $scope.downloadingInProgress = true;
                    $scope.downloadListId = list.id;

                    listId = list.id;
                    updateMessage();
                    $scope.interval = $interval( updateMessage, 10000 );

                    if (email) {
                        emailList(list, columns, code);
                    } else {
                        downloadList(list, columns, code);
                    }
                } );
            }

            var downloadList = function(list, columns, code) {
                listService.prepareDownloading( { listId: list.id,
                        userId: credentialsService.getUser().id,
                        'columns': columns,
                        'code': code },
                    function( response ) {
                        if ( response.status === 'OK' ) {
                            $scope.downloadingInProgress = false;
                            $scope.downloadListId = undefined;

                            var path = BASE_URL + '/rest/public/lists/download/' + list.id + '/' + response.message;
                            var frame = angular.element('<iframe src="' + path + '" style="display: none;" ></iframe>' );
                            angular.element(document.getElementById('hidden_frame')).append(frame);

                            toasty.success( {
                                title: 'Writing data completed',
                                msg: 'Data has been written to file successfully',
                                sound: false,
                                timeout: 5000,
                            } );
                        } else {
                            toasty.error( {
                                title: localization.localize( 'lists.download.error' ),
                                msg: localization.localize( 'lists.download.error.message' ),
                                sound: false
                            } );
                        }

                        if ( $scope.interval ) $interval.cancel( $scope.interval );
                        listId = undefined;
                    } );
            }

            var emailList = function(list, columns, code,emailAddreess) {
                listService.prepareEmail({ listId: list.id,
                        userId: credentialsService.getUser().id,
                        'columns': columns,
                        'code': code,
                        'emailAddreess':emailAddreess},
                    function(response) {
                        $scope.downloadingInProgress = false;
                        $scope.downloadListId = undefined;

                        if (response.status === 'OK') {
                            toasty.success({
                                title: 'Download link is ready',
                                msg: 'An email with download link has been sent to your email',
                                sound: false,
                                timeout: 5000,
                            });
                        } else {
                            toasty.error({
                                title: localization.localize('Download failed'),
                                msg: localization.localize('Access denied'),
                                sound: false
                            });
                        }

                        if ($scope.interval) $interval.cancel($scope.interval);
                        listId = undefined;
                    });
            }

            $scope.buyList = function( list ) {
                var modalInstance = $modal.open( {
                    templateUrl: BASE_URL + '/assets/partials/modal/buy.list.html',
                    controller: 'BuyListModalController',
                    resolve: { list: function() { return list; } }
                } );

                modalInstance.result.then( function() {
                    updateLists();
                } );
            }
        }
    ] ).
controller( 'DownloadColumnsModalController',
    [ '$scope', '$modalInstance', '$modal', 'BASE_URL', 'list',
        function( $scope, $modalInstance, $modal, BASE_URL, list ) {
            $scope.requiredFields = [];
            $scope.optionalFields = [];
            $scope.currentDataType = -1;
            var type = list.type;
            $scope.config = { all: false, phonesOnly: false,emailAddress:''};

            if ( type === 0 ) {
                $scope.requiredFields = [
                    { name: 'first name', value: 'FN' },
                    { name: 'last name', value: 'LN' },
                    { name: 'address', value: 'ADDR' },
                    { name: 'apt', value: 'APT' },
                    { name: 'city', value: 'CITY' },
                    { name: 'state', value: 'ST' },
                    { name: 'zip', value: 'ZIP' },
                    { name: 'phone', value: 'PHONE' },

                ];

                $scope.optionalFields = [
                    { name: 'phone type', value: 'phoneType' },
                    { name: 'DNC Info', value: 'dnc' },
                    { name: 'County', value: 'COUNTY' },
                    { name: 'Area code', value: 'AREA_CODE' },
                    { name: 'Date Of Birth', value: 'DOB' },
                    { name: 'Gender', value: 'GENDER' },
                    //{ name: 'education', value: 'EDUC' },

                    { name: 'Net Worth', value: 'NET_WORTH' },
                    { name: 'Credit Rating', value: 'CREDIT_RATING' },
                    { name: 'Active Credit Lines', value: 'CREDIT_LINES' },
                    { name: 'Range Of New Credit', value: 'CREDIT_RANGE_NEW' },

                    { name: 'Ethnic-group', value: 'ETHNIC_GRP' },
                    { name: 'Language', value: 'ETHNIC_LANG' },
                    //{ name: 'religion', value: 'ETHNIC_RELIG' },
                    { name: 'Ethnic(other)', value: 'ETHNIC' },

                    { name: 'Household Size', value: 'HH_SIZE' },
                    { name: 'Household Income', value: 'HH_INCOME' },
                    { name: 'Veteran In Household', value: 'VET_IN_HH' },

                    { name: 'Property Type', value: 'PROP_TYPE' },
                    { name: 'Ownership', value: 'HOME_OWNR' },
                    //{ name: 'length of residence', value: 'LOR' },
                    { name: 'Marital Status', value: 'HH_MARITAL_STAT' },
                    { name: 'Number Of Children', value: 'NUM_KIDS' },

                    //{ name: 'interest', value: 'INT' },
                    { name: 'Other', value: 'OTHER' }
                ];
            }
            else if (type === 21) {
                $scope.requiredFields = [
                    {name: 'First Name', value: 'first_name' },
                    {name: 'Last Name', value: 'last_name' },
                    {name: 'City', value: 'city' },
                    {name: 'State', value: 'st' },
                    {name: 'phone', value: 'phone' }
                ];
                $scope.optionalFields = [
                    {name: 'status', value: 'status' },
                    {name: 'Job', value: 'job' },
                    {name: 'Gender', value: 'gender' },
                    {value: "dnc", name: "DNC Info"},
                    {value: "phoneType", name: "Phone Type"},
                    {name: 'County', value: 'county' }
                ];
            }
            else if (type === 1) {
                $scope.requiredFields = [
                    { name: 'company name', value: 'COMPANY_NAME' },
                    { name: 'address', value: 'ADDRESS' },
                    { name: 'city', value: 'CITY' },
                    { name: 'state', value: 'STATE' },
                    { name: 'zip', value: 'ZIP' },
                    { name: 'phone', value: 'PHONE' },
                    { name: 'contact name', value: 'CONTACT_NAME' },
                    //{ name: 'email', value: 'email1' }
                ];

                $scope.optionalFields = [
                    { name: 'DNC Info', value: 'dnc' },
                    { name: 'County', value: 'COUNTY' },
                    //{ name: 'Fax', value: 'FAX' },
                    { name: 'Area Code', value: 'AREA_CODE' },
                    { name: 'Contact Gender', value: 'GENDER' },
                    { name: 'Latitude', value: 'LATITUDE' },
                    { name: 'Longitude', value: 'LONGITUDE' },
                    { name: 'SIC Code', value: 'SIC_CODE' },
                    { name: 'Title', value: 'TITLE' },
                    { name: 'Website', value: 'WWW' },
                    { name: 'Industry', value: 'INDUSTRY' },
                    { name: 'Employee', value: 'EMPLOYEE' },
                    { name: 'phone type', value: 'phoneType' },
                    { name: 'Annual Sales', value: 'ANNUAL_SALES' }
                ];
            } else if (type === 20) {

                $scope.requiredFields = [
                    {value: "phone", name: "Phone"},
                    {value: "firstname", name: "First name"},
                    {value: "state", name: "State"},
                ];

                $scope.optionalFields = [
                    {value: "dnc", name: "DNC Info"},
                    {value: "phoneType", name: "Phone Type"},
                    {value: "ADDRESS", name: "ADDRESS"},
                    {value: "email", name: "Email"},
                    {value: "CITY", name: "CITY"},
                    {value: "ZIP5", name: "ZIP5"},
                    {value: "ZIP4", name: "ZIP4"},
                    {value: "CLEAN_SOURCE", name: "Source"},
                    {name: 'Datime', value: 'date'},
                    {value: "carrier", name: "Carrier"},
                    {value: "ip", name: "IP"},
                ];
            } else if (type === 18) {
                $scope.requiredFields = [
                    {name: 'company name', value: 'COMPANY_NAME'},
                    {name: 'address', value: 'ADDRESS'},
                    {name: 'city', value: 'CITY'},
                    {name: 'state', value: 'STATE'},
                    {name: 'zip', value: 'ZIP'},
                    {name: 'phone', value: 'PHONE'},
                    {name: 'contact name', value: 'CONTACT_NAME'},
                    //{ name: 'email', value: 'email1' }
                ];

                $scope.optionalFields = [
                    {name: 'phone type', value: 'phoneType'},
                    {name: 'DNC Info', value: 'dnc'},
                    {name: 'County', value: 'COUNTY'},
                    //{ name: 'Fax', value: 'FAX' },
                    {name: 'Area Code', value: 'AREA_CODE'},
                    {name: 'Contact Gender', value: 'GENDER'},
                    {name: 'Latitude', value: 'LATITUDE'},
                    {name: 'Longitude', value: 'LONGITUDE'},
                    {name: 'SIC Code', value: 'SIC_CODE'},
                    {name: 'Website', value: 'WWW'},
                    {name: 'Employee', value: 'EMPLOYEE'},
                    {name: 'Annual Sales', value: 'ANNUAL_SALES'}
                ];
            } else if (type === 19) {
                $scope.requiredFields = [
                    {name: 'first name', value: 'firstname'},
                    {name: 'last name', value: 'lastname'},
                    {name: 'address', value: 'address'},
                    {name: 'state', value: 'state'},
                    {name: 'zip code', value: 'zipcode'},
                    {name: 'email', value: 'email'},
                    {name: 'phone', value: 'phone'},
                ];

                $scope.optionalFields = [
                    {name: 'phone type', value: 'phoneType'},
                    {name: 'DNC Info', value: 'dnc' },
                    {name: 'County', value: 'county'},
                    {name: 'City', value: 'city'},
                    {name: 'Area Code', value: 'area_code'},
                    {name: 'Gender', value: 'gender'},
                    {name: 'IP', value: 'ip'},
                    {name: 'Date', value: 'date'},
                    {name: 'Age', value: 'DOB_DATE'}
                ];
            } else if (type === 13) {
                $scope.requiredFields = [
                    { name: 'company name', value: 'company' },
                    { name: 'address', value: 'ADDRESS' },
                    { name: 'city', value: 'CITY' },
                    { name: 'state', value: 'STATE' },
                    { name: 'zip', value: 'ZIP' },
                    { name: 'phone', value: 'PHONE' },
                    { name: 'email', value: 'email' },
                    { name: 'Contact Name', value: 'name' }
                ];

                $scope.optionalFields = [
                    { name: 'phone type', value: 'phoneType' },
                    { name: 'DNC Info', value: 'dnc' },
                    { name: 'County', value: 'COUNTY' },
                    { name: 'Industry', value: 'INDUSTRY' },
                    { name: 'Fax', value: 'FAX' },
                    { name: 'Area Code', value: 'AREA_CODE' },
                    { name: 'SIC Code', value: 'SIC_CODE' },
                    { name: 'Title', value: 'TITLE' },
                    { name: 'Website', value: 'WWW' },
                    { name: 'Toll Free Number', value: 'tollFreePhone' },
                    { name: 'Fax Number', value: 'faxNumber' },
                    { name: 'Ethnic Code', value: 'ethnicCode' },
                    { name: 'Ethnic Group', value: 'ethnicGroup' },
                    { name: 'Language Code', value: 'languageCode' },
                    { name: 'Religion Code', value: 'religionCode' },
                    { name: 'Total Employees Corp Wide', value: 'totalEmployees' },
                    { name: 'Employees on Site', value: 'employeesOnSite' },
                    { name: 'Total Revenue Corp Wide', value: 'totalRevenue' },
                    { name: 'Revenue at Site', value: 'revenue' },
                    { name: 'Year Founded', value: 'yearFounded' },
                    { name: 'Minority Owned', value: 'minorityOwned' },
                    { name: 'Small Business', value: 'smallBusiness' },
                    { name: 'Large Business', value: 'largeBusiness' },
                    { name: 'Home Business', value: 'homeBusiness' },
                    { name: 'Import Export', value: 'importExport' },
                    { name: 'Public Company', value: 'publicCompany' },
                    { name: 'Headquarters Branch', value: 'headquartersBranch' },
                    { name: 'Stock Exchange', value: 'stockExchange' },
                    { name: 'Franchise', value: 'franchiseFlag' },
                    { name: 'Individual Firm Code', value: 'individualFirmCode' },
                    { name: 'Year Appeared', value: 'appearedYear' },
                    { name: 'Female Owned or Operated', value: 'femaleOwnedorOperated' },
                    { name: 'White Collar Code', value: 'whiteCollarCode' },
                    { name: 'Phone Contact', value: 'phoneContact' },
                    { name: 'Credit Score', value: 'creditScore' }
                ];
            } else if (type === 2){
                $scope.requiredFields = [
                    { name: 'company name', value: 'COMPANY_NAME' },
                    { name: 'contact name', value: 'contact_name' },
                    { name: 'address', value: 'ADDRESS' },
                    { name: 'city', value: 'CITY' },
                    { name: 'state', value: 'STATE' },
                    { name: 'zip', value: 'ZIP' },
                    { name: 'phone', value: 'PHONE' },
                ];

                $scope.optionalFields = [
                    { name: 'phone type', value: 'phoneType' },
                    { name: 'DNC Info', value: 'dnc' },
                    { name: 'County', value: 'COUNTY' },
                    { name: 'Area Code', value: 'AREA_CODE' },
                    { name: 'Source', value: 'WWW' },
                    { name: 'Industry', value: 'INDUSTRY' },
                    { name: 'Website', value: 'websites' }
                ];
            } else if (type === 22){
                $scope.requiredFields = [
                    { name: 'company name', value: 'COMPANY_NAME' },
                    { name: 'contact name', value: 'contact_name' },
                    { name: 'address', value: 'ADDRESS' },
                    { name: 'city', value: 'CITY' },
                    { name: 'state', value: 'STATE' },
                    { name: 'zip', value: 'ZIP' },
                    { name: 'phone', value: 'PHONE' },
                ];

                $scope.optionalFields = [
                    { name: 'phone type', value: 'phoneType' },
                    { name: 'Email', value: 'email' },
                    { name: 'DNC Info', value: 'dnc' },
                    { name: 'County', value: 'COUNTY' },
                    { name: 'Area Code', value: 'AREA_CODE' },
                    { name: 'Source', value: 'WWW' },
                    { name: 'Industry', value: 'INDUSTRY' },
                    { name: 'Website', value: 'websites' }
                ];
            } else if (type === 23){
                $scope.requiredFields = [
                    { name: 'First Name', value: 'firstname' },
                    { name: 'city', value: 'CITY' },
                    { name: 'state', value: 'STATE' },
                    { name: 'phone', value: 'PHONE' },
                    { name: 'county', value: 'COUNTY' },
                ];

                $scope.optionalFields = [
                    { name: 'Phone type', value: 'phoneType' },
                    { name: 'DNC Info', value: 'dnc' },
                    { name: 'Last Name', value: 'lastname' },
                    { name: 'Email', value: 'email' },
                    { name: 'DOB', value: 'dob' },
                    { name: 'Source', value: 'source' }
                ];
            }   else if (type === 3) {
                $scope.requiredFields = [
                    { name: 'state', value: 'STATE' },
                    { name: 'industry', value: 'INDUSTRY' },
                    { name: 'phone', value: 'PHONE' },
                ];

                $scope.optionalFields = [
                    { name: 'phone type', value: 'phoneType' },
                    { name: 'DNC Info', value: 'dnc' },
                    { name: 'Website', value: 'website' },
                    { name: 'Area Code', value: 'AREA_CODE' },
                    { name: 'Date', value: 'date' }
                ];
            } else if (type === 4) {
                $scope.requiredFields = [
                    { name: 'website', value: 'website' },
                    { name: 'email', value: 'email' },
                    { name: 'name', value: 'name' },
                    { name: 'business', value: 'business' },
                    { name: 'address', value: 'address' },
                    { name: 'city', value: 'CITY' },
                    { name: 'state', value: 'STATE' },
                    { name: 'zip', value: 'ZIP_CODE' },
                    { name: 'phone', value: 'PHONE' },
                ];

                $scope.optionalFields = [
                    { name: 'phone type', value: 'phoneType' },
                    { name: 'DNC Info', value: 'dnc' },
                    { name: 'County', value: 'COUNTY' },
                    { name: 'Area Code', value: 'AREA_CODE' }
                ];
            } else if (type === 5) {
                $scope.requiredFields = [
                    { name: 'state', value: 'STATE' },
                    { name: 'website', value: 'website' },
                    { name: 'phone', value: 'PHONE' },
                ];

                $scope.optionalFields = [
                    { name: 'phone type', value: 'phoneType' },
                    { name: 'DNC Info', value: 'dnc' },
                    { name: 'Area Code', value: 'AREA_CODE' }
                ];
            } else if (type === 7) {
                $scope.requiredFields = [
                    {name: 'username', value: 'username'},
                    {name: 'full name', value: 'fullname'},
                    {name: 'category', value: 'category'},
                    {name: 'state', value: 'state'},
                    {name: 'zip code', value: 'zipCode'},
                    {name: 'website', value: 'website'},
                    {name: 'email', value: 'email'},
                    {name: 'phone', value: 'phone'},
                ];

                $scope.optionalFields = [
                    {name: 'phone type', value: 'phoneType'},
                    {name: 'DNC Info', value: 'dnc' },
                    {name: 'County', value: 'county'},
                    {name: 'City', value: 'city'},
                    {name: 'Street', value: 'street'},
                    {name: 'Area Code', value: 'areaCode'}
                ];
            } else if (type === 14) {
                $scope.requiredFields = [
                    {name: 'username', value: 'username'},
                    {name: 'full name', value: 'fullname'},
                    {name: 'category', value: 'category'},
                    {name: 'state', value: 'state'},
                    {name: 'zip code', value: 'zipCode'},
                    {name: 'website', value: 'website'},
                    {name: 'email', value: 'email'},
                    {name: 'phone', value: 'phone'}
                ];

                $scope.optionalFields = [
                    {name: 'phone type', value: 'phoneType'},
                    {name: 'DNC Info', value: 'dnc' },
                    {name: 'County', value: 'county'},
                    {name: 'City', value: 'city'},
                    {name: 'Street', value: 'street'},
                    {name: 'Area Code', value: 'areaCode'},
                    {name: 'Publications Count', value: 'publicationsCount'},
                    {name: 'Followers Count', value: 'followersCount'},
                    {name: 'Subscriptions Count', value: 'subscriptionsCount'},
                    {name: 'Runs Ads', value: 'runsAds'},
                    {name: 'Feed Ads Count', value: 'feedAdsCount'},
                    {name: 'Story Ads Count', value: 'storyAdsCount'},
                    {name: 'Biography', value: 'biography'}
                ];
            } else if (type === 8) {
                $scope.requiredFields = [
                    {name: 'first name', value: 'firstName'},
                    {name: 'last name', value: 'lastName'},
                    {name: 'address', value: 'address'},
                    {name: 'state', value: 'state'},
                    {name: 'zip code', value: 'zipCode'},
                    {name: 'email', value: 'email'},
                    {name: 'phone', value: 'phone'},
                    {name: 'website', value: 'source'}
                ];

                $scope.optionalFields = [
                    {name: 'phone type', value: 'phoneType'},
                    {name: 'DNC Info', value: 'dnc' },
                    {name: 'Gender', value: 'genderCode'},
                    {name: 'Date of Birth', value: 'dobDate'},
                    {name: 'County', value: 'county'},
                    {name: 'City', value: 'city'},
                    {name: 'IP', value: 'ip'},
                    {name: 'Area Code', value: 'areaCode'}
                ];
            } else if (type === 17) {
                $scope.requiredFields = [
                    {name: 'name', value: 'name'},
                    {name: 'address', value: 'address'},
                    {name: 'state', value: 'state'},
                    {name: 'zip code', value: 'zip_code'},
                    {name: 'email', value: 'email'},
                    {name: 'phone', value: 'phone'}
                ];

                $scope.optionalFields = [
                    {name: 'phone type', value: 'phoneType'},
                    {name: 'DNC Info', value: 'dnc' },
                    {name: 'County', value: 'county'},
                    {name: 'City', value: 'city'},
                    {name: 'Area Code', value: 'area_code'}
                ];
            } else if (type === 10) {
                $scope.requiredFields = [
                    {name: 'first name', value: 'first_name'},
                    {name: 'last name', value: 'last_name'},
                    {name: 'address', value: 'address'},
                    {name: 'state', value: 'state'},
                    {name: 'zip code', value: 'zip_code'},
                    {name: 'phone', value: 'phone'}
                ];

                $scope.optionalFields = [
                    {name: 'phone type', value: 'phoneType'},
                    {name: 'DNC Info', value: 'dnc' },
                    {name: 'VIN', value: 'vin'},
                    {name: 'Model', value: 'model'},
                    {name: 'Make', value: 'make'},
                    {name: 'Year', value: 'year'},
                    {name: 'County', value: 'county'},
                    {name: 'City', value: 'city'},
                    {name: 'Area Code', value: 'areaCode'}
                ];
            } else if (type == 11) {
                $scope.requiredFields = [
                    {name: 'state', value: 'state'},
                    {name: 'phone', value: 'phone'}
                ];

                $scope.optionalFields = [
                    {name: 'phone type', value: 'phoneType'},
                    {name: 'DNC Info', value: 'dnc' },
                    {name: 'Area Code', value: 'areaCode'}
                ];
            } else if (type == 16) {
                $scope.requiredFields = [
                    {name: 'state', value: 'state'},
                    {name: 'phone', value: 'phone'}
                ];

                $scope.optionalFields = [
                    {name: 'phone type', value: 'phoneType'},
                    {name: 'DNC Info', value: 'dnc' },
                    {name: 'Area Code', value: 'areaCode'}
                ];
            } else if (type == 12) {
                $scope.requiredFields = [
                    {name: 'first name', value: 'firstname'},
                    {name: 'last name', value: 'lastname'},
                    {name: 'address', value: 'address'},
                    {name: 'state', value: 'state'},
                    {name: 'zip code', value: 'zipcode'},
                    {name: 'phone', value: 'phone'}
                ];

                $scope.optionalFields = [
                    {name: 'phone type', value: 'phoneType'},
                    {name: 'Company name', value: 'companyName'},
                    {name: 'Email', value: 'email'},
                    {name: 'Title', value: 'title'},
                    {name: 'County', value: 'county'},
                    {name: 'City', value: 'city'},
                    {name: 'Fax', value: 'fax'},
                    {name: 'Website', value: 'website'},
                    {name: 'IP', value: 'ip'},
                    {name: 'Linked In Id', value: 'linkedInId'},
                    {name: 'DNC Info', value: 'dnc' },
                    {name: 'Area Code', value: 'area_Code'}
                ];
            } else if (type === 15) {
                $scope.requiredFields = [
                    { name: 'first name', value: 'firstName' },
                    { name: 'last name', value: 'lastName' },
                    { name: 'address', value: 'address' },
                    { name: 'city', value: 'city' },
                    { name: 'state', value: 'state' },
                    { name: 'zip', value: 'zipCode' },
                    { name: 'phone', value: 'phone' }
                ];

                $scope.optionalFields = [
                    { name: 'phone type', value: 'phoneType' },
                    {name: 'County', value: 'county'},
                    {name: 'IP', value: 'IP'},
                    {name: 'DNC Info', value: 'dnc' },
                    {name: 'Area Code', value: 'areaCode'}
                ];
            } else if (type === 6 || type === 9) {
                $scope.currentDataType = 9;
                $scope.requiredFields = [
                    { name: 'first name', value: 'PERSONFIRSTNAME' },
                    { name: 'last name', value: 'PERSONLASTNAME' },
                    { name: 'email', value: 'EMAIL' },
                    { name: 'address', value: 'PRIMARYADDRESS' },
                    { name: 'apt', value: 'SECONDARYADDRESS' },
                    { name: 'city', value: 'CITYNAME' },
                    { name: 'state', value: 'STATE' },
                    { name: 'zip', value: 'ZIPCODE' },
                    { name: 'phone', value: 'PHONE' }
                ];

                $scope.optionalFields = [
                    { name: 'phone type', value: 'phoneType',type: 'type1'  },
                    // { name: 'Carrier Name', value: 'carrier_name',type: 'type1'  },
                    { name: 'Website', value: 'website',type: 'type1' },
                    { name: 'IP', value: 'ip',type: 'type1' },

                    { name: 'VIN', value: 'vin',type: 'type1' },
                    { name: 'Vehicle Make', value: 'make',type: 'type1' },
                    { name: 'Vehicle Model', value: 'model',type: 'type1' },
                    { name: 'Vehicle Year', value: 'year' ,type: 'type1'},

                    { name: 'DNC Info', value: 'dnc' ,type: 'type1'},
                    { name: 'County', value: 'Countyname' ,type: 'type1'},
                    { name: 'Area Code', value: 'Areacode',type: 'type1' },
                    { name: 'Date Of Birth', value: 'Persondateofbirthdate' ,type: 'type1'},
                    { name: 'Gender', value: 'Persongender' ,type: 'type1'},
                    { name: 'Education', value: 'Personeducation' ,type: 'type1'},

                    { name: 'Net Worth', value: 'Networth' ,type: 'type1'},
                    { name: 'Credit Rating', value: 'Creditrating',type: 'type1' },
                    { name: 'Active Credit Lines', value: 'Numberoflinesofcredit' ,type: 'type1'},
                    { name: 'Range Of New Credit', value: 'Credit_rangeofnewcredit' ,type: 'type1'},

                    { name: 'Ethnic-group', value: 'Ethnicgroup',type: 'type1' },
                    { name: 'Language', value: 'Languagecode' ,type: 'type1'},
                    { name: 'Religion', value: 'Religioncode' ,type: 'type1'},
                    { name: 'Veteran In Household', value: 'Veteraninhousehold',type: 'type1' },

                    { name: 'Property Type', value: 'Dwellingtype',type: 'type1' },
                    { name: 'Ownership', value: 'Homeownerprobabilitymodel' ,type: 'type1'},
                    { name: 'Length Of Residence', value: 'Lengthofresidence' ,type: 'type1'},
                    { name: 'Number Of Children', value: 'Numberofchildren' ,type: 'type1'},

                    {name: 'Next Age', value: 'Personexactage',type: 'type1'},
                    {name: 'Marital Status', value: 'Personmaritalstatus',type: 'type1'},
                    {name: 'Inferred Age', value: 'Inferredage',type: 'type1'},
                    {name: 'Hispanic Country Code', value: 'Hispaniccountrycode',type: 'type1'},
                    {name: 'Single Parent', value: 'Singleparent',type: 'type1'},
                    {name: 'Smoker', value: 'Smoker',type: 'type1'},
                    {name: 'Presence Of Credit Card', value: 'Presenceofcreditcard',type: 'type1'},
                    {name: 'Presence Of Gold Or Platinum Credit Card', value: 'Presenceofgoldorplatinumcreditcard',type: 'type1'},
                    {name: 'Presence If Premium Card', value: 'Presenceofpremiumcreditcard',type: 'type1'},
                    {name: 'Travel And Entertainment Card Holder', value: 'Travelandentertainmentcardholder',type: 'type1'},
                    {name: 'Credit Card User', value: 'Creditcarduser',type: 'type1'},
                    {name: 'New Credit Card Issue', value: 'Creditcardnewissue',type: 'type1'},
                    {name: 'Swimming Pool', value: 'Homeswimmingpoolindicator',type: 'type1'},
                    {name: 'Number Of Person Living In Unit', value: 'Numberofpersonsinlivingunit',type: 'type1'},
                    {name: 'Presence Of Children', value: 'Presenceofchildren',type: 'type1'},
                    {name: 'Inferred Household Rank', value: 'Inferredhouseholdrank',type: 'type1'},
                    {name: 'Number Of Adults', value: 'Numberofadults',type: 'type1'},
                    {name: 'Generations In Household', value: 'Generationsinhousehold',type: 'type1'},
                    {name: 'Senior Adult In House Hold', value: 'Senioradultinhousehold',type: 'type1'},
                    //{name: 'Air Conditioning', value: 'Airconditioning'},
                    //{name: 'Home Heat', value: 'Homeheatindicator'},
                    {name: 'Sewer', value: 'Sewer',type: 'type1'},
                    {name: 'Water', value: 'Water',type: 'type1'},

                    //Interest//
                    {name: 'News And Financial', value: 'Newsandfinancial'},
                    {name: 'Automotive Buff', value: 'Automotivebuff'},
                    {name: 'Book Reader', value: 'Bookreader'},
                    {name: 'Computer Owner', value: 'Computerowner'},
                    {name: 'Cooking Enthusiast', value: 'Cookingenthusiast'},
                    {name: 'Do It Yourselfer', value: 'Do_it_yourselfers'},
                    {name: 'Exercise Enthusiast', value: 'Exerciseenthusiast'},
                    {name: 'Gardener', value: 'Gardener'},
                    {name: 'Golf Enthusiast', value: 'Golfenthusiasts'},
                    {name: 'Home Decorating Enthusiast', value: 'Homedecoratingenthusiast'},
                    {name: 'Outdoor Enthusiast', value: 'Outdoorenthusiast'},
                    {name: 'Outdoor Sports Lover', value: 'Outdoorsportslover'},
                    {name: 'Photography', value: 'Photography'},
                    {name: 'Traveler', value: 'Traveler'},
                    {name: 'Pets', value: 'Pets'},
                    {name: 'Cats', value: 'Cats'},
                    {name: 'Dogs', value: 'Dogs'},
                    {name: 'Equestrian', value: 'Equestrian'},
                    {name: 'Reading Science Function', value: 'Readingsciencefiction' },
                    {name: 'Reading Audio Books', value: 'Readingaudiobooks' },
                    {name: 'History Military', value: 'Historymilitary' },
                    {name: 'Current Affairs Politics', value: 'Currentaffairspolitics'},
                    {name: 'Science Space', value: 'Sciencespace' },
                    {name: 'Gaming', value: 'Gaming' },
                    {name: 'Games/Video Games', value: 'Gamesvideogames' },
                    {name: 'Arts', value: 'Arts' },
                    {name: 'Games/computer Games', value: 'Gamescomputergames' },
                    {name: 'Movie/Music Grouping', value: 'Moviemusicgrouping' },
                    {name: 'Musical Instruments', value: 'Musicalinstruments' },
                    {name: 'Collectibles Stamps', value: 'Collectiblesstamps' },
                    {name: 'Collectibles Coins', value: 'Collectiblescoins' },
                    {name: 'Collectibles Arts', value: 'Collectiblesarts' },
                    {name: 'Collectibles Antiques', value: 'Collectiblesantiques' },
                    {name: 'Collectibles Sports Memorabilia', value: 'Collectiblessportsmemorabilia' },
                    {name: 'Military Memorabilia Weaponry', value: 'Militarymemorabiliaweaponry' },
                    {name: 'Auto Work', value: 'Autowork' },
                    {name: 'Wood Working', value: 'Woodworking' },
                    {name: 'Aviation', value: 'Aviation' },
                    {name: 'House Plants', value: 'Houseplants' },
                    {name: 'Home And Garden', value: 'Homeandgarden' },
                    {name: 'Home Improvement Grouping', value: 'Homeimprovementgrouping' },
                    {name: 'Photography And Video Equipment', value: 'Photographyandvideoequipment' },
                    {name: 'Home Furnishings Decorating', value: 'Homefurnishingsdecorating' },
                    {name: 'Home Improvement', value: 'Homeimprovement' },
                    {name: 'Food/Wines', value: 'Foodwines' },
                    {name: 'Cooking General', value: 'Cookinggeneral' },
                    {name: 'Cooking Gourmet', value: 'Cookinggourmet' },
                    {name: 'Foods Natural', value: 'Foodsnatural' },
                    {name: 'Cooking Food Grouping', value: 'Cookingfoodgrouping' },
                    {name: 'Gaming Casino', value: 'Gamingcasino' },
                    {name: 'Upscale Living', value: 'Upscaleliving' },
                    {name: 'Cultural Artistic Living', value: 'Culturalartisticliving' },
                    {name: 'High-Tech Living', value: 'Hightechliving' },
                    {name: 'Exercise Health Grouping', value: 'Exercisehealthgrouping' },
                    {name: 'Exercise Running Jogging', value: 'Exerciserunningjogging' },
                    {name: 'Exercise Walking', value: 'Exercisewalking' },
                    {name: 'Exercise Aerobic', value: 'Exerciseaerobic' },
                    {name: 'Spectator Sports/TV Sports', value: 'Spectatorsportstvsports' },
                    {name: 'Spectator Sports Football', value: 'Spectatorsportsfootball' },
                    {name: 'Spectator Sports Baseball', value: 'Spectatorsportsbaseball' },
                    {name: 'Spectator Sports Basketball', value: 'Spectatorsportsbasketball' },
                    {name: 'Spectator Sports Hockey', value: 'Spectatorsportshockey' },
                    {name: 'Spectator Sports Soccer', value: 'Spectatorsportssoccer' },
                    {name: 'Tennis', value: 'Tennis' },
                    {name: 'Snow Skiing', value: 'Snowskiing' },
                    {name: 'Motorcycling', value: 'Motorcycling' },
                    {name: 'Nascar', value: 'Nascar' },
                    {name: 'Boating Sailing', value: 'Boatingsailing' },
                    {name: 'Scuba Diving', value: 'Scubadiving' },
                    {name: 'Sports And Leisure', value: 'Sportsandleisure' },
                    {name: 'Hunting', value: 'Hunting' },
                    {name: 'Fishing', value: 'Fishing' },
                    {name: 'Camping Hiking', value: 'Campinghiking' },
                    {name: 'Hunting/Shooting', value: 'Huntingshooting' },
                    {name: 'Sports Grouping', value: 'Sportsgrouping' },
                    {name: 'Outdoors Grouping', value: 'Outdoorsgrouping' },
                    {name: 'Health Medical', value: 'Healthmedical' },
                    {name: 'Religious Contributor', value: 'Religiouscontributor' },
                    {name: 'Political Contributor', value: 'Politicalcontributor' },
                    {name: 'Charitable', value: 'Charitable' },
                    {name: 'Donates To Environmental Causes', value: 'Donatestoenvironmentalcauses' },
                    {name: 'Political Conservative Charitable Donation', value: 'Politicalconservativecharitabledonation' },
                    {name: 'Political Liberal Charitable Donation', value: 'Politicalliberalcharitabledonation' },
                    {name: 'Veterans Charitable Donation', value: 'Veteranscharitabledonation' },
                    {name: 'High-Tech Leader', value: 'Hightechleader' },
                    {name: 'Career Improvement', value: 'Careerimprovement' },
                    {name: 'Working Woman', value: 'Workingwoman' },
                    {name: 'African American Professionals', value: 'Africanamericanprofessionals' },
                    {name: 'Career', value: 'Career' },
                    {name: 'Education Online', value: 'Educationonline' },
                    {name: 'Computing/Home Office General', value: 'Computinghomeofficegeneral' },
                    {name: 'Electronics/Computing And Home Office', value: 'Electronicscomputingandhomeoffice' },
                    {name: 'Telecommunications', value: 'Telecommunications' },
                    {name: 'Self Improvement', value: 'Selfimprovement' },
                    {name: 'Value Hunter', value: 'valuehunter'},
                    {name: 'Mail Responder', value: 'Mailresponder' },
                    {name: 'Sweep Stakes', value: 'Sweepstakes' },
                    {name: 'Religious Magazine', value: 'Religiousmagazine' },
                    {name: 'Male Merch Buyer', value: 'Malemerchbuyer' },
                    {name: 'Female Merch Buyer', value: 'Femalemerchbuyer' },
                    {name: 'Crafts/Hobby Merch Buyer', value: 'Crafts_hobbmerchbuyer' },
                    {name: 'Gardering/Farming Buyer', value: 'Gardening_farmingbuyer' },
                    {name: 'Book Buyer', value: 'Bookbuyer' },
                    {name: 'Collect/Special Food Buyer', value: 'Collect_specialfoodsbuyer' },
                    {name: 'Mail Order Buyer', value: 'Mailorderbuyer' },
                    {name: 'Online Purchasing', value: 'Onlinepurchasingindicator' },
                    {name: 'Apparel Women', value: 'Apparelwomens'},
                    {name: 'Young Women Apparel', value: 'Youngwomensapparel' },
                    {name: 'Apparel Men', value: 'Apparelmens'},
                    {name: 'Apparel Men Big And Tall', value: 'Apparelmensbigandtall'},
                    {name: 'Young Men Apparel', value: 'Youngmensapparel' },
                    {name: 'Apparel Children', value: 'Apparelchildrens'},
                    {name: 'Health And Beauty', value: 'Healthandbeauty' },
                    {name: 'Beauty Cosmetics', value: 'Beautycosmetics' },
                    {name: 'Jewelry', value: 'Jewelry' },
                    {name: 'Luggage', value: 'Luggage' },
                    {name: 'TV Cable', value: 'Tvcable' },
                    {name: 'TV Satellite Dish', value: 'Tvsatellitedish' },
                    {name: 'High And Appliance', value: 'Highendappliances'},
                    {name: 'Consumers Electronics', value: 'Consumerelectronics' },
                    {name: 'Computers', value: 'Computers' },
                    {name: 'Electronics/Computers Grouping', value: 'Electronicscomputersgrouping' },
                    {name: 'Travel Grouping', value: 'Travelgrouping' },
                    {name: 'Travel', value: 'Travel' },
                    {name: 'Travel Domestic', value: 'Traveldomestic' },
                    {name: 'Travel International', value: 'Travelinternational'},
                    {name: 'Travel Cruise Vacations', value: 'Travelcruisevacations' },
                    {name: 'Dieting/Weight Loss', value: 'Dietingweightloss'},
                    {name: 'Automotive/Auto Parts And Accessories ', value: 'Automotiveautopartsandaccessories' },
                    //Interest//

                    {name: 'Occupation Group', value: 'Occupationgroup',type: 'type1'},
                    {name: 'Person Occupation', value: 'Personoccupation',type: 'type1'},
                    {name: 'Person Education', value: 'Personeducation',type: 'type1'},
                    {name: 'Business Owner', value: 'Businessowner',type: 'type1'},
                    {name: 'Opportunity Seeker', value: 'Opportunityseekers',type: 'type1'},
                    {name: 'Estimated Income Code', value: 'Estimatedincomecode',type: 'type1'},
                    {name: 'Investment', value: 'Investment',type: 'type1'},
                    {name: 'Investment Stock Securities', value: 'Investmentstocksecurities',type: 'type1'},
                    {name: 'Investing Active', value: 'Investing_active',type: 'type1'},
                    {name: 'Investment Personal', value: 'Investmentspersonal',type: 'type1'},
                    {name: 'Investments Real Estate ', value: 'Investmentsrealestate',type: 'type1'},
                    {name: 'Investing Finance Grouping', value: 'Investingfinancegrouping',type: 'type1'},
                    {name: 'Investments Foreign', value: 'Investmentsforeign',type: 'type1'},
                    {name: 'Residential Properties Owned', value: 'Investmentestimatedresidentialpropertiesowned',type: 'type1'},

                    {value: "homePurchasePrice", name: "Home Purchase Price",type: 'type1'},
                    {value: "homePurchaseDate", name: "Home Purchased Date",type: 'type1'},
                    {value: "homeYearBuilt", name: "Home Year Built",type: 'type1'},
                    {value: "estimatedCurrentHomeValueCode", name: "Estimated Home Value",type: 'type1'},
                    //{value: "mortgageAmountInThousands", name: "Mortgage Amount In Thousands"},
                    {value: "mortgageLenderName", name: "Mortgage Lender Name",type: 'type1'},
                    {value: "mortgageRate", name: "Mortgage Rate",type: 'type1'},
                    {value: "mortgageRateType", name: "Mortgage Rate Type",type: 'type1'},
                    {value: "mortgageLoanType", name: "Mortgage Loan Type",type: 'type1'},
                    {value: "transactionType", name: "Transaction Type",type: 'type1'},
                    {value: "deedDateOfRefinance", name: "Deed Date Of Refinance",type: 'type1'},
                    //{value: "refinanceAmountInThousands", name: "Refinance Amount In Thousands"},
                    {value: "refinanceLeaderName", name: "Refinance Lender Name",type: 'type1'},
                    {value: "refinanceRateType", name: "Refinance Rate Type",type: 'type1'},
                    {value: "refinanceLoanType", name: "Refinance Loan Type",type: 'type1'},
                    //{value: "censusMedianHomeValue", name: "Census Median Home Value"},
                    //{value: "censusMedianHouseHoldIncome", name: "Census Median House Hold Income"},
                    {value: "craIncomeClassificationCode", name: "CRA Income Classification Code",type: 'type1'},
                    {value: "purchaseMortgageDate", name: "Purchase Mortgage Date",type: 'type1'},
                    {value: "mostRecentLenderCode", name: "Most Recent Lender Code",type: 'type1'},
                    //{value: "purchaseLenderName", name: "Purchase Lender Name"},
                    {value: "mostRecentMortgageInterestRate", name: "Most Recent Mortgage Interest Rate",type: 'type1'},
                    {value: "loanToValue", name: "Loan To Value",type: 'type1'},

                    {value: "ChildrenAge00_02", name: "Children Age: 0-2 years",type: 'type1'},
                    {value: "ChildrenAge00_02FEMALE", name: "Children Age: 0-2 years, female",type: 'type1'},
                    {value: "ChildrenAge00_02MALE", name: "Children Age: 0-2 years, male",type: 'type1'},
                    {value: "ChildrenAge03_05", name: "Children Age: 3-5 years",type: 'type1'},
                    {value: "ChildrenAge03_05FEMALE", name: "Children Age: 3-5 years, female",type: 'type1'},
                    {value: "ChildrenAge03_05MALE", name: "Children Age: 3-5 years, male",type: 'type1'},
                    {value: "ChildrenAge06_10", name: "Children Age: 6-10 years",type: 'type1'},
                    {value: "ChildrenAge06_10FEMALE", name: "Children Age: 6-10 years, female",type: 'type1'},
                    {value: "ChildrenAge06_10MALE", name: "Children Age: 6-10 years, male",type: 'type1'},
                    {value: "ChildrenAge11_15", name: "Children Age: 11-15 years",type: 'type1'},
                    {value: "ChildrenAge11_15FEMALE", name: "Children Age: 11-15 years, female",type: 'type1'},
                    {value: "ChildrenAge11_15MALE", name: "Children Age: 11-15 years, male",type: 'type1'},
                    {value: "ChildrenAge16_17", name: "Children Age: 16-17 years",type: 'type1'},
                    {value: "ChildrenAge16_17FEMALE", name: "Children Age: 16-17 years, female",type: 'type1'},
                    {value: "ChildrenAge16_17MALE", name: "Children Age: 16-17 years, male",type: 'type1'},
                    {value: "DPV_CODE", name: "Delivery Point Verification Code",type: 'type1'},
                    {value: "NUMBEROFSOURCES", name: "Number Of Sources",type: 'type1'}

                ];
            }

            if (list.savedColumns && list.savedColumns.length > 0) {
                var columns = list.savedColumns.split(",");
                for (var i = 0; i < columns.length; i++) {
                    $scope.optionalFields.push({value: columns[i].trim(), name: columns[i].trim()});
                }
            }

            $scope.optionalFieldsObject = {};
            for (var i = 0; i < $scope.optionalFields.length; i++) {
                $scope.optionalFieldsObject[$scope.optionalFields[i].name.toLowerCase()] = true;
            }

            $scope.selectedOptionalField;
            $scope.selectedOptionalFields = {};

            var fixedProperties = {
                "ownerType": "HOMEOWNERPROBABILITYMODEL",
                "propertyType": "DWELLINGTYPE",
                "rating": "CREDITRATING",
                "activeLines": "NUMBEROFLINESOFCREDIT",
                "range": "CREDIT_RANGEOFNEWCREDIT",
                "estimatedIncome": "ESTIMATEDINCOMECODE",
                "propertyOwned": "INVESTMENTESTIMATEDRESIDENTIALPROPERTIESOWNED",
                "agesRange": (type === 6 || type === 9) ? "PERSONDATEOFBIRTHDATE": "DOBDATE",
            };

            var fixValue = function(value) {
                if (fixedProperties[value]) {
                    value = fixedProperties[value];
                }

                return value.toUpperCase().
                replace("PERSON", "").
                replace(/NEW/g, "").
                replace(/ED/g, "").
                replace(/E/g, "").
                replace(/S/g, "").
                replace(/ING/g, "").
                replace(/_/g, "");
            }

            var configObject = JSON.parse( list.request );
            if (type === 6 || type === 9) {
                for (var i = 0; i < $scope.optionalFields.length; i++) {
                    var fieldValue = fixValue($scope.optionalFields[i].value);

                    var properties = configObject.properties ? configObject.properties : [];
                    for (var j = 0; j < properties.length; j++) {
                        var fixedProp = fixValue(properties[j]);

                        if (fieldValue == fixedProp) {
                            $scope.selectedOptionalFields[$scope.optionalFields[i].name] = true;
                            break;
                        }
                    }

                    for (var prop in configObject) {
                        var fixedProp = fixValue(prop);

                        if (configObject[prop].length && configObject[prop].length > 0 && fieldValue == fixedProp) {
                            $scope.selectedOptionalFields[$scope.optionalFields[i].name] = true;
                            break;
                        }
                    }
                }
            }

            $scope.asArray = function(object) {
                var result = [];
                for (var prop in object) {
                    if (object[ prop ]) {
                        result.push(prop);
                    }
                }

                return result;
            }

            $scope.onSelectedOptionalField = function() {
                $scope.selectedOptionalFields[$scope.selectedOptionalField] = true;
                $scope.selectedOptionalField = undefined;
            }

            $scope.filterAutoCompleteValues = function(state, viewValue) {
                if (state.name) {
                    state = state.name;
                }

                if ($scope.optionalFieldsObject[viewValue.toLowerCase()]) {
                    return state.toLowerCase() === viewValue.toLowerCase();
                } else {
                    if (state.toLowerCase().indexOf(viewValue.toLowerCase()) != -1) {
                        return true;
                    }
                }

                return false;
            }

            $scope.close = function() {
                $modalInstance.dismiss();
            }

            $scope.changeAllByTitle = function() {
                if (!$scope.config.phonesOnly) {
                    $scope.config.all = !$scope.config.all;
                    $scope.changeAll();
                }
            }

            $scope.changeAll = function() {
                if (!$scope.config.all) {
                    $scope.selectedOptionalFields = {};
                } else {
                    for (var i = 0; i < $scope.optionalFields.length; i++) {
                        $scope.selectedOptionalFields[$scope.optionalFields[i].name] = true;
                    }
                }
            }

            $scope.onPhonesOnlyChanged = function() {
                if ($scope.config.phonesOnly) {
                    $scope.config.all = false;
                    $scope.config.code = '';
                    $scope.changeAll();
                }
            }

            function validateEmail(email) {
                const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            }

            $scope.previewAll = function() {
                var modalInstance = $modal.open( {
                    templateUrl: BASE_URL + '/assets/partials/modal/preview.optional.fields.html',
                    controller: 'PreviewOptionalFieldsModalController',
                    resolve: {
                        optionalFields: function() { return $scope.optionalFields; },
                        dataType: function() { return $scope.currentDataType; },
                        code: function() { return $scope.config.code; }
                    }
                } );

                modalInstance.result.then( function(result) {
                    var selectedFields = result.fields;
                    var code = result.code;

                    $scope.config.code = code;
                    for (var i = 0; i < selectedFields.length; i++) {
                        $scope.selectedOptionalFields[selectedFields[i].name] = true;
                        selectedFields[i].selected = false;
                    }
                } );
            }

            $scope.save = function() {
                var fields = [];

                for ( var i = 0; i < $scope.requiredFields.length; i++ ) {
                    fields.push( $scope.requiredFields[ i ].value );
                }

                for ( var i = 0; i < $scope.optionalFields.length; i++ ) {
                    if ( $scope.optionalFields[ i ].selected ) {
                        if ( $scope.optionalFields[ i ].value === 'DOB' ) {
                            fields.push( 'DOB_DAY' );
                            fields.push( 'DOB_MON' );
                            fields.push( 'DOB_YR' );
                            continue;
                        }

                        if ( $scope.optionalFields[ i ].value === 'ETHNIC' ) {
                            fields.push( 'ETHNIC_ASSIM' );
                            fields.push( 'ETHNIC_CODE' );
                            fields.push( 'ETHNIC_CONF' );
                            fields.push( 'ETHNIC_HISP_CNTRY' );
                            continue;
                        }

                        if ( $scope.optionalFields[ i ].value === 'INT' ) {
                            fields.push( 'INT_HOB_SWEEPS' );
                            fields.push( 'INT_TRAV_CASINO' );
                            fields.push( 'INT_TRAV_GENL' );
                            fields.push( 'LIFE_DIY' );
                            fields.push( 'LIFE_HOME' );
                            continue;
                        }

                        if ( $scope.optionalFields[ i ].value === 'OTHER' && type != 6) {
                            fields.push( 'HOME_OWNR_SRC' );
                            fields.push( 'INF_HH_RANK' );
                            fields.push( 'BUY_AUTO_PARTS' );
                            fields.push( 'BUY_GARDENING' );
                            fields.push( 'BUY_HOME_GARD' );
                            fields.push( 'CC_USER' );
                            fields.push( 'DONR_CHARITABLE' );
                            fields.push( 'DONR_MAIL_ORD' );
                            fields.push( 'DONR_POL' );
                            fields.push( 'DONR_POL_CONS' );
                            fields.push( 'DONR_POL_LIB' );
                            fields.push( 'DONR_RELIG' );
                            fields.push( 'DONR_VETS' );
                            fields.push( 'DWELL_TYP' );
                            fields.push( 'FIPS_CTY' );
                            fields.push( 'MI' );
                            fields.push( 'NAME_PRE' );
                            fields.push( 'NUM_ADULTS' );
                            fields.push( 'PRES_KIDS' );
                            fields.push( 'ZIP4' );
                            continue;
                        }

                        fields.push( $scope.optionalFields[ i ].value );
                    }
                }

                var selectedOptionalFields = $scope.asArray($scope.selectedOptionalFields);
                for (var i = 0; i < selectedOptionalFields.length; i++) {
                    for (var j = 0; j < $scope.optionalFields.length; j++) {
                        if (selectedOptionalFields[i] === $scope.optionalFields[j].name) {
                            fields.push($scope.optionalFields[j].value.toUpperCase());
                            break;
                        }
                    }
                }

                if ($scope.config.phonesOnly) {
                    fields = ['phone'];
                }
                if(validateEmail($scope.config.emailAddress)){
                    if(document.getElementById("errorField")){
                        document.getElementById("errorField").style['visibility'] = 'hidden';
                    }
                    $modalInstance.close({'fields': fields, code: $scope.config.code,'emailAddress':$scope.config.emailAddress});
                }else{
                    if(document.getElementById("errorField")){
                        document.getElementById("errorField").style['visibility'] = 'visible';
                    }
                    $modalInstance.close({'fields': fields, code: $scope.config.code,'emailAddress':$scope.config.emailAddress});
                }

            }
        } ] ).
controller( 'PreviewOptionalFieldsModalController',
    [ '$scope', '$modalInstance', 'optionalFields', 'credentialsService', 'systemService', '$filter', 'code','dataType',
        function( $scope, $modalInstance, optionalFields, credentialsService, systemService, $filter, code,dataType ) {
            $scope.optionalFields = [];
            $scope.config = {search: '', 'code': code};
            $scope.isAdditionalCodeEnabled = false;
            $scope.currentDataType = dataType;
            for (var i = 0; i < optionalFields.length; i++) {
                $scope.optionalFields.push(optionalFields[i]);
            }

            systemService.getUserDetails({userId: credentialsService.getUser().id}, function(response) {
                if (response.status == 'OK') {
                    $scope.isAdditionalCodeEnabled = response.data.listAdditionalCodeEnabled;
                }
            });

            $scope.select = function() {
                var selectedFields = [];

                for (var i = 0; i < $scope.optionalFields.length; i++) {
                    if ($scope.optionalFields[i].selected) {
                        selectedFields.push($scope.optionalFields[i]);
                    }
                }

                $modalInstance.close({fields: selectedFields, code: $scope.config.code});
            }

            $scope.loadinteresrsFields = function() {
                for ( var i = 0; i < $scope.optionalFields.length; i++ ) {
                    var list = $scope.optionalFields[ i ];
                    list.type = "type1";

                }
            }
            $scope.cancel = function() {
                $modalInstance.dismiss();
            }
        }]).
controller( 'EditListModalController',
    [ '$scope', '$state','$modalInstance', 'dictionaryService', 'list', 'uploaded', '$filter',
        function( $scope, $state, $modalInstance, dictionaryService, list, uploaded, $filter ) {
            $scope.name = list.name;
            $scope.uploaded = uploaded;
            if ( !uploaded ) {
                var configObject = JSON.parse( list.request );
                $scope.dataType = $scope.$root.reversedTypes[configObject.dataType]

                var convertDates = function(array) {
                    var results = [];
                    if (array == undefined) { return results; }

                    if (array[0] && array[0] != -1) {
                        var date = new Date(parseInt(array[0]));
                        results[0] = {title: $filter( 'date' )( date, 'dd.MM.yyyy' ), value: parseInt(array[0])};
                    }

                    if (array[1] && array[1] != -1) {
                        var date = new Date(parseInt(array[1]));
                        results[1] = {title: $filter( 'date' )( date, 'dd.MM.yyyy' ), value: parseInt(array[1])};
                    }

                    return results;
                }

                var convertSelectedDates = function(array) {
                    var results = [];
                    if (array == undefined) { return results; }

                    if (array[0] && array[0] != -1) {
                        var date = new Date(parseInt(array[0]));
                        results[0] = $filter( 'date' )( date, 'dd.MM.yyyy' ) + "|" + parseInt(array[0]);
                    }

                    if (array[1] && array[1] != -1) {
                        var date = new Date(parseInt(array[1]));
                        results[1] = $filter( 'date' )( date, 'dd.MM.yyyy' ) + "|" + parseInt(array[1]);
                    }

                    return results;
                }

                $scope.formatDob = function() {
                    var dobs = $scope.asArray($scope.dobs);
                    if (dobs.length == 1) {
                        return dobs[0].split('|')[0];
                    } else if (dobs.length == 2) {
                        var leftDob = dobs[0].split('|')[0];
                        var rightDob = dobs[1].split('|')[0];

                        var leftDate = new Date(leftDob).getTime();
                        var rightDate = new Date(rightDob).getTime();

                        if (leftDate > rightDate) {
                            var tmp = leftDob;
                            leftDob = rightDob;
                            rightDob = tmp;
                        }

                        return leftDob + ' - ' + rightDob;
                    } else {
                        return '';
                    }
                }

                $scope.updatePersonOccupations = function(occupations) {
                    var result = [];

                    for (var i = 0; i < occupations.length; i++) {
                        for (var j = 0; j < $scope.occupations.length; j++) {
                            if (occupations[i] == $scope.occupations[j].value) {
                                result.push($scope.occupations[j].title + '|' + $scope.occupations[j].value);
                                break;
                            }
                        }
                    }

                    return result;
                }

                $scope.selectedStates = configObject.states;
                $scope.selectedCities = configObject.cities;
                $scope.selectedZipCodes = configObject.zipCodes;
                $scope.selectedCounties = configObject.counties;
                $scope.selectedAreaCodes = configObject.areaCodes;
                $scope.selectedTimeZones = configObject.timeZones;
                $scope.selectedCarriers = configObject.carriers;

                $scope.omittedStates = configObject.omittedStates;
                $scope.omittedCities = configObject.omittedCities;
                $scope.omittedZipCodes = configObject.omittedZipCodes;
                $scope.omittedAreaCodes = configObject.omittedAreaCodes;

                $scope.selectedAges = configObject.ages;
                $scope.agesRange = configObject.agesRange;
                $scope.dobs = configObject.dobs;
                $scope.selectedGenders = configObject.genders;
                $scope.selectedEducations = configObject.educations;

                $scope.selectedNetWorth = configObject.netWorth;
                $scope.selectedRating = configObject.creditRating;
                $scope.selectedActiveLines = configObject.creditLines;
                $scope.selectedRange = configObject.creditRanges;

                $scope.selectedEthnicityGroup = configObject.ethnicityGroups;
                $scope.selectedEthnicityLanguage = configObject.ethnicityLanguages;
                $scope.selectedEthnicityReligion = configObject.ethnicityReligions;

                $scope.selectedHouseholdSize = configObject.householdSize;
                $scope.selectedHouseholdIncome = configObject.householdIncome;

                $scope.selectedResidenceType = configObject.residenceType;
                $scope.selectedResidenceOwnerShip = configObject.residenceOwnership;
                $scope.selectedResidenceVeteran = configObject.residenceVeteran;
                $scope.selectedResidenceLength = configObject.residenceLength;
                $scope.selectedResidenceMarital = configObject.residenceMarital;
                $scope.selectedResidenceChildren = configObject.residenceChildren;

                $scope.selectedInterests = configObject.interests;

                $scope.selectedLists = configObject.selectedLists;
                $scope.selectedUploadedLists = configObject.uploadedLists;

                $scope.selectedSales = configObject.sales;
                $scope.selectedEmployeeCount = configObject.employeeCount;
                $scope.selectedTitles = configObject.titles;

                $scope.selectedIndustries = configObject.industries;
                if (configObject.industries.length && configObject.industries.length > 0) {
                    dictionaryService.selectedDetailedBusinessIndustries({values: configObject.industries}, function(response) {
                        $scope.selectedIndustries = [];
                        for (var i = 0; i < response.data.length; i++) {
                            $scope.selectedIndustries.push(response.data[i].industry);
                        }
                    });
                }

                $scope.selectedSics = configObject.sics;
                $scope.sicRange = {fromSic: configObject.fromSic, toSic: configObject.toSic};

                $scope.selectedSources = configObject.sources;

                $scope.selectedSections = configObject.sections;
                $scope.selectedDates = convertSelectedDates(configObject.dates);

                $scope.selectedPhoneTypes = configObject.phoneTypes;
                $scope.selectedMaritalStatuses = configObject.maritalStatuses;
                $scope.selectedEthnicCodes = configObject.ethnicCodes;
                $scope.selectedLanguageCodes = configObject.languageCodes;
                $scope.selectedEthnicGroups = configObject.ethnicGroups;
                $scope.selectedReligionCodes = configObject.religionCodes;
                $scope.selectedHispanicCountryCodes = configObject.hispanicCountryCodes;
                $scope.selectedProperties = configObject.properties;

                $scope.selectedCompanyTypes = configObject.companyType;
                $scope.selectedCreditScores = configObject.creditScores;

                $scope.selectedPropertyType = configObject.propertyType;
                $scope.selectedOwnerType = configObject.ownerType;
                $scope.selectedLengthOfResidence = configObject.lengthOfResidence;
                $scope.selectedNumberOfPersonInLivingUnit = configObject.numberOfPersonInLivingUnit;
                $scope.selectedNumberOfChildren = configObject.numberOfChildren;
                $scope.selectedInferredHouseHoldRank = configObject.inferredHouseHoldRank;
                $scope.selectedNumberOfAdults = configObject.numberOfAdults;
                $scope.selectedGenerationsInHouseHold = configObject.generationsInHouseHold;
                $scope.selectedSewer = configObject.sewer;
                $scope.selectedWater = configObject.water;

                $scope.selectedOccupationGroups = configObject.occupationGroups;
                $scope.selectedPersonEducations = configObject.personEducations;
                $scope.selectedPersonOccupations = $scope.updatePersonOccupations(configObject.personOccupations);
                $scope.selectedBusinessOwners = configObject.businessOwners;
                $scope.selectedEstimatedIncome = configObject.estimatedIncome;
                $scope.selectedNetWorthes = configObject.netWorthes;
                $scope.selectedPropertyOwned = configObject.propertyOwned;

                $scope.selectedHomePurchasePrices = configObject.homePurchasePrices;
                $scope.selectedHomePurchasedDates = convertDates(configObject.homePurchasedDates);
                $scope.selectedHomeYearBuilt = convertDates(configObject.homeYearBuilt);
                $scope.selectedEstimatedCurrentHomeValueCodes = configObject.estimatedCurrentHomeValueCodes;
                $scope.selectedMortgageAmountInThousands = configObject.mortgageAmountInThousands;
                $scope.selectedMortgageLenderNames = configObject.mortgageLenderNames;
                $scope.selectedMortgageRate = configObject.mortgageRate;
                $scope.selectedMortgageRateTypes = configObject.mortgageRateTypes;
                $scope.selectedMortgageLoanTypes = configObject.mortgageLoanTypes;
                $scope.selectedTransactionTypes = configObject.transactionTypes;
                $scope.selectedRefinanceAmountInThousands = configObject.refinanceAmountInThousands;
                $scope.selectedRefinanceLeaderNames = configObject.refinanceLeaderNames;
                $scope.selectedDeedDatesOfRefinance = convertDates(configObject.deedDatesOfRefinance);
                $scope.selectedRefinanceRateTypes = configObject.refinanceRateTypes;
                $scope.selectedRefinanceLoanTypes = configObject.refinanceLoanTypes;
                $scope.selectedCensusMedianHouseHoldIncome = configObject.censusMedianHouseHoldIncome;
                $scope.selectedCensusMedianHomeValue = configObject.censusMedianHomeValue;
                $scope.selectedCraIncomeClassificationCodes = configObject.craIncomeClassificationCodes;
                $scope.selectedPurchaseMortgageDates = convertDates(configObject.purchaseMortgageDates);
                $scope.selectedMostRecentLenderCodes = configObject.mostRecentLenderCodes;
                $scope.selectedPurchaseLenderNames = configObject.purchaseLenderNames;
                $scope.selectedMostRecentMortgageInterestRates = configObject.mostRecentMortgageInterestRates;
                $scope.selectedLoanToValues = configObject.loanToValues;

                $scope.selectedNumberOfSources = configObject.numberOfSources;
                $scope.selectedDPV = configObject.dvp;

                $scope.selectedChildrenAgeGender = configObject.childrenAgeGender;

                $scope.selectedRating = configObject.rating;
                $scope.selectedActiveLines = configObject.activeLines;
                $scope.selectedRange = configObject.range;
                $scope.selectedCategory = configObject.categories;

                $scope.selectedYears = configObject.yearsRange;
                $scope.selectedModels = configObject.models;
                $scope.selectedMakes = configObject.makes;

                $scope.keywords = [];
                var keywords = configObject.keywords;
                if (keywords) {
                    for (var i = 0; i < keywords.length; i++) {
                        if (keywords[i]) {
                            $scope.keywords.push({keyword: keywords[i], selectedColumns: configObject.keywordsColumns[i]});
                        }
                    }
                }
            }

            $scope.asArray = function(array) {
                return array;
            }

            $scope.asSortedArray = function(array) {
                return array;
            }

            $scope.formatSourceName = function(name) {
                name = name.toLowerCase();

                if (name == 'bing2019') {
                    return 'bing';
                } else if (name == 'angieslist2019' || name == 'angieslist2020') {
                    return 'angieslist';
                } else if (name == 'superpages2019') {
                    return 'superpages';
                } else {
                    return name;
                }
            }

            $scope.getDatesRangeTitle = function(array) {
                if (array[0] && array[0] != -1 && array[1] && array[1] != -1) {
                    return new Date(array[0]).getFullYear()  + ' - ' + new Date(array[1]).getFullYear();
                } else if (array[0] && array[0] != -1) {
                    return new Date(array[0]).getFullYear();
                } else if (array[1] && array[1] != -1) {
                    return new Date(array[1]).getFullYear();
                }
            }

            $scope.generateColumnsString = function(keyword) {
                var result = "";
                for (var i = 0; i < keyword.selectedColumns.length; i++) {
                    if (result.length > 0) {
                        result = result + ", ";
                    }

                    result = result + keyword.selectedColumns[i];
                }

                if (result.length > 0) {
                    return (" [" + result + "]").toLowerCase().replace("_", " ");
                } else {
                    return result.toLowerCase().replace("_", " ");
                }
            }

            $scope.formatZipCode = function( value ) {
                if ( value.length === 4 ) {
                    return '0' + value;
                } else {
                    return value;
                }
            }

            $scope.close = function() {
                $modalInstance.dismiss();
            }

            $scope.save = function() {
                $modalInstance.close( $scope.name );
            }

            $scope.searchWithParams = function() {
                $modalInstance.dismiss();
                $state.transitionTo('main', {searchRequest: JSON.parse( list.request )});
            }
        } ] ).
controller( 'BuyListModalController',
    [ '$scope', '$modalInstance', 'credentialsService', 'profileService', 'list', 'listService', 'toasty', 'localization',
        function( $scope, $modalInstance, credentialsService, profileService, list, listService, toasty, localization ) {

            $scope.close = function() {
                $modalInstance.dismiss();
            }

            $scope.maxListSize = 1000000;
            $scope.showErrorMessage = false;
            $scope.checkListSize = function() {
                return $scope.showErrorMessage;
            }

            $scope.sliderOptions = { from: 0,
                to: list.cnt,
                step: 1,
                css: {
                    background: { 'background-color': 'silver' },
                    after:      { 'background-color': '#96b9c1' },
                    pointer:    { 'background-color': '#337ab7' }
                }
            };

            var init = function() {
                $scope.total = 0;
                $scope.amount = 0;
                profileService.protected(
                    credentialsService.getUser().id,
                    credentialsService.getUser().password).
                getUserBalance( { id: credentialsService.getUser().id }, function( response ) {
                    $scope.balance = response.data.toFixed(2);
                    $scope.commonBalance = response.data.toFixed(2);
                } );
                listService.getListItemPrice( { id: list.id }, function( response ) {
                    $scope.pricePerItem = response.data;
                } );
            }
            init();


            $scope.filterValue = function( $event ) {
                if( isNaN( String.fromCharCode( $event.charCode ) ) &&
                    $event.keyCode != 8 && $event.keyCode != 46 &&
                    $event.keyCode != 37 && $event.keyCode != 39 ) {
                    $event.preventDefault();
                }
            };

            $scope.pricePerItem = 0.001;
            $scope.totalChanged = function() {
                if ($scope.total > list.cnt) {
                    $scope.total = list.cnt;
                }

                $scope.amount = ( $scope.total * $scope.pricePerItem ).toFixed( 2 );
                $scope.balance = ( $scope.commonBalance - $scope.amount ).toFixed( 2 );
                $scope.showErrorMessage = false;

                if ( $scope.balance < 0 ) {
                    $scope.balance = 0;
                    $scope.total = $scope.commonBalance / $scope.pricePerItem;
                    $scope.amount = ( $scope.total * $scope.pricePerItem ).toFixed( 2 );
                }

                if ( parseInt($scope.total) > $scope.maxListSize ) {
                    $scope.showErrorMessage = true;

                    $scope.amount = ( $scope.total * $scope.pricePerItem ).toFixed( 2 );
                    $scope.balance = ( $scope.commonBalance - $scope.amount ).toFixed( 2 );
                }
            }

            $scope.buy = function() {
                var request = { listId: list.id,
                    userId: credentialsService.getUser().id,
                    total: $scope.total };
                $scope.loading = true;
                listService.buyList( request, function( response ) {
                    if ( response.status == 'OK' ) {
                        toasty.success( {
                            title: localization.localize( 'buy.list.success' ),
                            msg: localization.localize( 'buy.list.success.message' ),
                            sound: false
                        } );
                        $scope.updateUserBalance();
                    } else if (response.message == "reseller balance") {
                        toasty.error( {
                            title: localization.localize( 'buy.list.error' ),
                            msg: localization.localize( 'Parent Account Suspended' ),
                            sound: false
                        } );
                    } else {
                        toasty.error( {
                            title: localization.localize( 'buy.list.error' ),
                            msg: localization.localize( 'buy.list.error.message' ),
                            sound: false
                        } );
                        init();
                    }
                    $scope.loading = false;
                } );
            }

            $scope.updateUserBalance = function() {
                profileService.protected(
                    credentialsService.getUser().id,
                    credentialsService.getUser().password).
                getUserBalance( { id: credentialsService.getUser().id }, function( response ) {
                    var balance = response.data.toFixed(2);
                    var user = credentialsService.getUser();
                    user.balance = balance;
                    credentialsService.setUser(user);

                    $modalInstance.close();
                } );
            }
        } ] )
    .controller( 'UploadListForMatchingController',
        [ '$scope', '$state', 'BASE_URL', 'listService', 'systemService', 'dataService', 'credentialsService', 'toasty', 'administrationService', '$http', '$interval', '$timeout', '$modal', 'localization',
            function( $scope, $state, BASE_URL, listService, systemService, dataService, credentialsService, toasty, administrationService, $http, $interval, $timeout, $modal, localization ) {
                $scope.selected = [];
                $scope.tableArray = [];
                $scope.count = 0;
                $scope.fields = [];

                $scope.isFilterDNCEnabled = false;
                $scope.config = {filterDNC: false};

                systemService.getUserDetails({userId: credentialsService.getUser().id}, function(response) {
                    if (response.status == 'OK') {
                        $scope.isFilterDNCEnabled = response.data.filterDNC;

                        if (!response.data.allowMatchingLists) {
                            credentialsService.logout();
                            $state.transitionTo('login');
                        }
                    }
                });

                $scope.select_table = function(list) {
                    $scope.selected[list.name] = !$scope.selected[list.name];
                    if($scope.selected[list.name] == true){
                        $scope.tableArray.push(list.name);
                    }else{
                        $scope.tableArray = $scope.tableArray.filter(function(item) {
                            return item !== list.name
                        })

                    }
                }
                $scope.removeMatchList = function(list) {

                }
                var updateLists = function() {
                    console.log($scope.selected)
                    var handler = function( response ) {
                        if ( response.status === 'OK' ) {
                            $scope.matchLists = response.data;
                           // $scope.matchTotal = response.data.total;
                        }
                    };

                    administrationService.protected( $scope.userId, $scope.userPassword ).
                    allmatchlists( $scope.config, handler );

                    //listService.getPagedUsersPurchased( $scope.config, handler );

                }
                updateLists();

                $scope.getLocalizedListType = function( list ) {
                    return localization.localize( 'lists.type.' + list.type );
                }

                $scope.getDate = function( time ) {
                    return new Date( time );
                }

                var updateFields = function() {
                    var type = $scope.$root.types[$scope.dataType];
                    if ( type === 0 ) {
                        $scope.fields = [
                            { name: 'First Name', value: 'FN' },
                            { name: 'Last Name', value: 'LN' },
                            { name: 'Address', value: 'ADDR' },
                            { name: 'Apt', value: 'APT' },
                            { name: 'City', value: 'CITY' },
                            { name: 'State', value: 'ST' },
                            { name: 'Zip', value: 'ZIP' },
                            { name: 'Phone', value: 'PHONE' },
                            { name: 'Phone Type', value: 'phoneType' },
                            { name: 'County', value: 'COUNTY' },
                            { name: 'Area code', value: 'AREA_CODE' },
                            { name: 'Date Of Birth', value: 'DOB' },
                            { name: 'Gender', value: 'GENDER' },
                            { name: 'DNC Info', value: 'dnc' },
                            { name: 'Net Worth', value: 'NET_WORTH' },
                            { name: 'Credit Rating', value: 'CREDIT_RATING' },
                            { name: 'Active Credit Lines', value: 'CREDIT_LINES' },
                            { name: 'Range Of New Credit', value: 'CREDIT_RANGE_NEW' },
                            { name: 'Ethnic-group', value: 'ETHNIC_GRP' },
                            { name: 'Language', value: 'ETHNIC_LANG' },
                            { name: 'Ethnic(other)', value: 'ETHNIC' },
                            { name: 'Household Size', value: 'HH_SIZE' },
                            { name: 'Household Income', value: 'HH_INCOME' },
                            { name: 'Veteran In Household', value: 'VET_IN_HH' },
                            { name: 'Property Type', value: 'PROP_TYPE' },
                            { name: 'Ownership', value: 'HOME_OWNR' },
                            { name: 'Marital Status', value: 'HH_MARITAL_STAT' },
                            { name: 'Number Of Children', value: 'NUM_KIDS' }
                        ];
                    } else if (type === 1) {
                        $scope.fields = [
                            { name: 'Company Name', value: 'COMPANY_NAME' },
                            { name: 'Address', value: 'ADDRESS' },
                            { name: 'City', value: 'CITY' },
                            { name: 'State', value: 'STATE' },
                            { name: 'Zip', value: 'ZIP' },
                            { name: 'Phone', value: 'PHONE' },
                            { name: 'Phone Type', value: 'phoneType' },
                            { name: 'County', value: 'COUNTY' },
                            //{ name: 'Fax', value: 'FAX' },
                            { name: 'DNC Info', value: 'dnc' },
                            { name: 'Area Code', value: 'AREA_CODE' },
                            { name: 'Contact Name', value: 'CONTACT_NAME' },
                            { name: 'Contact Gender', value: 'GENDER' },
                            { name: 'Latitude', value: 'LATITUDE' },
                            { name: 'Longitude', value: 'LONGITUDE' },
                            { name: 'SIC Code', value: 'SIC_CODE' },
                            { name: 'Title', value: 'TITLE' },
                            { name: 'Website', value: 'WWW' },
                            { name: 'Industry', value: 'INDUSTRY' },
                            { name: 'Employee', value: 'EMPLOYEE' },
                            { name: 'Annual Sales', value: 'ANNUAL_SALES' },
                            //{ name: 'Email', value: 'email1' }
                        ];
                    } else if (type === 18) {
                        $scope.fields = [
                            { name: 'Company Name', value: 'COMPANY_NAME' },
                            { name: 'Address', value: 'ADDRESS' },
                            { name: 'City', value: 'CITY' },
                            { name: 'State', value: 'STATE' },
                            { name: 'Zip', value: 'ZIP' },
                            { name: 'Phone', value: 'PHONE' },
                            { name: 'Phone Type', value: 'phoneType' },
                            { name: 'County', value: 'COUNTY' },
                            //{ name: 'Fax', value: 'FAX' },
                            { name: 'DNC Info', value: 'dnc' },
                            { name: 'Area Code', value: 'AREA_CODE' },
                            { name: 'Contact Name', value: 'CONTACT_NAME' },
                            { name: 'Contact Gender', value: 'GENDER' },
                            { name: 'Latitude', value: 'LATITUDE' },
                            { name: 'Longitude', value: 'LONGITUDE' },
                            { name: 'SIC Code', value: 'SIC_CODE' },
                            { name: 'Website', value: 'WWW' },
                            { name: 'Employee', value: 'EMPLOYEE' },
                            { name: 'Annual Sales', value: 'ANNUAL_SALES' },
                            //{ name: 'Email', value: 'email1' }
                        ];
                    } else if (type === 2){
                        $scope.fields = [
                            { name: 'Company Name', value: 'COMPANY_NAME' },
                            { name: 'Address', value: 'ADDRESS' },
                            { name: 'City', value: 'CITY' },
                            { name: 'State', value: 'STATE' },
                            { name: 'Zip', value: 'ZIP' },
                            { name: 'Phone', value: 'PHONE' },
                            { name: 'Phone Type', value: 'phoneType' },
                            { name: 'County', value: 'COUNTY' },
                            { name: 'Area Code', value: 'AREA_CODE' },
                            { name: 'Source', value: 'WWW' },
                            { name: 'Industry', value: 'INDUSTRY' },
                            { name: 'DNC Info', value: 'dnc' },
                            { name: 'Contact Name', value: 'contact_name' },
                            { name: 'Website', value: 'websites' }
                        ];
                    } else if (type === 13) {
                        $scope.fields = [
                            { name: 'company name', value: 'company' },
                            { name: 'address', value: 'ADDRESS' },
                            { name: 'city', value: 'CITY' },
                            { name: 'state', value: 'STATE' },
                            { name: 'zip', value: 'ZIP' },
                            { name: 'phone', value: 'PHONE' },
                            { name: 'phone type', value: 'phoneType' },
                            { name: 'DNC Info', value: 'dnc' },
                            { name: 'County', value: 'COUNTY' },
                            { name: 'Fax', value: 'FAX' },
                            { name: 'Area Code', value: 'AREA_CODE' },
                            { name: 'Contact Name', value: 'name' },
                            { name: 'SIC Code', value: 'SIC_CODE' },
                            { name: 'Industry', value: 'INDUSTRY' },
                            { name: 'Title', value: 'TITLE' },
                            { name: 'Website', value: 'WWW' },
                            { name: 'Toll Free Number', value: 'tollFreePhone' },
                            { name: 'Fax Number', value: 'faxNumber' },
                            { name: 'Ethnic Code', value: 'ethnicCode' },
                            { name: 'Ethnic Group', value: 'ethnicGroup' },
                            { name: 'Language Code', value: 'languageCode' },
                            { name: 'Religion Code', value: 'religionCode' },
                            { name: 'Total Employees Corp Wide', value: 'totalEmployees' },
                            { name: 'Employees on Site', value: 'employeesOnSite' },
                            { name: 'Total Revenue Corp Wide', value: 'totalRevenue' },
                            { name: 'Revenue at Site', value: 'revenue' },
                            { name: 'Year Founded', value: 'yearFounded' },
                            { name: 'Minority Owned', value: 'minorityOwned' },
                            { name: 'Small Business', value: 'smallBusiness' },
                            { name: 'Large Business', value: 'largeBusiness' },
                            { name: 'Home Business', value: 'homeBusiness' },
                            { name: 'Import Export', value: 'importExport' },
                            { name: 'Public Company', value: 'publicCompany' },
                            { name: 'Headquarters Branch', value: 'headquartersBranch' },
                            { name: 'Stock Exchange', value: 'stockExchange' },
                            { name: 'Franchise', value: 'franchiseFlag' },
                            { name: 'Individual Firm Code', value: 'individualFirmCode' },
                            { name: 'Year Appeared', value: 'appearedYear' },
                            { name: 'Female Owned or Operated', value: 'femaleOwnedorOperated' },
                            { name: 'White Collar Code', value: 'whiteCollarCode' },
                            { name: 'Email', value: 'email' },
                            { name: 'Phone Contact', value: 'phoneContact' },
                            { name: 'Credit Score', value: 'creditScore' }
                        ];
                    } else if (type === 17) {
                        $scope.fields = [
                            {name: 'name', value: 'name'},
                            {name: 'address', value: 'address'},
                            {name: 'state', value: 'state'},
                            {name: 'zip code', value: 'zip_code'},
                            {name: 'email', value: 'email'},
                            {name: 'phone', value: 'phone'},
                            {name: 'phone type', value: 'phoneType'},
                            {name: 'DNC Info', value: 'dnc' },
                            {name: 'County', value: 'county'},
                            {name: 'City', value: 'city'},
                            {name: 'Area Code', value: 'area_code'}
                        ];
                    } else if (type === 3) {
                        $scope.fields = [
                            { name: 'State', value: 'STATE' },
                            { name: 'Industry', value: 'INDUSTRY' },
                            { name: 'Phone', value: 'PHONE' },
                            { name: 'phone type', value: 'phoneType' },
                            { name: 'Website', value: 'website' },
                            { name: 'Area Code', value: 'AREA_CODE' },
                            { name: 'DNC Info', value: 'dnc' }
                        ];
                    } else if (type === 4) {
                        $scope.fields = [
                            { name: 'Website', value: 'website' },
                            { name: 'Email', value: 'email' },
                            { name: 'Name', value: 'name' },
                            { name: 'Business', value: 'business' },
                            { name: 'Address', value: 'address' },
                            { name: 'City', value: 'CITY' },
                            { name: 'State', value: 'STATE' },
                            { name: 'Zip', value: 'ZIP_CODE' },
                            { name: 'Phone', value: 'PHONE' },
                            { name: 'Phone Type', value: 'phoneType' },
                            { name: 'County', value: 'COUNTY' },
                            { name: 'Area Code', value: 'AREA_CODE' },
                            { name: 'DNC Info', value: 'dnc' }
                        ];
                    } else if (type === 5) {
                        $scope.fields = [
                            { name: 'State', value: 'STATE' },
                            { name: 'Website', value: 'website' },
                            { name: 'Phone', value: 'PHONE' },
                            { name: 'Phone Type', value: 'phoneType' },
                            { name: 'Area Code', value: 'AREA_CODE' },
                            { name: 'DNC Info', value: 'dnc' }
                        ];
                    } else if (type === 7) {
                        $scope.fields = [
                            {name: 'Username', value: 'username'},
                            {name: 'Full Name', value: 'fullname'},
                            {name: 'Category', value: 'category'},
                            {name: 'State', value: 'state'},
                            {name: 'Zip Code', value: 'zipCode'},
                            {name: 'Website', value: 'website'},
                            {name: 'Email', value: 'email'},
                            {name: 'Phone', value: 'phone'},
                            {name: 'Phone Type', value: 'phoneType'},
                            {name: 'County', value: 'county'},
                            {name: 'City', value: 'city'},
                            {name: 'Street', value: 'street'},
                            {name: 'Area Code', value: 'areaCode'},
                            {name: 'DNC Info', value: 'dnc'}
                        ];
                    } else if (type === 14) {
                        $scope.fields = [
                            {name: 'username', value: 'username'},
                            {name: 'full name', value: 'fullname'},
                            {name: 'category', value: 'category'},
                            {name: 'state', value: 'state'},
                            {name: 'zip code', value: 'zipCode'},
                            {name: 'website', value: 'website'},
                            {name: 'email', value: 'email'},
                            {name: 'phone', value: 'phone'},
                            {name: 'phone type', value: 'phoneType'},
                            {name: 'DNC Info', value: 'dnc' },
                            {name: 'County', value: 'county'},
                            {name: 'City', value: 'city'},
                            {name: 'Street', value: 'street'},
                            {name: 'Area Code', value: 'areaCode'},
                            {name: 'Publications Count', value: 'publicationsCount'},
                            {name: 'Followers Count', value: 'followersCount'},
                            {name: 'Subscriptions Count', value: 'subscriptionsCount'},
                            {name: 'Feed Ads Count', value: 'feedAdsCount'},
                            {name: 'Story Ads Count', value: 'storyAdsCount'},
                            {name: 'Biography', value: 'biography'}
                        ];
                    } else if (type === 12) {
                        $scope.fields = [
                            {name: 'Company name', value: 'companyName'},
                            {name: 'Email', value: 'email'},
                            {name: 'Title', value: 'title'},
                            {name: 'County', value: 'county'},
                            {name: 'City', value: 'city'},
                            {name: 'Fax', value: 'fax'},
                            {name: 'Website', value: 'website'},
                            {name: 'IP', value: 'ip'},
                            {name: 'Linked In Id', value: 'linkedInId'},
                            {name: 'DNC Info', value: 'dnc' },
                            {name: 'Area Code', value: 'area_Code'}
                        ];
                    } else if (type === 15) {
                        $scope.fields = [
                            {name: 'First Name', value: 'firstName' },
                            {name: 'Last Name', value: 'lastName' },
                            {name: 'Address', value: 'address' },
                            {name: 'City', value: 'city' },
                            {name: 'State', value: 'state' },
                            {name: 'Zip', value: 'zipCode' },
                            {name: 'Phone', value: 'phone' },
                            {name: 'Phone Type', value: 'phoneType' },
                            {name: 'County', value: 'county'},
                            {name: 'IP', value: 'IP'},
                            {name: 'DNC Info', value: 'dnc' },
                            {name: 'Area Code', value: 'areaCode'}
                        ];
                    } else if (type === 6 || type === 9) {
                        $scope.fields = [
                            { name: 'First Name', value: 'PERSONFIRSTNAME' },
                            { name: 'Last Name', value: 'PERSONLASTNAME' },
                            { name: 'Email', value: 'EMAIL' },
                            { name: 'Address', value: 'PRIMARYADDRESS' },
                            { name: 'Apt', value: 'SECONDARYADDRESS' },
                            { name: 'City', value: 'CITYNAME' },
                            { name: 'State', value: 'STATE' },
                            { name: 'Zip', value: 'ZIPCODE' },
                            { name: 'Phone', value: 'PHONE' },
                            { name: 'Phone Type', value: 'phoneType' },
                            { name: 'County', value: 'Countyname' },
                            { name: 'Area Code', value: 'Areacode' },
                            { name: 'Gender', value: 'Persongender' },
                            { name: 'DNC Info', value: 'Dnc' },
                            { name: 'Education', value: 'Personeducation' },

                            { name: 'Website', value: 'website' },
                            { name: 'IP', value: 'ip' },

                            { name: 'VIN', value: 'vin' },
                            { name: 'Vehicle Make', value: 'make' },
                            { name: 'Vehicle Model', value: 'model' },
                            { name: 'Vehicle Year', value: 'year' },

                            { name: 'Net Worth', value: 'Networth' },
                            { name: 'Credit Rating', value: 'Creditrating' },
                            { name: 'Active Credit Lines', value: 'Numberoflinesofcredit' },
                            { name: 'Range Of New Credit', value: 'Credit_rangeofnewcredit' },

                            { name: 'Ethnic-group', value: 'Ethnicgroup' },
                            { name: 'Language', value: 'Languagecode' },
                            { name: 'Religion', value: 'Religioncode' },
                            { name: 'Veteran In Household', value: 'Veteraninhousehold' },

                            { name: 'Property Type', value: 'Dwellingtype' },
                            { name: 'Ownership', value: 'Homeownerprobabilitymodel' },
                            { name: 'Length Of Residence', value: 'Lengthofresidence' },
                            { name: 'Number Of Children', value: 'Numberofchildren' },

                            {name: 'Next Age', value: 'Personexactage'},
                            {name: 'Marital Status', value: 'Personmaritalstatus'},
                            {name: 'Inferred Age', value: 'Inferredage'},
                            {name: 'Hispanic Country Code', value: 'Hispaniccountrycode'},
                            {name: 'Single Parent', value: 'Singleparent'},
                            {name: 'Smoker', value: 'Smoker'},
                            {name: 'Presence Of Credit Card', value: 'Presenceofcreditcard'},
                            {name: 'Presence Of Gold Or Platinum Credit Card', value: 'Presenceofgoldorplatinumcreditcard'},
                            {name: 'Presence If Premium Card', value: 'Presenceofpremiumcreditcard'},
                            {name: 'Travel And Entertainment Card Holder', value: 'Travelandentertainmentcardholder'},
                            {name: 'Credit Card User', value: 'Creditcarduser'},
                            {name: 'New Credit Card Issue', value: 'Creditcardnewissue'},
                            {name: 'Swimming Pool', value: 'Homeswimmingpoolindicator'},
                            {name: 'Number Of Person Living In Unit', value: 'Numberofpersonsinlivingunit'},
                            {name: 'Presence Of Children', value: 'Presenceofchildren'},
                            {name: 'Inferred Household Rank', value: 'Inferredhouseholdrank'},
                            {name: 'Number Of Adults', value: 'Numberofadults'},
                            {name: 'Generations In Household', value: 'Generationsinhousehold'},
                            {name: 'Senior Adult In House Hold', value: 'Senioradultinhousehold'},
                            //{name: 'Air Conditioning', value: 'Airconditioning'},
                            //{name: 'Home Hear', value: 'Homeheatindicator'},
                            {name: 'Sewer', value: 'Sewer'},
                            {name: 'Water', value: 'Water'},
                            {name: 'News And Financial', value: 'Newsandfinancial'},
                            {name: 'Automotive Buff', value: 'Automotivebuff'},
                            {name: 'Book Reader', value: 'Bookreader'},
                            {name: 'Computer Owner', value: 'Computerowner'},
                            {name: 'Cooking Enthusiast', value: 'Cookingenthusiast'},
                            {name: 'Do It Yourselfer', value: 'Do_it_yourselfers'},
                            {name: 'Exercise Enthusiast', value: 'Exerciseenthusiast'},
                            {name: 'Gardener', value: 'Gardener'},
                            {name: 'Golf Enthusiast', value: 'Golfenthusiasts'},
                            {name: 'Home Decorating Enthusiast', value: 'Homedecoratingenthusiast'},
                            {name: 'Outdoor Enthusiast', value: 'Outdoorenthusiast'},
                            {name: 'Outdoor Sports Lover', value: 'Outdoorsportslover'},
                            {name: 'Photography', value: 'Photography'},
                            {name: 'Traveler', value: 'Traveler'},
                            {name: 'Pets', value: 'Pets'},
                            {name: 'Cats', value: 'Cats'},
                            {name: 'Dogs', value: 'Dogs'},
                            {name: 'Equestrian', value: 'Equestrian'},
                            {name: 'Reading Science Function', value: 'Readingsciencefiction'},
                            {name: 'Reading Audio Books', value: 'Readingaudiobooks'},
                            {name: 'History Military', value: 'Historymilitary'},
                            {name: 'Current Affairs Politics', value: 'Currentaffairspolitics'},
                            {name: 'Science Space', value: 'Sciencespace'},
                            {name: 'Gaming', value: 'Gaming'},
                            {name: 'Games/Video Games', value: 'Gamesvideogames'},
                            {name: 'Arts', value: 'Arts'},
                            {name: 'Games/computer Games', value: 'Gamescomputergames'},
                            {name: 'Movie/Music Grouping', value: 'Moviemusicgrouping'},
                            {name: 'Musical Instruments', value: 'Musicalinstruments'},
                            {name: 'Collectibles Stamps', value: 'Collectiblesstamps'},
                            {name: 'Collectibles Coins', value: 'Collectiblescoins'},
                            {name: 'Collectibles Arts', value: 'Collectiblesarts'},
                            {name: 'Collectibles Antiques', value: 'Collectiblesantiques'},
                            {name: 'Collectibles Sports Memorabilia', value: 'Collectiblessportsmemorabilia'},
                            {name: 'Military Memorabilia Weaponry', value: 'Militarymemorabiliaweaponry'},
                            {name: 'Auto Work', value: 'Autowork'},
                            {name: 'Wood Working', value: 'Woodworking'},
                            {name: 'Aviation', value: 'Aviation'},
                            {name: 'House Plants', value: 'Houseplants'},
                            {name: 'Home And Garden', value: 'Homeandgarden'},
                            {name: 'Home Improvement Grouping', value: 'Homeimprovementgrouping'},
                            {name: 'Photography And Video Equipment', value: 'Photographyandvideoequipment'},
                            {name: 'Home Furnishings Decorating', value: 'Homefurnishingsdecorating'},
                            {name: 'Home Improvement', value: 'Homeimprovement'},
                            {name: 'Food/Wines', value: 'Foodwines'},
                            {name: 'Cooking General', value: 'Cookinggeneral'},
                            {name: 'Cooking Gourmet', value: 'Cookinggourmet'},
                            {name: 'Foods Natural', value: 'Foodsnatural'},
                            {name: 'Cooking Food Grouping', value: 'Cookingfoodgrouping'},
                            {name: 'Gaming Casino', value: 'Gamingcasino'},
                            {name: 'Upscale Living', value: 'Upscaleliving'},
                            {name: 'Cultural Artistic Living', value: 'Culturalartisticliving'},
                            {name: 'High-Tech Living', value: 'Hightechliving'},
                            {name: 'Exercise Health Grouping', value: 'Exercisehealthgrouping'},
                            {name: 'Exercise Running Jogging', value: 'Exerciserunningjogging'},
                            {name: 'Exercise Walking', value: 'Exercisewalking'},
                            {name: 'Exercise Aerobic', value: 'Exerciseaerobic'},
                            {name: 'Spectator Sports/TV Sports', value: 'Spectatorsportstvsports'},
                            {name: 'Spectator Sports Football', value: 'Spectatorsportsfootball'},
                            {name: 'Spectator Sports Baseball', value: 'Spectatorsportsbaseball'},
                            {name: 'Spectator Sports Basketball', value: 'Spectatorsportsbasketball'},
                            {name: 'Spectator Sports Hockey', value: 'Spectatorsportshockey'},
                            {name: 'Spectator Sports Soccer', value: 'Spectatorsportssoccer'},
                            {name: 'Tennis', value: 'Tennis'},
                            {name: 'Snow Skiing', value: 'Snowskiing'},
                            {name: 'Motorcycling', value: 'Motorcycling'},
                            {name: 'Nascar', value: 'Nascar'},
                            {name: 'Boating Sailing', value: 'Boatingsailing'},
                            {name: 'Scuba Diving', value: 'Scubadiving'},
                            {name: 'Sports And Leisure', value: 'Sportsandleisure'},
                            {name: 'Hunting', value: 'Hunting'},
                            {name: 'Fishing', value: 'Fishing'},
                            {name: 'Camping Hiking', value: 'Campinghiking'},
                            {name: 'Hunting/Shooting', value: 'Huntingshooting'},
                            {name: 'Sports Grouping', value: 'Sportsgrouping'},
                            {name: 'Outdoors Grouping', value: 'Outdoorsgrouping'},
                            {name: 'Health Medical', value: 'Healthmedical'},
                            {name: 'Religious Contributor', value: 'Religiouscontributor'},
                            {name: 'Political Contributor', value: 'Politicalcontributor'},
                            {name: 'Charitable', value: 'Charitable'},
                            {name: 'Donates To Environmental Causes', value: 'Donatestoenvironmentalcauses'},
                            {name: 'Political Conservative Charitable Donation', value: 'Politicalconservativecharitabledonation'},
                            {name: 'Political Liberal Charitable Donation', value: 'Politicalliberalcharitabledonation'},
                            {name: 'Veterans Charitable Donation', value: 'Veteranscharitabledonation'},
                            {name: 'Occupation Group', value: 'Occupationgroup'},
                            {name: 'Person Occupation', value: 'Personoccupation'},
                            {name: 'Person Education', value: 'Personeducation'},
                            {name: 'Business Owner', value: 'Businessowner'},
                            {name: 'Opportunity Seeker', value: 'Opportunityseekers'},
                            {name: 'High-Tech Leader', value: 'Hightechleader'},
                            {name: 'Career Improvement', value: 'Careerimprovement'},
                            {name: 'Working Woman', value: 'Workingwoman'},
                            {name: 'African American Professionals', value: 'Africanamericanprofessionals'},
                            {name: 'Career', value: 'Career'},
                            {name: 'Education Online', value: 'Educationonline'},
                            {name: 'Computing/Home Office General', value: 'Computinghomeofficegeneral'},
                            {name: 'Electronics/Computing And Home Office', value: 'Electronicscomputingandhomeoffice'},
                            {name: 'Telecommunications', value: 'Telecommunications'},
                            {name: 'Self Improvement', value: 'Selfimprovement'},
                            {name: 'Value Hunter', value: 'valuehunter'},
                            {name: 'Mail Responder', value: 'Mailresponder'},
                            {name: 'Sweep Stakes', value: 'Sweepstakes'},
                            {name: 'Religious Magazine', value: 'Religiousmagazine'},
                            {name: 'Male Merch Buyer', value: 'Malemerchbuyer'},
                            {name: 'Female Merch Buyer', value: 'Femalemerchbuyer'},
                            {name: 'Crafts/Hobby Merch Buyer', value: 'Crafts_hobbmerchbuyer'},
                            {name: 'Gardering/Farming Buyer', value: 'Gardening_farmingbuyer'},
                            {name: 'Book Buyer', value: 'Bookbuyer'},
                            {name: 'Collect/Special Food Buyer', value: 'Collect_specialfoodsbuyer'},
                            {name: 'Mail Order Buyer', value: 'Mailorderbuyer'},
                            {name: 'Online Purchasing', value: 'Onlinepurchasingindicator'},
                            {name: 'Apparel Women', value: 'Apparelwomens'},
                            {name: 'Young Women Apparel', value: 'Youngwomensapparel'},
                            {name: 'Apparel Men', value: 'Apparelmens'},
                            {name: 'Apparel Men Big And Tall', value: 'Apparelmensbigandtall'},
                            {name: 'Young Men Apparel', value: 'Youngmensapparel'},
                            {name: 'Apparel Children', value: 'Apparelchildrens'},
                            {name: 'Health And Beauty', value: 'Healthandbeauty'},
                            {name: 'Beauty Cosmetics', value: 'Beautycosmetics'},
                            {name: 'Jewelry', value: 'Jewelry'},
                            {name: 'Luggage', value: 'Luggage'},
                            {name: 'TV Cable', value: 'Tvcable'},
                            {name: 'TV Satellite Dish', value: 'Tvsatellitedish'},
                            {name: 'High And Appliance', value: 'Highendappliances'},
                            {name: 'Consumers Electronics', value: 'Consumerelectronics'},
                            {name: 'Computers', value: 'Computers'},
                            {name: 'Electronics/Computers Grouping', value: 'Electronicscomputersgrouping'},
                            {name: 'Travel Grouping', value: 'Travelgrouping'},
                            {name: 'Travel', value: 'Travel'},
                            {name: 'Travel Domestic', value: 'Traveldomestic'},
                            {name: 'Travel International', value: 'Travelinternational'},
                            {name: 'Travel Cruise Vacations', value: 'Travelcruisevacations'},
                            {name: 'Dieting/Weight Loss', value: 'Dietingweightloss'},
                            {name: 'Automotive/Auto Parts And Accessories ', value: 'Automotiveautopartsandaccessories'},
                            {name: 'Estimated Income Code', value: 'Estimatedincomecode'},
                            {name: 'Investment', value: 'Investment'},
                            {name: 'Investment Stock Securities', value: 'Investmentstocksecurities'},
                            {name: 'Investing Active', value: 'Investing_active'},
                            {name: 'Investment Personal', value: 'Investmentspersonal'},
                            {name: 'Investments Real Estate ', value: 'Investmentsrealestate'},
                            {name: 'Investing Finance Grouping', value: 'Investingfinancegrouping'},
                            {name: 'Investments Foreign', value: 'Investmentsforeign'},
                            {name: 'Residential Properties Owned', value: 'Investmentestimatedresidentialpropertiesowned'}
                        ];
                    }
                    else if (type === 20) {
                        $scope.fields = [
                            {value: "dnc", name: "DNC Info"},
                            {value: "phoneType", name: "Phone Type"},
                            {name: 'First Name', value: 'firstName' },
                            {name: 'Last Name', value: 'lastName' },
                            {name: 'Address', value: 'address' },
                            {name: 'City', value: 'city' },
                            {name: 'State', value: 'state' },
                            {name: 'Zip4', value: 'zip4' },
                            {name: 'Zip5', value: 'zip5' },
                            {name: 'IP', value: 'ip'},
                            {name: 'Source', value: 'clean_source' },
                            {name: 'Datime', value: 'date' },
                            {name: 'Carrier', value: 'carrier'}
                        ];
                    }
                    else if (type === 21) {
                        $scope.fields = [
                            {value: "dnc", name: "DNC Info"},
                            {value: "phoneType", name: "Phone Type"},
                            {name: 'First Name', value: 'first_name' },
                            {name: 'Last Name', value: 'last_name' },
                            {name: 'Gender', value: 'gender' },
                            {name: 'City', value: 'city' },
                            {name: 'State', value: 'st' },
                            {name: 'status', value: 'status' },
                            {name: 'Job', value: 'job' },
                            {name: 'phone', value: 'phone' }
                        ];
                    }
                }

                $scope.selectedFields = {};
                $scope.selectedField = [];
                $scope.onSelectedField = function() {
                    $scope.selectedFields[$scope.selectedField] = true;
                    $scope.selectedField = undefined;
                }

                $scope.onSavedField = function() {
                    $scope.saveFields.push($scope.savedField);

                    $scope.selectedFields[$scope.savedField] = true;
                    $scope.savedField = undefined;
                }

                $scope.deselectField = function(item) {
                    $scope.selectedFields[item] = false;

                    if ($scope.saveFields.indexOf(item) != -1) {
                        $scope.saveFields.splice($scope.saveFields.indexOf(item), 1);
                    }
                }

                $scope.asArray = function(object) {
                    var result = [];
                    for (var prop in object) {
                        if (object[ prop ]) {
                            result.push(prop);
                        }
                    }

                    return result;
                }

                var updateMessage = function() {
                    dataService.message({userId: credentialsService.getUser().id}, function( response ) {
                        if ( response.message ) {
                            toasty.success( {
                                title: 'Getting count',
                                msg: response.message,
                                sound: false
                            } );

                            $scope.loading = true;
                        }
                    } );
                }

                updateMessage();
                $scope.interval = $interval( updateMessage, 10000 );
                $scope.$on( '$destroy', function() {
                    if ( $scope.interval ) $interval.cancel( $scope.interval );
                } );

                $scope.dataSources = []
                dataService.dataSources({userId: credentialsService.getUser().id}, function(response) {
                    $scope.dataSources = response.data;
                    if (response.data.length > 0) {
                        $scope.dataType = response.data[0].name;
                        initTables($scope.dataType);
                    }
                });

                $scope.dataTypeChanged = function() {
                    initTables( $scope.dataType );
                }

                var initTables = function( type ) {
                    var numType = $scope.$root.types[type];
                    updateFields();

                    administrationService.protected(credentialsService.getUser().id, credentialsService.getUser().password).tables({'type': numType}, function( response ) {
                        $scope.tableName = response.data[ 0 ];
                        $scope.tables = response.data;
                    } );
                };

                $scope.formattedAmount = function(value) {
                    return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                }

                $scope.params = {};
                $scope.columns = [];
                $scope.saveFields = [];
                $scope.headerFields = [];

                $scope.$watch('params.file', function() {
                    var input = document.getElementById('matching-upload-file');
                    var file = input.files[0];
                    if (file) {
                        var reader = new FileReader();
                        reader.onload = function(e) {
                            var text = reader.result;
                            var header = text.split('\n').shift();

                            var headerFields = [];
                            var parts = header.split(',');
                            for (var i = 0; i < parts.length; i++) {
                                headerFields.push(parts[i].trim());
                            }

                            $timeout(function() {
                                $scope.headerFields = headerFields;
                            }, 0)
                        }

                        reader.readAsText(file, 'UTF-8');
                    }
                });

                $scope.importFile = function() {
                    $scope.loading = true;
                    toasty.success( {
                        title: 'Uploading file',
                        msg: '',
                        sound: false
                    } );

                    $scope.columns = [];
                    var fields = $scope.asArray($scope.selectedFields);
                    for (var i = 0; i < fields.length; i++) {
                        var found = false;
                        for (var j = 0; j < $scope.fields.length; j++) {
                            if (fields[i] == $scope.fields[j].name && $scope.saveFields.indexOf(fields[i]) === -1) {
                                $scope.columns.push($scope.fields[j].value.toUpperCase());
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            $scope.columns.push(fields[i]);
                        }
                    }

                    if ($scope.columns.length === 0) {
                        $scope.loading = false;
                        toasty.error( {
                            title: 'Uploading file',
                            msg: 'Please select columns first',
                            sound: false
                        } );

                        return;
                    }

                    var fd = new FormData();
                    fd.append( 'file', $scope.params.file );
                    fd.append( 'columns', $scope.columns);
                   // fd.append( 'datatype', 1);
                    $http.post( BASE_URL + '/rest/public/lists/upload_tmp_file', fd, {
                        transformRequest: angular.identity,
                        headers: { 'Content-Type': undefined }
                    } ).success( function( response ) {
                        if(response == "{code:'uneven_fields'}"){
                            $scope.loading = false;
                            toasty.error( {
                                title: 'Uploading file',
                                msg: 'Please select the same amount of fields that the file has.',
                                sound: false
                            } );
                        }else{
                            updateLists();
                            console.log(response);
                            $scope.loading = false;
                        }

                    });



                }
                $scope.getCount = function() {

                    $scope.loading = true;
                    /*
                                        if (!$scope.params.file) {
                                            $scope.loading = false;
                                            toasty.error( {
                                                title: 'Uploading file',
                                                msg: 'Please select file first',
                                                sound: false
                                            } );

                                            return;
                                        }

                     */

                    $scope.columns = [];
                    var fields = $scope.asArray($scope.selectedFields);
                    for (var i = 0; i < fields.length; i++) {
                        var found = false;
                        for (var j = 0; j < $scope.fields.length; j++) {
                            if (fields[i] == $scope.fields[j].name && $scope.saveFields.indexOf(fields[i]) === -1) {
                                $scope.columns.push($scope.fields[j].value.toUpperCase());
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            $scope.columns.push(fields[i]);
                        }
                    }

                    if ($scope.columns.length === 0) {
                        $scope.loading = false;
                        toasty.error( {
                            title: 'Uploading file',
                            msg: 'Please select columns first',
                            sound: false
                        } );

                        return;
                    }

                    var fd = new FormData();
                    fd.append( 'file', $scope.params.file );

                    $scope.params.filePath = "";
                    //$scope.columns = response.data;

                    if($scope.tableArray.length == 1){
                        var request = {filePath: $scope.params.filePath,
                            tableName: $scope.tableName.name,
                            columns: $scope.columns,
                            filterDNC: $scope.config.filterDNC,
                            saveFields: asString($scope.saveFields),
                            selectedTable: $scope.tableArray[0],
                            userId: credentialsService.getUser().id};

                        dataService.matching(request, function(response) {
                            $scope.loading = false;
                            if (response.status == 'OK') {
                                $scope.count = response.data.count;
                            } else {
                                toasty.error( {
                                    title: 'Getting count',
                                    msg: 'Selected columns count, fields for saving or file data are incorrect for selected table',
                                    sound: false
                                } );
                            }
                        });
                    }else{
                        $scope.loading = false;
                        toasty.error( {
                            title: 'Getting count',
                            msg: 'Selected only 1 table',
                            sound: false
                        } );
                    }

                }

                $scope.getCount_count = function() {
                    $scope.loading = true;
/*
                    if (!$scope.params.file) {
                        $scope.loading = false;
                        toasty.error( {
                            title: 'Uploading file',
                            msg: 'Please select file first',
                            sound: false
                        } );

                        return;
                    }

 */

                    $scope.columns = [];
                    var fields = $scope.asArray($scope.selectedFields);
                    for (var i = 0; i < fields.length; i++) {
                        var found = false;
                        for (var j = 0; j < $scope.fields.length; j++) {
                            if (fields[i] == $scope.fields[j].name && $scope.saveFields.indexOf(fields[i]) === -1) {
                                $scope.columns.push($scope.fields[j].value.toUpperCase());
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            $scope.columns.push(fields[i]);
                        }
                    }

                    if ($scope.columns.length === 0) {
                        $scope.loading = false;
                        toasty.error( {
                            title: 'Uploading file',
                            msg: 'Please select columns first',
                            sound: false
                        } );

                        return;
                    }

                    var fd = new FormData();
                    fd.append( 'file', $scope.params.file );

                    $http.post( BASE_URL + '/rest/public/lists/uploaded/file', fd, {
                        transformRequest: angular.identity,
                        headers: { 'Content-Type': undefined }
                    } ).success( function( response ) {
                        if ( response.status === 'OK' ) {
                            $scope.params.filePath = response.message;
                            //$scope.columns = response.data;

                            var request = {filePath: $scope.params.filePath,
                                tableName: $scope.tableName.name,
                                columns: $scope.columns,
                                filterDNC: $scope.config.filterDNC,
                                saveFields: asString($scope.saveFields),
                                selectedTable: "test_TCvxhPVzhaKnkIJBZSXu",
                                userId: credentialsService.getUser().id};

                            dataService.matching(request, function(response) {
                                $scope.loading = false;
                                if (response.status == 'OK') {
                                    $scope.count = response.data.count;
                                } else {
                                    toasty.error( {
                                        title: 'Getting count',
                                        msg: 'Selected columns count, fields for saving or file data are incorrect for selected table',
                                        sound: false
                                    } );
                                }
                            });
                        } else {
                            $scope.loading = false;
                            toasty.error( {
                                title: 'Uploading file',
                                msg: 'Uploaded file has wrong format',
                                sound: false
                            } );
                        }
                    } );
                }

                $scope.reset = function() {
                    dataService.reset({userId: credentialsService.getUser().id}, function(response) {
                        if (response.status === 'OK') {
                            $scope.loading = false;
                            $scope.count = 0;
                            $scope.config.filterDNC = false;
                        }
                    });
                }

                $scope.saveList = function() {
                    var modalInstance = $modal.open( {
                        templateUrl: BASE_URL + '/assets/partials/modal/save.list.html',
                        controller: 'SaveListModalController',
                    } );

                    modalInstance.result.then( function( name ) {
                        var type = $scope.$root.types[$scope.dataType];

                        var request = { 'name': name,
                            userId: credentialsService.getUser().id,
                            cnt: $scope.count,
                            date: new Date().getTime(),
                            'type': type,
                            tableName: $scope.tableName.name,
                            filePath: $scope.params.filePath,
                            columns: $scope.columns,
                            filterDNC: $scope.config.filterDNC,
                            savedColumns: asString($scope.saveFields),
                            request: '{}' };

                        toasty.info( {
                            title: localization.localize( 'lists.saving.in.progress' ),
                            msg: localization.localize( 'lists.saving.in.progress.message' ),
                            timeout: 10000,
                            sound: false
                        } );

                        dataService.saveMatchingList( request, function( response ) {
                            if ( response.status === 'OK' ) {
                                toasty.success( {
                                    title: localization.localize( 'lists.saved.successfully' ),
                                    msg: localization.localize( 'lists.saved.successfully.message' ),
                                    timeout: 10000,
                                    sound: false
                                });
                            }
                        });
                    });
                }

                var asString = function(array) {
                    var result = "";
                    for (var i = 0; i < array.length; i++) {
                        result = result + array[i];
                        if (i + 1 < array.length) {
                            result = result + ",";
                        }
                    }

                    return result;
                }

            } ] );

