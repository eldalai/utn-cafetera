angular.module('materiasPrimasService', ['ngResource']).
    factory('MateriaPrima', function($resource){
    	  return $resource('../rest/materiasprimas/:materiaPrimaId', {}, {
    		  	query: {method:'GET', isArray:true} ,
    		    queryById: {method:'GET', params:{materiaPrimaId:'materiaPrimaId'}, isArray:false}, 
    		    update: {method:'PUT'}
    		  });
});