(function() {
    'use strict';

    angular
        .module('triviaApp')
        .controller('TriviaPlayController', TriviaPlayController);

    TriviaPlayController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Trivia', 'Question','Principal','WizardHandler'];

    function TriviaPlayController($scope, $rootScope, $stateParams, previousState, entity, Trivia, Question,Principal,WizardHandler) {
        var vm = this;
        vm.start=false;

        vm.trivia = entity;
        vm.previousState = previousState.name;
        vm.solvedQuestions=[99];

        var unsubscribe = $rootScope.$on('triviaApp:triviaUpdate', function(event, result) {
            vm.trivia = result;
            console.log(vm.trivia);
        });
        $scope.$on('$destroy', unsubscribe);

        vm.startTrivia= function () {
            vm.start=true;
        };
        vm.finishedWizard=function () {
            console.log("Finish!!!!!")
        };
        vm.cancelledWizard=function () {
            console.log("Cancel!!!!!")
        };
        vm.saveAnswer=function () {
            var pos=WizardHandler.wizard().currentStepNumber();
            vm.solvedQuestions.push(pos);
            console.log("aqui guardo la respuesta "+pos);
        };

    }
})();
