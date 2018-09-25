/**
 * 
 */
package org.gso.codeassignment.controller;

import java.util.ArrayList;
import java.util.List;

import org.gso.codeassignment.entity.Item;
import org.gso.codeassignment.repository.RoleRepository;
import org.gso.codeassignment.repository.TodoRepository;
import org.gso.codeassignment.repository.UserRepository;
import org.gso.codeassignment.service.TodoService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author AhmedSalem
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = TodoRestController.class, secure = false)
public class TodoRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TodoService todoService;

	@MockBean
	private TodoRepository todoRepo;

	@MockBean
	private UserRepository userRepo;

	@MockBean
	private RoleRepository roleRepo;

	Item mockItem = new Item(1l, "Sample Item", true, 1, null);

	Item mockItem2 = new Item(2l, "Sample Item 2", true, 1, null);

	Item mockItem3 = new Item(3l, "Work Item", false, 1, null);

	List<Item> myItems = new ArrayList<Item>();

	@Test
	public void testRetriveTodoList() throws Exception {

		myItems.add(mockItem);
		myItems.add(mockItem2);
		myItems.add(mockItem3);
		Mockito.when(todoService.retrieveItems()).thenReturn(myItems);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo/items").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("=>" + result.getResponse().getContentAsString());

		String expected = "[{\"itemId\":1,\"todo\":\"Sample Item\",\"done\":true,\"userId\":1,\"createdOn\":null},{\"itemId\":2,\"todo\":\"Sample Item 2\",\"done\":true,\"userId\":1,\"createdOn\":null},{\"itemId\":3,\"todo\":\"Work Item\",\"done\":false,\"userId\":1,\"createdOn\":null}]";

		System.out.println("2=>" + expected);
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), JSONCompareMode.LENIENT);

	}

	@Test
	public void testRetriveAllTodoList() throws Exception {

		myItems.add(mockItem3);
		myItems.add(mockItem2);
		myItems.add(mockItem);
		Mockito.when(todoService.retrieveMostRecentPendingItems()).thenReturn(myItems);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo/items/all")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("=>" + result.getResponse().getContentAsString());

		String expected = "[{\"itemId\":1,\"todo\":\"Sample Item\",\"done\":true,\"userId\":1,\"createdOn\":null},{\"itemId\":2,\"todo\":\"Sample Item 2\",\"done\":true,\"userId\":1,\"createdOn\":null},{\"itemId\":3,\"todo\":\"Work Item\",\"done\":false,\"userId\":1,\"createdOn\":null}]";

		System.out.println("2=>" + expected);
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), JSONCompareMode.LENIENT);

	}

	@Test
	public void testRetriveTodoItemById() throws Exception {

		Mockito.when(todoService.getItem(1l)).thenReturn(mockItem);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo/items/1").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("=>" + result.getResponse().getContentAsString());

		String expected = "{\"itemId\":1,\"todo\":\"Sample Item\",\"done\":true,\"userId\":1,\"createdOn\":null}";

		System.out.println("2=>" + expected);
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), JSONCompareMode.LENIENT);

	}

	@Test
	public void testCreateTodoItem() throws Exception {

		String expected = "{\"itemId\":100,\"todo\":\"Sample Item 100\",\"done\":true,\"userId\":1,\"createdOn\":null}";

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/todo/items")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(expected));

		result.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	@Ignore
	public void testdeleteTodoItem() throws Exception {

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, "/todo/items/2"));

		result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

	}

	@Test
	public void testUpdateTodoItem() throws Exception {

		Item mockItem100 = new Item(100l, "Work Item", false, 1, null);

		String expected = "{\"itemId\":100,\"todo\":\"Sample Item 100\",\"done\":true,\"userId\":1,\"createdOn\":null}";

		Mockito.when(todoService.getItem(100l)).thenReturn(mockItem100);

		ResultActions result = mockMvc
				.perform(MockMvcRequestBuilders.put("/todo/items/{id}", 100L).contentType(MediaType.APPLICATION_JSON)
						.content(expected).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());

		result.andExpect(MockMvcResultMatchers.status().isOk());

	}

}