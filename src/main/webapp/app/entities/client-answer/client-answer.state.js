(function() {
    'use strict';

    angular
        .module('triviaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('client-answer', {
            parent: 'entity',
            url: '/client-answer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ClientAnswers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-answer/client-answers.html',
                    controller: 'ClientAnswerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('client-answer-detail', {
            parent: 'client-answer',
            url: '/client-answer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ClientAnswer'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-answer/client-answer-detail.html',
                    controller: 'ClientAnswerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ClientAnswer', function($stateParams, ClientAnswer) {
                    return ClientAnswer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'client-answer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('client-answer-detail.edit', {
            parent: 'client-answer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-answer/client-answer-dialog.html',
                    controller: 'ClientAnswerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientAnswer', function(ClientAnswer) {
                            return ClientAnswer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-answer.new', {
            parent: 'client-answer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-answer/client-answer-dialog.html',
                    controller: 'ClientAnswerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                correct: false,
                                time: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('client-answer', null, { reload: 'client-answer' });
                }, function() {
                    $state.go('client-answer');
                });
            }]
        })
        .state('client-answer.edit', {
            parent: 'client-answer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-answer/client-answer-dialog.html',
                    controller: 'ClientAnswerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientAnswer', function(ClientAnswer) {
                            return ClientAnswer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-answer', null, { reload: 'client-answer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-answer.delete', {
            parent: 'client-answer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-answer/client-answer-delete-dialog.html',
                    controller: 'ClientAnswerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClientAnswer', function(ClientAnswer) {
                            return ClientAnswer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-answer', null, { reload: 'client-answer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
