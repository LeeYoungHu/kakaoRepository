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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.model.ResultCode;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = kakaoApplication.class)
@AutoConfigureMockMvc
class paymentValidTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	public void cardNoValidTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cardNo", "12341234");    
		map.put("validity", "1224");      
		map.put("cvc", "333");            
		map.put("installment", "0");      
		map.put("price", "1000");         
		map.put("tax", null);             
		
		String content = mapper.writeValueAsString(map);		
	
		mockMvc.perform(post("/payment").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.Pattern));
	}
	
	@Test
	public void validityValidTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cardNo", "123412341234");    
		map.put("validity", "12");      
		map.put("cvc", "333");            
		map.put("installment", "0");      
		map.put("price", "1000");         
		map.put("tax", null);             
		
		String content = mapper.writeValueAsString(map);		
	
		mockMvc.perform(post("/payment").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.Pattern));
	}
	
	@Test
	public void cvcValidTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cardNo", "123412341234");    
		map.put("validity", "1224");      
		map.put("cvc", "");            
		map.put("installment", "0");      
		map.put("price", "1000");         
		map.put("tax", null);             
		
		String content = mapper.writeValueAsString(map);		
	
		mockMvc.perform(post("/payment").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.Pattern));
	}
	
	@Test
	public void installmentValidTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cardNo", "123412341234");    
		map.put("validity", "1224");      
		map.put("cvc", "333");            
		map.put("installment", "14");      
		map.put("price", "1000");         
		map.put("tax", null);             
		
		String content = mapper.writeValueAsString(map);		
	
		mockMvc.perform(post("/payment").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.Pattern));	
	}
	
	@Test
	public void priceValidTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cardNo", "123412341234");    
		map.put("validity", "1224");      
		map.put("cvc", "333");            
		map.put("installment", "0");      
		map.put("price", "10");         
		map.put("tax", null);             
		
		String content = mapper.writeValueAsString(map);		
	
		mockMvc.perform(post("/payment").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.priceCheck));	
	}

	@Test
	public void taxValidTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cardNo", "123412341234");    
		map.put("validity", "1224");      
		map.put("cvc", "333");            
		map.put("installment", "0");      
		map.put("price", "1000");         
		map.put("tax", "1001");             
		
		String content = mapper.writeValueAsString(map);		
	
		mockMvc.perform(post("/payment").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.notBiggerTax));		
	}

}
