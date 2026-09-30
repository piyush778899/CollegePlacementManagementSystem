import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

function Navbar() {
  const location = useLocation();
  const navigate = useNavigate();
  const { isAuthenticated, user, logout } = useAuth();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const isActive = (path: string) => location.pathname === path;

  const handleLogout = () => {
    logout();
    navigate('/login');
    setMobileMenuOpen(false);
  };

  const closeMenu = () => setMobileMenuOpen(false);

  return (
    <header className="navbar-wrapper">
      <div className="navbar-container">
        <div className="navbar-brand">
          <Link to="/" onClick={closeMenu}>
            <span className="brand-logo-icon">🎓</span>
            <div className="brand-text-group">
              <span className="brand-title">College Placement</span>
              <span className="brand-sub">Management System</span>
            </div>
          </Link>
        </div>

        <button
          className="mobile-toggle-btn"
          onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
          aria-label="Toggle navigation menu"
        >
          <span className="toggle-bar"></span>
          <span className="toggle-bar"></span>
          <span className="toggle-bar"></span>
        </button>

        <nav className={`navbar-nav ${mobileMenuOpen ? 'open' : ''}`}>
          <div className="nav-primary-links">
            <Link to="/" className={`nav-link ${isActive('/') ? 'active' : ''}`} onClick={closeMenu}>
              Home
            </Link>
            <Link to="/drives" className={`nav-link ${isActive('/drives') ? 'active' : ''}`} onClick={closeMenu}>
              Drives
            </Link>
            <Link to="/companies" className={`nav-link ${isActive('/companies') ? 'active' : ''}`} onClick={closeMenu}>
              Companies
            </Link>

            {isAuthenticated && user?.role === 'STUDENT' && (
              <>
                <Link to="/my-applications" className={`nav-link ${isActive('/my-applications') ? 'active' : ''}`} onClick={closeMenu}>
                  My Applications
                </Link>
                <Link to="/profile" className={`nav-link ${isActive('/profile') ? 'active' : ''}`} onClick={closeMenu}>
                  Profile
                </Link>
              </>
            )}

            {isAuthenticated && (user?.role === 'ADMIN' || user?.role === 'COMPANY') && (
              <Link to="/manage-drives" className={`nav-link ${isActive('/manage-drives') ? 'active' : ''}`} onClick={closeMenu}>
                Manage Drives
              </Link>
            )}
          </div>

          <div className="nav-auth-section">
            {isAuthenticated ? (
              <div className="user-dropdown">
                <Link to="/dashboard" className={`nav-link dashboard-link ${isActive('/dashboard') ? 'active' : ''}`} onClick={closeMenu}>
                  Dashboard
                </Link>
                <div className="user-profile-summary">
                  <div className="avatar-circle">
                    {user?.fullName ? user.fullName.charAt(0).toUpperCase() : 'U'}
                  </div>
                  <div className="user-meta">
                    <span className="user-full-name">{user?.fullName}</span>
                    <span className="user-role-label">{user?.role}</span>
                  </div>
                </div>
                <button className="btn-nav-logout" onClick={handleLogout}>
                  Sign Out
                </button>
              </div>
            ) : (
              <Link to="/login" className="btn-nav-signin" onClick={closeMenu}>
                Sign In
              </Link>
            )}
          </div>
        </nav>
      </div>
    </header>
  );
}

export default Navbar;
