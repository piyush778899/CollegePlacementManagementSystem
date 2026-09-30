import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../api/axios';
import { useAuth } from '../context/AuthContext';
import type { ApiResponse } from '../types/api';
import './Home.css';

interface HealthData {
  status: string;
  application: string;
  version: string;
}

const CURRENT_YEAR = new Date().getFullYear();

function Home() {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [health, setHealth] = useState<HealthData | null>(null);

  useEffect(() => {
    api.get<ApiResponse<HealthData>>('/health')
      .then((res) => setHealth(res.data.data))
      .catch(() => {});
  }, []);

  const coreFeatures = [
    {
      icon: '🎓',
      title: 'Student Management',
      description: 'Comprehensive student academic profiling, CGPA tracking, backlogs monitoring, and resume hosting.',
      action: () => navigate(isAuthenticated ? '/profile' : '/login'),
    },
    {
      icon: '🏢',
      title: 'Recruiter Management',
      description: 'Coordinate with registered recruiting companies and manage campus placement drives.',
      action: () => navigate('/companies'),
    },
    {
      icon: '💼',
      title: 'Placement Drives',
      description: 'Publish and discover job openings, eligibility requirements, compensation packages, and drive dates.',
      action: () => navigate('/drives'),
    },
    {
      icon: '⚡',
      title: 'Eligibility Checking',
      description: 'Automatic real-time student evaluation against cutoff CGPA, 10th/12th percentages, and active backlogs.',
      action: () => navigate('/drives'),
    },
    {
      icon: '📑',
      title: 'Application Tracking',
      description: 'Submit applications in one click and track selection progress throughout the recruitment lifecycle.',
      action: () => navigate(isAuthenticated ? '/my-applications' : '/login'),
    },
    {
      icon: '🎯',
      title: 'Recruitment Rounds',
      description: 'Manage multi-stage hiring cycles from assessments and technical interviews to final offer extensions.',
      action: () => navigate(isAuthenticated ? '/manage-drives' : '/login'),
    },
    {
      icon: '📊',
      title: 'Placement Analytics',
      description: 'Institutional metrics tracking placement percentages, compensation packages, and department performance.',
      action: () => navigate(isAuthenticated ? '/dashboard' : '/login'),
    },
  ];

  return (
    <div className="home-page-wrapper">
      {/* Hero Section */}
      <section className="home-hero-section">
        <div className="home-hero-content">
          <div className="institution-badge">
            <span className="badge-dot"></span>
            <span>Training & Placement Cell</span>
          </div>

          <h1 className="home-hero-title">
            College Placement Management System
          </h1>

          <p className="home-hero-description">
            Manage students, recruiters, placement drives, applications,
            and recruitment activities in one centralized platform.
          </p>

          <div className="home-hero-actions">
            <Link to="/drives" className="btn-hero-primary">
              Explore Placement Drives
            </Link>
            {isAuthenticated ? (
              <Link to="/dashboard" className="btn-hero-secondary">
                Go to Dashboard
              </Link>
            ) : (
              <Link to="/login" className="btn-hero-secondary">
                Sign In
              </Link>
            )}
          </div>
        </div>
      </section>

      {/* Core Features Section */}
      <section className="core-features-section">
        <div className="section-header-block">
          <span className="section-eyebrow">PORTAL CAPABILITIES</span>
          <h2 className="section-heading">Core Features</h2>
          <p className="section-subtext">
            Designed for institutional training and placement cells, student candidates, and corporate recruiters.
          </p>
        </div>

        <div className="features-card-grid">
          {coreFeatures.map((feat, index) => (
            <div
              key={index}
              className="feature-card ui-card"
              onClick={feat.action}
              role="button"
              tabIndex={0}
              onKeyDown={(e) => e.key === 'Enter' && feat.action()}
            >
              <div className="feature-icon-wrapper">
                {feat.icon}
              </div>
              <h3 className="feature-card-title">{feat.title}</h3>
              <p className="feature-card-desc">{feat.description}</p>
              <div className="feature-card-footer">
                <span className="feature-link-text">Access Module →</span>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Institutional Highlights Banner */}
      <section className="institutional-banner">
        <div className="institutional-grid">
          <div className="inst-item">
            <div className="inst-num">Centralized</div>
            <div className="inst-label">Recruiter & Student Records</div>
          </div>
          <div className="inst-divider" />
          <div className="inst-item">
            <div className="inst-num">Automated</div>
            <div className="inst-label">Academic Eligibility Evaluation</div>
          </div>
          <div className="inst-divider" />
          <div className="inst-item">
            <div className="inst-num">Real-Time</div>
            <div className="inst-label">Application Status & Round Progression</div>
          </div>
        </div>
      </section>

      {/* Clean Institutional Footer */}
      <footer className="portal-footer">
        <div className="footer-content">
          <div className="health-status-badge">
            <span className={`status-indicator-dot ${health?.status === 'UP' ? 'online' : 'connecting'}`} />
            <span>API Backend: {health?.status === 'UP' ? 'Active (v1.0.0)' : 'Connecting...'}</span>
          </div>
          <div className="footer-meta">
            © {CURRENT_YEAR} College Placement Management System. All rights reserved.
          </div>
        </div>
      </footer>
    </div>
  );
}

export default Home;
