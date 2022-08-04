package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

public class ItemControllerTests {

    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);
    private Item item1;
    private List<Item> itemList;

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);

        item1 = new Item();
        item1.setId(1L);
        item1.setName("rice");
        item1.setPrice(BigDecimal.valueOf(20.8));
        item1.setDescription("20 pounds");
        itemList = new ArrayList<Item>();
        itemList.add(item1);

        when(itemRepo.findAll()).thenReturn(itemList);
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item1));
        when(itemRepo.findByName("rice")).thenReturn(itemList);
    }

    @Test
    public void verify_getItems() {

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertEquals(1, items.size());
    }

    @Test
    public void verify_getById() {


        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item item = response.getBody();
        assertEquals("rice", item.getName());
        assertEquals("20 pounds", item.getDescription());
        assertEquals(BigDecimal.valueOf(20.8), item.getPrice());
    }

    @Test
    public void getById_with_notFoundItem() {
        ResponseEntity<Item> response = itemController.getItemById(2L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void verify_getItemsByName() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("rice");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertEquals(1, items.size());
    }

    @Test
    public void getByName_with_notFoundItem() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("bread");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
