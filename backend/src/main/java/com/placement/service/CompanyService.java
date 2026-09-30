package com.placement.service;

import com.placement.dto.CompanyRequest;
import com.placement.entity.Company;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
    }

    @Transactional
    public Company createCompany(CompanyRequest request) {
        if (companyRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new IllegalArgumentException("Company already exists with name: " + request.getName());
        }

        Company company = new Company();
        updateEntityFromRequest(company, request);
        return companyRepository.save(company);
    }

    @Transactional
    public Company updateCompany(Long id, CompanyRequest request) {
        Company company = getCompanyById(id);
        if (!company.getName().equalsIgnoreCase(request.getName().trim()) &&
            companyRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new IllegalArgumentException("Company already exists with name: " + request.getName());
        }

        updateEntityFromRequest(company, request);
        return companyRepository.save(company);
    }

    @Transactional
    public void deleteCompany(Long id) {
        Company company = getCompanyById(id);
        companyRepository.delete(company);
    }

    private void updateEntityFromRequest(Company company, CompanyRequest request) {
        company.setName(request.getName().trim());
        company.setWebsite(request.getWebsite());
        company.setIndustry(request.getIndustry());
        company.setDescription(request.getDescription());
        company.setContactEmail(request.getContactEmail());
        company.setContactPhone(request.getContactPhone());
        company.setLocation(request.getLocation());
        company.setLogoUrl(request.getLogoUrl());
    }
}
