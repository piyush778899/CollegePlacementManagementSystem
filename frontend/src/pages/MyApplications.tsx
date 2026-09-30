import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';
import type { ApiResponse } from '../types/api';
import type { JobApplication } from '../types/placement';
import './MyApplications.css';

function MyApplications() {
  const [applications, setApplications] = useState<JobApplication[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchApplications();
  }, []);

  const fetchApplications = () => {
    setLoading(true);
    api.get<ApiResponse<JobApplication[]>>('/applications/my-applications')
      .then((res) => setApplications(res.data.data))
      .catch((err) => console.error('Failed to load applications', err))
      .finally(() => setLoading(false));
  };

  const getStatusBadgeClass = (status: string) => {
    switch (status) {
      case 'SELECTED': return 'status-selected';
      case 'SHORTLISTED': return 'status-shortlisted';
      case 'IN_INTERVIEW': return 'status-interview';
      case 'REJECTED': return 'status-rejected';
      case 'UNDER_REVIEW': return 'status-review';
      default: return 'status-applied';
    }
  };

  const getStatusLabel = (status: string) => {
    switch (status) {
      case 'SELECTED': return '🎉 Offer Extended';
      case 'SHORTLISTED': return '⭐ Shortlisted';
      case 'IN_INTERVIEW': return '🎙️ In Interview Stage';
      case 'REJECTED': return '❌ Not Selected';
      case 'UNDER_REVIEW': return '⏳ Under Review';
      default: return '📋 Applied';
    }
  };

  return (
    <div className="applications-page">
      <div className="applications-header">
        <div>
          <h1>My Job Applications</h1>
          <p className="subtitle">Track recruitment round progression, feedback, and offer status in real time.</p>
        </div>
        <Link to="/drives" className="btn-secondary">
          Browse Open Drives
        </Link>
      </div>

      {loading ? (
        <div className="loading-state">Loading your application history...</div>
      ) : applications.length === 0 ? (
        <div className="empty-state ui-card" style={{ padding: '48px 24px' }}>
          <p>You haven't applied to any placement drives yet.</p>
          <Link to="/drives" className="btn-primary" style={{ marginTop: '16px' }}>
            Explore Open Placement Drives →
          </Link>
        </div>
      ) : (
        <div className="applications-list">
          {applications.map((app) => (
            <div key={app.id} className="app-card ui-card">
              <div className="app-main">
                <div className="app-company-info">
                  <div className="company-logo-placeholder">
                    {app.companyName.charAt(0)}
                  </div>
                  <div>
                    <h3>{app.driveTitle}</h3>
                    <p className="app-role">{app.companyName} • {app.jobRole}</p>
                  </div>
                </div>

                <div className="app-details">
                  <div className="detail-item">
                    <span className="label">Package</span>
                    <span className="value package">₹{app.packageLpa} LPA</span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Applied Date</span>
                    <span className="value">
                      {new Date(app.appliedAt).toLocaleDateString()}
                    </span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Current Round</span>
                    <span className="value round-badge">Round {app.currentRound}</span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Status</span>
                    <span className={`status-badge ${getStatusBadgeClass(app.status)}`}>
                      {getStatusLabel(app.status)}
                    </span>
                  </div>
                </div>
              </div>

              {app.feedback && (
                <div className="app-feedback">
                  <strong>Recruiter Feedback:</strong> {app.feedback}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default MyApplications;
