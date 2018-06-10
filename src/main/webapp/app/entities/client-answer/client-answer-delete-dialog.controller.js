(function() {
    'use strict';

    angular
        .module('triviaApp')
        .controller('ClientAnswerDeleteController',ClientAnswerDeleteController);

    ClientAnswerDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClientAnswer'];

    function ClientAnswerDeleteController($uibModalInstance, entity, ClientAnswer) {
        var vm = this;

        vm.clientAnswer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ClientAnswer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
