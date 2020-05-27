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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.model.ResultCode;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = kakaoApplication.class)
@AutoConfigureMockMvc
public class OptionalTest3 {
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	public void OptionalPartailCancelTest3() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("cardNo", "1234123412341234");
		map.put("validity", "1224");
		map.put("cvc", "111");
		map.put("installment", "0");
		map.put("price", "20000");
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
		
		String orgManageId = (String) map.get("MANAGE_ID");
		map.put("uniqueId", orgManageId);
		map.put("cancelPrice", "10000");
		map.put("tax", "1000");
		content = mapper.writeValueAsString(map);
		
		mockMvc.perform(post("/cancel").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.Success))
			.andReturn();

		map.put("cancelPrice", "10000");
		map.put("tax", "909");
		content = mapper.writeValueAsString(map);
		
		mockMvc.perform(post("/cancel").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.rejectCancel))
		.andReturn();		

		map.put("cancelPrice", "10000");
		map.put("tax", null);
		content = mapper.writeValueAsString(map);
		
		mockMvc.perform(post("/cancel").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.Success))
		.andReturn();		

		map.put("uniqueId", orgManageId);
		content = mapper.writeValueAsString(map);

		mockMvc.perform(post("/lookup").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk());
		
	}

}
