(function() {
    'use strict';

    angular
        .module('triviaApp')
        .controller('TriviaStatsController', TriviaStatsController);

    TriviaStatsController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'Question','TriviaStats','$state'];

    function TriviaStatsController($scope, $rootScope, $stateParams, previousState, Question,TriviaStats,$state) {
        var vm = this;
        vm.previousState = previousState.name;
        vm.predicate = 'score';
        vm.reverse = false;
        vm.stats=TriviaStats.query({id:1});

        var unsubscribe = $rootScope.$on('triviaApp:triviaUpdate', function(event, result) {
            vm.trivia = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
