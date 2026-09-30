import { useState, useEffect } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import api from '../api/axios';
import { useAuth } from '../context/AuthContext';
import type { ApiResponse } from '../types/api';
import type { DashboardStats, PlacementDrive, StudentProfile, JobApplication } from '../types/placement';
import './Dashboard.css';

function Dashboard() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { user } = useAuth();

  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [recentDrives, setRecentDrives] = useState<PlacementDrive[]>([]);
  const [students, setStudents] = useState<StudentProfile[]>([]);
  const [myApplications, setMyApplications] = useState<JobApplication[]>([]);
  const [loading, setLoading] = useState(true);

  // Tab mode for admin view (overview, students, analytics)
  const currentView = searchParams.get('view') || 'overview';

  useEffect(() => {
    loadDashboard();
  }, [user]);

  const loadDashboard = async () => {
    setLoading(true);
    try {
      const promises: Promise<any>[] = [
        api.get<ApiResponse<DashboardStats>>('/dashboard/stats'),
        api.get<ApiResponse<PlacementDrive[]>>('/drives'),
      ];

      if (user?.role === 'ADMIN' || user?.role === 'COMPANY') {
        promises.push(api.get<ApiResponse<StudentProfile[]>>('/students').catch(() => ({ data: { data: [] } })));
      }

      if (user?.role === 'STUDENT') {
        promises.push(api.get<ApiResponse<JobApplication[]>>('/applications/my-applications').catch(() => ({ data: { data: [] } })));
      }

      const results = await Promise.all(promises);
      setStats(results[0].data.data);
      setRecentDrives(results[1].data.data);

      if (user?.role === 'ADMIN' || user?.role === 'COMPANY') {
        setStudents(results[2]?.data?.data || []);
      }

      if (user?.role === 'STUDENT') {
        setMyApplications(results[2]?.data?.data || []);
      }
    } catch (err) {
      console.error('Failed to load dashboard data', err);
    } finally {
      setLoading(false);
    }
  };

  const getRoleDisplayName = (role?: string) => {
    switch (role) {
      case 'ADMIN': return 'Placement Administrator';
      case 'COMPANY': return 'Corporate Recruiter';
      default: return 'Student Candidate';
    }
  };

  return (
    <div className="dashboard-page">
      {/* Header Banner */}
      <div className="dashboard-welcome-banner ui-card">
        <div className="welcome-text-side">
          <div className="welcome-role-tag">
            {getRoleDisplayName(user?.role)}
          </div>
          <h1>Welcome, {user?.fullName}</h1>
          <p className="welcome-subtext">
            {user?.role === 'STUDENT'
              ? 'Stay updated on upcoming placement drives, check your academic eligibility, and track your job applications.'
              : user?.role === 'ADMIN'
              ? 'Oversee campus placements, track recruitment KPIs, approve corporate drives, and evaluate candidate pipelines.'
              : 'Post career opportunities, review eligible students, and manage candidate interview rounds.'}
          </p>
        </div>

        <div className="welcome-actions">
          {user?.role === 'STUDENT' ? (
            <>
              <Link to="/drives" className="btn-primary">
                Explore Drives
              </Link>
              <Link to="/profile" className="btn-secondary">
                Update Profile
              </Link>
            </>
          ) : (
            <>
              <Link to="/manage-drives" className="btn-primary">
                Manage Drives
              </Link>
              <Link to="/companies" className="btn-secondary">
                Recruiters
              </Link>
            </>
          )}
        </div>
      </div>

      {/* Primary KPI Metric Cards */}
      <div className="stats-metric-grid">
        <div className="metric-card ui-card">
          <div className="metric-icon-box blue">👥</div>
          <div className="metric-content">
            <span className="metric-label">Total Students</span>
            <span className="metric-value">{stats?.totalStudents ?? 0}</span>
            <span className="metric-sub">{stats?.placedStudents ?? 0} Placed</span>
          </div>
        </div>

        <div className="metric-card ui-card">
          <div className="metric-icon-box indigo">🏢</div>
          <div className="metric-content">
            <span className="metric-label">Total Companies</span>
            <span className="metric-value">{stats?.totalCompanies ?? 0}</span>
            <span className="metric-sub">Participating Recruiters</span>
          </div>
        </div>

        <div className="metric-card ui-card">
          <div className="metric-icon-box sky">💼</div>
          <div className="metric-content">
            <span className="metric-label">Active Drives</span>
            <span className="metric-value">{stats?.activeDrives ?? 0}</span>
            <span className="metric-sub">{stats?.totalDrives ?? 0} Total Published</span>
          </div>
        </div>

        <div className="metric-card ui-card">
          <div className="metric-icon-box amber">📑</div>
          <div className="metric-content">
            <span className="metric-label">Applications</span>
            <span className="metric-value">{stats?.totalApplications ?? 0}</span>
            <span className="metric-sub">Total Submissions</span>
          </div>
        </div>

        <div className="metric-card ui-card">
          <div className="metric-icon-box green">📈</div>
          <div className="metric-content">
            <span className="metric-label">Placement Rate</span>
            <span className="metric-value">{stats?.placementRatePercentage ?? 0}%</span>
            <span className="metric-sub">{stats?.placedStudents ?? 0} of {stats?.totalStudents ?? 0}</span>
          </div>
        </div>

        <div className="metric-card ui-card">
          <div className="metric-icon-box purple">💰</div>
          <div className="metric-content">
            <span className="metric-label">Avg Package</span>
            <span className="metric-value">₹{stats?.averagePackage ?? 0} LPA</span>
            <span className="metric-sub">Highest: ₹{stats?.highestPackage ?? 0} LPA</span>
          </div>
        </div>
      </div>

      {/* Admin specific Student Directory view */}
      {user?.role === 'ADMIN' && currentView === 'students' && (
        <div className="dashboard-panel ui-card">
          <div className="panel-header">
            <div>
              <h2>Registered Students Directory</h2>
              <p className="panel-subtitle">Institutional candidate records and academic profiles.</p>
            </div>
            <Link to="/dashboard" className="btn-secondary">
              ← Back to Overview
            </Link>
          </div>

          {loading ? (
            <p className="panel-loading">Loading student records...</p>
          ) : students.length === 0 ? (
            <div className="empty-state">No student profiles registered yet.</div>
          ) : (
            <div className="table-responsive">
              <table className="enterprise-table">
                <thead>
                  <tr>
                    <th>Student Name</th>
                    <th>Roll Number</th>
                    <th>Department</th>
                    <th>CGPA</th>
                    <th>Backlogs</th>
                    <th>10th / 12th %</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {students.map((s) => (
                    <tr key={s.id}>
                      <td>
                        <strong>{s.studentName}</strong>
                        <div className="table-meta">{s.studentEmail}</div>
                      </td>
                      <td>{s.rollNumber || 'N/A'}</td>
                      <td>
                        <span className="badge-dept">{s.department}</span>
                      </td>
                      <td><strong>{s.cgpa}</strong></td>
                      <td>{s.backlogs}</td>
                      <td>{s.tenthPercentage}% / {s.twelfthPercentage}%</td>
                      <td>
                        {s.isPlaced ? (
                          <span className="status-pill selected">Placed</span>
                        ) : (
                          <span className="status-pill applied">Seeking</span>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {/* Admin Analytics Tab View */}
      {user?.role === 'ADMIN' && currentView === 'analytics' && (
        <div className="dashboard-panel ui-card">
          <div className="panel-header">
            <div>
              <h2>Placement Analytics & KPIs</h2>
              <p className="panel-subtitle">Institutional placement records, compensation summary, and drive engagement.</p>
            </div>
            <Link to="/dashboard" className="btn-secondary">
              ← Back to Overview
            </Link>
          </div>

          <div className="analytics-summary-grid">
            <div className="analytics-card">
              <h3>Compensation Overview</h3>
              <div className="analytics-stat-row">
                <span>Average Compensation Package</span>
                <strong>₹{stats?.averagePackage ?? 0} LPA</strong>
              </div>
              <div className="analytics-stat-row">
                <span>Highest Package Offered</span>
                <strong className="text-primary">₹{stats?.highestPackage ?? 0} LPA</strong>
              </div>
              <div className="analytics-stat-row">
                <span>Total Candidate Applications</span>
                <strong>{stats?.totalApplications ?? 0}</strong>
              </div>
            </div>

            <div className="analytics-card">
              <h3>Recruitment Drive Engagement</h3>
              <div className="analytics-stat-row">
                <span>Active / Ongoing Drives</span>
                <strong className="text-success">{stats?.activeDrives ?? 0}</strong>
              </div>
              <div className="analytics-stat-row">
                <span>Total Drives Published</span>
                <strong>{stats?.totalDrives ?? 0}</strong>
              </div>
              <div className="analytics-stat-row">
                <span>Registered Hiring Partners</span>
                <strong>{stats?.totalCompanies ?? 0}</strong>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Default Overview Layout */}
      {currentView === 'overview' && (
        <div className="dashboard-main-split">
          {/* Active Placement Drives */}
          <div className="dashboard-panel ui-card">
            <div className="panel-header">
              <div>
                <h2>Active Placement Drives</h2>
                <p className="panel-subtitle">Upcoming and ongoing campus recruitment drives.</p>
              </div>
              <Link to="/drives" className="panel-link">View All Drives →</Link>
            </div>

            {loading ? (
              <p className="panel-loading">Loading placement drives...</p>
            ) : recentDrives.length === 0 ? (
              <div className="empty-state">No drives published yet.</div>
            ) : (
              <div className="table-responsive">
                <table className="enterprise-table">
                  <thead>
                    <tr>
                      <th>Company / Role</th>
                      <th>Package</th>
                      <th>Min CGPA</th>
                      <th>Drive Date</th>
                      <th>Applicants</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {recentDrives.slice(0, 5).map((d) => (
                      <tr key={d.id}>
                        <td>
                          <strong>{d.title}</strong>
                          <div className="table-meta">{d.companyName} • {d.jobRole}</div>
                        </td>
                        <td><span className="text-primary font-bold">₹{d.packageLpa} LPA</span></td>
                        <td>{d.minCgpa}+</td>
                        <td>{d.driveDate || 'TBD'}</td>
                        <td>
                          <span className="badge-counter">{d.totalApplications} applied</span>
                        </td>
                        <td>
                          <button
                            className="btn-table-action"
                            onClick={() => navigate('/drives')}
                          >
                            Details
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>

          {/* Student's Recent Applications or Admin's Quick Portal Hub */}
          {user?.role === 'STUDENT' ? (
            <div className="dashboard-panel ui-card">
              <div className="panel-header">
                <div>
                  <h2>My Recent Applications</h2>
                  <p className="panel-subtitle">Current status of your drive applications.</p>
                </div>
                <Link to="/my-applications" className="panel-link">View All →</Link>
              </div>

              {myApplications.length === 0 ? (
                <div className="empty-state">
                  <p>You haven't submitted any applications yet.</p>
                  <Link to="/drives" className="btn-primary" style={{ marginTop: '12px' }}>
                    Browse Open Drives
                  </Link>
                </div>
              ) : (
                <div className="table-responsive">
                  <table className="enterprise-table">
                    <thead>
                      <tr>
                        <th>Drive</th>
                        <th>Round</th>
                        <th>Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      {myApplications.slice(0, 5).map((app) => (
                        <tr key={app.id}>
                          <td>
                            <strong>{app.driveTitle}</strong>
                            <div className="table-meta">{app.companyName} • ₹{app.packageLpa} LPA</div>
                          </td>
                          <td>Round {app.currentRound}</td>
                          <td>
                            <span className={`status-pill ${app.status.toLowerCase()}`}>
                              {app.status}
                            </span>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          ) : (
            <div className="dashboard-panel ui-card">
              <div className="panel-header">
                <div>
                  <h2>Quick Management Hub</h2>
                  <p className="panel-subtitle">Common placement administration actions.</p>
                </div>
              </div>

              <div className="admin-quick-hub">
                <div className="hub-tile" onClick={() => navigate('/manage-drives')}>
                  <div className="hub-icon">⚙️</div>
                  <div>
                    <h4>Manage Drives & Applicants</h4>
                    <p>Publish new drives, evaluate applicant submissions, advance rounds.</p>
                  </div>
                </div>

                <div className="hub-tile" onClick={() => navigate('/companies')}>
                  <div className="hub-icon">🏢</div>
                  <div>
                    <h4>Recruiting Companies</h4>
                    <p>Register partner organizations and campus recruiting contacts.</p>
                  </div>
                </div>

                <div className="hub-tile" onClick={() => navigate('/dashboard?view=students')}>
                  <div className="hub-icon">🎓</div>
                  <div>
                    <h4>Student Profiles</h4>
                    <p>Review student academic records, CGPAs, and placement statuses.</p>
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default Dashboard;
