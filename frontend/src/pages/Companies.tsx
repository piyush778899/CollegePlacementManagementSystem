import { useState, useEffect } from 'react';
import api from '../api/axios';
import { useAuth } from '../context/AuthContext';
import type { ApiResponse } from '../types/api';
import type { Company, CompanyRequest } from '../types/placement';
import './Companies.css';

function Companies() {
  const { isAuthenticated, user } = useAuth();
  const [companies, setCompanies] = useState<Company[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');

  const [showAddModal, setShowAddModal] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [name, setName] = useState('');
  const [industry, setIndustry] = useState('');
  const [website, setWebsite] = useState('');
  const [location, setLocation] = useState('');
  const [contactEmail, setContactEmail] = useState('');
  const [contactPhone, setContactPhone] = useState('');
  const [description, setDescription] = useState('');

  useEffect(() => {
    fetchCompanies();
  }, []);

  const fetchCompanies = () => {
    setLoading(true);
    api.get<ApiResponse<Company[]>>('/companies')
      .then((res) => setCompanies(res.data.data))
      .catch((err) => console.error('Failed to load companies', err))
      .finally(() => setLoading(false));
  };

  const handleAddCompany = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSubmitting(true);

    const payload: CompanyRequest = {
      name,
      industry,
      website,
      location,
      contactEmail,
      contactPhone,
      description,
    };

    try {
      await api.post('/companies', payload);
      setShowAddModal(false);
      resetForm();
      fetchCompanies();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to add company');
    } finally {
      setSubmitting(false);
    }
  };

  const resetForm = () => {
    setName('');
    setIndustry('');
    setWebsite('');
    setLocation('');
    setContactEmail('');
    setContactPhone('');
    setDescription('');
  };

  const filteredCompanies = companies.filter((c) => {
    const q = searchQuery.toLowerCase();
    return (
      c.name.toLowerCase().includes(q) ||
      (c.industry && c.industry.toLowerCase().includes(q)) ||
      (c.location && c.location.toLowerCase().includes(q))
    );
  });

  return (
    <div className="companies-page">
      <div className="companies-header">
        <div>
          <h1>Companies</h1>
          <p className="subtitle">Recruiting companies participating in campus placement activities.</p>
        </div>
        {isAuthenticated && (user?.role === 'ADMIN' || user?.role === 'COMPANY') && (
          <button className="btn-primary" onClick={() => setShowAddModal(true)}>
            + Add Recruiter
          </button>
        )}
      </div>

      <div className="companies-search-bar">
        <span className="search-icon">🔍</span>
        <input
          type="text"
          placeholder="Filter companies by name, industry, or location..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {loading ? (
        <div className="loading-state">Loading companies...</div>
      ) : filteredCompanies.length === 0 ? (
        <div className="empty-state">
          {searchQuery ? 'No companies match your search.' : 'No companies registered yet.'}
        </div>
      ) : (
        <div className="companies-grid">
          {filteredCompanies.map((c) => (
            <div key={c.id} className="company-card ui-card">
              <div className="company-top">
                <div className="company-avatar">
                  {c.name.charAt(0)}
                </div>
                <div className="company-title-area">
                  <h3>{c.name}</h3>
                  <span className="industry-badge">{c.industry || 'Technology'}</span>
                </div>
              </div>

              <p className="company-desc">
                {c.description || 'Recruiting partner participating in campus placement activities.'}
              </p>

              <div className="company-details">
                {c.location && (
                  <div className="detail-row">
                    <span className="icon">📍</span>
                    <span>{c.location}</span>
                  </div>
                )}
                {c.website && (
                  <div className="detail-row">
                    <span className="icon">🌐</span>
                    <a href={c.website} target="_blank" rel="noopener noreferrer" className="company-link">
                      {c.website.replace(/^https?:\/\//, '')}
                    </a>
                  </div>
                )}
                {c.contactEmail && (
                  <div className="detail-row">
                    <span className="icon">✉️</span>
                    <span>{c.contactEmail}</span>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Add Company Modal */}
      {showAddModal && (
        <div className="modal-overlay" onClick={() => setShowAddModal(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Register Recruiting Company</h2>
              <button className="close-btn" onClick={() => setShowAddModal(false)}>×</button>
            </div>

            {error && <div className="apply-alert error">{error}</div>}

            <form onSubmit={handleAddCompany} className="modal-body">
              <div className="form-field">
                <label>Company Name *</label>
                <input
                  type="text"
                  placeholder="e.g. Infosys, TCS, Wipro"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
                />
              </div>

              <div className="form-field">
                <label>Industry / Sector</label>
                <input
                  type="text"
                  placeholder="e.g. Software & Cloud, Finance, Consulting"
                  value={industry}
                  onChange={(e) => setIndustry(e.target.value)}
                />
              </div>

              <div className="form-field">
                <label>Official Website</label>
                <input
                  type="url"
                  placeholder="https://company.com"
                  value={website}
                  onChange={(e) => setWebsite(e.target.value)}
                />
              </div>

              <div className="form-field">
                <label>Office / Hiring Location</label>
                <input
                  type="text"
                  placeholder="e.g. Bengaluru, Hyderabad, Pune"
                  value={location}
                  onChange={(e) => setLocation(e.target.value)}
                />
              </div>

              <div className="form-field">
                <label>Campus Hiring Contact Email</label>
                <input
                  type="email"
                  placeholder="campus@company.com"
                  value={contactEmail}
                  onChange={(e) => setContactEmail(e.target.value)}
                />
              </div>

              <div className="form-field">
                <label>Company Overview</label>
                <textarea
                  rows={3}
                  placeholder="Brief overview of company services and recruitment focus..."
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </div>

              <div className="modal-footer" style={{ padding: '16px 0 0 0' }}>
                <button type="button" className="btn-secondary" onClick={() => setShowAddModal(false)}>
                  Cancel
                </button>
                <button type="submit" className="btn-primary" disabled={submitting}>
                  {submitting ? 'Registering...' : 'Save Recruiter'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default Companies;
