package backend.server.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import backend.business.dto.OrderCreateDTO;
import backend.business.dto.OrderDTO;
import backend.business.dto.OrderDetailsCreateDTO;
import backend.business.dto.OrderDetailsDTO;
import backend.business.dto.OrderDetailsUpdateDTO;
import backend.business.dto.OrderUpdateDTO;
import backend.server.service.impl.OrderServiceImpl;
import io.swagger.annotations.Api;

@Api(value = "orders", description = "orders")
@Controller
@RequestMapping(value = "/orders")
public class OrderController
{
    @Autowired
    private OrderServiceImpl orderService;

    @PreAuthorize("hasRole('ACCESS_ALL')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public OrderDTO createOrder(@RequestBody OrderCreateDTO createDTO)
    {
        return orderService.createOrder(createDTO);
    }

    @PreAuthorize("hasRole('ACCESS_ALL')")
    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public OrderDTO getOrder(@PathVariable Integer orderId)
    {
        return orderService.getOrder(orderId);
    }

    @PreAuthorize("hasRole('ACCESS_ALL')")
    @RequestMapping(value = "/{orderId}/orderDetails", method = RequestMethod.GET)
    @ResponseBody
    public List<OrderDetailsDTO> getOrderItems(@PathVariable Integer orderId)
    {
        return orderService.getOrderItems(orderId);
    }

    @PreAuthorize("hasRole('ACCESS_ALL')")
    @RequestMapping(value = "/{orderId}/orderDetails", method = RequestMethod.POST)
    @ResponseBody
    public List<OrderDetailsDTO> addMenuItemsToOrder(@PathVariable Integer orderId, @Valid @RequestBody List<OrderDetailsCreateDTO> orderDetailscreateDTO)
    {
        return orderService.addMenuItemsToOrder(orderId, orderDetailscreateDTO);
    }

    @PreAuthorize("hasRole('ACCESS_ALL')")
    @RequestMapping(value = "/{orderId}/orderDetails", method = RequestMethod.PUT)
    @ResponseBody
    public List<OrderDetailsDTO> updateOrderItems(@PathVariable Integer orderId, @Valid @RequestBody List<OrderDetailsUpdateDTO> orderDetailsUpdateDTO)
    {
        return orderService.updateOrderItems(orderId, orderDetailsUpdateDTO);
    }

    @PreAuthorize("hasRole('ACCESS_ALL')")
    @RequestMapping(value = "/{orderId}", method = RequestMethod.PUT)
    @ResponseBody
    public OrderDTO updateOrderStatus(@PathVariable Integer orderId, @Valid @RequestBody OrderUpdateDTO updateDTO)
    {
        return orderService.updateOrderStatus(orderId, updateDTO);
    }
}
