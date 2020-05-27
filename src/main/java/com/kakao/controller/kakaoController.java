package com.kakao.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.dto.cancelDto;
import com.kakao.dto.lookupDto;
import com.kakao.dto.paymentDto;
import com.kakao.model.Model;
import com.kakao.model.ResultCode;
import com.kakao.service.kakaoService;
import com.kakao.util.CommonUtil;

@RestController
public class kakaoController {
	
	@Autowired
	private kakaoService service;
	
	@Autowired
    private MessageSource messageSource;
	
	@PostMapping(path="/payment",consumes = "application/json;charset=utf-8",produces = "application/json;charset=utf-8")
	public Map<String, Object> payment(@RequestBody@Valid paymentDto dto, Errors errors) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if (errors.hasErrors()) {
			resultMap = CommonUtil.getErrorCode(errors);
			return resultMap;
		}
		if (!StringUtils.isEmpty(dto.getTax()) && Integer.parseInt(dto.getTax()) > Integer.parseInt(dto.getPrice())) {
			resultMap.put("ERROR_CODE", ResultCode.notBiggerTax);
			resultMap.put("ERROR_MSG", messageSource.getMessage(ResultCode.notBiggerTax, null, LocaleContextHolder.getLocale()));
			return resultMap;
		}
		
		String uniqueId = CommonUtil.encAES(dto.getCardNo());
		
		String resultCode = service.insertTransactionData(uniqueId);
		if (resultCode.equals(ResultCode.alreadyUsed)) {
			resultMap.put("ERROR_CODE", ResultCode.alreadyUsed);
			resultMap.put("ERROR_MSG", messageSource.getMessage(ResultCode.alreadyUsed, null, LocaleContextHolder.getLocale()));
			return resultMap;
		}		
		resultMap = service.insertPaymentData(dto);
		service.deleteTransactionData(uniqueId);
		return resultMap;
	}

	@PostMapping(path="/cancel",consumes = "application/json;charset=utf-8",produces = "application/json;charset=utf-8")
	public Map<String, Object> cancel(@RequestBody@Valid cancelDto dto, Errors errors) throws Exception{
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if (errors.hasErrors()) {
			resultMap = CommonUtil.getErrorCode(errors);
			return resultMap;
		}
		HashMap<String, Object> tempMap = service.getPaymentInfo(dto.getUniqueId());
		if (tempMap == null || tempMap.isEmpty()) {
			resultMap.put("ERROR_CODE", ResultCode.orgPaymentNull);
			resultMap.put("ERROR_MSG", messageSource.getMessage(ResultCode.orgPaymentNull, null, LocaleContextHolder.getLocale()));
			return resultMap;						
		}
		
		int price = Integer.parseInt((String)tempMap.get("PRICE")) - Integer.parseInt((String)tempMap.get("ACCUM_CANCEL_PRICE"));
		int tax = Integer.parseInt((String)tempMap.get("TAX")) - Integer.parseInt((String)tempMap.get("ACCUM_TAX_CANCEL_PRICE"));
		if (price < Integer.parseInt(dto.getCancelPrice()) || (!StringUtils.isEmpty(dto.getTax()) && tax < Integer.parseInt(dto.getTax()))) {
			resultMap.put("ERROR_CODE", ResultCode.rejectCancel);
			resultMap.put("ERROR_MSG", messageSource.getMessage(ResultCode.rejectCancel, null, LocaleContextHolder.getLocale()));
			return resultMap;			
		}

		if (StringUtils.isEmpty(dto.getTax())) {
			dto.setTax(String.valueOf((int)Math.round(Double.parseDouble(dto.getCancelPrice()) / 11)));
			if (Integer.parseInt(dto.getTax()) > Integer.parseInt((String)tempMap.get("TAX"))) dto.setTax((String)tempMap.get("TAX"));
		}

		if (price - Integer.parseInt(dto.getCancelPrice()) < tax - Integer.parseInt(dto.getTax())) {
			resultMap.put("ERROR_CODE", ResultCode.notBiggerRemaingTax);
			resultMap.put("ERROR_MSG", messageSource.getMessage(ResultCode.notBiggerRemaingTax, null, LocaleContextHolder.getLocale()));
			return resultMap;			
		}
		
		String resultCode = service.insertTransactionData(dto.getUniqueId());
		if (resultCode.equals(ResultCode.alreadyUsed)) {
			resultMap.put("ERROR_CODE", ResultCode.alreadyUsed);
			resultMap.put("ERROR_MSG", messageSource.getMessage(ResultCode.alreadyUsed, null, LocaleContextHolder.getLocale()));
			return resultMap;
		}		

		tempMap.put("PRICE", dto.getCancelPrice());
		tempMap.put("TAX", dto.getTax());
		
		resultMap = service.insertCancelData(tempMap);
		service.deleteTransactionData(dto.getUniqueId());
		return resultMap;
	}

	@PostMapping(path="/lookup",consumes = "application/json;charset=utf-8",produces = "application/json;charset=utf-8")
	public Map<String, Object> lookup(@RequestBody@Valid lookupDto dto, Errors errors) throws Exception{
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if (errors.hasErrors()) {
			resultMap = CommonUtil.getErrorCode(errors);
			return resultMap;
		}
		
		resultMap = service.getPaymentInfo(dto.getUniqueId());
		if (resultMap == null) resultMap = new HashMap<String, Object>();
		resultMap.put("ERROR_CODE", ResultCode.Success);
		resultMap.put("ERROR_MSG", messageSource.getMessage(ResultCode.Success, null, LocaleContextHolder.getLocale()));	
		if (resultMap.containsKey("CARD_INFO")) resultMap.remove("CARD_INFO");
		
		return resultMap;
	}

}
