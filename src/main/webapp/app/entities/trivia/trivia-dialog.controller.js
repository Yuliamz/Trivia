(function() {
    'use strict';

    angular
        .module('triviaApp')
        .controller('TriviaDialogController', TriviaDialogController);

    TriviaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Trivia', 'Question'];

    function TriviaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Trivia, Question) {
        var vm = this;

        vm.trivia = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.questions = Question.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
		
		vm.change = function(){
			var u=0;
			for(var i=0;i<vm.trivia.questions.length;i++){
				u=u+vm.trivia.questions[i].time;
			}
			vm.trivia.duration=u;
		}

        function save () {
            vm.isSaving = true;
            if (vm.trivia.id !== null) {
                Trivia.update(vm.trivia, onSaveSuccess, onSaveError);
            } else {
                Trivia.save(vm.trivia, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('triviaApp:triviaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.start = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
