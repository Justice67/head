/*
 * Copyright (c) 2005-2011 Grameen Foundation USA
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package org.mifos.ui.core.controller;

import org.mifos.dto.screen.PenaltyParametersDto;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
public class PenaltyFormValidator implements Validator {
    private final static boolean DECIMAL = true;
    private final static boolean INTEGER = false;
    private final static boolean MANDATORY = true;
    private final static boolean OPTIONAL = false;
    
    @Override
    public boolean supports(Class<?> clazz) {
        return PenaltyFormBean.class.isAssignableFrom(clazz) || PenaltyParametersDto.class.isAssignableFrom(clazz);
    }

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = { "REC_CATCH_EXCEPTION"}, justification = "Using catch all to detect invalid dates.")
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
    @Override
    public void validate(Object target, Errors errors) {
        if(target instanceof PenaltyParametersDto) {
            return;
        }
        
        PenaltyFormBean formBean = (PenaltyFormBean) target;
        
        checkCategory(errors, formBean.getCategoryTypeId());
        checkStatus(errors, formBean.getStatusId());
        
        emptyField(errors, formBean.getName(), "Penalty Name");
        emptyField(errors, formBean.getPeriodTypeId(), "Grace Period Type");
        emptyField(errors, formBean.getFrequencyId(), "Penalty Application Frequency");
        emptyField(errors, formBean.getGlCodeId(), "GL Code");
        
        checkNumber(errors, formBean.getDuration(), "Grace Period Duration", OPTIONAL, INTEGER);
        checkNumber(errors, formBean.getMin(), "Cumulative Penalty Limit (Minimum)", MANDATORY, INTEGER);
        checkNumber(errors, formBean.getMax(), "Cumulative Penalty Limit (Maximum)", MANDATORY, INTEGER);
        
        checkMinGreaterMax(errors, formBean.getMin(), formBean.getMax());
        
        if(StringUtils.hasText(formBean.getCategoryTypeId()) && formBean.getCategoryTypeId().equalsIgnoreCase("1")) {
            if (StringUtils.hasText(formBean.getRate()) && StringUtils.hasText(formBean.getAmount())) {
                errors.reject("error.penalty.amountAndRateOrFormula");
            } else if (StringUtils.hasText(formBean.getRate()) && StringUtils.hasText(formBean.getFormulaId())) {
                checkNumber(errors, formBean.getRate(), "Rate", MANDATORY, DECIMAL);
            } else if (StringUtils.hasText(formBean.getRate()) && !StringUtils.hasText(formBean.getFormulaId())) {
                errors.reject("error.penalty.rate");
            } else if (StringUtils.hasText(formBean.getAmount())) {
                checkNumber(errors, formBean.getAmount(), "Amount", MANDATORY, DECIMAL);
            } else {
                errors.reject("error.penalty.amountAndRateOrFormula");
            }
        } else {
            checkNumber(errors, formBean.getAmount(), "Amount", MANDATORY, DECIMAL);
        }
        

    }
    
    private void checkNumber(Errors errors, String value, String field, boolean isMandatory, boolean isDecimal) {
        int count = errors.getErrorCount();

        if(isMandatory) {
            emptyField(errors, value, field);
        
            if(errors.getErrorCount() > count) {
                return;
            }
        }
        
        if(isDecimal) {
            incorrectDoubleValue(errors, value, field);
        } else {
            incorrectIntegerValue(errors, value, field);
        }
    }
    
    private void incorrectDoubleValue(Errors errors, String value, String field) {
        if(value != null && StringUtils.hasText(value)) {
            try {
                Double val = Double.valueOf(value);
                
                if(val < 0) {
                    errors.reject("error.penalty.incorrectDouble", new String[] { field }, null);
                }
            } catch (NumberFormatException e) {
                errors.reject("error.penalty.incorrectDouble", new String[] { field }, null);
            }
        }
    }
    
    private void incorrectIntegerValue(Errors errors, String value, String field) {
        if(value != null && StringUtils.hasText(value)) {
            try {
                Integer val = Integer.valueOf(value);
                
                if(val < 0) {
                    errors.reject("error.penalty.incorrectInteger", new String[] { field }, null);
                }
            } catch (NumberFormatException e) {
                errors.reject("error.penalty.incorrectInteger", new String[] { field }, null);
            }
        }
    }
    
    private void emptyField(Errors errors, String value, String field) {
        if(value == null || !StringUtils.hasText(value)) {
            if(field == null) {
                errors.reject("error.penalty.mandatory");
            } else {
                errors.reject("error.penalty.mandatory", new String[] { field }, null);
            }
        }
    }
    
    @SuppressWarnings("PMD.EmptyCatchBlock")
    private void checkMinGreaterMax(Errors errors, String min, String max) {
        if((min == null || !StringUtils.hasText(min)) || (max == null || !StringUtils.hasText(max))) {
            return;
        }
        
        try {
            int minimum = Integer.valueOf(min);
            int maximum = Integer.valueOf(max);

            if(minimum - maximum > 0) {
                errors.reject("error.penalty.minGreaterMax");
            }
        } catch (NumberFormatException e) {
            //error was found in incorrectIntegerValue method
        }
    }
    
    private void checkCategory(Errors errors, String value) {
        if(value == null || !StringUtils.hasText(value)) {
            errors.reject("error.penalty.category");
        }
    }
    
    private void checkStatus(Errors errors, String value) {
        if(value == null || !StringUtils.hasText(value)) {
            errors.reject("error.penalty.status");
        }
    }

}
