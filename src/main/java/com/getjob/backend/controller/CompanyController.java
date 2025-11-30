package com.getjob.backend.controller;

import com.getjob.backend.annotation.RequireAuth;
import com.getjob.backend.common.ApiResponse;
import com.getjob.backend.common.ResultCode;
import com.getjob.backend.dto.CompanyDTO;
import com.getjob.backend.service.CompanyService;
import com.getjob.backend.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@RequireAuth
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/companyinfo")
    public ApiResponse<CompanyDTO.CompanyInfoResponse> getCompanyInfo() {
        try {
            String userId = UserContext.getUserId();
            CompanyDTO.CompanyInfoResponse response = companyService.getCompanyInfo(userId);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.NOT_FOUND.getCode(), e.getMessage());
        }
    }

    @PutMapping("/companyinfo")
    public ApiResponse<CompanyDTO.CompanyInfoResponse> updateCompanyInfo(
            @RequestBody CompanyDTO.UpdateCompanyRequest request) {
        try {
            String userId = UserContext.getUserId();
            CompanyDTO.CompanyInfoResponse response = companyService.updateCompanyInfo(userId, request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @PostMapping("/companyinfo")
    public ApiResponse<CompanyDTO.CompanyInfoResponse> createCompanyInfo(
            @RequestBody CompanyDTO.CreateCompanyRequest request) {
        try {
            String userId = UserContext.getUserId();
            CompanyDTO.CompanyInfoResponse response = companyService.createCompanyInfo(userId, request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }
}
