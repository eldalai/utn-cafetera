angular.module('cafetera', ['materiasPrimasService']).
  config(function($routeProvider) {
    $routeProvider.
      when('/', {
        controller:CtrlMateriasPrimas, 
        templateUrl:'list.html'
      }).
      when('/edit/:materiaPrimaId', {
        controller:EditCtrl, 
        templateUrl:'detail.html'
//        	,
//        resolve: {
//          project: function(Restangular, $route){
//            return Restangular.one('projects', $route.current.params.projectId).get();
//          }
//        }
      }).
      when('/new', {controller:CreateCtrl, templateUrl:'detail.html'}).
      otherwise({redirectTo:'/'});
      
//      RestangularProvider.setBaseUrl('https://api.mongolab.com/api/1/databases/angularjs/collections');
//      RestangularProvider.setDefaultRequestParams({ apiKey: '4f847ad3e4b08a2eed5f3b54' })
//      RestangularProvider.setRestangularFields({
//        id: '_id.$oid'
//      });
//      
//      RestangularProvider.setRequestInterceptor(function(elem, operation, what) {
//        
//        if (operation === 'put') {
//          elem._id = undefined;
//          return elem;
//        }
//        return elem;
//      })
  });