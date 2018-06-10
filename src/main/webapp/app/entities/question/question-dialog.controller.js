(function() {
    'use strict';

    angular
        .module('triviaApp')
        .controller('QuestionDialogController', QuestionDialogController);

    QuestionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Question', 'ClientAnswer', 'Trivia'];

    function QuestionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Question, ClientAnswer, Trivia) {
        var vm = this;

        vm.question = entity;
        vm.clear = clear;
        vm.save = save;
        vm.clientanswers = ClientAnswer.query();
        vm.trivias = Trivia.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.question.id !== null) {
                Question.update(vm.question, onSaveSuccess, onSaveError);
            } else {
                Question.save(vm.question, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('triviaApp:questionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
