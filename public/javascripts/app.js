//import mainServerAddress} from "../util/constants";

angular.module( 'consumer_data_base', [ 'ngResource', 'ngCookies', 'ui.bootstrap', 'ui.router', 'ui.utils.masks', 'angular-toasty', 'angularAwesomeSlider' ] )

.provider('BASE_URL', [function() {
    this.$get = function() {
        if (document.URL.indexOf('localhost') != -1) {
            return '';
        }else {
        //    return "http://dev.wsdevworld.com:9000";
          return "https://www.makemydata.com";
        }
    }
}])
.config(function($sceDelegateProvider) {
    $sceDelegateProvider.resourceUrlWhitelist([
        'self',
        'https://www.makemydata.com/**',
        'https://youtube.com/**',
      //  "http://dev.wsdevworld.com:9000"+"/**"
        "https://www.makemydata.com"+"/**"
    ])
})
.config( function( $stateProvider, $urlRouterProvider ) {
     $urlRouterProvider.otherwise( '/login' );

     var BASE_URL = '';
     if (document.URL.indexOf('localhost') == -1) {

     //    BASE_URL = "http://dev.wsdevworld.com:9000";
       BASE_URL = "https://www.makemydata.com";

     }

     var isMarketing = document.URL.indexOf('makedatalist') != -1 ||
                       document.URL.indexOf('makethedata') != -1 ||
                       document.URL.indexOf('allwebreferrals') != -1 ||
                       document.URL.indexOf('mytargetdata') != -1 ||
                       document.URL.indexOf('sales-list') != -1;

     $stateProvider
         .state( 'main', {
             url: '/dashboard',
             templateUrl: BASE_URL + '/assets/partials/dashboard.html',
             controller: 'DashboardController',
             params: {searchRequest: null}
         } )
         .state( 'mobile', {
             url: '/mobile',
             templateUrl: BASE_URL + '/assets/partials/mobile.html',
             controller: 'MobileDashboardController'
         } )
         /*.state('mobile_purchased', {
             url: '/mobile/purchased',
             templateUrl: BASE_URL + '/assets/partials/mobile/lists.html',
             controller: 'MobileListsController'
         })
         .state('mobile_profile', {
             url: '/mobile/profile',
             templateUrl: BASE_URL + '/assets/partials/mobile/profile.html',
             controller: 'MobileProfileController'
         })*/
         .state( 'administration', {
             url: '/administration',
             templateUrl: BASE_URL + '/assets/partials/administration.html',
             controller: 'AdministrationController'
         } )
         .state( 'all_purchased_tables_logs', {
             url: '/all_purchased_tables_logs',
             templateUrl: BASE_URL + '/assets/partials/all_purchased_tables_logs.html',
             controller: 'AllPurchasedTablesLogsController'
         } )
         .state( 'all_lists_sent', {
             url: '/all_lists_sent',
             templateUrl: BASE_URL + '/assets/partials/all_lists_sent.html',
             controller: 'AllListSentController'
         } )
         .state( 'update', {
             url: '/update',
             templateUrl: BASE_URL + '/assets/partials/update.data.html',
             controller: 'UpdateDataController'
         } )
         .state( 'purchased', {
             url: '/purchased',
             templateUrl: BASE_URL + '/assets/partials/lists/purchased.html',
             controller: 'ListsController'
         } )
         .state( 'all_purchased', {
             url: '/purchased/all',
             templateUrl: BASE_URL + '/assets/partials/lists/purchased.html',
             controller: 'ListsController'
         } )
         .state( 'non_purchased', {
              url: '/non/purchased',
              templateUrl: BASE_URL + '/assets/partials/lists/non.purchased.html',
              controller: 'ListsController'
         } )
         .state( 'uploaded', {
              url: '/uploaded',
              templateUrl: BASE_URL + '/assets/partials/lists/uploaded.html',
              controller: 'UploadedListsController'
         } )
         .state( 'uploadForMatching', {
              url: '/matching',
              templateUrl: BASE_URL + '/assets/partials/lists/upload.for.matching.html',
              controller: 'UploadListForMatchingController'
         } )
         .state( 'login', {
             url: '/login?token&pas_token&login_token',
             templateUrl: isMarketing ? 'login.html' : (BASE_URL + '/assets/partials/login.html'),
             controller: 'LoginController'
         } )
         .state( 'profile', {
             url: '/profile',
             templateUrl: BASE_URL + '/assets/partials/profile.html',
             controller: 'ProfileController'
         } )
         .state( 'users', {
             url: '/users',
             templateUrl: BASE_URL + '/assets/partials/users.html',
             controller: 'UsersController'
         } )
         .state( 'payments', {
             url: '/payments',
             templateUrl: BASE_URL + '/assets/partials/payments.html',
             controller: 'PaymentsController',
             params: { username: '' }
         } )
         .state( 'paymentRequests', {
             url: '/payment/requests',
             templateUrl: BASE_URL + '/assets/partials/payment.requests.html',
             controller: 'PaymentRequestsController'
         } )
         .state( 'prefixes', {
             url: '/prefixes',
             templateUrl: BASE_URL + '/assets/partials/prefixes.html',
             controller: 'PrefixesController',
             params: { username: '' }
         } )
         .state( 'password_recovery', {
             url: '/recovery/password',
             templateUrl: BASE_URL + '/assets/partials/password.recovery.html',
             controller: 'RecoveryPasswordController'
         } )
         .state( 'prices', {
             url: '/prices',
             templateUrl: BASE_URL + '/assets/partials/prices.html',
             controller: 'PricesController'
         } )
         .state( 'dataSources', {
            url: '/datasources',
            templateUrl: BASE_URL + '/assets/partials/data.sources.html',
            controller: 'DataSourcesController'
         })
         .state('registrationRequests', {
             url: '/registration/requests',
             templateUrl: BASE_URL + '/assets/partials/registration.requests.html',
             controller: 'RegistrationRequestsController'
         })
         .state('comments', {
             url: '/comments',
             templateUrl: BASE_URL + '/assets/partials/comments.html',
             controller: 'CommentsController'
         })
         .state('terms', {
             url: '/terms',
             templateUrl: BASE_URL + '/assets/partials/end_user_agreement.html'
         })

} )
.config( function( $httpProvider ) {
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

    $httpProvider.interceptors.push( function( $q, $injector ) {
        return {
            'responseError': function( rejection ) {
                if ( rejection.status === 403 ) {
                    $injector.get( 'credentialsService' ).logout();

                    if (isCalienteLeads()) {
                        document.location.href = 'http://axiomleads.com';
                    } else if (isMultimediaListsCom()) {
                        document.location.href = 'https://multimedialists.com';
                    } else if (isMultimediaListsNet()) {
                        document.location.href = 'https://multimedialists.net';
                    } else {
                        $injector.get( '$state' ).transitionTo( 'login' );
                    }

                    return new Promise( function() {} );
                }

                return $q.reject( rejection );
            }
        };
    } );

    $httpProvider.defaults.timeout = 500000;
    $httpProvider.defaults.cache = false;
    if ( !$httpProvider.defaults.headers.get )
        $httpProvider.defaults.headers.get = {};

    $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Thu, 01 Jan 1970 00:00:00 GMT';
    $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
} )
.config( [ 'toastyConfigProvider', function( toastyConfigProvider ) {
	toastyConfigProvider.setConfig( {
		limit: 1
	} );
} ] )
.run( function( $rootScope, $state, $stateParams, credentialsService ) {
    $rootScope.$state = $state;
    $rootScope.areaState = "AK";
    $rootScope.$stateParams = $stateParams;

    $rootScope.types = {
        'consumers': 0,
        'business': 1,
        'directory': 2,
        'craigslist': 3,
        'WHOIS': 4,
        'search engine': 5,
        'consumer2018': 6,
        'instagram': 7,
        'OPT IN': 8,
        'consumer': 9,
        'AUTO': 10,
        'blacklist': 11,
        'linked in': 12,
        'business detailed': 13,
        'instagram2020': 14,
        'student': 15,
        'callleads': 16,
        'healthbuyers': 17,
        'business2': 18,
        'healthinsuranceleads': 19,
        'Opt-In': 20,
        'facebook': 21,
        'phildirectory':22,
        'everydata':23
    };

    $rootScope.reversedTypes = {
        0: 'consumers',
        1: 'business',
        2: 'directory',
        3: 'craigslist',
        4: 'WHOIS',
        5: 'search engine',
        6: 'consumer2018',
        7: 'instagram',
        8: 'OPT IN',
        9: 'consumer',
        10: 'AUTO',
        11: 'blacklist',
        12: 'linked in',
        13: 'business detailed',
        14: 'instagram2020',
        15: 'student',
        16: 'callleads',
        17: 'healthbuyers',
        18: 'business2',
        19: 'healthinsuranceleads',
        20: 'Opt-In',
        21: 'facebook',
        22:'phildirectory',
        23:'everydata'
    };

    $rootScope.occupations = [
        {"value": "A197", "title": "Navy Credit Union Trades"},
        {"value": "A198", "title": "Army Credit Union Trades"},
        {"value": "A199", "title": "Armed Forces"},
        {"value": "C120", "title": "Police/Trooper"},
        {"value": "C121", "title": "Mail/Postmaster"},
        {"value": "C122", "title": "Mail Carrier/Postal"},
        {"value": "C123", "title": "Judge/Referee"},
        {"value": "C124", "title": "Firefighter"},
        {"value": "C125", "title": "Court Reporter"},
        {"value": "C126", "title": "Corrections/Probation/Parole"},
        {"value": "C127", "title": "Civil Service/Government"},
        {"value": "C128", "title": "Air Traffic Control"},
        {"value": "E793", "title": "Vice President"},
        {"value": "E794", "title": "Treasurer"},
        {"value": "E795", "title": "President"},
        {"value": "E796", "title": "Politician/Legislator/Diplomat"},
        {"value": "E797", "title": "Comptroller"},
        {"value": "E798", "title": "CEO/CFO/Chairman/Corp Officer"},
        {"value": "E799", "title": "Executive/Upper Management"},
        {"value": "H326", "title": "Social Worker/Case Worker"},
        {"value": "H327", "title": "Nurse/LPN"},
        {"value": "H328", "title": "Nurse (Registered)"},
        {"value": "H329", "title": "Nurse"},
        {"value": "H330", "title": "Therapists/Physical"},
        {"value": "H331", "title": "Therapist"},
        {"value": "H332", "title": "Technician/X-ray"},
        {"value": "H333", "title": "Technician/Lab"},
        {"value": "H334", "title": "Psychologist"},
        {"value": "H335", "title": "Pharmacist/Pharmacy"},
        {"value": "H336", "title": "Optometrist"},
        {"value": "H337", "title": "Optician"},
        {"value": "H338", "title": "Nurses Aide/Orderly"},
        {"value": "H339", "title": "Medical/Paramedic"},
        {"value": "H340", "title": "Medical Technician"},
        {"value": "H341", "title": "Medical Secretary"},
        {"value": "H342", "title": "Medical Assistant"},
        {"value": "H343", "title": "Health Care"},
        {"value": "H344", "title": "Dietician"},
        {"value": "H345", "title": "Dentist"},
        {"value": "H346", "title": "Dental Hygienist"},
        {"value": "H347", "title": "Dental Assistant"},
        {"value": "H348", "title": "Chiropractor"},
        {"value": "I143", "title": "Trainer"},
        {"value": "I144", "title": "Teacher"},
        {"value": "I145", "title": "Professor"},
        {"value": "I146", "title": "Lecturer"},
        {"value": "I147", "title": "Instructor"},
        {"value": "I148", "title": "Counselor"},
        {"value": "I149", "title": "Coach"},
        {"value": "L394", "title": "Welder"},
        {"value": "L395", "title": "Waiter/Waitress"},
        {"value": "L396", "title": "Utility"},
        {"value": "L397", "title": "Upholstery"},
        {"value": "L398", "title": "Typesetter"},
        {"value": "L399", "title": "Transportation"},
        {"value": "L400", "title": "Toolmaker"},
        {"value": "L401", "title": "Sorter"},
        {"value": "L402", "title": "Shipping/Import/Export/Custom"},
        {"value": "L403", "title": "Sheet Metal Worker/Steel Worker"},
        {"value": "L404", "title": "Setup man"},
        {"value": "L405", "title": "Seamstress/Tailor/Handicraft"},
        {"value": "L406", "title": "Sanitation/Exterminator"},
        {"value": "L407", "title": "Roofer"},
        {"value": "L408", "title": "Repairman"},
        {"value": "L409", "title": "Production"},
        {"value": "L410", "title": "Printer"},
        {"value": "L411", "title": "Presser"},
        {"value": "L412", "title": "Press Operator"},
        {"value": "L413", "title": "Porter"},
        {"value": "L414", "title": "Polisher"},
        {"value": "L415", "title": "Plumber"},
        {"value": "L416", "title": "Pipe fitter"},
        {"value": "L417", "title": "Parts (Auto Etc.)"},
        {"value": "L418", "title": "Painter"},
        {"value": "L419", "title": "Packer"},
        {"value": "L420", "title": "Operator/Machine Operator"},
        {"value": "L421", "title": "Operator/Forklift Operator"},
        {"value": "L422", "title": "Operator/Crane Operator"},
        {"value": "L423", "title": "Operator/Boilermaker"},
        {"value": "L424", "title": "Operator"},
        {"value": "L425", "title": "Oil Industry/Driller"},
        {"value": "L426", "title": "Mold Maker/Molder/Injection Mold"},
        {"value": "L427", "title": "Miner"},
        {"value": "L428", "title": "Millwright"},
        {"value": "L429", "title": "Mill worker"},
        {"value": "L430", "title": "Meter Reader"},
        {"value": "L431", "title": "Mechanic"},
        {"value": "L432", "title": "Material Handler"},
        {"value": "L433", "title": "Mason/Brick/Etc."},
        {"value": "L434", "title": "Maintenance/Supervisor"},
        {"value": "L435", "title": "Maintenance"},
        {"value": "L436", "title": "Machinist"},
        {"value": "L437", "title": "Locksmith"},
        {"value": "L438", "title": "Loader"},
        {"value": "L439", "title": "Lithographer"},
        {"value": "L440", "title": "Lineman"},
        {"value": "L441", "title": "Laborer"},
        {"value": "L442", "title": "Journeyman"},
        {"value": "L443", "title": "Janitor"},
        {"value": "L444", "title": "Ironworker"},
        {"value": "L445", "title": "Installer"},
        {"value": "L446", "title": "Inspector"},
        {"value": "L447", "title": "Housekeeper/Maid"},
        {"value": "L448", "title": "Helper"},
        {"value": "L449", "title": "Grocer"},
        {"value": "L450", "title": "Grinder"},
        {"value": "L451", "title": "Glazier"},
        {"value": "L452", "title": "Gardener/Landscaper"},
        {"value": "L453", "title": "Furrier"},
        {"value": "L454", "title": "Foundry Worker"},
        {"value": "L455", "title": "Forestry"},
        {"value": "L456", "title": "Foreman/Shop Foreman"},
        {"value": "L457", "title": "Foreman/Crew leader"},
        {"value": "L458", "title": "Food Service"},
        {"value": "L459", "title": "Fitter"},
        {"value": "L460", "title": "Fisherman/Seaman"},
        {"value": "L461", "title": "Finisher"},
        {"value": "L462", "title": "Farmer/Dairyman"},
        {"value": "L463", "title": "Factory Workman"},
        {"value": "L464", "title": "Fabricator"},
        {"value": "L465", "title": "Electrician"},
        {"value": "L466", "title": "Driver/Truck Driver"},
        {"value": "L467", "title": "Driver/Bus Driver"},
        {"value": "L468", "title": "Driver"},
        {"value": "L469", "title": "Dock Worker"},
        {"value": "L470", "title": "Cutter"},
        {"value": "L471", "title": "Custodian"},
        {"value": "L472", "title": "Crewman"},
        {"value": "L473", "title": "Courier/Delivery/Messenger"},
        {"value": "L474", "title": "Cosmetologist"},
        {"value": "L475", "title": "Cook"},
        {"value": "L476", "title": "Construction"},
        {"value": "L477", "title": "Conductor"},
        {"value": "L478", "title": "Clerk/Stock"},
        {"value": "L479", "title": "Clerk/Produce"},
        {"value": "L480", "title": "Clerk/Deli"},
        {"value": "L481", "title": "Cleaner/Laundry"},
        {"value": "L482", "title": "Child Care/Day Care/Babysitter"},
        {"value": "L483", "title": "Chef/Butler"},
        {"value": "L484", "title": "Carpenter/Furniture/Woodworking"},
        {"value": "L485", "title": "Butcher/Meat Cutter"},
        {"value": "L486", "title": "Brewer"},
        {"value": "L487", "title": "Brakeman"},
        {"value": "L488", "title": "Bodyman"},
        {"value": "L489", "title": "Binder"},
        {"value": "L490", "title": "Bartender"},
        {"value": "L491", "title": "Barber/Hairstylist/Beautician"},
        {"value": "L492", "title": "Baker"},
        {"value": "L493", "title": "Auto Mechanic"},
        {"value": "L494", "title": "Attendant"},
        {"value": "L495", "title": "Athlete/Professional"},
        {"value": "L496", "title": "Assembler"},
        {"value": "L497", "title": "Apprentice"},
        {"value": "L498", "title": "Animal Technician/Groomer"},
        {"value": "M673", "title": "Supervisor"},
        {"value": "M674", "title": "Superintendent"},
        {"value": "M675", "title": "Principal/Dean/Educator"},
        {"value": "M676", "title": "Planner"},
        {"value": "M677", "title": "Manager/Warehouse Manager"},
        {"value": "M678", "title": "Manager/Traffic Manager"},
        {"value": "M679", "title": "Manager/Store Manager"},
        {"value": "M680", "title": "Manager/Sales Manager"},
        {"value": "M681", "title": "Manager/Regional Manager"},
        {"value": "M682", "title": "Manager/Property Manager"},
        {"value": "M683", "title": "Manager/Project Manager"},
        {"value": "M684", "title": "Manager/Product Manager"},
        {"value": "M685", "title": "Manager/Plant Manager"},
        {"value": "M686", "title": "Manager/Office Manager"},
        {"value": "M687", "title": "Manager/Marketing Manager"},
        {"value": "M688", "title": "Manger/General Manager"},
        {"value": "M689", "title": "Manager/Division Manager"},
        {"value": "M690", "title": "Manager/District Manager"},
        {"value": "M691", "title": "Manager/Credit Manager"},
        {"value": "M692", "title": "Manager/Branch Manager"},
        {"value": "M693", "title": "Manager/Assistant Manager"},
        {"value": "M694", "title": "Manager"},
        {"value": "M695", "title": "Editor"},
        {"value": "M696", "title": "Director/Executive Director"},
        {"value": "M697", "title": "Director/Art Director"},
        {"value": "M698", "title": "Account Executive"},
        {"value": "P244", "title": "Volunteer"},
        {"value": "P245", "title": "Student"},
        {"value": "P246", "title": "Part Time"},
        {"value": "P247", "title": "Retired/Pensioner"},
        {"value": "P249", "title": "Homemaker"},
        {"value": "S295", "title": "Data Entry/Key Punch"},
        {"value": "S296", "title": "Typist"},
        {"value": "S297", "title": "Secretary"},
        {"value": "S298", "title": "Legal Secretary"},
        {"value": "S299", "title": "Legal/Paralegal/Assistant"},
        {"value": "T896", "title": "Computer/Systems Analyst"},
        {"value": "T897", "title": "Computer Programmer"},
        {"value": "T898", "title": "Computer Operator"},
        {"value": "T978", "title": "Veterinarian"},
        {"value": "T979", "title": "Statistician/Actuary"},
        {"value": "T980", "title": "Scientist"},
        {"value": "T981", "title": "Pilot"},
        {"value": "T982", "title": "Pastor"},
        {"value": "T983", "title": "Medical Doctor/Physician"},
        {"value": "T984", "title": "Librarian/Archivist"},
        {"value": "T985", "title": "Legal/Attorney/Lawyer"},
        {"value": "T987", "title": "Geologist"},
        {"value": "T988", "title": "Engineer/Mechanical"},
        {"value": "T989", "title": "Engineer/Industrial"},
        {"value": "T990", "title": "Engineer/Field"},
        {"value": "T991", "title": "Engineer/Electrical/Electronic"},
        {"value": "T992", "title": "Engineer/Civil"},
        {"value": "T993", "title": "Engineer/Chemical"},
        {"value": "T994", "title": "Engineer/Aerospace"},
        {"value": "T995", "title": "Engineer"},
        {"value": "T996", "title": "Curator"},
        {"value": "T997", "title": "Chemist"},
        {"value": "T998", "title": "Architect"},
        {"value": "W520", "title": "Writer"},
        {"value": "W521", "title": "Water Treatment"},
        {"value": "W522", "title": "Ward Clerk"},
        {"value": "W523", "title": "Union Member/Rep."},
        {"value": "W524", "title": "Travel Agent"},
        {"value": "W525", "title": "Transcripter/Translator"},
        {"value": "W526", "title": "Tester"},
        {"value": "W527", "title": "Teller/Bank Teller"},
        {"value": "W528", "title": "Telemarketer/Telephone/Operator"},
        {"value": "W529", "title": "Technician"},
        {"value": "W530", "title": "Surveyor"},
        {"value": "W531", "title": "Security"},
        {"value": "W532", "title": "Sales Clerk/Counterman"},
        {"value": "W533", "title": "Sales"},
        {"value": "W534", "title": "Researcher"},
        {"value": "W535", "title": "Reporter"},
        {"value": "W536", "title": "Receptionist"},
        {"value": "W537", "title": "Real Estate/Realtor"},
        {"value": "W538", "title": "Quality Control"},
        {"value": "W539", "title": "Purchasing"},
        {"value": "W540", "title": "Publishing"},
        {"value": "W541", "title": "Public Relations"},
        {"value": "W542", "title": "Photography"},
        {"value": "W543", "title": "Personnel/Recruiter/Interviewer"},
        {"value": "W544", "title": "Musician/Music/Dance"},
        {"value": "W545", "title": "Model"},
        {"value": "W546", "title": "Merchandiser"},
        {"value": "W547", "title": "Marketing"},
        {"value": "W548", "title": "Jeweler"},
        {"value": "W549", "title": "Interior Designer"},
        {"value": "W550", "title": "Insurance/Underwriter"},
        {"value": "W551", "title": "Insurance/Agent"},
        {"value": "W552", "title": "Hostess/Host/Usher"},
        {"value": "W553", "title": "Graphic Designer/Commercial Artist"},
        {"value": "W554", "title": "Florist"},
        {"value": "W555", "title": "Flight Attendant/Steward"},
        {"value": "W556", "title": "Finance"},
        {"value": "W557", "title": "Expeditor"},
        {"value": "W558", "title": "Estimator"},
        {"value": "W559", "title": "Draftsman"},
        {"value": "W560", "title": "Dispatcher"},
        {"value": "W561", "title": "Detective/Investigator"},
        {"value": "W562", "title": "Designer"},
        {"value": "W563", "title": "Customer Service/Representative"},
        {"value": "W564", "title": "Coordinator"},
        {"value": "W565", "title": "Consultant/Advisor"},
        {"value": "W566", "title": "Conservation/Environment"},
        {"value": "W567", "title": "Communications"},
        {"value": "W568", "title": "Collector"},
        {"value": "W569", "title": "Clerk/File"},
        {"value": "W570", "title": "Clerk"},
        {"value": "W571", "title": "Claims Examiner/Rep/Adjudicator"},
        {"value": "W572", "title": "Checker"},
        {"value": "W573", "title": "Caterer"},
        {"value": "W574", "title": "Cashier"},
        {"value": "W575", "title": "Buyer"},
        {"value": "W576", "title": "Broker/Stock/Trader"},
        {"value": "W577", "title": "Broker"},
        {"value": "W578", "title": "Bookkeeper"},
        {"value": "W579", "title": "Banker/Loan Processor"},
        {"value": "W580", "title": "Banker/Loan Office"},
        {"value": "W581", "title": "Banker"},
        {"value": "W582", "title": "Auditor"},
        {"value": "W583", "title": "Auctioneer"},
        {"value": "W584", "title": "Artist"},
        {"value": "W585", "title": "Appraiser"},
        {"value": "W586", "title": "Analyst"},
        {"value": "W587", "title": "Aide/Assistant/Technical"},
        {"value": "W588", "title": "Aide/Assistant/Staff"},
        {"value": "W589", "title": "Aide/Assistant/School"},
        {"value": "W590", "title": "Aide/Assistant/Office"},
        {"value": "W591", "title": "Aide/Assistant/Executive"},
        {"value": "W592", "title": "Aide/Assistant"},
        {"value": "W593", "title": "Agent"},
        {"value": "W594", "title": "Advertising"},
        {"value": "W595", "title": "Administration/Management"},
        {"value": "W596", "title": "Adjuster"},
        {"value": "W597", "title": "Actor/Entertainer/Announcer"},
        {"value": "W598", "title": "Accounting/Biller/Billing clerk"}
    ];

    $rootScope.capitalize = function(str) {
        var splitStr = str.toLowerCase().split(' ');
        for (var i = 0; i < splitStr.length; i++) {
            splitStr[i] = splitStr[i].charAt(0).toUpperCase() + splitStr[i].substring(1);
        }

        return splitStr.join(' ');
    }

    var isCalienteLeads = function() {
        return document.URL.indexOf('axiomleads.com') != -1;
    }

    var isMultimediaListsCom = function() {
        return document.URL.indexOf('multimedialists.com') != -1;
    }

    var isMultimediaListsNet = function() {
        return document.URL.indexOf('multimedialists.net') != -1;
    }

    $rootScope.$on( '$stateChangeStart',
        function( event, toState, toParams, fromState, fromParams ){
            if ( toState.name !== 'password_recovery' && toState.name != 'registration' &&
                 toState.name != 'terms' ) {
                if ( !credentialsService.isLoggedIn() && toState.name !== 'login' ) {
                    event.preventDefault();

                    if (isCalienteLeads()) {
                        document.location.href = 'http://axiomleads.com';
                    } else if (isMultimediaListsCom()) {
                        document.location.href = 'https://multimedialists.com';
                    } else if (isMultimediaListsNet()) {
                        document.location.href = 'https://multimedialists.net';
                    } else {
                        $state.transitionTo( 'login' );
                    }

                    return;
                }
            }

            if ( credentialsService.isLoggedIn() &&
                ( toState.name == 'login' || toState.name == 'password_recovery' ) ) {
                event.preventDefault();

                var md = new MobileDetect( window.navigator.userAgent );
                if (md.mobile()) {
                    $state.transitionTo( 'mobile' );
                } else {
                    $state.transitionTo( 'main' );
                }

                return;
            }
        } );
} );

