import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import { useAuth } from '../context/AuthContext';
import type { ApiResponse } from '../types/api';
import type { PlacementDrive, EligibilityResponse } from '../types/placement';
import './Drives.css';

function Drives() {
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth();

  const [drives, setDrives] = useState<PlacementDrive[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [departmentFilter, setDepartmentFilter] = useState('ALL');
  const [selectedDrive, setSelectedDrive] = useState<PlacementDrive | null>(null);

  // Eligibility modal state
  const [eligibilityData, setEligibilityData] = useState<EligibilityResponse | null>(null);
  const [checkingEligibility, setCheckingEligibility] = useState(false);
  const [applying, setApplying] = useState(false);
  const [applyMessage, setApplyMessage] = useState<{ text: string; success: boolean } | null>(null);

  useEffect(() => {
    fetchDrives();
  }, []);

  const fetchDrives = () => {
    setLoading(true);
    api.get<ApiResponse<PlacementDrive[]>>('/drives')
      .then((res) => setDrives(res.data.data))
      .catch((err) => console.error('Failed to load drives', err))
      .finally(() => setLoading(false));
  };

  const handleOpenDriveDetails = async (drive: PlacementDrive) => {
    setSelectedDrive(drive);
    setApplyMessage(null);
    setEligibilityData(null);

    if (isAuthenticated && user?.role === 'STUDENT') {
      setCheckingEligibility(true);
      try {
        const res = await api.get<ApiResponse<EligibilityResponse>>(`/drives/${drive.id}/eligibility`);
        setEligibilityData(res.data.data);
      } catch (err: any) {
        console.error('Could not check eligibility', err);
      } finally {
        setCheckingEligibility(false);
      }
    }
  };

  const handleApply = async (driveId: number) => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    setApplying(true);
    setApplyMessage(null);
    try {
      await api.post(`/applications/apply/${driveId}`);
      setApplyMessage({ text: 'Application submitted successfully!', success: true });
      fetchDrives(); // Refresh drives to show updated state
    } catch (err: any) {
      const msg = err.response?.data?.message || 'Could not submit application. Please check your eligibility.';
      setApplyMessage({ text: msg, success: false });
    } finally {
      setApplying(false);
    }
  };

  const filteredDrives = drives.filter((drive) => {
    const matchesSearch =
      drive.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      drive.companyName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      drive.jobRole.toLowerCase().includes(searchQuery.toLowerCase());

    const matchesDept =
      departmentFilter === 'ALL' ||
      drive.eligibleDepartments === 'ALL' ||
      drive.eligibleDepartments.toUpperCase().includes(departmentFilter.toUpperCase());

    return matchesSearch && matchesDept;
  });

  return (
    <div className="drives-page">
      <div className="drives-header">
        <div>
          <h1>Placement Drives</h1>
          <p className="subtitle">Discover recruitment opportunities and check eligibility.</p>
        </div>
        {isAuthenticated && (user?.role === 'ADMIN' || user?.role === 'COMPANY') && (
          <button className="btn-primary" onClick={() => navigate('/manage-drives')}>
            + Post New Drive
          </button>
        )}
      </div>

      <div className="drives-controls">
        <div className="search-box">
          <span className="search-icon">🔍</span>
          <input
            type="text"
            placeholder="Search company, role or title..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>

        <div className="filter-select">
          <label htmlFor="dept-select">Department:</label>
          <select
            id="dept-select"
            value={departmentFilter}
            onChange={(e) => setDepartmentFilter(e.target.value)}
          >
            <option value="ALL">All Departments</option>
            <option value="CSE">Computer Science (CSE)</option>
            <option value="IT">Information Tech (IT)</option>
            <option value="ECE">Electronics (ECE)</option>
            <option value="EE">Electrical (EE)</option>
            <option value="MECH">Mechanical (MECH)</option>
          </select>
        </div>
      </div>

      {loading ? (
        <div className="loading-state">Loading available placement drives...</div>
      ) : filteredDrives.length === 0 ? (
        <div className="empty-state">
          <p>No placement drives match your current search criteria.</p>
        </div>
      ) : (
        <div className="drives-grid">
          {filteredDrives.map((drive) => (
            <div key={drive.id} className="drive-card ui-card" onClick={() => handleOpenDriveDetails(drive)}>
              <div className="card-top">
                <div className="company-info">
                  <div className="company-avatar-box">
                    {drive.companyName.charAt(0)}
                  </div>
                  <div>
                    <h3 className="company-name">{drive.companyName}</h3>
                    <p className="job-role">{drive.jobRole}</p>
                  </div>
                </div>
                <div className="package-badge">
                  ₹{drive.packageLpa} LPA
                </div>
              </div>

              <h4 className="drive-title">{drive.title}</h4>
              <p className="drive-desc">{drive.description || 'Full-time campus placement drive.'}</p>

              <div className="criteria-tags">
                <span className="tag">Min CGPA: <strong>{drive.minCgpa}</strong></span>
                <span className="tag">Max Backlogs: <strong>{drive.maxBacklogs}</strong></span>
                <span className="tag">Depts: <strong>{drive.eligibleDepartments}</strong></span>
              </div>

              <div className="card-bottom">
                <div className="drive-meta">
                  <span className="drive-date">📅 Drive: {drive.driveDate || 'TBD'}</span>
                  {drive.totalApplications > 0 && (
                    <span className="applicants-count">👥 {drive.totalApplications} applied</span>
                  )}
                </div>

                <div className="card-action-status">
                  {drive.hasApplied ? (
                    <span className="status-badge applied">✓ Applied</span>
                  ) : drive.isEligibleForCurrentUser === true ? (
                    <span className="status-badge eligible">Eligible to Apply</span>
                  ) : drive.isEligibleForCurrentUser === false ? (
                    <span className="status-badge ineligible">Criteria Mismatch</span>
                  ) : (
                    <span className="view-btn-text">View Details →</span>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Drive Details Modal */}
      {selectedDrive && (
        <div className="modal-overlay" onClick={() => setSelectedDrive(null)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <div>
                <h2>{selectedDrive.title}</h2>
                <p className="modal-subtitle">{selectedDrive.companyName} • {selectedDrive.jobRole}</p>
              </div>
              <button className="close-btn" onClick={() => setSelectedDrive(null)}>×</button>
            </div>

            <div className="modal-body">
              <div className="modal-meta-grid">
                <div className="meta-item">
                  <span className="meta-label">Annual Package</span>
                  <span className="meta-value highlight">₹{selectedDrive.packageLpa} LPA</span>
                </div>
                <div className="meta-item">
                  <span className="meta-label">Location</span>
                  <span className="meta-value">{selectedDrive.location || 'Pan India'}</span>
                </div>
                <div className="meta-item">
                  <span className="meta-label">Drive Date</span>
                  <span className="meta-value">{selectedDrive.driveDate || 'To be announced'}</span>
                </div>
                <div className="meta-item">
                  <span className="meta-label">Application Deadline</span>
                  <span className="meta-value">{selectedDrive.deadline ? new Date(selectedDrive.deadline).toLocaleDateString() : 'Rolling'}</span>
                </div>
              </div>

              {selectedDrive.description && (
                <div className="modal-section">
                  <h3>Job Description</h3>
                  <p>{selectedDrive.description}</p>
                </div>
              )}

              {/* Eligibility Evaluation Box */}
              {isAuthenticated && user?.role === 'STUDENT' && (
                <div className="eligibility-box">
                  <h3>Academic Eligibility Evaluation</h3>
                  {checkingEligibility ? (
                    <p className="loading-eval">Evaluating your academic profile against drive criteria...</p>
                  ) : eligibilityData ? (
                    <div>
                      <div className={`eval-banner ${eligibilityData.eligible ? 'passed' : 'failed'}`}>
                        {eligibilityData.eligible ? '✅ You meet all criteria for this drive' : '❌ Not Eligible: ' + eligibilityData.reason}
                      </div>

                      <div className="checks-list">
                        {eligibilityData.checks.map((c, i) => (
                          <div key={i} className={`check-row ${c.passed ? 'check-pass' : 'check-fail'}`}>
                            <span className="check-icon">{c.passed ? '✓' : '✗'}</span>
                            <span className="check-name">{c.criterion}</span>
                            <span className="check-req">Required: {c.required}</span>
                            <span className="check-actual">Your Profile: {c.actual}</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  ) : (
                    <div className="no-profile-warning">
                      <p>⚠️ Complete your student profile to automatically evaluate your eligibility.</p>
                      <button className="btn-secondary" onClick={() => navigate('/profile')}>
                        Update Profile
                      </button>
                    </div>
                  )}
                </div>
              )}

              {applyMessage && (
                <div className={`apply-alert ${applyMessage.success ? 'success' : 'error'}`}>
                  {applyMessage.text}
                </div>
              )}
            </div>

            <div className="modal-footer">
              <button className="btn-secondary" onClick={() => setSelectedDrive(null)}>
                Close
              </button>
              {isAuthenticated && user?.role === 'STUDENT' ? (
                selectedDrive.hasApplied ? (
                  <button className="btn-primary" disabled>
                    ✓ Already Applied
                  </button>
                ) : (
                  <button
                    className="btn-primary"
                    disabled={applying || (eligibilityData !== null && !eligibilityData.eligible)}
                    onClick={() => handleApply(selectedDrive.id)}
                  >
                    {applying ? 'Submitting...' : 'Apply Now'}
                  </button>
                )
              ) : !isAuthenticated ? (
                <button className="btn-primary" onClick={() => navigate('/login')}>
                  Login to Apply
                </button>
              ) : null}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Drives;
