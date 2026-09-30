package com.placement.dto;

public class DashboardStatsResponse {

    private long totalDrives;
    private long activeDrives;
    private long totalCompanies;
    private long totalStudents;
    private long placedStudents;
    private long totalApplications;
    private Double averagePackage;
    private Double highestPackage;
    private double placementRatePercentage;

    public DashboardStatsResponse() {}

    // Getters and Setters
    public long getTotalDrives() { return totalDrives; }
    public void setTotalDrives(long totalDrives) { this.totalDrives = totalDrives; }

    public long getActiveDrives() { return activeDrives; }
    public void setActiveDrives(long activeDrives) { this.activeDrives = activeDrives; }

    public long getTotalCompanies() { return totalCompanies; }
    public void setTotalCompanies(long totalCompanies) { this.totalCompanies = totalCompanies; }

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getPlacedStudents() { return placedStudents; }
    public void setPlacedStudents(long placedStudents) { this.placedStudents = placedStudents; }

    public long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }

    public Double getAveragePackage() { return averagePackage; }
    public void setAveragePackage(Double averagePackage) { this.averagePackage = averagePackage; }

    public Double getHighestPackage() { return highestPackage; }
    public void setHighestPackage(Double highestPackage) { this.highestPackage = highestPackage; }

    public double getPlacementRatePercentage() { return placementRatePercentage; }
    public void setPlacementRatePercentage(double placementRatePercentage) { this.placementRatePercentage = placementRatePercentage; }
}
