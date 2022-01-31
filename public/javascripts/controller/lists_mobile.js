angular.module('consumer_data_base').
controller('MobileListsController',
['$scope', '$state', 'listService', 'credentialsService', 'confirm', 'toasty', '$state', '$interval', 'administrationService',
function($scope, $state, listService, credentialsService, confirm, toasty, $state, $interval, administrationService) {

    var fixHeight = function() {
        var elements = document.getElementsByClassName('main-container');
        for (var i = 0; i < elements.length; i++) {
            var element = elements[i];

            element.style.minHeight = (window.innerHeight - 194) + 'px';
        }
    }
    fixHeight();

    $scope.back = function() {
        $state.transitionTo('mobile');
    }

    $scope.hideDownloadButton = function(list) {
        if (list.userId == credentialsService.getUser().id) {
            return false;
        }

        if ($scope.isReseller()) {
            return true;
        }

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

    var updateLists = function() {
        var handler = function(response) {
            if (response.status === 'OK') {
                $scope.lists = response.data.lists;
                $scope.total = response.data.total;
            }
        };

        listService.getPagedPurchased($scope.config, handler);
    }
    updateLists();

    $scope.removeList = function(list) {
        confirm.getUserConfirmation('Are you sure you want to delete this list?', function() {
            listService.deleteList({ id: list.id }, function(response) {
                if (response.status === 'OK') {
                    toasty.success({
                        title: 'Deleted successfully',
                        msg: 'List has been deleted successfully',
                        sound: false
                    });

                    updateLists();
                }
            })
        });
    }

    $scope.sortByField = function(field) {
        if (field === $scope.config.sortValue) {
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
        if (listId) {
            administrationService.protected().preparedItemsCount({ id: listId },
            function(response) {
                if (response.data != undefined) {
                    if (response.data === 0) {
                        toasty.success({
                            limit: 1,
                            title: 'Writing data in progress',
                            msg: 'Preparing data',
                            sound: false,
                            timeout: 15000,
                        });
                    } else {
                        toasty.success({
                            limit: 1,
                            title: 'Writing data in progress',
                            msg: response.data + ' records has been written to file',
                            sound: false,
                            timeout: 15000,
                        });
                    }
                }
            });
        }
    }

    $scope.$on('$destroy', function() {
        if ($scope.interval) $interval.cancel($scope.interval);
    });

    $scope.downloadList = function(list) {
        updateFields(list);
        var columns = [];

        for (var i = 0; i < $scope.requiredFields.length; i++) {
            columns.push($scope.requiredFields[i].value);
        }

        var code = '';

        listId = list.id;
        updateMessage();
        $scope.interval = $interval(updateMessage, 10000);

        listService.prepareEmail({ listId: list.id,
                                   userId: credentialsService.getUser().id,
                                   'columns': columns,
                                   'code': code },
        function(response) {
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

    $scope.requiredFields = [];
    $scope.optionalFields = [];

    var updateFields = function(list) {
        var type = list.type;
        if (type === 0) {
            $scope.requiredFields = [
                { name: 'first name', value: 'FN' },
                { name: 'last name', value: 'LN' },
                { name: 'address', value: 'ADDR' },
                { name: 'apt', value: 'APT' },
                { name: 'city', value: 'CITY' },
                { name: 'state', value: 'ST' },
                { name: 'zip', value: 'ZIP' },
                { name: 'phone', value: 'PHONE' },
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
        } else if (type === 1) {
            $scope.requiredFields = [
                { name: 'company name', value: 'COMPANY_NAME' },
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
                { name: 'Contact Name', value: 'CONTACT_NAME' },
                { name: 'Contact Gender', value: 'GENDER' },
                { name: 'Latitude', value: 'LATITUDE' },
                { name: 'Longitude', value: 'LONGITUDE' },
                { name: 'SIC Code', value: 'SIC_CODE' },
                { name: 'Title', value: 'TITLE' },
                { name: 'Website', value: 'WWW' },
                { name: 'Industry', value: 'INDUSTRY' },
                { name: 'Employee', value: 'EMPLOYEE' },
                { name: 'Annual Sales', value: 'ANNUAL_SALES' }
           ];
        } else if (type === 2){
            $scope.requiredFields = [
                { name: 'company name', value: 'COMPANY_NAME' },
                { name: 'address', value: 'ADDRESS' },
                { name: 'city', value: 'CITY' },
                { name: 'state', value: 'STATE' },
                { name: 'zip', value: 'ZIP' },
                { name: 'phone', value: 'PHONE' },
                { name: 'phone type', value: 'phoneType' },
                { name: 'DNC Info', value: 'dnc' },
                { name: 'County', value: 'COUNTY' },
                { name: 'Area Code', value: 'AREA_CODE' },
                { name: 'Source', value: 'WWW' },
                { name: 'Industry', value: 'INDUSTRY' },
                { name: 'Contact Name', value: 'contact_name' },
                { name: 'Website', value: 'websites' }
           ];
        } else if (type === 3) {
            $scope.requiredFields = [
                { name: 'state', value: 'STATE' },
                { name: 'industry', value: 'INDUSTRY' },
                { name: 'phone', value: 'PHONE' },
                { name: 'phone type', value: 'phoneType' },
                { name: 'DNC Info', value: 'dnc' },
                { name: 'Website', value: 'website' },
                { name: 'Area Code', value: 'AREA_CODE' }
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
                { name: 'phone type', value: 'phoneType' },
                { name: 'DNC Info', value: 'dnc' },
                { name: 'Area Code', value: 'AREA_CODE' }
           ];
        } else if (type === 7 || type === 14) {
            $scope.requiredFields = [
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
                {name: 'Area Code', value: 'areaCode'}
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
                {name: 'phone type', value: 'phoneType'},
                {name: 'DNC Info', value: 'dnc' },
                {name: 'Source', value: 'source'},
                {name: 'Gender', value: 'genderCode'},
                {name: 'Date of Birth', value: 'dobDate'},
                {name: 'County', value: 'county'},
                {name: 'City', value: 'city'},
                {name: 'IP', value: 'ip'},
                {name: 'Area Code', value: 'areaCode'}
           ];
        } else if (type === 10) {
            $scope.requiredFields = [
                {name: 'first name', value: 'first_name'},
                {name: 'last name', value: 'last_name'},
                {name: 'address', value: 'address'},
                {name: 'state', value: 'state'},
                {name: 'zip code', value: 'zip_code'},
                {name: 'phone', value: 'phone'},
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
                {name: 'phone', value: 'phone'},
                {name: 'phone type', value: 'phoneType'},
                {name: 'DNC Info', value: 'dnc' },
                {name: 'Area Code', value: 'areaCode'}
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
                                                        {name: 'Datime', value: 'date' },
                                                        {value: "carrier", name: "Carrier"},
                                                        {value: "ip", name: "IP"},
                                                    ];
                    }else if (type === 6 || type === 9) {
            $scope.requiredFields = [
                { name: 'first name', value: 'PERSONFIRSTNAME' },
                { name: 'last name', value: 'PERSONLASTNAME' },
                { name: 'address', value: 'PRIMARYADDRESS' },
                { name: 'apt', value: 'SECONDARYADDRESS' },
                { name: 'city', value: 'CITYNAME' },
                { name: 'state', value: 'STATE' },
                { name: 'zip', value: 'ZIPCODE' },
                { name: 'phone', value: 'PHONE' },
                { name: 'phone type', value: 'phoneType' },
                { name: 'DNC Info', value: 'dnc' },
                { name: 'County', value: 'Countyname' },
                { name: 'Email', value: 'email' },
                { name: 'Area Code', value: 'Areacode' },
                { name: 'Date Of Birth', value: 'Persondateofbirthdate' },
                { name: 'Gender', value: 'Persongender' },
                { name: 'Education', value: 'Personeducation' },

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
                //{name: 'Home Heat', value: 'Homeheatindicator'},
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
                {name: 'Residential Properties Owned', value: 'Investmentestimatedresidentialpropertiesowned'},

                {value: "homePurchasePrice", name: "Home Purchase Price"},
                {value: "homePurchaseDate", name: "Home Purchased Date"},
                {value: "homeYearBuilt", name: "Home Year Built"},
                {value: "estimatedCurrentHomeValueCode", name: "Estimated Home Value"},
                //{value: "mortgageAmountInThousands", name: "Mortgage Amount In Thousands"},
                {value: "mortgageLenderName", name: "Mortgage Lender Name"},
                {value: "mortgageRate", name: "Mortgage Rate"},
                {value: "mortgageRateType", name: "Mortgage Rate Type"},
                {value: "mortgageLoanType", name: "Mortgage Loan Type"},
                {value: "transactionType", name: "Transaction Type"},
                {value: "deedDateOfRefinance", name: "Deed Date Of Refinance"},
                //{value: "refinanceAmountInThousands", name: "Refinance Amount In Thousands"},
                {value: "refinanceLeaderName", name: "Refinance Lender Name"},
                {value: "refinanceRateType", name: "Refinance Rate Type"},
                {value: "refinanceLoanType", name: "Refinance Loan Type"},
                //{value: "censusMedianHomeValue", name: "Census Median Home Value"},
                //{value: "censusMedianHouseHoldIncome", name: "Census Median House Hold Income"},
                {value: "craIncomeClassificationCode", name: "CRA Income Classification Code"},
                {value: "purchaseMortgageDate", name: "Purchase Mortgage Date"},
                {value: "mostRecentLenderCode", name: "Most Recent Lender Code"},
                //{value: "purchaseLenderName", name: "Purchase Lender Name"},
                {value: "mostRecentMortgageInterestRate", name: "Most Recent Mortgage Interest Rate"},
                {value: "loanToValue", name: "Loan To Value"},

                {value: "ChildrenAge00_02", name: "Children Age: 0-2 years"},
                {value: "ChildrenAge00_02FEMALE", name: "Children Age: 0-2 years, female"},
                {value: "ChildrenAge00_02MALE", name: "Children Age: 0-2 years, male"},
                {value: "ChildrenAge03_05", name: "Children Age: 3-5 years"},
                {value: "ChildrenAge03_05FEMALE", name: "Children Age: 3-5 years, female",},
                {value: "ChildrenAge03_05MALE", name: "Children Age: 3-5 years, male",},
                {value: "ChildrenAge06_10", name: "Children Age: 6-10 years"},
                {value: "ChildrenAge06_10FEMALE", name: "Children Age: 6-10 years, female"},
                {value: "ChildrenAge06_10MALE", name: "Children Age: 6-10 years, male"},
                {value: "ChildrenAge11_15", name: "Children Age: 11-15 years"},
                {value: "ChildrenAge11_15FEMALE", name: "Children Age: 11-15 years, female"},
                {value: "ChildrenAge11_15MALE", name: "Children Age: 11-15 years, male"},
                {value: "ChildrenAge16_17", name: "Children Age: 16-17 years"},
                {value: "ChildrenAge16_17FEMALE", name: "Children Age: 16-17 years, female"},
                {value: "ChildrenAge16_17MALE", name: "Children Age: 16-17 years, male"},
                {value: "DPV_CODE", name: "Delivery Point Verification Code"},
                {value: "NUMBEROFSOURCES", name: "Number Of Sources"}

           ];
        }
    }
}]);
