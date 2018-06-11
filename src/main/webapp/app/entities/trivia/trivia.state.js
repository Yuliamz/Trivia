(function() {
    'use strict';

    angular
        .module('triviaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('trivia', {
            parent: 'entity',
            url: '/trivia',
            data: {
                authorities: ['ROLE_USER','ROLE_ADMIN'],
                pageTitle: 'Trivias'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trivia/trivias.html',
                    controller: 'TriviaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('trivia-detail', {
            parent: 'trivia',
            url: '/trivia/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Trivia'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trivia/trivia-detail.html',
                    controller: 'TriviaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Trivia', function($stateParams, Trivia) {
                    return Trivia.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'trivia',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
		.state('trivia-play', {
            parent: 'trivia',
            url: '/play/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Jugar Trivia'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trivia/trivia-play.html',
                    controller: 'TriviaPlayController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Trivia', function($stateParams, Trivia) {
                    return Trivia.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'trivia',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('trivia-detail.edit', {
            parent: 'trivia-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trivia/trivia-dialog.html',
                    controller: 'TriviaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Trivia', function(Trivia) {
                            return Trivia.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trivia.new', {
            parent: 'trivia',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trivia/trivia-dialog.html',
                    controller: 'TriviaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                start: null,
                                duration: null,
                                level: 5,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('trivia', null, { reload: 'trivia' });
                }, function() {
                    $state.go('trivia');
                });
            }]
        })
        .state('trivia.edit', {
            parent: 'trivia',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trivia/trivia-dialog.html',
                    controller: 'TriviaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Trivia', function(Trivia) {
                            return Trivia.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trivia', null, { reload: 'trivia' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trivia.delete', {
            parent: 'trivia',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trivia/trivia-delete-dialog.html',
                    controller: 'TriviaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Trivia', function(Trivia) {
                            return Trivia.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trivia', null, { reload: 'trivia' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
