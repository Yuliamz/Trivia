(function() {
    'use strict';

    angular
        .module('triviaApp')
        .controller('TriviaDeleteController',TriviaDeleteController);

    TriviaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Trivia'];

    function TriviaDeleteController($uibModalInstance, entity, Trivia) {
        var vm = this;

        vm.trivia = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Trivia.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
