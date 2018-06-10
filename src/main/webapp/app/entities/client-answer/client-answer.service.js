(function() {
    'use strict';
    angular
        .module('triviaApp')
        .factory('ClientAnswer', ClientAnswer);

    ClientAnswer.$inject = ['$resource'];

    function ClientAnswer ($resource) {
        var resourceUrl =  'api/client-answers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
