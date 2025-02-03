package com.shop.payment.service;

import com.shop.common.exception.PaymentException;
import com.shop.common.service.payment.CreditCardInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.YearMonth;
import java.util.regex.Pattern;

@Service
@Slf4j
public class CreditCardValidationService {
    
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("\\d{16}");
    private static final Pattern CVV_PATTERN = Pattern.compile("\\d{3,4}");
    
    // 支持的信用卡类型前缀
    private static final String[] VISA_PREFIX = {"4"};
    private static final String[] MASTERCARD_PREFIX = {"51", "52", "53", "54", "55"};
    private static final String[] AMEX_PREFIX = {"34", "37"};
    
    public void validate(CreditCardInfo card) {
        if (card == null) {
            throw new PaymentException("信用卡信息不能为空");
        }

        validateCardNumber(card.getCreditCardNumber());
        validateCVV(String.valueOf(card.getCreditCardCvv()));
        validateExpiryDate(card.getCreditCardExpirationMonth(), 
                          card.getCreditCardExpirationYear());
        validateCardType(card.getCreditCardNumber());
    }

    private void validateCardNumber(String cardNumber) {
        if (!StringUtils.hasText(cardNumber)) {
            throw new PaymentException("卡号不能为空");
        }

        if (!CARD_NUMBER_PATTERN.matcher(cardNumber).matches()) {
            throw new PaymentException("无效的卡号格式");
        }

        if (!validateLuhn(cardNumber)) {
            throw new PaymentException("无效的卡号校验");
        }
    }

    private void validateCVV(String cvv) {
        if (!StringUtils.hasText(cvv)) {
            throw new PaymentException("CVV不能为空");
        }

        if (!CVV_PATTERN.matcher(cvv).matches()) {
            throw new PaymentException("无效的CVV格式");
        }
    }

    private void validateExpiryDate(int month, int year) {
        if (month < 1 || month > 12) {
            throw new PaymentException("无效的过期月份");
        }

        YearMonth cardExpiry = YearMonth.of(year, month);
        YearMonth now = YearMonth.now();

        if (cardExpiry.isBefore(now)) {
            throw new PaymentException("信用卡已过期");
        }

        // 检查是否超过最大有效期(通常为10年)
        YearMonth maxValidity = now.plusYears(10);
        if (cardExpiry.isAfter(maxValidity)) {
            throw new PaymentException("信用卡有效期不合理");
        }
    }

    private void validateCardType(String cardNumber) {
        if (isVisa(cardNumber) || isMasterCard(cardNumber) || isAmex(cardNumber)) {
            return;
        }
        throw new PaymentException("不支持的信用卡类型");
    }

    private boolean validateLuhn(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return sum % 10 == 0;
    }

    private boolean isVisa(String cardNumber) {
        return startsWith(cardNumber, VISA_PREFIX);
    }

    private boolean isMasterCard(String cardNumber) {
        return startsWith(cardNumber, MASTERCARD_PREFIX);
    }

    private boolean isAmex(String cardNumber) {
        return startsWith(cardNumber, AMEX_PREFIX);
    }

    private boolean startsWith(String cardNumber, String[] prefixes) {
        for (String prefix : prefixes) {
            if (cardNumber.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}