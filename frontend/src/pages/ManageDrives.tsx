import { useState, useEffect } from 'react';
import api from '../api/axios';
import type { ApiResponse } from '../types/api';
import type { PlacementDrive, Company, JobApplication, PlacementDriveRequest } from '../types/placement';
import './ManageDrives.css';

function ManageDrives() {
  const [drives, setDrives] = useState<PlacementDrive[]>([]);
  const [companies, setCompanies] = useState<Company[]>([]);
  const [loading, setLoading] = useState(true);

  // New drive form modal
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [creating, setCreating] = useState(false);
  const [createError, setCreateError] = useState<string | null>(null);

  // Form state
  const [companyId, setCompanyId] = useState<number | ''>('');
  const [title, setTitle] = useState('');
  const [jobRole, setJobRole] = useState('');
  const [description, setDescription] = useState('');
  const [packageLpa, setPackageLpa] = useState<number | ''>(10.0);
  const [location, setLocation] = useState('');
  const [driveDate, setDriveDate] = useState('');
  const [minCgpa, setMinCgpa] = useState<number | ''>(7.0);
  const [maxBacklogs, setMaxBacklogs] = useState<number | ''>(0);
  const [minTenthPercentage, setMinTenthPercentage] = useState<number | ''>(70.0);
  const [minTwelfthPercentage, setMinTwelfthPercentage] = useState<number | ''>(70.0);
  const [eligibleDepartments, setEligibleDepartments] = useState('ALL');

  // Applicants view modal
  const [selectedDriveForApplicants, setSelectedDriveForApplicants] = useState<PlacementDrive | null>(null);
  const [applicants, setApplicants] = useState<JobApplication[]>([]);
  const [loadingApplicants, setLoadingApplicants] = useState(false);
  const [statusUpdatingId, setStatusUpdatingId] = useState<number | null>(null);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const [drivesRes, companiesRes] = await Promise.all([
        api.get<ApiResponse<PlacementDrive[]>>('/drives'),
        api.get<ApiResponse<Company[]>>('/companies'),
      ]);
      setDrives(drivesRes.data.data);
      setCompanies(companiesRes.data.data);
      if (companiesRes.data.data.length > 0 && companyId === '') {
        setCompanyId(companiesRes.data.data[0].id);
      }
    } catch (err) {
      console.error('Failed to load data', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateDrive = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!companyId) {
      setCreateError('Please select a company');
      return;
    }
    setCreating(true);
    setCreateError(null);

    const payload: PlacementDriveRequest = {
      companyId: Number(companyId),
      title,
      jobRole,
      description,
      packageLpa: Number(packageLpa),
      location,
      driveDate: driveDate || undefined,
      minCgpa: Number(minCgpa),
      maxBacklogs: Number(maxBacklogs),
      minTenthPercentage: Number(minTenthPercentage),
      minTwelfthPercentage: Number(minTwelfthPercentage),
      eligibleDepartments,
      status: 'UPCOMING',
    };

    try {
      await api.post('/drives', payload);
      setShowCreateModal(false);
      resetForm();
      loadData();
    } catch (err: any) {
      setCreateError(err.response?.data?.message || 'Failed to create placement drive');
    } finally {
      setCreating(false);
    }
  };

  const handleViewApplicants = async (drive: PlacementDrive) => {
    setSelectedDriveForApplicants(drive);
    setLoadingApplicants(true);
    try {
      const res = await api.get<ApiResponse<JobApplication[]>>(`/applications/drive/${drive.id}`);
      setApplicants(res.data.data);
    } catch (err) {
      console.error('Failed to load applicants', err);
    } finally {
      setLoadingApplicants(false);
    }
  };

  const handleUpdateStatus = async (appId: number, newStatus: string, currentRound: number) => {
    setStatusUpdatingId(appId);
    try {
      await api.patch(`/applications/${appId}/status`, {
        status: newStatus,
        currentRound,
      });
      // Refresh applicants list
      if (selectedDriveForApplicants) {
        const res = await api.get<ApiResponse<JobApplication[]>>(`/applications/drive/${selectedDriveForApplicants.id}`);
        setApplicants(res.data.data);
      }
      loadData();
    } catch (err) {
      console.error('Failed to update status', err);
    } finally {
      setStatusUpdatingId(null);
    }
  };

  const resetForm = () => {
    setTitle('');
    setJobRole('');
    setDescription('');
    setPackageLpa(10.0);
    setLocation('');
    setDriveDate('');
    setMinCgpa(7.0);
    setMaxBacklogs(0);
    setMinTenthPercentage(70.0);
    setMinTwelfthPercentage(70.0);
    setEligibleDepartments('ALL');
  };

  return (
    <div className="manage-drives-page">
      <div className="manage-header">
        <div>
          <h1>Placement Drives Management</h1>
          <p className="subtitle">Configure recruitment drives, define eligibility cutoffs, and manage candidate pipelines.</p>
        </div>
        <button className="btn-primary" onClick={() => setShowCreateModal(true)}>
          + Create New Drive
        </button>
      </div>

      {loading ? (
        <div className="loading-state">Loading placement drives...</div>
      ) : drives.length === 0 ? (
        <div className="empty-state">No placement drives found. Click "+ Create New Drive" to add one.</div>
      ) : (
        <div className="table-card ui-card">
          <div className="table-responsive">
            <table className="drives-table">
              <thead>
                <tr>
                  <th>Drive / Role</th>
                  <th>Company</th>
                  <th>Package</th>
                  <th>Criteria Cutoffs</th>
                  <th>Drive Date</th>
                  <th>Applicants</th>
                  <th>Pipeline Action</th>
                </tr>
              </thead>
              <tbody>
                {drives.map((d) => (
                  <tr key={d.id}>
                    <td>
                      <strong>{d.title}</strong>
                      <div className="table-sub">{d.jobRole}</div>
                    </td>
                    <td>{d.companyName}</td>
                    <td><span className="highlight-pkg">₹{d.packageLpa} LPA</span></td>
                    <td>
                      <span className="crit-pill">CGPA: {d.minCgpa}+</span>
                      <span className="crit-pill">Backlogs: {d.maxBacklogs}</span>
                    </td>
                    <td>{d.driveDate || 'TBD'}</td>
                    <td>
                      <span className="badge-count">{d.totalApplications} applied</span>
                    </td>
                    <td>
                      <button className="btn-manage-app" onClick={() => handleViewApplicants(d)}>
                        Manage Candidates →
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Create Drive Modal */}
      {showCreateModal && (
        <div className="modal-overlay" onClick={() => setShowCreateModal(false)}>
          <div className="modal-content large" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <div>
                <h2>Post Placement Drive</h2>
                <p className="modal-subtitle">Publish a new campus recruitment drive for eligible candidates.</p>
              </div>
              <button className="close-btn" onClick={() => setShowCreateModal(false)}>×</button>
            </div>
            {createError && <div className="apply-alert error">{createError}</div>}
            <form onSubmit={handleCreateDrive} className="modal-body">
              <div className="form-grid">
                <div className="form-field">
                  <label>Recruiting Company *</label>
                  <select
                    value={companyId}
                    onChange={(e) => setCompanyId(e.target.value === '' ? '' : Number(e.target.value))}
                    required
                  >
                    <option value="">Select Company</option>
                    {companies.map((c) => (
                      <option key={c.id} value={c.id}>{c.name}</option>
                    ))}
                  </select>
                </div>
                <div className="form-field">
                  <label>Drive Title *</label>
                  <input
                    type="text"
                    placeholder="e.g. Graduate Engineer Trainee 2025"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                  />
                </div>
                <div className="form-field">
                  <label>Job Role *</label>
                  <input
                    type="text"
                    placeholder="e.g. Software Engineer / Data Analyst"
                    value={jobRole}
                    onChange={(e) => setJobRole(e.target.value)}
                    required
                  />
                </div>
                <div className="form-field">
                  <label>Package (LPA in ₹) *</label>
                  <input
                    type="number"
                    step="0.1"
                    min="0"
                    placeholder="12.5"
                    value={packageLpa}
                    onChange={(e) => setPackageLpa(e.target.value === '' ? '' : parseFloat(e.target.value))}
                    required
                  />
                </div>
                <div className="form-field">
                  <label>Drive Date</label>
                  <input
                    type="date"
                    value={driveDate}
                    onChange={(e) => setDriveDate(e.target.value)}
                  />
                </div>
                <div className="form-field">
                  <label>Work Location</label>
                  <input
                    type="text"
                    placeholder="e.g. Bengaluru / Pan India"
                    value={location}
                    onChange={(e) => setLocation(e.target.value)}
                  />
                </div>
                <div className="form-field">
                  <label>Minimum CGPA Cutoff</label>
                  <input
                    type="number"
                    step="0.1"
                    min="0"
                    max="10"
                    value={minCgpa}
                    onChange={(e) => setMinCgpa(e.target.value === '' ? '' : parseFloat(e.target.value))}
                  />
                </div>
                <div className="form-field">
                  <label>Maximum Allowed Backlogs</label>
                  <input
                    type="number"
                    min="0"
                    value={maxBacklogs}
                    onChange={(e) => setMaxBacklogs(e.target.value === '' ? '' : parseInt(e.target.value))}
                  />
                </div>
                <div className="form-field">
                  <label>Minimum 10th Percentage (%)</label>
                  <input
                    type="number"
                    step="0.1"
                    value={minTenthPercentage}
                    onChange={(e) => setMinTenthPercentage(e.target.value === '' ? '' : parseFloat(e.target.value))}
                  />
                </div>
                <div className="form-field">
                  <label>Minimum 12th Percentage (%)</label>
                  <input
                    type="number"
                    step="0.1"
                    value={minTwelfthPercentage}
                    onChange={(e) => setMinTwelfthPercentage(e.target.value === '' ? '' : parseFloat(e.target.value))}
                  />
                </div>
                <div className="form-field full-width">
                  <label>Eligible Departments (e.g. CSE,IT,ECE or ALL)</label>
                  <input
                    type="text"
                    value={eligibleDepartments}
                    onChange={(e) => setEligibleDepartments(e.target.value)}
                    placeholder="CSE,IT,ECE or ALL"
                  />
                </div>
                <div className="form-field full-width">
                  <label>Job Description & Responsibilities</label>
                  <textarea
                    rows={3}
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    placeholder="Detailed responsibilities, interview prerequisites, and perks..."
                  />
                </div>
              </div>
              <div className="modal-footer" style={{ padding: '16px 0 0 0' }}>
                <button type="button" className="btn-secondary" onClick={() => setShowCreateModal(false)}>
                  Cancel
                </button>
                <button type="submit" className="btn-primary" disabled={creating}>
                  {creating ? 'Publishing...' : 'Publish Drive'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Manage Applicants Modal */}
      {selectedDriveForApplicants && (
        <div className="modal-overlay" onClick={() => setSelectedDriveForApplicants(null)}>
          <div className="modal-content extra-large" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <div>
                <h2>Applicants: {selectedDriveForApplicants.title}</h2>
                <p className="modal-subtitle">{selectedDriveForApplicants.companyName} • {applicants.length} Total Applicants</p>
              </div>
              <button className="close-btn" onClick={() => setSelectedDriveForApplicants(null)}>×</button>
            </div>

            <div className="modal-body">
              {loadingApplicants ? (
                <p className="loading-state">Loading candidate submissions...</p>
              ) : applicants.length === 0 ? (
                <div className="empty-state">No candidates have applied to this drive yet.</div>
              ) : (
                <div className="table-responsive">
                  <table className="applicants-table">
                    <thead>
                      <tr>
                        <th>Candidate</th>
                        <th>Roll No / Dept</th>
                        <th>CGPA / Backlogs</th>
                        <th>Resume</th>
                        <th>Current Round</th>
                        <th>Status</th>
                        <th>Advance & Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      {applicants.map((a) => (
                        <tr key={a.id}>
                          <td>
                            <strong>{a.studentName}</strong>
                            <div className="table-sub">{a.studentEmail}</div>
                          </td>
                          <td>
                            {a.rollNumber}
                            <div className="table-sub">{a.department}</div>
                          </td>
                          <td>
                            <span className="crit-pill">CGPA: {a.cgpa}</span>
                            <span className="crit-pill">{a.backlogs} Backlogs</span>
                          </td>
                          <td>
                            {a.resumeUrl ? (
                              <a href={a.resumeUrl} target="_blank" rel="noopener noreferrer" className="link-resume">
                                View ↗
                              </a>
                            ) : (
                              <span className="table-sub">N/A</span>
                            )}
                          </td>
                          <td>
                            <div className="round-controller">
                              <span className="round-tag">R{a.currentRound}</span>
                              <button
                                className="btn-round-advance"
                                title="Advance candidate to next round"
                                onClick={() => handleUpdateStatus(a.id, 'IN_INTERVIEW', a.currentRound + 1)}
                                disabled={statusUpdatingId === a.id}
                              >
                                + Next Round
                              </button>
                            </div>
                          </td>
                          <td>
                            <span className={`status-pill ${a.status.toLowerCase()}`}>
                              {a.status}
                            </span>
                          </td>
                          <td>
                            <div className="action-buttons-cell">
                              <button
                                className="btn-action shortlist"
                                onClick={() => handleUpdateStatus(a.id, 'SHORTLISTED', a.currentRound)}
                                disabled={statusUpdatingId === a.id}
                              >
                                Shortlist
                              </button>
                              <button
                                className="btn-action select"
                                onClick={() => handleUpdateStatus(a.id, 'SELECTED', a.currentRound)}
                                disabled={statusUpdatingId === a.id}
                              >
                                Select / Offer
                              </button>
                              <button
                                className="btn-action reject"
                                onClick={() => handleUpdateStatus(a.id, 'REJECTED', a.currentRound)}
                                disabled={statusUpdatingId === a.id}
                              >
                                Reject
                              </button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>

            <div className="modal-footer">
              <button className="btn-secondary" onClick={() => setSelectedDriveForApplicants(null)}>
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default ManageDrives;
