(function() {
    'use strict';
    angular
        .module('triviaApp')
        .factory('Trivia', Trivia);

    Trivia.$inject = ['$resource', 'DateUtils'];

    function Trivia ($resource, DateUtils) {
        var resourceUrl =  'api/trivias/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.start = DateUtils.convertDateTimeFromServer(data.start);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
