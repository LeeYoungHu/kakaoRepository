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
 * 전체 취소 Test
 * @author 이영후님
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = kakaoApplication.class)
@AutoConfigureMockMvc
public class cancelPaymentTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	public void cancelPaymentTaxNull() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("cardNo", "432143214321");
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
		
		String orgManageId = (String) map.get("MANAGE_ID");
		map.put("uniqueId", orgManageId);
		map.put("cancelPrice", "10000");
		map.put("tax", null);
		content = mapper.writeValueAsString(map);
		
		result = mockMvc.perform(post("/cancel").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.Success))
			.andReturn();
		
		content = result.getResponse().getContentAsString(); 
		map = mapper.readValue(content, HashMap.class);
		
		map.put("uniqueId", (String)map.get("MANAGE_ID"));
		content = mapper.writeValueAsString(map);
		
		mockMvc.perform(post("/lookup").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk());

		map.put("uniqueId", orgManageId);
		content = mapper.writeValueAsString(map);

		mockMvc.perform(post("/lookup").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void cancelPayment() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("cardNo", "123412341234");
		map.put("validity", "1224");
		map.put("cvc", "111");
		map.put("installment", "0");
		map.put("price", "10000");
		map.put("tax", "1000");
		
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
		
		result = mockMvc.perform(post("/cancel").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.Success))
			.andReturn();
		
		content = result.getResponse().getContentAsString(); 
		map = mapper.readValue(content, HashMap.class);
		
		map.put("uniqueId", (String)map.get("MANAGE_ID"));
		content = mapper.writeValueAsString(map);
		
		mockMvc.perform(post("/lookup").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk());

		map.put("uniqueId", orgManageId);
		content = mapper.writeValueAsString(map);

		mockMvc.perform(post("/lookup").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk());
	}

}
