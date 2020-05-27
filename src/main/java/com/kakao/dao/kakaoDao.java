package com.kakao.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper	
public interface kakaoDao {

	public int insertTransactionData(String uniqueId);

	public void deleteTransactionData(String uniqueId);

	public String getNewManageId();

	public void insertPaymentData(HashMap<String, Object> tempMap);

	public HashMap<String, Object> getPaymentInfo(String uniqueId);

	public int getTransactionDataCount(String uniqueId);

	public void updateOrgPaymentData(HashMap<String, Object> tempMap);
	
}
