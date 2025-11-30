package com.getjob.backend.service;

import com.getjob.backend.dto.CompanyDTO;

public interface CompanyService {
    CompanyDTO.CompanyInfoResponse getCompanyInfo(String userId);
    CompanyDTO.CompanyInfoResponse updateCompanyInfo(String userId, CompanyDTO.UpdateCompanyRequest request);
    CompanyDTO.CompanyInfoResponse createCompanyInfo(String userId, CompanyDTO.CreateCompanyRequest request);
}
