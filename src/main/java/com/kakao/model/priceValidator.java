package com.kakao.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class priceValidator implements ConstraintValidator<priceCheck, String> {@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean priceValid = true;
		if (!StringUtils.isEmpty(value)) {
			try {
				int price = Integer.parseInt(value);
				if (price < 100) {
					priceValid = false;
				}
				if (price > 1000000000) {
					priceValid = false;					
				}
			} catch (Exception e) {
				e.printStackTrace();
				priceValid = false;
			}
		}
	
		return priceValid;
	}
	
}
