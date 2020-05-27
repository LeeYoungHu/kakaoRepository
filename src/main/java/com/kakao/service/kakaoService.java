package com.kakao.service;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kakao.dao.kakaoDao;
import com.kakao.dto.paymentDto;
import com.kakao.model.Model;
import com.kakao.model.ResultCode;
import com.kakao.util.CommonUtil;

@Service
public class kakaoService {
	
	@Autowired
	private kakaoDao dao;

	@Autowired
    private MessageSource messageSource;
	
	public String insertTransactionData(String uniqueId) {
		String resultCode = ResultCode.Success;
		int result = dao.getTransactionDataCount(uniqueId);
		if (result != 0) {
			resultCode = ResultCode.alreadyUsed;
		} else {
			try {
				dao.insertTransactionData(uniqueId);			
			} catch (Exception e) {
				resultCode = ResultCode.alreadyUsed;
				e.printStackTrace();
			}			
		}
		
		return resultCode;
	}
	
	public void deleteTransactionData(String uniqueId) throws Exception {
		dao.deleteTransactionData(uniqueId);
	}

	public Map<String, Object> insertPaymentData(@Valid paymentDto dto) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String encCardData = CommonUtil.encAES(dto.getCardNo() + Model.delimiter + dto.getValidity() + Model.delimiter + dto.getCvc());
			String manageId = dao.getNewManageId();
			
			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("MANAGE_ID", manageId);
			
			tempMap.put("CARD_INFO", encCardData);
			dto.setInstallment(CommonUtil.returnDataType(dto.getInstallment(), Model.numberFormat_0, 2));
			tempMap.put("INSTALLMENT", dto.getInstallment());
			tempMap.put("PRICE", String.valueOf(dto.getPrice()));
			if (StringUtils.isEmpty(dto.getTax())) {
				dto.setTax(String.valueOf((int)Math.round(Double.parseDouble(dto.getPrice()) / 11)));
			}
			tempMap.put("TAX", dto.getTax());
			tempMap.put("STATUS", Model.Payment);
			tempMap.put("CARD_NO", dto.getCardNo());
			tempMap.put("VALIDITY", dto.getValidity());
			tempMap.put("CVC", dto.getCvc());
			tempMap.put("GUBUN", "PAYMENT");
			
			String ifData = CommonUtil.returnIfData(tempMap);
			
			tempMap.put("IF_DATA", ifData);
			
			dao.insertPaymentData(tempMap);
			
			resultMap.put("MANAGE_ID", manageId);			
			resultMap.put("ERROR_CODE", ResultCode.Success);
			resultMap.put("ERROR_MSG", messageSource.getMessage(ResultCode.Success, null, LocaleContextHolder.getLocale()));
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("ERROR_CODE", ResultCode.occurError);
			resultMap.put("ERROR_MSG", messageSource.getMessage(ResultCode.occurError, null, LocaleContextHolder.getLocale()));
		}
		
		return resultMap;
	}

	public HashMap<String, Object> insertCancelData(HashMap<String, Object> tempMap) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			String manageId = dao.getNewManageId();
			tempMap.put("ORG_MANAGE_ID", tempMap.get("MANAGE_ID"));
			tempMap.put("MANAGE_ID", manageId);
			tempMap.put("STATUS", Model.Cancel);
			tempMap.put("PRICE", String.valueOf(tempMap.get("PRICE")));
		
			tempMap.put("GUBUN", "CANCEL");

			String ifData = CommonUtil.returnIfData(tempMap);
			
			tempMap.put("IF_DATA", ifData);
			
			dao.insertPaymentData(tempMap);
			dao.updateOrgPaymentData(tempMap);
			
			resultMap.put("MANAGE_ID", manageId);
			resultMap.put("ERROR_CODE", ResultCode.Success);
			resultMap.put("ERROR_MSG", messageSource.getMessage(ResultCode.Success, null, LocaleContextHolder.getLocale()));			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("ERROR_CODE", ResultCode.occurError);
			resultMap.put("ERROR_MSG", messageSource.getMessage(ResultCode.occurError, null, LocaleContextHolder.getLocale()));
		}
		
		return resultMap;
	}
	

	public HashMap<String, Object> getPaymentInfo(String uniqueId) throws Exception {
		HashMap<String, Object> tempMap = dao.getPaymentInfo(uniqueId);
		if (tempMap != null && !tempMap.isEmpty()) {
			String[] cardInfo = CommonUtil.decAES((String)tempMap.get("CARD_INFO")).split("\\" + Model.delimiter);
			tempMap.put("CARD_NO", cardInfo[0]);
			tempMap.put("VALIDITY", cardInfo[1]);
			tempMap.put("CVC", cardInfo[2]);			
		}
		return tempMap;
	}
	
}
