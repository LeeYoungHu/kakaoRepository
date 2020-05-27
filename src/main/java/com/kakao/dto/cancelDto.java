package com.kakao.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.kakao.model.ResultCode;
import com.kakao.model.priceCheck;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class cancelDto {
	
	@Pattern(regexp = "[0-9]{20,20}", message = "관리번호가 {" + ResultCode.Pattern + "}")
	private String uniqueId;
	
	@NotEmpty(message = "금액은 {" + ResultCode.NotEmpty + "}")
	@priceCheck(message = "금액이 {" + ResultCode.Pattern + "}")
	private String cancelPrice;
	
	private String tax;
	
	@Builder 
	public cancelDto(String uniqueId, String cancelPrice, String tax) { 
		this.uniqueId = uniqueId;
		this.cancelPrice = cancelPrice;
		this.tax = tax;
	}

}
