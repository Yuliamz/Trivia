(function() {
    'use strict';

    angular
        .module('triviaApp')
        .controller('ClientAnswerDetailController', ClientAnswerDetailController);

    ClientAnswerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ClientAnswer', 'Question'];

    function ClientAnswerDetailController($scope, $rootScope, $stateParams, previousState, entity, ClientAnswer, Question) {
        var vm = this;

        vm.clientAnswer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('triviaApp:clientAnswerUpdate', function(event, result) {
            vm.clientAnswer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
