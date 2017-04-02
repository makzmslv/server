//
// product services
//
var restServices = angular.module('restServices', [ 'ngResource' ]);
restServices.factory('HotelService', function ($resource) {
        "use strict";

        return {
            "getAllHotels": function (queryParams) {
            	var ret, restResource;
                restResource = $resource('/hotels', {}, {
                    "query": {
                        "method": "GET",
                        "isArray": true
                    }
                });
                ret = restResource.query({
                    "name" : queryParams.name,
                    "area": queryParams.area,
                });

                return ret;
            },
            "getOrdersForHotel": function (hotelId) {
                var ret, restResource;

                restResource = $resource('/hotels/:hotelId/orders', {}, {
                    "query": {
                        "method": "GET",
                        "isArray": true
                    }
                });

                ret = restResource.query({ "hotelId": hotelId });

                return ret;
            },
            "getOrderDetails": function (orderId) {
                var ret, restResource;

                restResource = $resource('/orders/:orderId/orderDetails', {}, {
                    "query": {
                        "method": "GET",
                        "isArray": true
                    }
                });

                ret = restResource.query({ "orderId": orderId });

                return ret;
            }

        };
    });