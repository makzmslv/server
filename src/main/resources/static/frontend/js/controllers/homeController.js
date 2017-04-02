var homeController = angular.module('homeController', [ 'ngRoute' ]).controller('homeController',['$scope','HotelService', 'filterFilter', '$cookies', function($scope, HotelService, filterFilter, $cookies) {
        'use strict';


        $scope.searchParameters = {};
        $scope.screenOptions = {};
        var initPage;
        
        $scope.getHotels = function () {
        	var queryParams = $scope.searchParameters;
        	HotelService.getAllHotels(queryParams).$promise.then(function(result) {
                $scope.hotels = result;
            });
        };
        
        $scope.getOrders = function (hotel) {
        	HotelService.getOrdersForHotel(hotel.id).$promise.then(function(result) {
        		$scope.orders = result;
        		$scope.screenOptions.showHotels = false;
        		$scope.screenOptions.selectedHotel = hotel;
        		$scope.screenOptions.showOrders = true;
        		$scope.screenOptions.showOrderDetails = false;
            });
        };
        
        $scope.getOrderDetails = function (order) {
        	HotelService.getOrderDetails(order.id).$promise.then(function(result) {
        		$scope.orderDetails = result;
        		$scope.screenOptions.showHotels = false;
        		$scope.screenOptions.showOrders = false;
        		$scope.screenOptions.showOrderDetails = true;
        		$scope.screenOptions.selectedOrder = order;
            });
        };
        
        initPage = function () {
        	$scope.screenOptions.showHotels = true;
    		$scope.screenOptions.showOrders = false;
    		$scope.screenOptions.showOrderDetails = false;
    		$scope.screenOptions.selectedHotel = null;
        };
        
        $scope.goBacktoHotels = function () {
        	$scope.screenOptions.showHotels = true;
    		$scope.screenOptions.showOrders = false;
    		$scope.screenOptions.showOrderDetails = false;
    		$scope.screenOptions.selectedHotel = null;
        };
        
       $scope.goBacktoOrders = function () {
        	$scope.screenOptions.showHotels = false;
    		$scope.screenOptions.showOrders = true;
    		$scope.screenOptions.showOrderDetails = false;
        };
        
        initPage();
    }

]);