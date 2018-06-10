(function() {
    'use strict';

    angular
        .module('triviaApp')
        .controller('TriviaDetailController', TriviaDetailController);

    TriviaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Trivia', 'Question'];

    function TriviaDetailController($scope, $rootScope, $stateParams, previousState, entity, Trivia, Question) {
        var vm = this;

        vm.trivia = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('triviaApp:triviaUpdate', function(event, result) {
            vm.trivia = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
