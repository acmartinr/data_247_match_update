angular.module( 'consumer_data_base' ).
factory( 'dictionaryService', function( $resource, BASE_URL ) {
    return $resource( '', {}, {
        cities: { url: BASE_URL + '/rest/public/dictionary/cities/:state', method: 'GET' },
        zipCodes: { url: BASE_URL + '/rest/public/dictionary/zip/:state', method: 'GET' },
        areaCodes: { url: BASE_URL + '/rest/public/dictionary/code/:state', method: 'GET' },
        counties: { url: BASE_URL + '/rest/public/dictionary/counties/:state', method: 'GET' },
        industries: { url: BASE_URL + '/rest/public/dictionary/industries', method: 'GET' },
        detailedBusinessIndustries: { url: BASE_URL + '/rest/public/dictionary/detailed/business/industries', method: 'POST' },
        selectedDetailedBusinessIndustries: { url: BASE_URL + '/rest/public/dictionary/detailed/business/industries/selected', method: 'POST' },
        sources: { url: BASE_URL + '/rest/public/dictionary/categories', method: 'GET' },
        everydataSources: { url: BASE_URL + '/rest/public/dictionary/everydata_categories', method: 'GET' },
        lenders: { url: BASE_URL + '/rest/public/dictionary/lenders/:value', method: 'GET' },
        autoMakes: {url: BASE_URL + '/rest/public/dictionary/makes', method: 'GET'},
        domainSources: { url: BASE_URL + '/rest/public/dictionary/domainSources/:domainSource/:tableName', method: 'GET' },
        carrierBrand: { url: BASE_URL + '/rest/public/dictionary/carrierBrand/:tableName', method: 'GET' },
        c2CarriersName: { url: BASE_URL + '/rest/public/dictionary/c2carriersName/:name', method: 'GET' },
        facebookJobs: { url: BASE_URL + '/rest/public/dictionary/facebookJobs/:tableName', method: 'GET' },
        facebookHLName: { url: BASE_URL + '/rest/public/dictionary/facebookHLastName/:strSearch', method: 'GET' },
        autoModels: {url: BASE_URL + '/rest/public/dictionary/models', method: 'GET'},
        titles: {url: BASE_URL + '/rest/public/dictionary/titles', method: 'POST'},
        businessTitles: {url: BASE_URL + '/rest/public/dictionary/titles/business', method: 'POST'},
        categories: {url: BASE_URL + '/rest/public/dictionary/categories/instagram', method: 'POST'},
        optinSources: {url: BASE_URL + '/rest/public/dictionary/optin/sources', method: 'POST'},
        studentSources: { url: BASE_URL + '/rest/public/dictionary/student/sources', method: 'GET' }
    } );
} );