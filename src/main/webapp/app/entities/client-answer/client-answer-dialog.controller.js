(function() {
    'use strict';

    angular
        .module('triviaApp')
        .controller('ClientAnswerDialogController', ClientAnswerDialogController);

    ClientAnswerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClientAnswer', 'Question','Principal'];

    function ClientAnswerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ClientAnswer, Question,Principal) {
        var vm = this;

        vm.clientAnswer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.questions = Question.query();
		vm.account = null;
		getAccount();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
				vm.clientAnswer.user=vm.account;
            });
        }
		

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.clientAnswer.id !== null) {
                ClientAnswer.update(vm.clientAnswer, onSaveSuccess, onSaveError);
            } else {
                ClientAnswer.save(vm.clientAnswer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('triviaApp:clientAnswerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
