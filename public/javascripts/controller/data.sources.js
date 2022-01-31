angular.module( 'consumer_data_base' ).
controller( 'DataSourcesController',
[ '$scope', 'BASE_URL', 'administrationService', 'credentialsService', '$modal',
function( $scope, BASE_URL, administrationService, credentialsService, $modal ) {

    $scope.userId = credentialsService.getUser().id;
    $scope.userPassword = credentialsService.getUser().password;

    administrationService.protected($scope.userId, $scope.userPassword).
        dataSources(function(response) {
            $scope.dataSources = response.data;
        });

    $scope.changeDataSourceVisible = function(dataSource) {
        administrationService.protected($scope.userId, $scope.userPassword).
                updateDataSourceVisible({'id': dataSource.id, 'visible': !dataSource.visible}, function(response) {
            dataSource.visible = !dataSource.visible;
        });
    }

    $scope.openBlockedUsersModal = function(dataSource) {
        var modalInstance = $modal.open( {
            templateUrl: BASE_URL + '/assets/partials/modal/data.source.blocked.users.html',
            controller: 'DataSourceBlockedUsersController',
            resolve: {dataSource: function() { return dataSource;}}
        } );
    }
} ] )
.controller( 'DataSourceBlockedUsersController',
[ '$scope', 'administrationService', 'credentialsService', '$modalInstance', 'dataSource',
function( $scope, administrationService, credentialsService, $modalInstance, dataSource ) {
    $scope.userId = credentialsService.getUser().id;
    $scope.userPassword = credentialsService.getUser().password;
    $scope.datasourceVisibility = dataSource.visible;
    $scope.blockedUsers = {};

    administrationService.protected($scope.userId, $scope.userPassword).
        getDataSourceBlockedUsers({id: dataSource.id,state: $scope.datasourceVisibility}, function(response) {
            for (var i = 0; i < response.data.length; i++) {
                $scope.blockedUsers[response.data[i].userId] = true;
            }
        });

    $scope.config = { sortValue: 'username', sortDesc: false, page: 1, limit: 10 };

    $scope.search = function() {
        administrationService.protected( $scope.userId, $scope.userPassword ).
            getUserList( $scope.config, function( response ) {
                $scope.users = response.users;
                $scope.total = response.total;
            } );
    }
    $scope.search();

    $scope.searchByValue = function() {
        $scope.config.page = 1;
        $scope.search();
    }

    $scope.sortByField = function( value ) {
        if ( value === $scope.config.sortValue ) {
            $scope.config.sortDesc = !$scope.config.sortDesc;
        } else {
            $scope.config.sortDesc = false;
        }

        $scope.config.sortValue = value;
        $scope.config.page = 1;

        $scope.search();
    }

    $scope.saveBlockedUsers = function() {
        var userIds = [];
        for (var prop in $scope.blockedUsers) {
            if ($scope.blockedUsers[prop]) {
               userIds.push(prop);
            }
        }

        var request = {id: dataSource.id,
                       'userIds': userIds};
        if($scope.datasourceVisibility == true){
            administrationService.protected( $scope.userId, $scope.userPassword ).
            updateDataSourceBlockedState(request, function(response) {
             $modalInstance.close();
             });

            }else{
             administrationService.protected( $scope.userId, $scope.userPassword ).
             updateDataSourceBlockedUsers(request, function(response) {
             $modalInstance.close();
             });
           }

    }

} ] )