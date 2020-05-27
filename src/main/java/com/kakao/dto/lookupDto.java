package com.kakao.dto;

import javax.validation.constraints.Pattern;

import com.kakao.model.ResultCode;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class lookupDto {
	
	@Pattern(regexp = "[0-9]{20,20}", message = "관리번호가 {" + ResultCode.Pattern + "}")
	private String uniqueId;
	
	@Builder 
	public lookupDto(String uniqueId) { 
		this.uniqueId = uniqueId;
	}

}
