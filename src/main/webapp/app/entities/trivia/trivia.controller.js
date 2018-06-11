(function() {
    'use strict';

    angular
        .module('triviaApp')
        .controller('TriviaController', TriviaController);

    TriviaController.$inject = ['Trivia', 'ParseLinks', 'AlertService', 'paginationConstants','CanStart','$ngConfirm','$state','$http'];

    function TriviaController(Trivia, ParseLinks, AlertService, paginationConstants,CanStart,$ngConfirm,$state,$http) {

        var vm = this;

        vm.trivias = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        loadAll();

        function loadAll () {
            Trivia.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.trivias.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.loadStart = function(id) {
            CanStart.get({id:id}, onSuccess, onError);

            function onSuccess(data) {
                if (data[0]==='t') {
                    $state.go('trivia-play', {id: id});
                }else{
                    $ngConfirm('Esta trivia no esta disponible aún');
                }
            }
            function onError(error) {
                console.log(error);
                $ngConfirm('Esta trivia no esta disponible aún');
            }
        };



        function reset () {
            vm.page = 0;
            vm.trivias = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
