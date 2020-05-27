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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.model.ResultCode;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = kakaoApplication.class)
@AutoConfigureMockMvc
class cancelValidTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	public void cancelPriceValidTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uniqueId", "20200525000000000001");
		map.put("cancelPrice", "10");         
		map.put("tax", null);             
		
		String content = mapper.writeValueAsString(map);		
	
		mockMvc.perform(post("/cancel").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.priceCheck));	
	}
	
	@Test
	public void uniqueIdValidTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uniqueId", "2020052500000000");
		map.put("cancelPrice", "100");         
		map.put("tax", null);             
		
		String content = mapper.writeValueAsString(map);		
	
		mockMvc.perform(post("/cancel").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.Pattern));	
	}
	
	@Test
	public void uniqueIdValidTest2() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uniqueId", "20200525000000000001");
		map.put("cancelPrice", "100");         
		map.put("tax", null);             
		
		String content = mapper.writeValueAsString(map);		
	
		mockMvc.perform(post("/cancel").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.orgPaymentNull));	
	}

}
