package com.mbacms.mapper;

import com.mbacms.DTO.InsurancePlanDto;
import com.mbacms.model.InsurancePlan;
import org.springframework.stereotype.Component;

@Component
public class InsurancePlanMapper {


    public InsurancePlanDto entityToDto(InsurancePlan insurancePlan){
        return new InsurancePlanDto(
                insurancePlan.getId(),
                insurancePlan.getPlanName(),
                insurancePlan.getPlanType(),
                insurancePlan.getPlanDesc(),
                insurancePlan.getCoverageAmount(),
                insurancePlan.getPremiumAmount(),
                insurancePlan.getDurationMonths(),
                insurancePlan.getActiveStatus()
        );
    }
}
