import { useState, useEffect } from 'react';
import api from '../api/axios';
import { useAuth } from '../context/AuthContext';
import type { ApiResponse } from '../types/api';
import type { StudentProfile, StudentProfileRequest } from '../types/placement';
import './Profile.css';

function Profile() {
  const { user } = useAuth();

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState<{ text: string; success: boolean } | null>(null);

  const [rollNumber, setRollNumber] = useState('');
  const [department, setDepartment] = useState('CSE');
  const [cgpa, setCgpa] = useState<number | ''>(8.0);
  const [tenthPercentage, setTenthPercentage] = useState<number | ''>(85.0);
  const [twelfthPercentage, setTwelfthPercentage] = useState<number | ''>(85.0);
  const [backlogs, setBacklogs] = useState<number | ''>(0);
  const [passingYear, setPassingYear] = useState<number | ''>(2025);
  const [phone, setPhone] = useState('');
  const [gender, setGender] = useState('Male');
  const [skills, setSkills] = useState('');
  const [resumeUrl, setResumeUrl] = useState('');
  const [isPlaced, setIsPlaced] = useState(false);

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = () => {
    setLoading(true);
    api.get<ApiResponse<StudentProfile>>('/students/profile')
      .then((res) => {
        const p = res.data.data;
        if (p) {
          setRollNumber(p.rollNumber || '');
          setDepartment(p.department || 'CSE');
          setCgpa(p.cgpa ?? '');
          setTenthPercentage(p.tenthPercentage ?? '');
          setTwelfthPercentage(p.twelfthPercentage ?? '');
          setBacklogs(p.backlogs ?? 0);
          setPassingYear(p.passingYear ?? 2025);
          setPhone(p.phone || '');
          setGender(p.gender || 'Male');
          setSkills(p.skills || '');
          setResumeUrl(p.resumeUrl || '');
          setIsPlaced(p.isPlaced || false);
        }
      })
      .catch((err) => {
        if (err.response?.status !== 404) {
          console.error('Error loading profile', err);
        }
      })
      .finally(() => setLoading(false));
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);
    setSaving(true);

    const payload: StudentProfileRequest = {
      rollNumber,
      department,
      cgpa: Number(cgpa),
      tenthPercentage: Number(tenthPercentage),
      twelfthPercentage: Number(twelfthPercentage),
      backlogs: Number(backlogs),
      passingYear: passingYear ? Number(passingYear) : undefined,
      phone,
      gender,
      skills,
      resumeUrl,
    };

    try {
      await api.put('/students/profile', payload);
      setMessage({ text: 'Academic profile updated successfully!', success: true });
    } catch (err: any) {
      const msg = err.response?.data?.message || 'Failed to update profile. Please verify your inputs.';
      setMessage({ text: msg, success: false });
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <div className="profile-page"><p className="profile-loading">Loading student academic profile...</p></div>;
  }

  return (
    <div className="profile-page">
      <div className="profile-header">
        <div>
          <h1>Student Academic Profile</h1>
          <p className="subtitle">
            Maintain accurate academic credentials. Drive eligibility is automatically evaluated against these values.
          </p>
        </div>
        {isPlaced && (
          <div className="placed-tag">
            ✓ Placement Secured
          </div>
        )}
      </div>

      {message && (
        <div className={`profile-alert ${message.success ? 'success' : 'error'}`}>
          {message.text}
        </div>
      )}

      <form className="profile-form" onSubmit={handleSave}>
        <div className="form-card ui-card">
          <h3>Basic Information</h3>
          <div className="form-grid">
            <div className="form-field">
              <label>Full Name</label>
              <input type="text" value={user?.fullName || ''} disabled />
            </div>
            <div className="form-field">
              <label>Registered Email</label>
              <input type="email" value={user?.email || ''} disabled />
            </div>
            <div className="form-field">
              <label>Roll Number *</label>
              <input
                type="text"
                placeholder="e.g. 2024CS101"
                value={rollNumber}
                onChange={(e) => setRollNumber(e.target.value)}
                required
              />
            </div>
            <div className="form-field">
              <label>Department / Branch *</label>
              <select value={department} onChange={(e) => setDepartment(e.target.value)} required>
                <option value="CSE">Computer Science & Engineering (CSE)</option>
                <option value="IT">Information Technology (IT)</option>
                <option value="ECE">Electronics & Communication (ECE)</option>
                <option value="EE">Electrical Engineering (EE)</option>
                <option value="MECH">Mechanical Engineering (MECH)</option>
                <option value="CIVIL">Civil Engineering (CIVIL)</option>
              </select>
            </div>
          </div>
        </div>

        <div className="form-card ui-card">
          <h3>Academic Performance & Cutoff Criteria</h3>
          <div className="form-grid">
            <div className="form-field">
              <label>Current CGPA (out of 10) *</label>
              <input
                type="number"
                step="0.01"
                min="0"
                max="10"
                placeholder="8.50"
                value={cgpa}
                onChange={(e) => setCgpa(e.target.value === '' ? '' : parseFloat(e.target.value))}
                required
              />
            </div>
            <div className="form-field">
              <label>Active Backlogs Count *</label>
              <input
                type="number"
                min="0"
                placeholder="0"
                value={backlogs}
                onChange={(e) => setBacklogs(e.target.value === '' ? '' : parseInt(e.target.value))}
                required
              />
            </div>
            <div className="form-field">
              <label>10th Standard Percentage (%) *</label>
              <input
                type="number"
                step="0.1"
                min="0"
                max="100"
                placeholder="90.5"
                value={tenthPercentage}
                onChange={(e) => setTenthPercentage(e.target.value === '' ? '' : parseFloat(e.target.value))}
                required
              />
            </div>
            <div className="form-field">
              <label>12th Standard Percentage (%) *</label>
              <input
                type="number"
                step="0.1"
                min="0"
                max="100"
                placeholder="88.0"
                value={twelfthPercentage}
                onChange={(e) => setTwelfthPercentage(e.target.value === '' ? '' : parseFloat(e.target.value))}
                required
              />
            </div>
            <div className="form-field">
              <label>Graduation Year</label>
              <input
                type="number"
                placeholder="2025"
                value={passingYear}
                onChange={(e) => setPassingYear(e.target.value === '' ? '' : parseInt(e.target.value))}
              />
            </div>
            <div className="form-field">
              <label>Contact Phone</label>
              <input
                type="tel"
                placeholder="+91 9876543210"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
              />
            </div>
          </div>
        </div>

        <div className="form-card ui-card">
          <h3>Skills & Resume Link</h3>
          <div className="form-grid">
            <div className="form-field full-width">
              <label>Key Technical Skills (comma-separated)</label>
              <input
                type="text"
                placeholder="e.g. Java, Python, React, Spring Boot, MySQL, Data Structures"
                value={skills}
                onChange={(e) => setSkills(e.target.value)}
              />
            </div>
            <div className="form-field full-width">
              <label>Online Resume / Portfolio Link</label>
              <input
                type="url"
                placeholder="https://drive.google.com/your-resume or https://linkedin.com/in/you"
                value={resumeUrl}
                onChange={(e) => setResumeUrl(e.target.value)}
              />
            </div>
          </div>
        </div>

        <div className="form-actions">
          <button type="submit" className="btn-primary" disabled={saving}>
            {saving ? 'Saving Profile...' : 'Save Academic Profile'}
          </button>
        </div>
      </form>
    </div>
  );
}

export default Profile;
