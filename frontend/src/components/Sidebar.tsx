import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Sidebar.css';

interface SidebarProps {
  isOpen: boolean;
  onClose: () => void;
}

export function Sidebar({ isOpen, onClose }: SidebarProps) {
  const location = useLocation();
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const isActive = (path: string) => {
    if (path === '/dashboard' && location.pathname === '/dashboard') return true;
    if (path !== '/dashboard' && location.pathname.startsWith(path)) return true;
    return false;
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
    onClose();
  };

  const role = user?.role;

  return (
    <>
      {isOpen && <div className="sidebar-backdrop" onClick={onClose} />}
      <aside className={`app-sidebar ${isOpen ? 'open' : ''}`}>
        <div className="sidebar-header">
          <Link to="/dashboard" className="sidebar-brand" onClick={onClose}>
            <div className="brand-icon-box">🎓</div>
            <div className="brand-text">
              <span className="brand-name">College Placement</span>
              <span className="brand-tagline">Management Portal</span>
            </div>
          </Link>
        </div>

        <div className="sidebar-user-card">
          <div className="sidebar-avatar">
            {user?.fullName ? user.fullName.charAt(0).toUpperCase() : 'U'}
          </div>
          <div className="sidebar-user-details">
            <span className="sidebar-user-name" title={user?.fullName}>
              {user?.fullName}
            </span>
            <span className={`role-badge ${role?.toLowerCase()}`}>
              {role === 'ADMIN' ? 'Administrator' : role === 'COMPANY' ? 'Recruiter' : 'Student'}
            </span>
          </div>
        </div>

        <div className="sidebar-nav-container">
          <div className="sidebar-section-title">MAIN MENU</div>

          <nav className="sidebar-nav">
            <Link
              to="/dashboard"
              className={`sidebar-link ${isActive('/dashboard') ? 'active' : ''}`}
              onClick={onClose}
            >
              <span className="link-icon">📊</span>
              <span className="link-label">Dashboard</span>
            </Link>

            {role === 'ADMIN' && (
              <>
                <Link
                  to="/dashboard?view=students"
                  className={`sidebar-link ${location.search.includes('view=students') ? 'active' : ''}`}
                  onClick={onClose}
                >
                  <span className="link-icon">🎓</span>
                  <span className="link-label">Students</span>
                </Link>

                <Link
                  to="/companies"
                  className={`sidebar-link ${isActive('/companies') ? 'active' : ''}`}
                  onClick={onClose}
                >
                  <span className="link-icon">🏢</span>
                  <span className="link-label">Companies</span>
                </Link>

                <Link
                  to="/drives"
                  className={`sidebar-link ${isActive('/drives') ? 'active' : ''}`}
                  onClick={onClose}
                >
                  <span className="link-icon">💼</span>
                  <span className="link-label">Placement Drives</span>
                </Link>

                <Link
                  to="/manage-drives"
                  className={`sidebar-link ${isActive('/manage-drives') ? 'active' : ''}`}
                  onClick={onClose}
                >
                  <span className="link-icon">🎯</span>
                  <span className="link-label">Applications & Rounds</span>
                </Link>

                <Link
                  to="/dashboard?view=analytics"
                  className={`sidebar-link ${location.search.includes('view=analytics') ? 'active' : ''}`}
                  onClick={onClose}
                >
                  <span className="link-icon">📈</span>
                  <span className="link-label">Analytics</span>
                </Link>
              </>
            )}

            {role === 'COMPANY' && (
              <>
                <Link
                  to="/manage-drives"
                  className={`sidebar-link ${isActive('/manage-drives') ? 'active' : ''}`}
                  onClick={onClose}
                >
                  <span className="link-icon">⚙️</span>
                  <span className="link-label">Manage Drives</span>
                </Link>

                <Link
                  to="/companies"
                  className={`sidebar-link ${isActive('/companies') ? 'active' : ''}`}
                  onClick={onClose}
                >
                  <span className="link-icon">🏢</span>
                  <span className="link-label">Companies</span>
                </Link>

                <Link
                  to="/drives"
                  className={`sidebar-link ${isActive('/drives') ? 'active' : ''}`}
                  onClick={onClose}
                >
                  <span className="link-icon">💼</span>
                  <span className="link-label">All Drives</span>
                </Link>
              </>
            )}

            {role === 'STUDENT' && (
              <>
                <Link
                  to="/drives"
                  className={`sidebar-link ${isActive('/drives') ? 'active' : ''}`}
                  onClick={onClose}
                >
                  <span className="link-icon">💼</span>
                  <span className="link-label">Placement Drives</span>
                </Link>

                <Link
                  to="/my-applications"
                  className={`sidebar-link ${isActive('/my-applications') ? 'active' : ''}`}
                  onClick={onClose}
                >
                  <span className="link-icon">📑</span>
                  <span className="link-label">My Applications</span>
                </Link>

                <Link
                  to="/profile"
                  className={`sidebar-link ${isActive('/profile') ? 'active' : ''}`}
                  onClick={onClose}
                >
                  <span className="link-icon">👤</span>
                  <span className="link-label">Academic Profile</span>
                </Link>

                <Link
                  to="/companies"
                  className={`sidebar-link ${isActive('/companies') ? 'active' : ''}`}
                  onClick={onClose}
                >
                  <span className="link-icon">🏢</span>
                  <span className="link-label">Companies</span>
                </Link>
              </>
            )}
          </nav>
        </div>

        <div className="sidebar-footer">
          <button className="sidebar-logout-btn" onClick={handleLogout}>
            <span className="link-icon">🚪</span>
            <span className="link-label">Logout</span>
          </button>
        </div>
      </aside>
    </>
  );
}

export default Sidebar;
