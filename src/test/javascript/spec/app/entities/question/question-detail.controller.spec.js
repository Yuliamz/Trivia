'use strict';

describe('Controller Tests', function() {

    describe('Question Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockQuestion, MockClientAnswer, MockTrivia;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockQuestion = jasmine.createSpy('MockQuestion');
            MockClientAnswer = jasmine.createSpy('MockClientAnswer');
            MockTrivia = jasmine.createSpy('MockTrivia');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Question': MockQuestion,
                'ClientAnswer': MockClientAnswer,
                'Trivia': MockTrivia
            };
            createController = function() {
                $injector.get('$controller')("QuestionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'triviaApp:questionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
