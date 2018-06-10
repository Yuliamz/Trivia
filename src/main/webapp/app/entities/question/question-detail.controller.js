(function() {
    'use strict';

    angular
        .module('triviaApp')
        .controller('QuestionDetailController', QuestionDetailController);

    QuestionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Question', 'ClientAnswer', 'Trivia'];

    function QuestionDetailController($scope, $rootScope, $stateParams, previousState, entity, Question, ClientAnswer, Trivia) {
        var vm = this;

        vm.question = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('triviaApp:questionUpdate', function(event, result) {
            vm.question = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
