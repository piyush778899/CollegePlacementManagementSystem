package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.CompanyRequest;
import com.placement.entity.Company;
import com.placement.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Company>>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(ApiResponse.success(companies));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Company>> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        return ResponseEntity.ok(ApiResponse.success(company));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<ApiResponse<Company>> createCompany(@Valid @RequestBody CompanyRequest request) {
        Company created = companyService.createCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Company created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<ApiResponse<Company>> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequest request) {
        Company updated = companyService.updateCompany(id, request);
        return ResponseEntity.ok(ApiResponse.success("Company updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok(ApiResponse.success("Company deleted successfully", null));
    }
}
