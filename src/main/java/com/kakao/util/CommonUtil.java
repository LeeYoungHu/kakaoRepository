package com.kakao.util;

import java.security.Key;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.kakao.model.Model;
import com.kakao.model.ResultCode;

public class CommonUtil {

	public static ArrayList<HashMap<String, Object>> getErrorList(Errors errors) throws Exception {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		Class<Types> types = Types.class;
		for (int i = 0; i < errors.getErrorCount(); i++) {
			HashMap<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("ERROR_CODE", ResultCode.class.getDeclaredField(errors.getAllErrors().get(i).getCode()).get(types));
			tempMap.put("ERROR_MSG", errors.getAllErrors().get(i).getDefaultMessage());	
			list.add(tempMap);
		}
		return list;
	}
	
	public static HashMap<String, Object> getErrorCode(Errors errors) throws Exception {
		Class<Types> types = Types.class;
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("ERROR_CODE", ResultCode.class.getDeclaredField(errors.getAllErrors().get(0).getCode()).get(types));
		tempMap.put("ERROR_MSG", errors.getAllErrors().get(0).getDefaultMessage());	
		return tempMap;
	}
	
	public static String returnDataType(String data, int length, String addBlank, boolean forward) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isEmpty(data)) data = "";
		
		if (forward) {
			for (int i = 0; i < length - data.length(); i++) {
				sb.append(addBlank);
			}
			sb.append(data);						
		} else {
			sb.append(data);			
			for (int i = 0; i < length - data.length(); i++) {
				sb.append(addBlank);
			}
		}
		
		return sb.toString();
	}
	
	public static String returnDataType(String data, int length) {
		return returnDataType(data, length, " ", false);
	}

	public static String returnDataType(int data, int dataType, int length) {
		String returnString = "";
		if (dataType == Model.numberFormat) {
			returnString = returnDataType(String.valueOf(data), length, " ", true);
		} else if (dataType == Model.numberFormat_0) {
			returnString = returnDataType(String.valueOf(data), length, "0", true);			
		} else if (dataType == Model.numberFormat_L) {
			returnString = returnDataType(String.valueOf(data), length, " ", false);			
		}
		
		return returnString;
	}
	
	public static String returnDataType(String data, int dataType, int length) {
		String returnString = "";
		if (dataType == Model.numberFormat) {
			returnString = returnDataType(data, length, " ", true);
		} else if (dataType == Model.numberFormat_0) {
			returnString = returnDataType(data, length, "0", true);			
		} else if (dataType == Model.numberFormat_L) {
			returnString = returnDataType(data, length, " ", false);			
		}
		
		return returnString;
	}
	
	public static Key getAESKey() throws Exception {
		String key = Model.encKey;
		Key keySpec;
		
		key = key.substring(0, 16);
		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		
		int len = b.length;
		if (len > keyBytes.length) {
			len = keyBytes.length;
		}
		
		System.arraycopy(b, 0, keyBytes, 0, len);
		keySpec = new SecretKeySpec(keyBytes, "AES");
		
		return keySpec;
	}
	
	public static String encAES(String str) throws Exception {
		Key keySpec = getAESKey();
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(Model.iv.getBytes("UTF-8")));
		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String encStr = new String(Base64.getEncoder().encode(encrypted));
		
		return encStr;
	}
	
	public static String decAES(String str) throws Exception {
		Key keySpec = getAESKey();
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(Model.iv.getBytes("UTF-8")));
		byte[] decrypted = Base64.getDecoder().decode(str.getBytes("UTF-8"));
		String decStr = new String(c.doFinal(decrypted), "UTF-8");
		
		return decStr;
	}
	
	public static String returnIfData(HashMap<String, Object> map) {
		StringBuilder ifData = new StringBuilder();
		
		ifData.append(CommonUtil.returnDataType((String)map.get("GUBUN"), 10));
		ifData.append(CommonUtil.returnDataType((String)map.get("MANAGE_ID"), 20));
		ifData.append(CommonUtil.returnDataType((String)map.get("CARD_NO"), Model.numberFormat_L, 20));
		ifData.append(CommonUtil.returnDataType((String)map.get("INSTALLMENT"), Model.numberFormat_0, 2));
		ifData.append(CommonUtil.returnDataType((String)map.get("VALIDITY"), Model.numberFormat_L, 4));
		ifData.append(CommonUtil.returnDataType((String)map.get("CVC"), Model.numberFormat_L, 3));
		ifData.append(CommonUtil.returnDataType((String)map.get("PRICE"), Model.numberFormat, 10));
		ifData.append(CommonUtil.returnDataType((String)map.get("TAX"), Model.numberFormat_0, 10));
		ifData.append(CommonUtil.returnDataType((String)map.get("ORG_MANAGE_ID"), 20));
		ifData.append(CommonUtil.returnDataType((String)map.get("CARD_INFO"), 300));
		ifData.append(CommonUtil.returnDataType("", 47));
		
		return CommonUtil.returnDataType(ifData.toString().length(), Model.numberFormat, 4) + ifData.toString();
	}

}
