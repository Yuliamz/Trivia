(function () {
    'use strict';

    angular
        .module('triviaApp')
        .controller('TriviaPlayController', TriviaPlayController);

    TriviaPlayController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Trivia', 'Question', 'Principal', 'WizardHandler', 'ClientAnswer', '$timeout'];

    function TriviaPlayController($scope, $rootScope, $stateParams, previousState, entity, Trivia, Question, Principal, WizardHandler, ClientAnswer, $timeout) {
        var vm = this;
        vm.start = false;
        vm.account = {};
        vm.trivia = entity;
        vm.savedAnswers=[];
        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.clientAnswer = {};
                vm.clientAnswer.correct = false;
                vm.clientAnswer.user = vm.account;
            });
        }

        vm.clientAnswer = {};
        vm.clientAnswer.correct = false;
        vm.clientAnswer.user = vm.account;

        vm.question1 = vm.trivia.questions[0];
        vm.question2 = vm.trivia.questions[1];
        vm.question3 = vm.trivia.questions[2];
        vm.question4 = vm.trivia.questions[3];
        vm.question5 = vm.trivia.questions[4];
        vm.question6 = vm.trivia.questions[5];
        vm.question7 = vm.trivia.questions[6];
        vm.question8 = vm.trivia.questions[7];
        vm.question9 = vm.trivia.questions[8];
        vm.question10 = vm.trivia.questions[9];

        vm.time = vm.question1.time;
        vm.watch = true;

        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('triviaApp:triviaUpdate', function (event, result) {
            vm.trivia = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.startTrivia = function () {
            vm.start = true;
        };
        vm.finishedWizard = function () {
            vm.watch = false;
            console.log("Finish!!!!!");
            $state.go('trivia-stats', {id: id});
        };
        vm.cancelledWizard = function () {
            console.log("Cancel!!!!!")
        };

        function stopTime() {
            var promise = $timeout(function () {
                $scope.$broadcast('timer-stop');
            }, 100);
            return promise;
        }

        function resetTime() {
            var promise = $timeout(function () {
                $scope.$broadcast('timer-reset');
            }, 100);
            return promise;
        }

        function startTime() {
            var promise = $timeout(function () {
                $scope.$broadcast('timer-start');
            }, 100);
            return promise;
        }

        function restartTime() {
            var promise = $timeout(function () {
                stopTime().then(
                    resetTime().then(
                        startTime().then(
                            console.log("tiempo reiniciado")
                        )
                    )
                )
            }, 300);
            return promise;
        }


        vm.saveAnswer = function () {
            $scope.$broadcast('timer-stop');
            vm.watch = false;
            var pos = WizardHandler.wizard().currentStepNumber();
            vm.clientAnswer.question = vm.trivia.questions[pos - 1];
            if (vm.clientAnswer.time == null) {
                vm.clientAnswer.time = 500;
            }
            console.log(vm.clientAnswer);
            if (!vm.savedAnswers.includes(pos)) {
                ClientAnswer.save(vm.clientAnswer,
                    function () {
                        vm.savedAnswers.push(pos);
                        restartClientAnswer();
                        if (pos < 10) {
                            vm.time = vm.trivia.questions[pos].time;
                        } else {
                            vm.time = 0;
                            $scope.$broadcast('timer-stop');
                        }
                        restartTime().then(
                            vm.watch = true
                        );
                    },
                    function () {
                        $ngConfirm('Lo sentimos el servidor no se encuentra disponible');
                    });
            }
        };

        vm.finishTime = function (time) {
            vm.watch = false;
            var pos = WizardHandler.wizard().currentStepNumber();
            vm.clientAnswer.question = vm.trivia.questions[pos - 1];
            if (!vm.savedAnswers.includes(pos)) {
                ClientAnswer.save(vm.clientAnswer, function () {
                    vm.savedAnswers.push(pos);
                    restartClientAnswer();
                    if (pos < 10) {
                        vm.time = vm.trivia.questions[pos].time;
                        restartTime().then(
                            vm.watch = true
                        );
                        WizardHandler.wizard().next();
                    } else {
                        vm.time = 0;
                        $scope.$broadcast('timer-stop');
                    }
                },function () {
                    $ngConfirm('Lo sentimos el servidor no se encuentra disponible');
                });
            }
        };

        $scope.$on('timer-tick', function (event, args) {
            var pos = WizardHandler.wizard().currentStepNumber();
            vm.clientAnswer.time = vm.trivia.questions[pos - 1].time - args.millis;
        });

        vm.setAnswer = function (numAnswer) {
            var pos = WizardHandler.wizard().currentStepNumber();
            vm.clientAnswer.correct = vm.trivia.questions[pos - 1].correctAnswer === numAnswer;

        };

        function restartClientAnswer() {
            vm.clientAnswer = {};
            vm.clientAnswer.correct = false;
            vm.clientAnswer.user = vm.account;
        }

    }
})();
