package com.leniolabs.challenge.calculator.factory;


import com.leniolabs.challenge.calculator.FeeCalculatorIF;
import com.leniolabs.challenge.custom.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeeCalculatorFactory {

    private final Map<String, FeeCalculatorIF> feeCalculators;

    @Autowired
    public FeeCalculatorFactory(List<FeeCalculatorIF> feeCalculatorList) {
        feeCalculators = new HashMap<>();
        for (FeeCalculatorIF calculator : feeCalculatorList) {
            AccountType accountTypeAnnotation = calculator.getClass().getAnnotation(AccountType.class);
            if (accountTypeAnnotation != null) {
                feeCalculators.put(accountTypeAnnotation.value(), calculator);
            }
        }
    }

    public FeeCalculatorIF getFeeCalculator(String accountType) {
        return feeCalculators.getOrDefault(accountType, new DefaultFeeCalculator());
    }

    private class DefaultFeeCalculator implements FeeCalculatorIF {
        @Override
        public Double calculateFee() {
            return 0.001;
        }
    }
}
