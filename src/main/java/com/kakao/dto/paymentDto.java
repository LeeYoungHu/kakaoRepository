package com.kakao.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.kakao.model.ResultCode;
import com.kakao.model.priceCheck;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class paymentDto {
	
	@Pattern(regexp = "[0-9]{10,16}", message = "카드번호가 {" + ResultCode.Pattern + "}")
	private String cardNo;
	
	@Pattern(regexp = "^(0[0-9]|1[0-2])[0-9]?[0-9]$", message = "유효기간이 {" + ResultCode.Pattern + "}")
	private String validity;
	
	@Pattern(regexp = "[0-9]{3,3}", message = "CVC가 {" + ResultCode.Pattern + "}")
	private String cvc;
	
	@Pattern(regexp = "^(1[0-2]|[0-9])$", message = "할부개월이 {" + ResultCode.Pattern + "}")
	private String installment;	
	
	@NotEmpty(message = "금액은 {" + ResultCode.NotEmpty + "}")
	@priceCheck(message = "금액이 {" + ResultCode.Pattern + "}")
	private String price;		
	
	private String tax;

	@Builder 
	public paymentDto(String cardNo, String validity, String cvc, String installment, String price, String tax) { 
		this.cardNo = cardNo;
		this.validity = validity;
		this.cvc = cvc;
		this.installment = installment;
		this.price = price;
		this.tax = tax;
	}
}
