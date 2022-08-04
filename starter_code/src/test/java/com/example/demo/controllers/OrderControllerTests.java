package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTests {

    private OrderController orderController;
    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private User user;
    private List<Item> items;
    private Item item;
    private UserOrder userOrder;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);


        item = new Item();
        item.setId(1L);
        item.setName("rice");
        item.setPrice(BigDecimal.valueOf(20.8));
        item.setDescription("20 pounds");
        items = new ArrayList<>();
        items.add(item);

        Cart cart = new Cart();
        cart.setItems(items);

        user = new User();
        user.setId(1L);
        user.setPassword("testPassword");
        user.setUsername("test");
        user.setCart(cart);

        userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setItems(items);
        userOrder.setTotal(BigDecimal.valueOf(20.8));

        when(userRepo.findByUsername("test")).thenReturn(user);
    }

    @Test
    public void verify_submit() {

        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(1, userOrder.getItems().size());
    }

    @Test
    public void verify_submit_NotFoundUser() {
        ResponseEntity<UserOrder> response = orderController.submit("notfound");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void verify_getOrderByUser() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getOrderByUser_with_notFoundUser() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("notfound");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
