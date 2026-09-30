import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import type { LoginRequest, RegisterRequest } from '../types/auth';
import './Login.css';

type TabMode = 'login' | 'register';

function Login() {
  const navigate = useNavigate();
  const { login, register, isAuthenticated } = useAuth();

  const [activeTab, setActiveTab] = useState<TabMode>('login');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Login form state
  const [loginEmail, setLoginEmail] = useState('');
  const [loginPassword, setLoginPassword] = useState('');

  // Register form state
  const [regFullName, setRegFullName] = useState('');
  const [regEmail, setRegEmail] = useState('');
  const [regPassword, setRegPassword] = useState('');
  const [regConfirmPassword, setRegConfirmPassword] = useState('');
  const [regRole, setRegRole] = useState<'STUDENT' | 'ADMIN' | 'COMPANY'>('STUDENT');

  // Redirect if already authenticated
  if (isAuthenticated) {
    navigate('/dashboard', { replace: true });
    return null;
  }

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const data: LoginRequest = { email: loginEmail, password: loginPassword };
      await login(data);
      navigate('/dashboard', { replace: true });
    } catch (err: any) {
      const message =
        err.response?.data?.message || 'Login failed. Please check your credentials.';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (regPassword !== regConfirmPassword) {
      setError('Passwords do not match');
      return;
    }

    if (regPassword.length < 6) {
      setError('Password must be at least 6 characters');
      return;
    }

    setLoading(true);
    try {
      const data: RegisterRequest = {
        fullName: regFullName,
        email: regEmail,
        password: regPassword,
        role: regRole,
      };
      await register(data);
      navigate('/dashboard', { replace: true });
    } catch (err: any) {
      const message =
        err.response?.data?.message || 'Registration failed. Please try again.';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-card-container">
        <div className="auth-brand-header">
          <div className="auth-logo-badge">🎓</div>
          <h2>College Placement Portal</h2>
          <p>Access campus recruitment drives, student profiles, and placement records.</p>
        </div>

        <div className="login-card">
          <div className="auth-tabs">
            <button
              type="button"
              className={`auth-tab ${activeTab === 'login' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('login');
                setError(null);
              }}
            >
              Sign In
            </button>
            <button
              type="button"
              className={`auth-tab ${activeTab === 'register' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('register');
                setError(null);
              }}
            >
              Register
            </button>
          </div>

          {error && <div className="auth-error">{error}</div>}

          {activeTab === 'login' ? (
            <form onSubmit={handleLogin} id="login-form">
              <div className="form-group">
                <label htmlFor="login-email">Official / Registered Email</label>
                <input
                  type="email"
                  id="login-email"
                  placeholder="name@college.edu or personal email"
                  value={loginEmail}
                  onChange={(e) => setLoginEmail(e.target.value)}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="login-password">Password</label>
                <input
                  type="password"
                  id="login-password"
                  placeholder="Enter your password"
                  value={loginPassword}
                  onChange={(e) => setLoginPassword(e.target.value)}
                  required
                />
              </div>

              <button type="submit" className="btn-auth-submit" disabled={loading}>
                {loading ? 'Authenticating...' : 'Sign In'}
              </button>
            </form>
          ) : (
            <form onSubmit={handleRegister} id="register-form">
              <div className="form-group">
                <label htmlFor="reg-fullname">Full Name</label>
                <input
                  type="text"
                  id="reg-fullname"
                  placeholder="First and last name"
                  value={regFullName}
                  onChange={(e) => setRegFullName(e.target.value)}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="reg-email">Email Address</label>
                <input
                  type="email"
                  id="reg-email"
                  placeholder="Valid institutional or personal email"
                  value={regEmail}
                  onChange={(e) => setRegEmail(e.target.value)}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="reg-password">Password</label>
                <input
                  type="password"
                  id="reg-password"
                  placeholder="Minimum 6 characters"
                  value={regPassword}
                  onChange={(e) => setRegPassword(e.target.value)}
                  required
                  minLength={6}
                />
              </div>

              <div className="form-group">
                <label htmlFor="reg-confirm-password">Confirm Password</label>
                <input
                  type="password"
                  id="reg-confirm-password"
                  placeholder="Re-enter your password"
                  value={regConfirmPassword}
                  onChange={(e) => setRegConfirmPassword(e.target.value)}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="reg-role">Portal Role</label>
                <select
                  id="reg-role"
                  value={regRole}
                  onChange={(e) =>
                    setRegRole(e.target.value as 'STUDENT' | 'ADMIN' | 'COMPANY')
                  }
                >
                  <option value="STUDENT">Student Candidate</option>
                  <option value="COMPANY">Recruiting Partner (Company)</option>
                  <option value="ADMIN">Placement Administrator</option>
                </select>
              </div>

              <button type="submit" className="btn-auth-submit" disabled={loading}>
                {loading ? 'Creating account...' : 'Create Account'}
              </button>
            </form>
          )}

          <div className="auth-footer-help">
            <Link to="/">← Back to Homepage</Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
