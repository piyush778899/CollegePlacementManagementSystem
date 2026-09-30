export interface Company {
  id: number;
  name: string;
  website?: string;
  industry?: string;
  description?: string;
  contactEmail?: string;
  contactPhone?: string;
  location?: string;
  logoUrl?: string;
  createdAt?: string;
}

export interface CompanyRequest {
  name: string;
  website?: string;
  industry?: string;
  description?: string;
  contactEmail?: string;
  contactPhone?: string;
  location?: string;
  logoUrl?: string;
}

export interface StudentProfile {
  id: number;
  userId: number;
  studentName: string;
  studentEmail: string;
  rollNumber: string;
  department: string;
  cgpa: number;
  tenthPercentage: number;
  twelfthPercentage: number;
  backlogs: number;
  passingYear?: number;
  phone?: string;
  gender?: string;
  skills?: string;
  resumeUrl?: string;
  isPlaced: boolean;
}

export interface StudentProfileRequest {
  rollNumber: string;
  department: string;
  cgpa: number;
  tenthPercentage: number;
  twelfthPercentage: number;
  backlogs: number;
  passingYear?: number;
  phone?: string;
  gender?: string;
  skills?: string;
  resumeUrl?: string;
}

export interface PlacementDrive {
  id: number;
  companyId: number;
  companyName: string;
  companyLogoUrl?: string;
  companyLocation?: string;
  title: string;
  jobRole: string;
  description?: string;
  packageLpa: number;
  location?: string;
  driveDate?: string;
  deadline?: string;
  minCgpa: number;
  maxBacklogs: number;
  minTenthPercentage: number;
  minTwelfthPercentage: number;
  eligibleDepartments: string;
  status: 'UPCOMING' | 'ONGOING' | 'COMPLETED' | 'CANCELLED';
  totalApplications: number;
  isEligibleForCurrentUser?: boolean;
  hasApplied?: boolean;
}

export interface PlacementDriveRequest {
  companyId: number;
  title: string;
  jobRole: string;
  description?: string;
  packageLpa: number;
  location?: string;
  driveDate?: string;
  deadline?: string;
  minCgpa: number;
  maxBacklogs: number;
  minTenthPercentage: number;
  minTwelfthPercentage: number;
  eligibleDepartments: string;
  status: string;
}

export interface CriterionCheck {
  criterion: string;
  required: string;
  actual: string;
  passed: boolean;
}

export interface EligibilityResponse {
  eligible: boolean;
  reason: string;
  checks: CriterionCheck[];
}

export interface JobApplication {
  id: number;
  driveId: number;
  driveTitle: string;
  companyName: string;
  companyLogoUrl?: string;
  jobRole: string;
  packageLpa: number;
  studentProfileId: number;
  studentName: string;
  studentEmail: string;
  rollNumber: string;
  department: string;
  cgpa: number;
  backlogs: number;
  resumeUrl?: string;
  status: 'APPLIED' | 'UNDER_REVIEW' | 'SHORTLISTED' | 'IN_INTERVIEW' | 'SELECTED' | 'REJECTED';
  currentRound: number;
  feedback?: string;
  appliedAt: string;
}

export interface DashboardStats {
  totalDrives: number;
  activeDrives: number;
  totalCompanies: number;
  totalStudents: number;
  placedStudents: number;
  totalApplications: number;
  averagePackage: number;
  highestPackage: number;
  placementRatePercentage: number;
}
