package com.kakao;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.model.ResultCode;

/**
 * 일반 결재 Tax 포함/미포함
 * @author 이영후님
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = kakaoApplication.class)
@AutoConfigureMockMvc
public class normalPaymentTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	public void normalPaymentTaxNull() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("cardNo", "123412341234");
		map.put("validity", "1224");
		map.put("cvc", "111");
		map.put("installment", "0");
		map.put("price", "10000");
		map.put("tax", null);
		
		String content = mapper.writeValueAsString(map);
		
		MvcResult result = mockMvc.perform(post("/payment").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.Success))
			.andReturn();

		content = result.getResponse().getContentAsString(); 
		mapper = new ObjectMapper(); 
		map = mapper.readValue(content, HashMap.class);
		content = mapper.writeValueAsString(map);
		
		mockMvc.perform(post("/lookup").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void normalPaymentTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("cardNo", "432143214321");   
		map.put("validity", "1224");         
		map.put("cvc", "111");               
		map.put("installment", "0");         
		map.put("price", "10500");           
		map.put("tax", "200");               
		
		String content = mapper.writeValueAsString(map);
		
		MvcResult result = mockMvc.perform(post("/payment").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.Success))
			.andReturn();

		content = result.getResponse().getContentAsString(); 
		mapper = new ObjectMapper(); 
		map = mapper.readValue(content, HashMap.class);
		content = mapper.writeValueAsString(map);
		
		mockMvc.perform(post("/lookup").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk());
	}

}
