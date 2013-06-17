function CtrlMateriasPrimas($scope, MateriaPrima) {
  $scope.materiasPrimas = MateriaPrima.query();
}
 
function EditCtrl($scope, $routeParams, MateriaPrima, $location) {
//	 $scope.materiaPrima = MateriaPrima.queryById({materiaPrimaId:$routeParams.materiaPrimaId});
	 $scope.materiaPrima = MateriaPrima.get({materiaPrimaId:$routeParams.materiaPrimaId});

	  $scope.destroy = function() {
	    original.remove().then(function() {
	      $location.path('/list');
	    });
	  };

	  $scope.save = function() {
		  $scope.materiaPrima.$update({materiaPrimaId:$routeParams.materiaPrimaId}, function() {
			  $location.path('/');
	    });
	  };
}

function CreateCtrl($scope, MateriaPrima, $location) {
	$scope.materiaPrima = new MateriaPrima();
	
	  $scope.save = function() {
		  $scope.materiaPrima.$save( function() {
			  $location.path('/');
		  });
	  };
	
}