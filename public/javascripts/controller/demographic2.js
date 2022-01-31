angular.module('consumer_data_base').
controller('C2PersonController',
['$scope', 'localization',
function($scope, localization) {
    $scope.searchConfig = {option: "gender", search: "", limit: "", show: false};
    $scope.loadMore = function() {
        $scope.searchConfig.limit = $scope.searchConfig.limit + 30;
    }

    $scope.tabs = [
        {value: "gender", title: "Gender"},
        {value: "age", title: "Age"},
        {value: "dob", title: "Date Of Birth"},
        {value: "maritalStatus", title: "Marital Status"},
        {value: "ethnicCode", title: "Ethnic Code"},
        {value: "languageCode", title: "Language Code"},
        //{value: "ethnicGroup", title: "Ethnic Group"},
        {value: "religionCode", title: "Religion Code"},
        {value: "hispanicCountryCode", title: "Hispanic Country Code"},
        {value: "singleParent", title: "Single Parent"},
        {value: "smoker", title: "Smoker"}
   ];

    $scope.$watch('searchConfig.option', function() {
        $scope.searchConfig.search = "";
        $scope.searchConfig.limit = 30;

        if ($scope.searchConfig.option == "ethnicCode" || $scope.searchConfig.option == "languageCode" ||
            $scope.searchConfig.option == "ethnicGroup" || $scope.searchConfig.option == "religionCode" ||
            $scope.searchConfig.option == "hispanicCountryCode") {
            $scope.searchConfig.show = true;
        } else {
            $scope.searchConfig.show = false;
        }
    });

    $scope.genders = [
        {title: "Male", value: "M"},
        {title: "Female", value: "F"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.genders) {
        for (var i = 0; i < $scope.genders.length; i++) {
            for (var j = 0; j < $scope.searchRequest.genders.length; j++) {
                if ($scope.genders[i].value == $scope.searchRequest.genders[j]) {
                    var gender = $scope.genders[i];
                    $scope.selectedGenders[gender.title + "|" + gender.value] = true;
                    break;
                }
            }
        }
    }

    $scope.maritalStatuses = [
        //{value: "A", title: "Inferred Married"},
        //{value: "B", title: "Inferred Single"},
        {value: "M", title: "Married"},
        {value: "S", title: "Single"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.maritalStatuses) {
        for (var i = 0; i < $scope.maritalStatuses.length; i++) {
            for (var j = 0; j < $scope.searchRequest.maritalStatuses.length; j++) {
                if ($scope.maritalStatuses[i].value == $scope.searchRequest.maritalStatuses[j]) {
                    var status = $scope.maritalStatuses[i];
                    $scope.selectedMaritalStatuses[status.title + "|" + status.value] = true;
                    break;
                }
            }
        }
    }

    $scope.ages = {};
    $scope.onStartChanged = function() {
        var value = $scope.ages.rangeStart;
        for (var prop in $scope.agesRange) {
            if (prop.indexOf("From") != -1) {
                $scope.agesRange[prop] = false;
            }
        }

        if (value == '') {
            $scope.agesRange["From: " + value + "|" + value] = false;
        } else {
            $scope.agesRange["From: " + value + "|" + value] = true;
        }
    }

    $scope.addDOB = function() {
        for (var p in $scope.dobs) {
            $scope.dobs[p] = undefined;
        }

        var title = document.getElementById('personDobFrom').value;
        if (title) {
            $scope.dobs[title + "|" + title] = title;
        }

        title = document.getElementById('personDobTo').value;
        if (title) {
            $scope.dobs[title + "|" + title] = title;
        }

        setTimeout(function() {
            document.getElementById('personDobFrom').blur();
            document.getElementById('personDobTo').blur();
        }, 200);
    }

    if ($scope.searchRequest && $scope.searchRequest.dobs) {
        if ($scope.searchRequest.dobs.length > 0) {
            var title = $scope.searchRequest.dobs[0];
            $scope.dobs[title + "|" + title] = title;
        }

        if ($scope.searchRequest.dobs.length > 1) {
            var title = $scope.searchRequest.dobs[1];
            $scope.dobs[title + "|" + title] = title;
        }
    }

    $scope.clearDOB = function() {
        $scope.ages.dobFrom = undefined;
        $scope.ages.dobTo = undefined;
    }

    $scope.onEndChanged = function() {
        var value = $scope.ages.rangeEnd;
        for (var prop in $scope.agesRange) {
            if (prop.indexOf("To") != -1) {
                $scope.agesRange[prop] = false;
            }
        }

        if (value == '') {
            $scope.agesRange["To: " + value + "|" + value] = false;
        } else {
            $scope.agesRange["To: " + value + "|" + value] = true;
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.agesRange) {
        if ($scope.searchRequest.agesRange[0]) {
            var value = $scope.searchRequest.agesRange[0];
            $scope.agesRange["From: " + value + "|" + value] = true;
        }

        if ($scope.searchRequest.agesRange[1]) {
            var value = $scope.searchRequest.agesRange[1];
            $scope.agesRange["To: " + value + "|" + value] = true;
        }
    }

    $scope.ethnicCodes = [
        {"title":"Afghani", "value":"C1"},
        {"title":"African American 1", "value":"M0"},
        {"title":"African American 2", "value":"WL"},
        {"title":"Albanian", "value":"U0"},
        {"title":"Aleut", "value":"R1"},
        {"title":"Algerian", "value":"D0"},
        {"title":"Angolan", "value":"M1"},
        {"title":"Arab", "value":"D1"},
        {"title":"Armenian", "value":"U1"},
        {"title":"Ashanti", "value":"M2"},
        {"title":"Australian", "value":"RP"},
        {"title":"Austrian", "value":"U2"},
        {"title":"Azerb", "value":"U3"},
        {"title":"Bahrain", "value":"D2"},
        {"title":"Bangladesh", "value":"C2"},
        {"title":"Basotho", "value":"M3"},
        {"title":"Basque", "value":"T2"},
        {"title":"Belgian", "value":"T1"},
        {"title":"Benin", "value":"M4"},
        {"title":"Bhutanese", "value":"M5"},
        {"title":"Bosnian", "value":"U4"},
        {"title":"Botswanian", "value":"WM"},
        {"title":"Bulgarian", "value":"U5"},
        {"title":"Burkina", "value":"Faso", "value":"M6"},
        {"title":"Burundi", "value":"M7"},
        {"title":"Byelorussian/Belorussian", "value":"U6"},
        {"title":"Cameroon", "value":"M8"},
        {"title":"Caribbean African American", "value":"WP"},
        {"title":"Cent Afric Rep", "value":"M9"},
        {"title":"Chad", "value":"MA"},
        {"title":"Chechnian", "value":"U7"},
        {"title":"Chinese", "value":"R3"},
        {"title":"Comoros", "value":"MB"},
        {"title":"Congo", "value":"MC"},
        {"title":"Croatian", "value":"U8"},
        {"title":"Czech", "value":"U9"},
        {"title":"Danish", "value":"N1"},
        {"title":"Djibouti", "value":"WE"},
        {"title":"Dutch", "value":"N2"},
        {"title":"Egyptian", "value":"D3"},
        {"title":"English", "value":"T3"},
        {"title":"Equat", "value":"Guinea", "value":"MD"},
        {"title":"Estonian", "value":"UA"},
        {"title":"Ethiopian", "value":"ME"},
        {"title":"Fiji", "value":"R4"},
        {"title":"Filipino/Philippines", "value":"RE"},
        {"title":"Finnish", "value":"N3"},
        {"title":"French", "value":"T4"},
        {"title":"Gabon", "value":"MF"},
        {"title":"Gambia", "value":"MG"},
        {"title":"Georgian", "value":"UB"},
        {"title":"German", "value":"T5"},
        {"title":"Ghana", "value":"MH"},
        {"title":"Greek", "value":"D4"},
        {"title":"Guinea-Bissau", "value":"MJ"},
        {"title":"Guinean", "value":"WF"},
        {"title":"Guyana", "value":"MK"},
        {"title":"Hausa", "value":"WN"},
        {"title":"Hawaiian", "value":"R5"},
        {"title":"Hispanic", "value":"T9"},
        {"title":"Hungarian", "value":"UC"},
        {"title":"Icelandic", "value":"N4"},
        {"title":"Indian", "value":"C3"},
        {"title":"Indonesian", "value":"R6"},
        {"title":"Iraqi", "value":"D5"},
        {"title":"Irish", "value":"T6"},
        {"title":"Italian", "value":"T7"},
        {"title":"Ivory Coast", "value":"ML"},
        {"title":"Japanese", "value":"R7"},
        {"title":"Jewish", "value":"D7"},
        {"title":"Kazakh", "value":"UD"},
        {"title":"Kenya", "value":"MM"},
        {"title":"Khmer/Cambodia/Kampuchea", "value":"R8"},
        {"title":"Kirghiz", "value":"UE"},
        {"title":"Korean", "value":"R9"},
        {"title":"Kurdish", "value":"D6"},
        {"title":"Kuwaiti", "value":"D8"},
        {"title":"Kyrgyzstani", "value":"UF"},
        {"title":"Laotian", "value":"RA"},
        {"title":"Latvian", "value":"UG"},
        {"title":"Lesotho", "value":"MN"},
        {"title":"Liberian", "value":"MO"},
        {"title":"Libyan", "value":"D9"},
        {"title":"Liechtenstein", "value":"TE"},
        {"title":"Lithuanian", "value":"UH"},
        {"title":"Luxembourgian", "value":"TF"},
        {"title":"Macedonian", "value":"DE"},
        {"title":"Madagascar", "value":"MP"},
        {"title":"Malawi", "value":"MQ"},
        {"title":"Malay", "value":"RB"},
        {"title":"Maldivian", "value":"RJ"},
        {"title":"Mali", "value":"MR"},
        {"title":"Maltese", "value":"DS"},
        {"title":"Manx", "value":"TJ"},
        {"title":"Mauritania", "value":"WG"},
        {"title":"Moldavian", "value":"UI"},
        {"title":"Mongolian", "value":"RC"},
        {"title":"Moroccan", "value":"DF"},
        {"title":"Mozambique", "value":"MU"},
        {"title":"Multi-Ethnic", "value":"ZZ"},
        {"title":"Myanmar", "value":"R2"},
        {"title":"Namibian", "value":"MS"},
        {"title":"Native American", "value":"KS"},
        {"title":"Nauruan", "value":"RK"},
        {"title":"Nepal", "value":"C6"},
        {"title":"New Zealand", "value":"RM"},
        {"title":"Niger", "value":"WH"},
        {"title":"Nigerian", "value":"MT"},
        {"title":"Norwegian", "value":"N5"},
        {"title":"Other Asian", "value":"RD"},
        {"title":"Pakistani", "value":"C4"},
        {"title":"Papua New Guinea", "value":"MV"},
        {"title":"Persian", "value":"DH"},
        {"title":"Pili", "value":"RS"},
        {"title":"Polish", "value":"UJ"},
        {"title":"Portuguese", "value":"T8"},
        {"title":"Qatar", "value":"DG"},
        {"title":"Romanian", "value":"UK"},
        {"title":"Ruandan", "value":"MW"},
        {"title":"Russian", "value":"UL"},
        {"title":"Saudi", "value":"DJ"},
        {"title":"Scottish", "value":"N6"},
        {"title":"Senegalese", "value":"MX"},
        {"title":"Serbian", "value":"UM"},
        {"title":"Seychelles", "value":"WJ"},
        {"title":"Sierre Leone", "value":"MY"},
        {"title":"Slovakian", "value":"UN"},
        {"title":"Slovenian", "value":"UP"},
        {"title":"Somalia", "value":"MZ"},
        {"title":"South African", "value":"W0"},
        {"title":"Sri Lankan", "value":"C5"},
        {"title":"Sudanese", "value":"W2"},
        {"title":"Surinam", "value":"W1"},
        {"title":"Swahili", "value":"WS"},
        {"title":"Swaziland", "value":"W3"},
        {"title":"Swedish", "value":"N7"},
        {"title":"Swiss", "value":"TH"},
        {"title":"Syrian", "value":"DK"},
        {"title":"Tajik", "value":"UR"},
        {"title":"Tajikistan", "value":"UQ"},
        {"title":"Tanzanian", "value":"W4"},
        {"title":"Telugan", "value":"C7"},
        {"title":"Thai", "value":"RF"},
        {"title":"Tibetan", "value":"RG"},
        {"title":"Togo", "value":"W5"},
        {"title":"Tonga", "value":"W6"},
        {"title":"Tunisian", "value":"DL"},
        {"title":"Turkish", "value":"DM"},
        {"title":"Turkmenistan", "value":"UT"},
        {"title":"Ugandan", "value":"W7"},
        {"title":"Ukrainian", "value":"UV"},
        {"title":"Uzbekistani", "value":"UW"},
        {"title":"Vanuatuan", "value":"RQ"},
        {"title":"Vietnamese", "value":"RH"},
        {"title":"Welsh", "value":"N8"},
        {"title":"Western Samoa", "value":"WK"},
        {"title":"Xhosa", "value":"W8"},
        {"title":"Yemeni", "value":"DN"},
        {"title":"Zaire", "value":"W9"},
        {"title":"Zambian", "value":"WA"},
        {"title":"Zimbabwe", "value":"WB"},
        {"title":"Zulu", "value":"WC"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.ethnicCodes) {
        for (var i = 0; i < $scope.ethnicCodes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.ethnicCodes.length; j++) {
                if ($scope.ethnicCodes[i].value == $scope.searchRequest.ethnicCodes[j]) {
                    var code = $scope.ethnicCodes[i];
                    $scope.selectedEthnicCodes[code.title + "|" + code.value] = true;
                    break;
                }
            }
        }
    }

    $scope.languageCodes = [
        {"title":"Afrikaans", "value":"A1"},
        {"title":"Albanian", "value":"A2"},
        {"title":"Amharic", "value":"A3"},
        {"title":"Arabic", "value":"A4"},
        {"title":"Armenian", "value":"A5"},
        {"title":"Ashanti", "value":"A6"},
        {"title":"Azeri", "value":"A7"},
        {"title":"Bantu", "value":"B1"},
        {"title":"Basque", "value":"B2"},
        {"title":"Bengali", "value":"B3"},
        {"title":"Bulgarian", "value":"B4"},
        {"title":"Burmese", "value":"B5"},
        {"title":"Chinese", "value":"C1"},
        {"title":"Comorian", "value":"C2"},
        {"title":"Czech", "value":"C3"},
        {"title":"Danish", "value":"D1"},
        {"title":"Dutch", "value":"D2"},
        {"title":"Dzongha", "value":"D3"},
        {"title":"English", "value":"E1"},
        {"title":"Estonian", "value":"E2"},
        {"title":"Farsi", "value":"F1"},
        {"title":"Finnish", "value":"F2"},
        {"title":"French", "value":"F3"},
        {"title":"Georgian", "value":"G1"},
        {"title":"German", "value":"G2"},
        {"title":"Ga", "value":"G3"},
        {"title":"Greek", "value":"G4"},
        {"title":"Hausa", "value":"H1"},
        {"title":"Hebrew", "value":"H2"},
        {"title":"Hindi", "value":"H3"},
        {"title":"Hungarian", "value":"H4"},
        {"title":"Icelandic", "value":"I1"},
        {"title":"Indonesian", "value":"I2"},
        {"title":"Italian", "value":"I3"},
        {"title":"Japanese", "value":"J1"},
        {"title":"Kazakh", "value":"K1"},
        {"title":"Khmer", "value":"K2"},
        {"title":"Kirghiz", "value":"K3"},
        {"title":"Korean", "value":"K4"},
        {"title":"Laotian", "value":"L1"},
        {"title":"Latvian", "value":"L2"},
        {"title":"Lithuanian", "value":"L3"},
        {"title":"Macedonian", "value":"M1"},
        {"title":"Malagasy", "value":"M2"},
        {"title":"Malay", "value":"M3"},
        {"title":"Moldavian", "value":"M4"},
        {"title":"Mongolian", "value":"M5"},
        {"title":"Nepali", "value":"N1"},
        {"title":"Norwegian", "value":"N2"},
        {"title":"Oromo", "value":"O1"},
        {"title":"Pashto", "value":"P1"},
        {"title":"Polish", "value":"P2"},
        {"title":"Portuguese", "value":"P3"},
        {"title":"Romanian", "value":"R1"},
        {"title":"Russian", "value":"R2"},
        {"title":"Samoan", "value":"S1"},
        {"title":"Serbo-Croatian", "value":"S2"},
        {"title":"Sinhalese", "value":"S3"},
        {"title":"Slovakian", "value":"S4"},
        {"title":"Slovenian", "value":"S5"},
        {"title":"Somali", "value":"S6"},
        {"title":"Sotho", "value":"S7"},
        {"title":"Spanish", "value":"S8"},
        {"title":"Swahili", "value":"S9"},
        {"title":"Swazi", "value":"SA"},
        {"title":"Swedish", "value":"SB"},
        {"title":"Tagalog", "value":"T1"},
        {"title":"Tajik", "value":"T2"},
        {"title":"Thai", "value":"T3"},
        {"title":"Tibetan", "value":"T4"},
        {"title":"Tongan", "value":"T5"},
        {"title":"Turkish", "value":"T6"},
        {"title":"Turkmeni", "value":"T7"},
        {"title":"Tswana", "value":"T8"},
        {"title":"Urdu", "value":"U1"},
        {"title":"Uzbeki", "value":"U2"},
        {"title":"Vietnamese", "value":"V1"},
        {"title":"Xhosa", "value":"X1"},
        {"title":"Zulu", "value":"Z1"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.languageCodes) {
        for (var i = 0; i < $scope.languageCodes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.languageCodes.length; j++) {
                if ($scope.languageCodes[i].value == $scope.searchRequest.languageCodes[j]) {
                    var code = $scope.languageCodes[i];
                    $scope.selectedLanguageCodes[code.title + "|" + code.value] = true;
                    break;
                }
            }
        }
    }

    $scope.ethnicGroups = [];
    var groupsArray = ['F', 'C', 'E', 'O', 'Y', 'J', 'M', 'I', 'N', 'P', 'S', 'A', 'W'];
    for (var i = 0; i < groupsArray.length; i++) {
        $scope.ethnicGroups.push({
            value: groupsArray[i],
            title: localization.localize('ethnicity.group.' + groupsArray[i].toLowerCase())});
    }

    if ($scope.searchRequest && $scope.searchRequest.ethnicGroups) {
        for (var i = 0; i < $scope.ethnicGroups.length; i++) {
            for (var j = 0; j < $scope.searchRequest.ethnicGroups.length; j++) {
                if ($scope.ethnicGroups[i].value == $scope.searchRequest.ethnicGroups[j]) {
                    var group = $scope.ethnicGroups[i];
                    $scope.selectedEthnicGroups[group.title + "|" + group.value] = true;
                    break;
                }
            }
        }
    }

    $scope.religionCodes = [];
    var religionsArray = ['B', 'C', 'O', 'G', 'I', 'J', 'H', 'M', 'P', 'S'];
    for (var i = 0; i < religionsArray.length; i++) {
        $scope.religionCodes.push({
            value: religionsArray[i],
            title: localization.localize('ethnicity.religion.' + religionsArray[i].toLowerCase())});
    }

    if ($scope.searchRequest && $scope.searchRequest.religionCodes) {
        for (var i = 0; i < $scope.religionCodes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.religionCodes.length; j++) {
                if ($scope.religionCodes[i].value == $scope.searchRequest.religionCodes[j]) {
                    var code = $scope.religionCodes[i];
                    $scope.selectedReligionCodes[code.title + "|" + code.value] = true;
                    break;
                }
            }
        }
    }

    $scope.hispanicCountryCodes = [
        {"title":"Argentina", "value":"HA"},
        {"title":"Bolivia", "value":"HB"},
        {"title":"Brazil", "value":"HZ"},
        {"title":"Chile", "value":"HQ"},
        {"title":"Colombia", "value":"HJ"},
        {"title":"Costa Rica", "value":"HR"},
        {"title":"Cuba", "value":"HC"},
        {"title":"Dominican Republic", "value":"HD"},
        {"title":"Ecuador", "value":"HL"},
        {"title":"El Salvador", "value":"HE"},
        {"title":"Guatemala", "value":"HG"},
        {"title":"Honduras", "value":"HH"},
        {"title":"Mexico", "value":"HM"},
        {"title":"Nicaragua", "value":"HN"},
        {"title":"Panama", "value":"HK"},
        {"title":"Paraguay", "value":"HY"},
        {"title":"Peru", "value":"HX"},
        {"title":"Puerto Rico", "value":"HP"},
        {"title":"Spain", "value":"HS"},
        {"title":"Uruguay", "value":"HU"},
        {"title":"Venezuela", "value":"HV"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.hispanicCountryCodes) {
        for (var i = 0; i < $scope.hispanicCountryCodes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.hispanicCountryCodes.length; j++) {
                if ($scope.hispanicCountryCodes[i].value == $scope.searchRequest.hispanicCountryCodes[j]) {
                    var code = $scope.hispanicCountryCodes[i];
                    $scope.selectedHispanicCountryCodes[code.title + "|" + code.value] = true;
                    break;
                }
            }
        }
    }

    $scope.convertToTitle = function(property) {
        var result = "";
        for (var i = 0; i < property.length; i++) {
            if (i == 0) {
                result = result + property[i].toUpperCase();
            } else if (property[i] == "_") {
                i = i + 1;
                result = result + " " + property[i].toUpperCase();
            } else if (property[i] == property[i].toUpperCase()) {
                result = result + " " + property[i];
            } else {
                result = result + property[i];
            }
        }

        return result;
    }

    if ($scope.searchRequest && $scope.searchRequest.properties) {
        for (var i = 0; i < $scope.searchRequest.properties.length; i++) {
            var property = $scope.searchRequest.properties[i];
            if (property !== property.toUpperCase()) {
                $scope.selectedProperties[$scope.convertToTitle(property) + "|" + property] = true;
            }
        }
    }
}
])
.controller('C2CreditController',
['$scope', 'localization',
function($scope, localization) {
    $scope.searchConfig = {option: "creditRating", search: "", limit: "", show: false};

    $scope.tabs = [
        {value: "creditRating", title: "Credit Rating"},
        {value: "numberOfLinesOfCredit", title: "Number Of Lines Of Credit"},
        {value: "rangeOfNewCredit", title: "Range Of New Credit"},
        {value: "presenceOfCreditCard", title: "Presence Of Credit Card"},
        {value: "presenceOfGoldOrPlatinumCreditCard", title: "Presence Of Gold Or Platinum Credit Card"},
        {value: "presenceOfPremiumCreditCard", title: "Presence Of Premium Credit Card"},
        {value: "travelAndEntertainmentCardHolder", title: "Travel And Entertainment Card Holder"},
        {value: "creditCardUser", title: "Credit Card User"},
        {value: "creditCardNewIssue", title: "Credit Card New Issue"}
   ];

    $scope.rating = [];
    var ratingArray = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];
    for (var i = 0; i < ratingArray.length; i++) {
        $scope.rating.push({
            value: ratingArray[i],
            title: localization.localize('credit.rating.' + ratingArray[i].toLowerCase())});
    }

    if ($scope.searchRequest && $scope.searchRequest.rating) {
        for (var i = 0; i < $scope.rating.length; i++) {
            for (var j = 0; j < $scope.searchRequest.rating.length; j++) {
                if ($scope.rating[i].value == $scope.searchRequest.rating[j]) {
                    var rating = $scope.rating[i];
                    $scope.selectedRating[rating.title + "|" + rating.value] = true;
                    break;
                }
            }
        }
    }

    $scope.activeLines = [
        {value: "1", title:"1 Line of Credit"},
        {value: "2", title:"2 Lines of Credit"},
        {value: "3", title:"3 Lines of Credit"},
        {value: "4", title:"4 Lines of Credit"},
        {value: "5", title:"5 Lines of Credit"},
        {value: "6", title:"6 Lines of Credit"},
        {value: "7", title:"7 Lines of Credit"},
        {value: "8", title:"8 Lines of Credit"},
        {value: "9", title:"9 Lines of Credit"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.activeLines) {
        for (var i = 0; i < $scope.activeLines.length; i++) {
            for (var j = 0; j < $scope.searchRequest.activeLines.length; j++) {
                if ($scope.activeLines[i].value == $scope.searchRequest.activeLines[j]) {
                    var activeLine = $scope.activeLines[i];
                    $scope.selectedActiveLines[activeLine.title + "|" + activeLine.value] = true;
                    break;
                }
            }
        }
    }

    $scope.range = [];
    var rangeArray = [0, 1, 2, 3, 4, 5, 6, 7];
    for (var i = 0; i < rangeArray.length; i++) {
        $scope.range.push({
            value: rangeArray[i],
            title: localization.localize('credit.range.new.credits.' + rangeArray[i])});
    }

    if ($scope.searchRequest && $scope.searchRequest.range) {
        for (var i = 0; i < $scope.range.length; i++) {
            for (var j = 0; j < $scope.searchRequest.range.length; j++) {
                if ($scope.range[i].value == $scope.searchRequest.range[j]) {
                    var range = $scope.range[i];
                    $scope.selectedRange[range.title + "|" + range.value] = true;
                    break;
                }
            }
        }
    }
}
])
.controller('C2ResidenceController',
['$scope',
function($scope) {
    $scope.searchConfig = {option: "ownerShip", search: "", limit: "", show: false};

    $scope.tabs = [
        {value: "ownerShip", title: "Ownership"},
        {value: "propertyType", title: "Property Type"},
        {value: "lengthOfResidence", title: "Length Of Residence"},
        {value: "homeSwimmingPoolIndicator", title: "Swimming Pool"},
        {value: "numberOfPersonInLivingUnit", title: "Number Of Person In Living Unit"},
        {value: "presenceOfChildren", title: "Presence Of Children"},
        {value: "numberOfChildren", title: "Number Of Children"},
        {value: "childrenAgeGender", title: "Children Age/Gender"},
        {value: "inferredHouseHoldRank", title: "Inferred House Hold Rank"},
        {value: "numberOfAdults", title: "Number Of Adults"},
        {value: "generationsInHouseHold", title: "Generations In House Hold"},
        {value: "veteranInHouseHold", title: "Veteran In House Hold"},
        {value: "seniorAdultInHouseHold", title: "Senior Adult In House Hold"},
        //{value: "airConditioning", title: "Air Conditioning"},
        //{value: "homeHeatIndicator", title: "Home Heat"},
        {value: "sewer", title: "Sewer"},
        {value: "water", title: "Water"},
    ];

    $scope.propertyTypes = [
        {value: "M", title: "Multi Family Dwelling Unit"},
        {value: "S", title: "Single Family Dwelling Unit"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.propertyType) {
        for (var i = 0; i < $scope.propertyTypes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.propertyType.length; j++) {
                if ($scope.propertyTypes[i].value == $scope.searchRequest.propertyType[j]) {
                    var type = $scope.propertyTypes[i];
                    $scope.selectedPropertyType[type.title + "|" + type.value] = true;
                    break;
                }
            }
        }
    }


    $scope.ownerTypes = [
        {value: "H", title: "Homeowner"},
        {value: "9", title: "Probable Homeowners(90%-99%)"},
        {value: "R", title: "Renter"},
    ];

    if ($scope.searchRequest && $scope.searchRequest.ownerType) {
        for (var i = 0; i < $scope.ownerTypes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.ownerType.length; j++) {
                if ($scope.ownerTypes[i].value == $scope.searchRequest.ownerType[j]) {
                    var type = $scope.ownerTypes[i];
                    $scope.selectedOwnerType[type.title + "|" + type.value] = true;
                    break;
                }
            }
        }
    }


    $scope.lengthOfResidences = [
        {value: "0", title: "Under 1 year"},
        {value: "1", title: "1 year"},
        {value: "2", title: "2 years"},
        {value: "3", title: "3 years"},
        {value: "4", title: "4 years"},
        {value: "5", title: "5 years"},
        {value: "6", title: "6 years"},
        {value: "7", title: "7 years"},
        {value: "8", title: "8 years"},
        {value: "9", title: "9 years"},
        {value: "10", title: "10 years"},
        {value: "11", title: "11 years"},
        {value: "12", title: "12 years"},
        {value: "13", title: "13 years"},
        {value: "14", title: "14 years"},
        {value: "15", title: "15+ years"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.lengthOfResidence) {
        for (var i = 0; i < $scope.lengthOfResidences.length; i++) {
            for (var j = 0; j < $scope.searchRequest.lengthOfResidence.length; j++) {
                if ($scope.lengthOfResidences[i].value == $scope.searchRequest.lengthOfResidence[j]) {
                    var lengthOfResidence = $scope.lengthOfResidences[i];
                    $scope.selectedLengthOfResidence[lengthOfResidence.title + "|" + lengthOfResidence.value] = true;
                    break;
                }
            }
        }
    }


    $scope.ranks = [
        {value: "1", title: "Inferred Rank in Household of 1st"},
        {value: "2", title: "Inferred Rank in Household of 2nd"},
        {value: "3", title: "Inferred Rank in Household of 3rd"},
        {value: "4", title: "Inferred Rank in Household of 4th"},
        {value: "5", title: "Inferred Rank in Household of 5th"},
        {value: "6", title: "Inferred Rank in Household of 6th"},
        {value: "7", title: "Inferred Rank in Household of 7th"},
        {value: "8", title: "Inferred Rank in Household of 8th"},
        {value: "9", title: "Inferred Rank in Household of 9th"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.inferredHouseHoldRank) {
        for (var i = 0; i < $scope.ranks.length; i++) {
            for (var j = 0; j < $scope.searchRequest.inferredHouseHoldRank.length; j++) {
                if ($scope.ranks[i].value == $scope.searchRequest.inferredHouseHoldRank[j]) {
                    var rank = $scope.ranks[i];
                    $scope.selectedInferredHouseHoldRank[rank.title + "|" + rank.value] = true;
                    break;
                }
            }
        }
    }


    $scope.generations = [
        {value: "1", title: "1 Generation - 1 Adult"},
        {value: "2", title: "2 Generations -  Adult / Child"},
        {value: "3", title: "3 Generations - Adults / Child / Parent"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.generationsInHouseHold) {
        for (var i = 0; i < $scope.generations.length; i++) {
            for (var j = 0; j < $scope.searchRequest.generationsInHouseHold.length; j++) {
                if ($scope.generations[i].value == $scope.searchRequest.generationsInHouseHold[j]) {
                    var generation = $scope.generations[i];
                    $scope.selectedGenerationsInHouseHold[generation.title + "|" + generation.value] = true;
                    break;
                }
            }
        }
    }

    $scope.sewers = [
        {value: "1", title: "Commercial"},
        {value: "2", title: "Private"},
        {value: "3", title: "Public"},
        {value: "4", title: "Septic"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.sewer) {
        for (var i = 0; i < $scope.sewers.length; i++) {
            for (var j = 0; j < $scope.searchRequest.sewer.length; j++) {
                if ($scope.sewers[i].value == $scope.searchRequest.sewer[j]) {
                    var sewer = $scope.sewers[i];
                    $scope.selectedSewer[sewer.title + "|" + sewer.value] = true;
                    break;
                }
            }
        }
    }


    $scope.waters = [
        {value: "1", title: "Commercial"},
        {value: "2", title: "Private"},
        {value: "3", title: "Public"},
        {value: "4", title: "Well"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.water) {
        for (var i = 0; i < $scope.waters.length; i++) {
            for (var j = 0; j < $scope.searchRequest.water.length; j++) {
                if ($scope.waters[i].value == $scope.searchRequest.water[j]) {
                    var water = $scope.waters[i];
                    $scope.selectedWater[water.title + "|" + water.value] = true;
                    break;
                }
            }
        }
    }


    $scope.adultNumbers = [
        {value: "1", title: "1 Adult in Household"},
        {value: "2", title: "2 Adults in Household"},
        {value: "3", title: "3 Adults in Household"},
        {value: "4", title: "4 Adults in Household"},
        {value: "5", title: "5 Adults in Household"},
        {value: "6", title: "6+ Adults in Household"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.numberOfAdults) {
        for (var i = 0; i < $scope.adultNumbers.length; i++) {
            for (var j = 0; j < $scope.searchRequest.numberOfAdults.length; j++) {
                if ($scope.adultNumbers[i].value == $scope.searchRequest.numberOfAdults[j]) {
                    var adultNumber = $scope.adultNumbers[i];
                    $scope.selectedNumberOfAdults[adultNumber.title + "|" + adultNumber.value] = true;
                    break;
                }
            }
        }
    }


    $scope.childrenNumbers = [
        {value: "0", title: "No Children"},
        {value: "1", title: "1 Child"},
        {value: "2", title: "2 Children"},
        {value: "3", title: "3 Children"},
        {value: "4", title: "4 Children"},
        {value: "5", title: "5 Children"},
        {value: "6", title: "6 Children"},
        {value: "7", title: "7 Children"},
        {value: "8", title: "8 or more Children"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.numberOfChildren) {
        for (var i = 0; i < $scope.childrenNumbers.length; i++) {
            for (var j = 0; j < $scope.searchRequest.numberOfChildren.length; j++) {
                if ($scope.childrenNumbers[i].value == $scope.searchRequest.numberOfChildren[j]) {
                    var childrenNumber = $scope.childrenNumbers[i];
                    $scope.selectedNumberOfChildren[childrenNumber.title + "|" + childrenNumber.value] = true;
                    break;
                }
            }
        }
    }

    $scope.childrenAgeGender = [
        {value: "ChildrenAge00_02", title: "Age: 0-2 years"},
        {value: "ChildrenAge00_02FEMALE", title: "female", visible: function() {return $scope.selectedChildrenAgeGender["Age: 0-2 years|ChildrenAge00_02"]}},
        {value: "ChildrenAge00_02MALE", title: "male", visible: function() {return $scope.selectedChildrenAgeGender["Age: 0-2 years|ChildrenAge00_02"]}},
        {value: "ChildrenAge03_05", title: "Age: 3-5 years"},
        {value: "ChildrenAge03_05FEMALE", title: "female", visible: function() {return $scope.selectedChildrenAgeGender["Age: 3-5 years|ChildrenAge03_05"]}},
        {value: "ChildrenAge03_05MALE", title: "male", visible: function() {return $scope.selectedChildrenAgeGender["Age: 3-5 years|ChildrenAge03_05"]}},
        {value: "ChildrenAge06_10", title: "Age: 6-10 years"},
        {value: "ChildrenAge06_10FEMALE", title: "female", visible: function() {return $scope.selectedChildrenAgeGender["Age: 6-10 years|ChildrenAge06_10"]}},
        {value: "ChildrenAge06_10MALE", title: "male", visible: function() {return $scope.selectedChildrenAgeGender["Age: 6-10 years|ChildrenAge06_10"]}},
        {value: "ChildrenAge11_15", title: "Age: 11-15 years"},
        {value: "ChildrenAge11_15FEMALE", title: "female", visible: function() {return $scope.selectedChildrenAgeGender["Age: 11-15 years|ChildrenAge11_15"]}},
        {value: "ChildrenAge11_15MALE", title: "male", visible: function() {return $scope.selectedChildrenAgeGender["Age: 11-15 years|ChildrenAge11_15"]}},
        {value: "ChildrenAge16_17", title: "Age: 16-17 years"},
        {value: "ChildrenAge16_17FEMALE", title: "female", visible: function() {return $scope.selectedChildrenAgeGender["Age: 16-17 years|ChildrenAge16_17"]}},
        {value: "ChildrenAge16_17MALE", title: "male", visible: function() {return $scope.selectedChildrenAgeGender["Age: 16-17 years|ChildrenAge16_17"]}}
    ];

    if ($scope.searchRequest && $scope.searchRequest.childrenAgeGender) {
        for (var i = 0; i < $scope.childrenAgeGender.length; i++) {
            for (var j = 0; j < $scope.searchRequest.childrenAgeGender.length; j++) {
                if ($scope.childrenAgeGender[i].value == $scope.searchRequest.childrenAgeGender[j]) {
                    var childAgeGender = $scope.childrenAgeGender[i];
                    $scope.selectedChildrenAgeGender[childAgeGender.title + "|" + childAgeGender.value] = true;
                    break;
                }
            }
        }
    }


    $scope.personNumbers = [
        {value: "1", title: "1 Person"},
        {value: "2", title: "2 Persons"},
        {value: "3", title: "3 Persons"},
        {value: "4", title: "4 Persons"},
        {value: "5", title: "5 Persons"},
        {value: "6", title: "6 Persons"},
        {value: "7", title: "7 Persons"},
        {value: "8", title: "8 Persons"},
        {value: "9", title: "9+ Persons"}
    ]

    if ($scope.searchRequest && $scope.searchRequest.numberOfPersonInLivingUnit) {
        for (var i = 0; i < $scope.personNumbers.length; i++) {
            for (var j = 0; j < $scope.searchRequest.numberOfPersonInLivingUnit.length; j++) {
                if ($scope.personNumbers[i].value == $scope.searchRequest.numberOfPersonInLivingUnit[j]) {
                    var personNumber = $scope.personNumbers[i];
                    $scope.selectedNumberOfPersonInLivingUnit[personNumber.title + "|" + personNumber.value] = true;
                    break;
                }
            }
        }
    }
}
])
.controller('C2InterestsController',
['$scope',
function($scope) {
    $scope.searchConfig = {search: "", limit: 30};
    $scope.loadMore = function() {
        $scope.searchConfig.limit = $scope.searchConfig.limit + 30;
    }

    $scope.interests = [
        {value:"NEWSANDFINANCIAL",title:"News And Financial"},
        {value:"AUTOMOTIVEBUFF",title:"Automotive Buff"},
        {value:"BOOKREADER",title:"Book Reader"},
        {value:"COMPUTEROWNER",title:"Computer Owner"},
        {value:"COOKINGENTHUSIAST",title:"Cooking Enthusiast"},
        {value:"DO_IT_YOURSELFERS",title:"Do It Yourselfer"},
        {value:"EXERCISEENTHUSIAST",title:"Exercise Enthusiast"},
        {value:"GARDENER",title:"Gardener"},
        {value:"GOLFENTHUSIASTS",title:"Golf Enthusiast"},
        {value:"HOMEDECORATINGENTHUSIAST",title:"Home Decorating Enthusiast"},
        {value:"OUTDOORENTHUSIAST",title:"Outdoor Enthusiast"},
        {value:"OUTDOORSPORTSLOVER",title:"Outdoor Sports Lover"},
        {value:"PHOTOGRAPHY",title:"Photography"},
        {value:"TRAVELER",title:"Traveler"},
        {value:"PETS",title:"Pets"},
        {value:"CATS",title:"Cats"},
        {value:"DOGS",title:"Dogs"},
        {value:"EQUESTRIAN",title:"Equestrian"},
        {value:"READINGSCIENCEFICTION",title:"Reading Science Fiction"},
        {value:"READINGAUDIOBOOKS",title:"Reading Audio Books"},
        {value:"HISTORYMILITARY",title:"History Military"},
        {value:"CURRENTAFFAIRSPOLITICS",title:"Current Affairs Politics"},
        {value:"SCIENCESPACE",title:"Science Space"},
        {value:"GAMING",title:"Gaming"},
        {value:"GAMESVIDEOGAMES",title:"Games/Video Games"},
        {value:"ARTS",title:"Arts"},
        {value:"GAMESCOMPUTERGAMES",title:"Games/Computer Games"},
        {value:"MOVIEMUSICGROUPING",title:"Movie/Music Grouping"},
        {value:"MUSICALINSTRUMENTS",title:"Musical Instruments"},
        {value:"COLLECTIBLESSTAMPS",title:"Collectibles Stamps"},
        {value:"COLLECTIBLESCOINS",title:"Collectibles Coins"},
        {value:"COLLECTIBLESARTS",title:"Collectibles Arts"},
        {value:"COLLECTIBLESANTIQUES",title:"Collectibles Antiques"},
        {value:"COLLECTIBLESSPORTSMEMORABILIA",title:"Collectibles Sports Memorabilia"},
        {value:"MILITARYMEMORABILIAWEAPONRY",title:"Military Memorabilia Weaponry"},
        {value:"AUTOWORK",title:"Auto work"},
        {value:"WOODWORKING",title:"Wood working"},
        {value:"AVIATION",title:"Aviation"},
        {value:"HOUSEPLANTS",title:"House Plants"},
        {value:"HOMEANDGARDEN",title:"Home And Garden"},
        {value:"HOMEIMPROVEMENTGROUPING",title:"Home Improvement Grouping"},
        {value:"PHOTOGRAPHYANDVIDEOEQUIPMENT",title:"Photography And Video Equipment"},
        {value:"HOMEFURNISHINGSDECORATING",title:"Home Furnishings Decorating"},
        {value:"HOMEIMPROVEMENT",title:"Home Improvement"},
        {value:"FOODWINES",title:"Food Wines"},
        {value:"COOKINGGENERAL",title:"Cooking General"},
        //{value:"COOKINGGOURMET",title:"Cooking Gourmet"},
        {value:"FOODSNATURAL",title:"Foods Natural"},
        {value:"COOKINGFOODGROUPING",title:"Cooking Food Grouping"},
        {value:"GAMINGCASINO",title:"Gaming Casino"},
        {value:"UPSCALELIVING",title:"Up Scale Living"},
        {value:"CULTURALARTISTICLIVING",title:"Cultural Artistic Living"},
        {value:"HIGHTECHLIVING",title:"High-Tech Living"},
        {value:"EXERCISEHEALTHGROUPING",title:"Exercise Health Grouping"},
        {value:"EXERCISERUNNINGJOGGING",title:"Exercise Running/Jogging"},
        {value:"EXERCISEWALKING",title:"Exercise Walking"},
        {value:"EXERCISEAEROBIC",title:"Exercise Aerobic"},
        {value:"SPECTATORSPORTSTVSPORTS",title:"Spectator Sports/TV Sports"},
        {value:"SPECTATORSPORTSFOOTBALL",title:"Spectator Sports/Football"},
        {value:"SPECTATORSPORTSBASEBALL",title:"Spectator Sports/Baseball"},
        {value:"SPECTATORSPORTSBASKETBALL",title:"Spectator Sports/Basketball"},
        {value:"SPECTATORSPORTSHOCKEY",title:"Spectator Sports/Hockey"},
        {value:"SPECTATORSPORTSSOCCER",title:"Spectator Sports/Soccer"},
        {value:"TENNIS",title:"Tennis"},
        {value:"SNOWSKIING",title:"Snow Skiing"},
        {value:"MOTORCYCLING",title:"Motorcycling"},
        {value:"NASCAR",title:"Nascar"},
        {value:"BOATINGSAILING",title:"Boating Sailing"},
        {value:"SCUBADIVING",title:"Scuba Diving"},
        {value:"SPORTSANDLEISURE",title:"Sports And Leisure"},
        {value:"HUNTING",title:"Hunting"},
        {value:"FISHING",title:"Fishing"},
        {value:"CAMPINGHIKING",title:"Camping/Hiking"},
        {value:"HUNTINGSHOOTING",title:"Hunting/Shooting"},
        {value:"SPORTSGROUPING",title:"Sports Grouping"},
        {value:"OUTDOORSGROUPING",title:"Outdoors Grouping"},
        {value:"HEALTHMEDICAL",title:"Health Medical"},
        {value: "VALUEHUNTER", title: "Value Hunter"},
        {value: "MAILRESPONDER", title: "Mail Responder"},
        {value: "SWEEPSTAKES", title: "Sweepstakes"},
        {value: "RELIGIOUSMAGAZINE", title: "Religious Magazine"},
        {value: "MALEMERCHBUYER", title: "Male Merch Buyer"},
        {value: "FEMALEMERCHBUYER", title: "Female Merch Buyer"},
        {value: "CRAFTS_HOBBMERCHBUYER", title: "Crafts/Hobby Merch Buyer"},
        {value: "GARDENING_FARMINGBUYER", title: "Gardering/Farming Buyer"},
        {value: "BOOKBUYER", title: "Book Buyer"},
        {value: "COLLECT_SPECIALFOODSBUYER", title: "Collect/Special Foods Buyer"},
        {value: "MAILORDERBUYER", title: "Mail Order Buyer"},
        {value: "ONLINEPURCHASINGINDICATOR", title: "Online Purchasing Indicator"},
        {value: "APPARELWOMENS", title: "Apparel Women"},
        {value: "YOUNGWOMENSAPPAREL", title: "Young Women Apparel"},
        {value: "APPARELMENS", title: "Apparel Men"},
        {value: "APPARELMENSBIGANDTALL", title: "Apparel Men Big And Tall"},
        {value: "YOUNGMENSAPPAREL", title: "Young Men Apparel"},
        {value: "APPARELCHILDRENS", title: "Apparel Children"},
        {value: "HEALTHANDBEAUTY", title: "Health And Beauty"},
        {value: "BEAUTYCOSMETICS", title: "Beauty Cosmetics"},
        {value: "JEWELRY", title: "Jewelry"},
        {value: "LUGGAGE", title: "Luggage"},
        {value: "TVCABLE", title: "TV Cable"},
        {value: "TVSATELLITEDISH", title: "TV Satellite Dish"},
        //{value: "INTENDTOPURCHASEHDTVSATELLITEDISH", title: "Intend To Purchase HDTV Satellite Dish"},
        {value: "HIGHENDAPPLIANCES", title: "High End Appliances"},
        {value: "CONSUMERELECTRONICS", title: "Consumer Electronics"},
        //{value: "COMPUTERS", title: "Computers"},
        {value: "ELECTRONICSCOMPUTERSGROUPING", title: "Electronics Computers Grouping"},
        //{value: "INTENDTOPURCHASEHOMEIMPROVEMENT", title: "Intend To Purchase Home Improvement"},
        {value: "TRAVELGROUPING", title: "Travel Grouping"},
        {value: "TRAVEL", title: "Travel"},
        {value: "TRAVELDOMESTIC", title: "Travel Domestic"},
        {value: "TRAVELINTERNATIONAL", title: "Travel International"},
        {value: "TRAVELCRUISEVACATIONS", title: "Travel Cruise Vacations"},
        {value: "DIETINGWEIGHTLOSS", title: "Dieting/Weight Loss"},
        {value: "AUTOMOTIVEAUTOPARTSANDACCESSORIES", title: "Automotive, Auto Parts And Accessories"},
        {value:"RELIGIOUSCONTRIBUTOR", title:"Religious Contributor"},
        {value:"POLITICALCONTRIBUTOR", title:"Political Contributor"},
        {value:"CHARITABLE", title:"Charitable"},
        {value:"DONATESTOENVIRONMENTALCAUSES", title:"Donates To Environmental Causes"},
        {value:"POLITICALCONSERVATIVECHARITABLEDONATION", title:"Political Conservative Charitable Donation"},
        {value:"POLITICALLIBERALCHARITABLEDONATION", title:"Political Liberal Charitable Donation"},
        {value:"VETERANSCHARITABLEDONATION", title:"Veterans Charitable Donation"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.properties) {
        for (var i = 0; i < $scope.interests.length; i++) {
            for (var j = 0; j < $scope.searchRequest.properties.length; j++) {
                if ($scope.interests[i].value == $scope.searchRequest.properties[j]) {
                    var interest = $scope.interests[i];
                    $scope.selectedProperties[interest.title + "|" + interest.value] = true;
                    break;
                }
            }
        }
    }
}
])
.controller('C2ContributesController',
['$scope',
function($scope) {
    $scope.contributors = [
        {value:"RELIGIOUSCONTRIBUTOR", title:"Religious Contributor"},
        {value:"POLITICALCONTRIBUTOR", title:"Political Contributor"},
        {value:"CHARITABLE", title:"Charitable"},
        {value:"DONATESTOENVIRONMENTALCAUSES", title:"Donates To Environmental Causes"},
        {value:"POLITICALCONSERVATIVECHARITABLEDONATION", title:"Political Conservative Charitable Donation"},
        {value:"POLITICALLIBERALCHARITABLEDONATION", title:"Political Liberal Charitable Donation"},
        {value:"VETERANSCHARITABLEDONATION", title:"Veterans Charitable Donation"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.properties) {
        for (var i = 0; i < $scope.contributors.length; i++) {
            for (var j = 0; j < $scope.searchRequest.properties.length; j++) {
                if ($scope.contributors[i].value == $scope.searchRequest.properties[j]) {
                    var contributor = $scope.contributors[i];
                    $scope.selectedProperties[contributor.title + "|" + contributor.value] = true;
                    break;
                }
            }
        }
    }
}
])
.controller('C2CareerController',
['$scope',
function($scope) {
    $scope.searchConfig = {option: "occupationGroup",
                           search: "",
                           limit: 30,
                           show: false};

    $scope.loadMore = function() {
        $scope.searchConfig.limit = $scope.searchConfig.limit + 30;
    }

    $scope.$watch('searchConfig.option', function() {
        $scope.searchConfig.search = "";
        $scope.searchConfig.limit = 30;

        $scope.searchConfig.show = $scope.searchConfig.option == "personOccupation";
    });

    if ($scope.searchRequest && $scope.searchRequest.personOccupations) {
        for (var i = 0; i < $scope.occupations.length; i++) {
            for (var j = 0; j < $scope.searchRequest.personOccupations.length; j++) {
                if ($scope.occupations[i].value == $scope.searchRequest.personOccupations[j]) {
                    var occupation = $scope.occupations[i];
                    $scope.selectedPersonOccupations[occupation.title + "|" + occupation.value] = true;
                    break;
                }
            }
        }
    }


    $scope.tabs = [
        {value: "occupationGroup", title: "Occupation Group"},
        {value: "personOccupation", title: "Person Occupation"},
        {value: "personEducation", title: "Person Education"},
        {value: "businessOwner", title: "Business Owner"},
        {value: "opportunitySeekers", title: "Opportunity Seeker"},
        {value: "highTechLeader", title: "High-Tech Leader"},
        {value: "careerImprovement", title: "Career Improvement"},
        {value: "workingWoman", title: "Working Woman"},
        {value: "africanAmericanProfessionals", title: "African / American Professionals"},
        {value: "career", title: "Career"},
        {value: "educationOnline", title: "Education Online"},
        {value: "computingHomeOfficeGeneral", title: "Computing / Home Office - General"},
        {value: "electronicsComputingAndHomeOffice", title: "Electronics, Computing And Home Office"},
        {value: "telecommunications", title: "Telecommunications"},
        {value: "selfImprovement", title: "Self Improvement"},
    ];

    $scope.groups = [
        {value: "A", title: "Professional / Technical"},
        {value: "B", title: "Administration / Managerial"},
        {value: "C", title: "Sales / Service"},
        {value: "D", title: "Clerical / White Collar"},
        {value: "E", title: "Craftsman / Blue Collar"},
        {value: "F", title: "Student"},
        {value: "G", title: "Homemaker"},
        {value: "H", title: "Retired"},
        {value: "I", title: "Farmer"},
        {value: "J", title: "Military"},
        {value: "K", title: "Religious"},
        {value: "L", title: "Self Employed"},
        {value: "M", title: "Self Employed - Professional / Technical"},
        {value: "N", title: "Self Employed - Administration / Managerial"},
        {value: "P", title: "Self Employed - Clerical / White Collar"},
        {value: "Q", title: "Self Employed - Craftsman / Blue Collar"},
        {value: "U", title: "Self Employed - Other"},
        {value: "V", title: "Educator"},
        {value: "W", title: "Financial Professional"},
        {value: "X", title: "Legal Professional"},
        {value: "Y", title: "Medical Professional"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.occupationGroups) {
        for (var i = 0; i < $scope.groups.length; i++) {
            for (var j = 0; j < $scope.searchRequest.occupationGroups.length; j++) {
                if ($scope.groups[i].value == $scope.searchRequest.occupationGroups[j]) {
                    var group = $scope.groups[i];
                    $scope.selectedOccupationGroups[group.title + "|" + group.value] = true;
                    break;
                }
            }
        }
    }


    $scope.educations = [
        {value: "A", title: "Completed High School"},
        {value: "B", title: "Completed College"},
        {value: "C", title: "Completed Graduate School"},
        {value: "D", title: "Attended Vocational/Technical"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.personEducations) {
        for (var i = 0; i < $scope.educations.length; i++) {
            for (var j = 0; j < $scope.searchRequest.personEducations.length; j++) {
                if ($scope.educations[i].value == $scope.searchRequest.personEducations[j]) {
                    var education = $scope.educations[i];
                    $scope.selectedPersonEducations[education.title + "|" + education.value] = true;
                    break;
                }
            }
        }
    }


    $scope.businessOwners = [
        {value: "1", title: "Accountant"},
        {value: "2", title: "Builder"},
        {value: "3", title: "Contractor"},
        {value: "4", title: "Dealer/Retailer/Storekeeper"},
        {value: "5", title: "Distributor/Wholesaler"},
        {value: "6", title: "Funeral Director"},
        {value: "7", title: "Maker/Manufacturer"},
        {value: "8", title: "Owner"},
        {value: "9", title: "Partner"},
        {value: "A", title: "Self-Employed"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.businessOwners) {
        for (var i = 0; i < $scope.businessOwners.length; i++) {
            for (var j = 0; j < $scope.searchRequest.businessOwners.length; j++) {
                if ($scope.businessOwners[i].value == $scope.searchRequest.businessOwners[j]) {
                    var businessOwner = $scope.businessOwners[i];
                    $scope.selectedBusinessOwners[businessOwner.title + "|" + businessOwner.value] = true;
                    break;
                }
            }
        }
    }

}
])
.controller('C2PurchasesController',
['$scope',
function($scope) {
    $scope.searchConfig = {search: "", limit: 20};
    $scope.loadMore = function() {
        $scope.searchConfig.limit = $scope.searchConfig.limit + 20;
    }

    $scope.purchases = [
        {value: "VALUEHUNTER", title: "Value Hunter"},
        {value: "MAILRESPONDER", title: "Mail Responder"},
        {value: "SWEEPSTAKES", title: "Sweepstakes"},
        {value: "RELIGIOUSMAGAZINE", title: "Religious Magazine"},
        {value: "MALEMERCHBUYER", title: "Male Merch Buyer"},
        {value: "FEMALEMERCHBUYER", title: "Female Merch Buyer"},
        {value: "CRAFTS_HOBBMERCHBUYER", title: "Crafts/Hobby Merch Buyer"},
        {value: "GARDENING_FARMINGBUYER", title: "Gardering/Farming Buyer"},
        {value: "BOOKBUYER", title: "Book Buyer"},
        {value: "COLLECT_SPECIALFOODSBUYER", title: "Collect/Special Foods Buyer"},
        {value: "MAILORDERBUYER", title: "Mail Order Buyer"},
        {value: "ONLINEPURCHASINGINDICATOR", title: "Online Purchasing Indicator"},
        {value: "APPARELWOMENS", title: "Apparel Women"},
        {value: "YOUNGWOMENSAPPAREL", title: "Young Women Apparel"},
        {value: "APPARELMENS", title: "Apparel Men"},
        {value: "APPARELMENSBIGANDTALL", title: "Apparel Men Big And Tall"},
        {value: "YOUNGMENSAPPAREL", title: "Young Men Apparel"},
        {value: "APPARELCHILDRENS", title: "Apparel Children"},
        {value: "HEALTHANDBEAUTY", title: "Health And Beauty"},
        {value: "BEAUTYCOSMETICS", title: "Beauty Cosmetics"},
        {value: "JEWELRY", title: "Jewelry"},
        {value: "LUGGAGE", title: "Luggage"},
        {value: "TVCABLE", title: "TV Cable"},
        {value: "TVSATELLITEDISH", title: "TV Satellite Dish"},
        //{value: "INTENDTOPURCHASEHDTVSATELLITEDISH", title: "Intend To Purchase HDTV Satellite Dish"},
        {value: "HIGHENDAPPLIANCES", title: "High End Appliances"},
        {value: "CONSUMERELECTRONICS", title: "Consumer Electronics"},
        //{value: "COMPUTERS", title: "Computers"},
        {value: "ELECTRONICSCOMPUTERSGROUPING", title: "Electronics Computers Grouping"},
        //{value: "INTENDTOPURCHASEHOMEIMPROVEMENT", title: "Intend To Purchase Home Improvement"},
        {value: "TRAVELGROUPING", title: "Travel Grouping"},
        {value: "TRAVEL", title: "Travel"},
        {value: "TRAVELDOMESTIC", title: "Travel Domestic"},
        {value: "TRAVELINTERNATIONAL", title: "Travel International"},
        {value: "TRAVELCRUISEVACATIONS", title: "Travel Cruise Vacations"},
        {value: "DIETINGWEIGHTLOSS", title: "Dieting/Weight Loss"},
        {value: "AUTOMOTIVEAUTOPARTSANDACCESSORIES", title: "Automotive, Auto Parts And Accessories"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.properties) {
        for (var i = 0; i < $scope.purchases.length; i++) {
            for (var j = 0; j < $scope.searchRequest.properties.length; j++) {
                if ($scope.purchases[i].value == $scope.searchRequest.properties[j]) {
                    var purchase = $scope.purchases[i];
                    $scope.selectedProperties[purchase.title + "|" + purchase.value] = true;
                    break;
                }
            }
        }
    }
}
])
.controller('C2FinanceController',
['$scope',
function($scope) {
    $scope.searchConfig = {option: "estimatedIncome"};

    $scope.tabs = [
        {value: "estimatedIncome", title: "Estimated Income"},
        {value: "investment", title: "Investment"},
        {value: "investmentStockSecurities", title: "Investment Stock Securities"},
        {value: "netWorth", title: "Net Worth"},
        {value: "investing_active", title: "Investing Active"},
        {value: "investmentsPersonal", title: "Investments Personal"},
        {value: "investmentsRealeState", title: "Investments Real Estate"},
        {value: "investingFinanceGrouping", title: "Investing Finance Grouping"},
        {value: "investmentsForeign", title: "Investments Foreign"},
        {value: "investmentEstimatedResidentialPropertiesOwned", title: "Investment - Residential Properties Owned"}
    ];

    $scope.incomes = [
        {value: "A", title: "Under $10,000"},
        {value: "B", title: "$10,000 - $14,999"},
        {value: "C", title: "$15,000 - $19,999"},
        {value: "D", title: "$20,000 - $24,999"},
        {value: "E", title: "$25,000 - $29,999"},
        {value: "F", title: "$30,000 - $34,999"},
        {value: "G", title: "$35,000 - $39,999"},
        {value: "H", title: "$40,000 - $44,999"},
        {value: "I", title: "$45,000 - $49,999"},
        {value: "J", title: "$50,000 - $54,999"},
        {value: "K", title: "$55,000 - $59,999"},
        {value: "L", title: "$60,000 - $64,999"},
        {value: "M", title: "$65,000 - $74,999"},
        {value: "N", title: "$75,000 - $99,999"},
        {value: "O", title: "$100,000 - $149,999"},
        {value: "P", title: "$150,000 - $174,999"},
        {value: "Q", title: "$175,000 - $199,999"},
        {value: "R", title: "$200,000 - $249,999"},
        {value: "S", title: "$250,000 +"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.estimatedIncome) {
        for (var i = 0; i < $scope.incomes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.estimatedIncome.length; j++) {
                if ($scope.incomes[i].value == $scope.searchRequest.estimatedIncome[j]) {
                    var income = $scope.incomes[i];
                    $scope.selectedEstimatedIncome[income.title + "|" + income.value] = true;
                    break;
                }
            }
        }
    }


    $scope.nets = [
        {value: "A", title: "Less than $1"},
        {value: "B", title: "$1 - $4,999"},
        {value: "C", title: "$5,000 - $9,999"},
        {value: "D", title: "$10,000 - $24,999"},
        {value: "E", title: "$25,000 - $49,999"},
        {value: "F", title: "$50,000 - $99,999"},
        {value: "G", title: "$100,000 - $249,999"},
        {value: "H", title: "$250,000 - $499,999"},
        {value: "I", title: "Greater than $499,999"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.netWorthes) {
        for (var i = 0; i < $scope.nets.length; i++) {
            for (var j = 0; j < $scope.searchRequest.netWorthes.length; j++) {
                if ($scope.nets[i].value == $scope.searchRequest.netWorthes[j]) {
                    var netWorth = $scope.nets[i];
                    $scope.selectedNetWorthes[netWorth.title + "|" + netWorth.value] = true;
                    break;
                }
            }
        }
    }


    $scope.propertyOwned = [
        {value: "001", title: "1"},
        {value: "002", title: "2"},
        {value: "003", title: "3"},
        {value: "004", title: "4"},
        {value: "005", title: "5"},
        {value: "006", title: "6"},
        {value: "007", title: "7"},
        {value: "008", title: "8"},
        {value: "009", title: "9"},
        {value: "010", title: "10"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.propertyOwned) {
        for (var i = 0; i < $scope.propertyOwned.length; i++) {
            for (var j = 0; j < $scope.searchRequest.propertyOwned.length; j++) {
                if ($scope.propertyOwned[i].value == $scope.searchRequest.propertyOwned[j]) {
                    var propertyOwned = $scope.propertyOwned[i];
                    $scope.selectedPropertyOwned[propertyOwned.title + "|" + propertyOwned.value] = true;
                    break;
                }
            }
        }
    }
}
])
.controller('C2SourcesController',
['$scope',
function($scope) {
    $scope.searchConfig = {option: "numberOfSources", limit: 30};

    $scope.tabs = [
        {value: "numberOfSources", title: "Number Of Sources"},
        {value: "dpv", title: "Delivery Point Verification Code"},
    ];

    $scope.numberOfSources = [
        {title: "Matched 1 Source", value: "1"},
        {title: "Matched 2 Sources", value: "2"},
        {title: "Matched 3 Sources", value: "3"},
        {title: "Matched 4 Sources", value: "4"},
        {title: "Matched 5 Sources", value: "5"},
        {title: "Matched 6 Sources", value: "6"},
        {title: "Matched 7 Sources", value: "7"},
        {title: "Matched 8 Sources", value: "8"},
        {title: "Matched 9 Sources", value: "9"},
        {title: "Matched 10+ Sources", value: "10"},
    ];

    $scope.dpv = [
        {title: "HiRise/Box missing Sec. Addr", value: "D"},
        {title: "Not Confirmed", value: "N"},
        {title: "Confirmed by ignoring Sec. Addr", value: "S"},
        {title: "VACANT", value: "V"},
        {title: "Confirmed", value: "C"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.numberOfSources) {
        for (var i = 0; i < $scope.searchRequest.numberOfSources.length; i++) {
            var value = $scope.searchRequest.numberOfSources[i];
            $scope.selectedNumberOfSources[getTitle(value, $scope.numberOfSources) + "|" + value] = true;
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.dpv) {
        for (var i = 0; i < $scope.searchRequest.dpv.length; i++) {
            var value = $scope.searchRequest.dpv[i];
            $scope.selectedDPV[getTitle(value, $scope.dpv) + "|" + value] = true;
        }
    }

    function getTitle(value, array) {
        for (var i = 0; i < array.length; i++) {
            if (array[i].value === value) {
                return array[i].title;
            }
        }

        return "";
    }
}
])
.controller('C2VehicleController',
['$scope', 'BASE_URL',
function($scope, BASE_URL) {
    $scope.searchConfig = {option: "make"};

    $scope.tabs = [
        {value: "make", title: "Make"},
        {value: "model", title: "Model"},
        {value: "year", title: "Year"}
    ];

    $scope.BASE_URL = BASE_URL;
}])
.controller('C2RealEstateController',
['$scope', 'dictionaryService',
function($scope, dictionaryService) {
    $scope.searchConfig = {option: "homePurchasedDate", limit: 30};
    $scope.values = {};

    $scope.loadMore = function() {
        $scope.searchConfig.limit = $scope.searchConfig.limit + 30;
    }

    $scope.$watch('searchConfig.option', function() {
        $scope.values.date = undefined;

        $scope.values.endDate = undefined;
        $scope.values.startDate = undefined;

        $scope.values.before = undefined;
        $scope.values.after = undefined;

        $scope.values.name = undefined;
        $scope.values.value = undefined;

        $scope.values.loanFrom = undefined;
        $scope.values.loanTo = undefined;

        $scope.searchConfig.limit = 30;
        $scope.searchConfig.filter = '';
        $scope.notFound = false;
        $scope.lenders = [];
    });

    $scope.lenders = [];
    $scope.notFound = false;
    $scope.onSearchChanged = function() {
        $scope.notFound = false;
        if ($scope.searchConfig.filter != undefined && $scope.searchConfig.filter.length == 0) {
            $scope.lenders = [];
        } else {
            $scope.loading = true;
            dictionaryService.lenders({value: $scope.searchConfig.filter}, function(response) {
                if (response.status === 'OK') {
                    $scope.lenders = response.data;
                    $scope.loading = false;
                    $scope.notFound = $scope.lenders.length == 0;
                }
            });
        }
    }

    $scope.loanFromChanged = function() {
        var value = $scope.values.loanFrom;
        for (var prop in $scope.selectedLoanToValues) {
            if (prop.indexOf("From") != -1) {
                $scope.selectedLoanToValues[prop] = false;
            }
        }

        if (value == '') {
            $scope.selectedLoanToValues["From: " + value + "|" + value] = false;
        } else {
            $scope.selectedLoanToValues["From: " + value + "|" + value] = true;
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.loanToValue) {
        if ($scope.searchRequest.loanToValue.length == 2) {
            var value = $scope.searchRequest.loanToValue[0];
            $scope.selectedLoanToValues["From: " + value + "|" + value] = false;

            value = $scope.searchRequest.loanToValue[1];
            $scope.selectedLoanToValues["To: " + value + "|" + value] = false;
        }
    }

    $scope.loanToChanged = function() {
        var value = $scope.values.loanTo;
        for (var prop in $scope.selectedLoanToValues) {
            if (prop.indexOf("To") != -1) {
                $scope.selectedLoanToValues[prop] = false;
            }
        }

        if (value == '') {
            $scope.selectedLoanToValues["To: " + value + "|" + value] = false;
        } else {
            $scope.selectedLoanToValues["To: " + value + "|" + value] = true;
        }
    }

    $scope.onStartDateChanged = function() {
        $scope.addDate(true);

        if ($scope.values.endDate) {
            $scope.values.before = false;
            $scope.values.after = false;

            $scope.addDate(false);
        }
    }

    $scope.onEndDateChanged = function() {
        $scope.addDate(false);

        if ($scope.values.startDate) {
            $scope.values.before = false;
            $scope.values.after = false;

            $scope.addDate(true);
        }
    }

    $scope.onStartDateIntervalParamChanged = function() {
        if ($scope.values.before) {
            $scope.values.endDate = undefined;
            $scope.updateDashBoardObject(false)
        }

        $scope.addDate(true, undefined);
    }

    $scope.onEndDateIntervalParamChanged = function() {
        if ($scope.values.after) {
            $scope.values.startDate = undefined;
            $scope.updateDashBoardObject(true)
        }

        $scope.addDate(false, undefined);
    }

    $scope.addDate = function(startDate) {
        if (startDate && !$scope.values.startDate) { return; }
        if (!startDate && !$scope.values.endDate) { return; }

        var title = document.getElementById("startDate").value;
        if (!startDate) {
            var title = document.getElementById("endDate").value;
        }

        var value = 0;
        if (startDate) {
            value = $scope.values.startDate.getTime();
        } else {
            value = $scope.values.endDate.getTime();
        }

        if (startDate && $scope.values.before) {
            title = title + " and earlier";
            value = value + (value > 0 ? 999 : -999);
        }

        if (!startDate && $scope.values.after) {
            title = title + " and later";
            value = value + (value > 0 ? 999 : -999);
        }

        $scope.updateDashBoardObject(startDate, {'title': title, 'value': value })
    }

    $scope.updateDashBoardObject = function(startDate, value) {
        var index = startDate ? 0 : 1;
        if ($scope.searchConfig.option === 'homePurchasedDate') {
            $scope.selectedHomePurchasedDates[index] = value;
        } else if ($scope.searchConfig.option === 'homeYearBuilt') {
            $scope.selectedHomeYearBuilt[index] = value;
        } else if ($scope.searchConfig.option === 'deedDateOfRefinance') {
            $scope.selectedDeedDatesOfRefinance[index] = value;
        } else if ($scope.searchConfig.option === 'purchaseMortgageDate') {
            $scope.selectedPurchaseMortgageDates[index] = value;
        }
    }

    $scope.getDateValue = function(time) {
        var date = new Date(parseInt(time));
        var month = (date.getMonth() + 1) + "";
        if (month.length == 1) {
            month = "0" + month;
        }

        return month + "." + date.getFullYear();
    }

    $scope.getYearValue = function(time) {
        var date = new Date(parseInt(time));
        return date.getFullYear() + "";
    }

    $scope.fillDates = function(selectedArray, dates, yearOnly) {
        if (dates[0] && dates[0] != -1) {
            var title = yearOnly ? $scope.getYearValue(dates[0]) : $scope.getDateValue(dates[0]);
            if (parseInt(dates[0]) % 1000 == 999 || parseInt(dates[0]) % 1000 == -999) {
                title = title + " and earlier";
            }

            selectedArray[0] = {'title': title, value: parseInt(dates[0])};
        }

        if (dates[1] && dates[1] != -1) {
            var title = yearOnly ? $scope.getYearValue(dates[1]) : $scope.getDateValue(dates[1]);
            if (parseInt(dates[1]) % 1000 == 999 || parseInt(dates[1]) % 1000 == -999) {
                title = title + " and later";
            }

            selectedArray[1] = {'title': title, value: parseInt(dates[1])};
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.homePurchasedDates) {
        $scope.fillDates($scope.selectedHomePurchasedDates, $scope.searchRequest.homePurchasedDates, false);
    }

    if ($scope.searchRequest && $scope.searchRequest.homeYearBuilt) {
        $scope.fillDates($scope.selectedHomeYearBuilt, $scope.searchRequest.homeYearBuilt, true);
    }

    if ($scope.searchRequest && $scope.searchRequest.deedDatesOfRefinance) {
        $scope.fillDates($scope.selectedDeedDatesOfRefinance, $scope.searchRequest.deedDatesOfRefinance, false);
    }

    if ($scope.searchRequest && $scope.searchRequest.purchaseMortgageDates) {
        $scope.fillDates($scope.selectedPurchaseMortgageDates, $scope.searchRequest.purchaseMortgageDates, false);
    }

    $scope.addName = function() {
        if (!$scope.values.name) { return; }

        var names = $scope.values.name.split(",");
        for (var i = 0; i < names.length; i++) {
            var name = names[i].trim();
            if (name) {
                if ($scope.searchConfig.option === 'mortgageLenderName') {
                    $scope.selectedMortgageLenderNames[name + "|" + name] = name;
                } else if ($scope.searchConfig.option === 'refinanceLeaderName') {
                    $scope.selectedRefinanceLeaderNames[name + "|" + name] = name;
                } else if ($scope.searchConfig.option === 'purchaseLenderName') {
                    $scope.selectedPurchaseLenderNames[name + "|" + name] = name;
                }
            }
        }

        $scope.values.name = undefined;
    }

    if ($scope.searchRequest && $scope.searchRequest.mortgageLenderNames) {
        for (var i = 0; i < $scope.searchRequest.mortgageLenderNames.length; i++) {
            var value = $scope.searchRequest.mortgageLenderNames[i];
            $scope.selectedMortgageLenderNames[value + "|" + value] = true;
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.refinanceLeaderName) {
        for (var i = 0; i < $scope.searchRequest.refinanceLeaderName.length; i++) {
            var value = $scope.searchRequest.refinanceLeaderName[i];
            $scope.selectedRefinanceLeaderName[value + "|" + value] = true;
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.purchaseLenderName) {
        for (var i = 0; i < $scope.searchRequest.purchaseLenderName.length; i++) {
            var value = $scope.searchRequest.purchaseLenderName[i];
            $scope.selectedPurchaseLenderName[value + "|" + value] = true;
        }
    }

    $scope.updateMortgageRateValue = function(isFrom) {
        if (isFrom) {
            cleanSelectedMortgage('From');
        } else {
            cleanSelectedMortgage('To');
        }

        if (isFrom && $scope.values.mortgageRateFrom) {
            $scope.selectedMortgageRate['From ' + $scope.values.mortgageRateFrom + '|f' + $scope.values.mortgageRateFrom] = true;
        } else if (!isFrom && $scope.values.mortgageRateTo){
            $scope.selectedMortgageRate['To ' + $scope.values.mortgageRateTo + '|t' + $scope.values.mortgageRateTo] = true;
        }
    }

    $scope.updateMostRecentMortgageRateValue = function(isFrom) {
        if (isFrom) {
            cleanMostRecentSelectedMortgage('From');
        } else {
            cleanMostRecentSelectedMortgage('To');
        }

        if (isFrom && $scope.values.mostRecentMortgageInterestRateFrom) {
            $scope.selectedMostRecentMortgageInterestRates['From ' + $scope.values.mostRecentMortgageInterestRateFrom + '|f' + $scope.values.mostRecentMortgageInterestRateFrom] = true;
        } else if (!isFrom && $scope.values.mostRecentMortgageInterestRateTo){
            $scope.selectedMostRecentMortgageInterestRates['To ' + $scope.values.mostRecentMortgageInterestRateTo + '|t' + $scope.values.mostRecentMortgageInterestRateTo] = true;
        }
    }

    var cleanSelectedMortgage = function(startsWith) {
        for (prop in $scope.selectedMortgageRate) {
            if (prop.indexOf(startsWith) == 0) {
                $scope.selectedMortgageRate[prop] = undefined;
            }
        }
    }

    var cleanMostRecentSelectedMortgage = function(startsWith) {
        for (prop in $scope.selectedMostRecentMortgageInterestRates) {
            if (prop.indexOf(startsWith) == 0) {
                $scope.selectedMostRecentMortgageInterestRates[prop] = undefined;
            }
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.mortgageRate) {
        for (var i = 0; i < $scope.searchRequest.mortgageRate.length; i++) {
            var value = $scope.searchRequest.mortgageRate[i];
            if (value.indexOf('f') == 0) {
                $scope.selectedMortgageRate[value.replace('f', 'From ') + "|" + value] = true;
            } else if (value.indexOf('t') == 0) {
                $scope.selectedMortgageRate[value.replace('t', 'To ') + "|" + value] = true;
            } else {
                $scope.selectedMortgageRate[value + "|" + value] = true;
            }
        }
    }

    $scope.$on('mortgageRateChanged', function(event, args){
        var fromValue = '';
        var toValue = '';

        for (prop in $scope.selectedMortgageRate) {
            if (prop.indexOf('From') == 0 && $scope.selectedMortgageRate[prop]) {
                fromValue = prop.split('|')[1].split('f')[1];
            }

            if (prop.indexOf('To') == 0 && $scope.selectedMortgageRate[prop]) {
                toValue = prop.split('|')[1].split('t')[1];
            }
        }

        $scope.values.mortgageRateFrom = fromValue;
        $scope.values.mortgageRateTo = toValue;
    });
    $scope.$broadcast('mortgageRateChanged');

    $scope.addValue = function() {
        if (!$scope.values.value) { return; }

        var values = $scope.values.value.split(",");
        for (var i = 0; i < values.length; i++) {
            var value = values[i].trim();
            if (value) {
                if ($scope.searchConfig.option === 'mortgageRate') {
                    $scope.selectedMortgageRate[value + "|" + value] = value;
                } else if ($scope.searchConfig.option === 'censusMedianHouseHoldIncome') {
                    $scope.selectedCensusMedianHouseHoldIncome[value + "|" + value] = value;
                } else if ($scope.searchConfig.option === 'censusMedianHomeValue') {
                    $scope.selectedCensusMedianHomeValue[value + "|" + value] = value;
                } else if ($scope.searchConfig.option === 'mostRecentLenderCode') {
                    $scope.selectedMostRecentLenderCodes[value + "|" + value] = value;
                } else if ($scope.searchConfig.option === 'mostRecentMortgageInterestRate') {
                    $scope.selectedMostRecentMortgageInterestRates[value + "|" + value] = value;
                } else if ($scope.searchConfig.option === 'loanToValue') {
                    $scope.selectedLoanToValues[value + "|" + value] = value;
                }
            }
        }

        $scope.values.value = undefined;
    }

    if ($scope.searchRequest && $scope.searchRequest.censusMedianHouseHoldIncome) {
        for (var i = 0; i < $scope.searchRequest.censusMedianHouseHoldIncome.length; i++) {
            var value = $scope.searchRequest.censusMedianHouseHoldIncome[i];
            $scope.selectedCensusMedianHouseHoldIncome[value + "|" + value] = true;
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.censusMedianHomeValue) {
        for (var i = 0; i < $scope.searchRequest.censusMedianHomeValue.length; i++) {
            var value = $scope.searchRequest.censusMedianHomeValue[i];
            $scope.selectedCensusMedianHomeValue[value + "|" + value] = true;
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.mostRecentLenderCodes) {
        for (var i = 0; i < $scope.searchRequest.mostRecentLenderCodes.length; i++) {
            var value = $scope.searchRequest.mostRecentLenderCodes[i];
            $scope.selectedMostRecentLenderCodes[value + "|" + value] = true;
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.mostRecentMortgageInterestRates) {
        for (var i = 0; i < $scope.searchRequest.mostRecentMortgageInterestRates.length; i++) {
            var value = $scope.searchRequest.mostRecentMortgageInterestRates[i];
            $scope.selectedMostRecentMortgageInterestRates[value + "|" + value] = true;
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.loanToValues) {
        for (var i = 0; i < $scope.searchRequest.loanToValues.length; i++) {
            var value = $scope.searchRequest.loanToValues[i];
            $scope.selectedLoanToValues[value + "|" + value] = true;
        }
    }

    $scope.tabs = [
        //{value: "homePurchasePrice", title: "Home Purchase Price"},
        {value: "homePurchasedDate", title: "Home Purchased Date"},
        {value: "homeYearBuilt", title: "Home Year Built"},
        {value: "estimatedCurrentHomeValueCode", title: "Estimated Home Value"},
        //{value: "mortgageAmountInThousands", title: "Mortgage Amount In Thousands"},
        {value: "mortgageLenderName", title: "Mortgage Lender Name"},
        {value: "refinanceLeaderName", title: "Refinance Lender Name"},
        //{value: "purchaseLenderName", title: "Purchase Lender Name"},
        {value: "mortgageRate", title: "Mortgage Rate"},
        {value: "mostRecentMortgageInterestRate", title: "Most Recent Mortgage Interest Rate"},
        {value: "mortgageRateType", title: "Mortgage Rate Type"},
        {value: "mortgageLoanType", title: "Mortgage Loan Type"},
        {value: "transactionType", title: "Transaction Type"},
        {value: "deedDateOfRefinance", title: "Deed Date Of Refinance"},
        //{value: "refinanceAmountInThousands", title: "Refinance Amount In Thousands"},
        {value: "refinanceRateType", title: "Refinance Rate Type"},
        {value: "refinanceLoanType", title: "Refinance Loan Type"},
        //{value: "censusMedianHomeValue", title: "Census Median Home Value"},
        //{value: "censusMedianHouseHoldIncome", title: "Census Median House Hold Income"},
        //{value: "craIncomeClassificationCode", title: "CRA Income Classification Code"},
        {value: "purchaseMortgageDate", title: "Purchase Mortgage Date"},
        //{value: "mostRecentLenderCode", title: "Most Recent Lender Code"},
        {value: "loanToValue", title: "Loan To Value"}
    ];

    $scope.homePurchasedPrices = [
        {value: "1", title: "Home Purchase Price under  $75k"},
        {value: "2", title: "Home Purchase Price $76k - $125K"},
        {value: "3", title: "Home Purchase Price $126k- $175k"},
        {value: "4", title: "Home Purchase Price $176k- $250k"},
        {value: "5", title: "Home Purchase Price $251k- $325k"},
        {value: "6", title: "Home Purchase Price $326k- $425k"},
        {value: "7", title: "Home Purchase Price $426k- $550k"},
        {value: "8", title: "Home Purchase Price $551k- $750k"},
        {value: "9", title: "Home Purchase Price $751k- $999k"},
        {value: "10", title: "Home Purchase Price $1mm +"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.homePurchasePrices) {
        for (var i = 0; i < $scope.homePurchasedPrices.length; i++) {
            for (var j = 0; j < $scope.searchRequest.homePurchasePrices.length; j++) {
                if ($scope.homePurchasedPrices[i].value == $scope.searchRequest.homePurchasePrices[j]) {
                    var homePurchasedPrice = $scope.homePurchasedPrices[i];
                    $scope.selectedHomePurchasePrices[homePurchasedPrice.title + "|" + homePurchasedPrice.value] = true;
                    break;
                }
            }
        }
    }


    $scope.estimatedCurrentHomeValueCodes = [
        {value: "A", title: "$1,000 - $24,999"},
        {value: "B", title: "$25,000 - $49,999"},
        {value: "C", title: "$50,000 - $74,999"},
        {value: "D", title: "$75,000 - $99,999"},
        {value: "E", title: "$100,000 - $124,999"},
        {value: "F", title: "$125,000 - $149,999"},
        {value: "G", title: "$150,000 - $174,999"},
        {value: "H", title: "$175,000 - $199,999"},
        {value: "I", title: "$200,000 - $224,999"},
        {value: "J", title: "$225,000 - $249,999"},
        {value: "K", title: "$250,000 - $274,999"},
        {value: "L", title: "$275,000 - $299,999"},
        {value: "M", title: "$300,000 - $349,999"},
        {value: "N", title: "$350,000 - $399,999"},
        {value: "O", title: "$400,000 - $449,999"},
        {value: "P", title: "$450,000 - $499,999"},
        {value: "Q", title: "$500,000 - $749,999"},
        {value: "R", title: "$750,000 - $999,999"},
        {value: "S", title: "$1,000,000 Plus"},
    ];

    if ($scope.searchRequest && $scope.searchRequest.estimatedCurrentHomeValueCodes) {
        for (var i = 0; i < $scope.estimatedCurrentHomeValueCodes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.estimatedCurrentHomeValueCodes.length; j++) {
                if ($scope.estimatedCurrentHomeValueCodes[i].value == $scope.searchRequest.estimatedCurrentHomeValueCodes[j]) {
                    var code = $scope.estimatedCurrentHomeValueCodes[i];
                    $scope.selectedEstimatedCurrentHomeValueCodes[code.title + "|" + code.value] = true;
                    break;
                }
            }
        }
    }


    $scope.mortgageAmountInThousands = [
        {value: "1", title: "Mortgage Amount under  $75k"},
        {value: "2", title: "Mortgage Amount $76k - $125K"},
        {value: "3", title: "Mortgage Amount $126k- $175k"},
        {value: "4", title: "Mortgage Amount $176k- $250k"},
        {value: "5", title: "Mortgage Amount $251k- $325k"},
        {value: "6", title: "Mortgage Amount $326k- $425k"},
        {value: "7", title: "Mortgage Amount $426k- $550k"},
        {value: "8", title: "Mortgage Amount $551k- $750k"},
        {value: "9", title: "Mortgage Amount $751k- $999k"},
        {value: "10", title: "Mortgage Amount $1mm +"},
    ];

    if ($scope.searchRequest && $scope.searchRequest.mortgageAmountInThousands) {
        for (var i = 0; i < $scope.mortgageAmountInThousands.length; i++) {
            for (var j = 0; j < $scope.searchRequest.mortgageAmountInThousands.length; j++) {
                if ($scope.mortgageAmountInThousands[i].value == $scope.searchRequest.mortgageAmountInThousands[j]) {
                    var amount = $scope.mortgageAmountInThousands[i];
                    $scope.selectedMortgageAmountInThousands[amount.title + "|" + amount.value] = true;
                    break;
                }
            }
        }
    }


    $scope.mortgageRateTypes = [
        {value: "A", title: "Adjustable"},
        {value: "B", title: "Balloon"},
        {value: "F", title: "Fixed"},
    ];

    if ($scope.searchRequest && $scope.searchRequest.mortgageRateTypes) {
        for (var i = 0; i < $scope.mortgageRateTypes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.mortgageRateTypes.length; j++) {
                if ($scope.mortgageRateTypes[i].value == $scope.searchRequest.mortgageRateTypes[j]) {
                    var type = $scope.mortgageRateTypes[i];
                    $scope.selectedMortgageRateTypes[type.title + "|" + type.value] = true;
                    break;
                }
            }
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.refinanceRateTypes) {
        for (var i = 0; i < $scope.mortgageRateTypes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.refinanceRateTypes.length; j++) {
                if ($scope.mortgageRateTypes[i].value == $scope.searchRequest.refinanceRateTypes[j]) {
                    var type = $scope.mortgageRateTypes[i];
                    $scope.selectedRefinanceRateTypes[type.title + "|" + type.value] = true;
                    break;
                }
            }
        }
    }


    $scope.mortgageLoanTypes = [
        {value: "5", title: "Community Development Authority"},
        {value: "C", title: "Conventional"},
        {value: "F", title: "FHA"},
        {value: "P", title: "Private Party Lender"},
        {value: "S", title: "Small Business Administration"},
        {value: "V", title: "VA"},
        {value: "W", title: "Wrap-Around Mortgage"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.mortgageLoanTypes) {
        for (var i = 0; i < $scope.mortgageLoanTypes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.mortgageLoanTypes.length; j++) {
                if ($scope.mortgageLoanTypes[i].value == $scope.searchRequest.mortgageLoanTypes[j]) {
                    var type = $scope.mortgageLoanTypes[i];
                    $scope.selectedMortgageLoanTypes[type.title + "|" + type.value] = true;
                    break;
                }
            }
        }
    }

    if ($scope.searchRequest && $scope.searchRequest.refinanceLoanTypes) {
        for (var i = 0; i < $scope.mortgageLoanTypes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.refinanceLoanTypes.length; j++) {
                if ($scope.mortgageLoanTypes[i].value == $scope.searchRequest.refinanceLoanTypes[j]) {
                    var type = $scope.mortgageLoanTypes[i];
                    $scope.selectedRefinanceLoanTypes[type.title + "|" + type.value] = true;
                    break;
                }
            }
        }
    }


    $scope.transactionTypes = [
        {value: "C", title: "Construction Loan"},
        {value: "N", title: "Subdivision/New Construction"},
        {value: "R", title: "Resale"},
        {value: "S", title: "Seller Carryback"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.transactionTypes) {
        for (var i = 0; i < $scope.transactionTypes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.transactionTypes.length; j++) {
                if ($scope.transactionTypes[i].value == $scope.searchRequest.transactionTypes[j]) {
                    var type = $scope.transactionTypes[i];
                    $scope.selectedTransactionTypes[type.title + "|" + type.value] = true;
                    break;
                }
            }
        }
    }


    $scope.refinanceAmountInThousands = [
        {value: "1", title: "Refi Loan Amount under  $75k"},
        {value: "2", title: "Refi Loan Amount $76k - $125K"},
        {value: "3", title: "Refi Loan Amount $126k- $175k"},
        {value: "4", title: "Refi Loan Amount $176k- $250k"},
        {value: "5", title: "Refi Loan Amount $251k- $325k"},
        {value: "6", title: "Refi Loan Amount $326k- $425k"},
        {value: "7", title: "Refi Loan Amount $426k- $550k"},
        {value: "8", title: "Refi Loan Amount $551k- $750k"},
        {value: "9", title: "Refi Loan Amount $751k- $999k"},
        {value: "10", title: "Refi Loan Amount $1mm +"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.refinanceAmountInThousands) {
        for (var i = 0; i < $scope.refinanceAmountInThousands.length; i++) {
            for (var j = 0; j < $scope.searchRequest.refinanceAmountInThousands.length; j++) {
                if ($scope.refinanceAmountInThousands[i].value == $scope.searchRequest.refinanceAmountInThousands[j]) {
                    var amount = $scope.refinanceAmountInThousands[i];
                    $scope.selectedRefinanceAmountInThousands[amount.title + "|" + amount.value] = true;
                    break;
                }
            }
        }
    }

    $scope.craIncomeClassificationCodes = [
        {value: "1", title: "Low Income"},
        {value: "2", title: "Moderate Income"},
        {value: "3", title: "Middle Income"},
        {value: "4", title: "High Income"}
    ];

    if ($scope.searchRequest && $scope.searchRequest.craIncomeClassificationCodes) {
        for (var i = 0; i < $scope.craIncomeClassificationCodes.length; i++) {
            for (var j = 0; j < $scope.searchRequest.craIncomeClassificationCodes.length; j++) {
                if ($scope.craIncomeClassificationCodes[i].value == $scope.searchRequest.craIncomeClassificationCodes[j]) {
                    var code = $scope.craIncomeClassificationCodes[i];
                    $scope.selectedCraIncomeClassificationCodes[code.title + "|" + code.value] = true;
                    break;
                }
            }
        }
    }
}
]);
