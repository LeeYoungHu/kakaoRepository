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

/**
 * 동일 카드로 거래 두개가 동시에 들어왔을 때 Test
 * 때때로 반대로 결과가 나오지만 정상적으로 한 건만 처리되는 것은 확인 가능
 * @author 이영후님
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = kakaoApplication.class)
@AutoConfigureMockMvc
class parallelPaymentTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	public void parallelPaymentTest2() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cardNo", "123412341234");
		map.put("validity", "1224");
		map.put("cvc", "111");
		map.put("installment", "0");
		map.put("price", "10000");
		map.put("tax", null);
		
		String content = mapper.writeValueAsString(map);
		
		mockMvc.perform(post("/payment").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.alreadyUsed));
	}
	
	@Test
	public void parallelPaymentTest1() throws Exception {
		ObjectMapper mapper = new ObjectMapper(); 
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cardNo", "123412341234");
		map.put("validity", "1224");
		map.put("cvc", "111");
		map.put("installment", "0");
		map.put("price", "10000");
		map.put("tax", null);
		
		String content = mapper.writeValueAsString(map);

		mockMvc.perform(post("/payment").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.ERROR_CODE").value(ResultCode.Success));
	}
}
