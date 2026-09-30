import { useState } from 'react';
import { useLocation, Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Navbar from './Navbar';
import Sidebar from './Sidebar';
import './AppLayout.css';

interface AppLayoutProps {
  children: React.ReactNode;
}

export function AppLayout({ children }: AppLayoutProps) {
  const { isAuthenticated, user, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  // Home page or unauthenticated users use the public Navbar
  const isPublicPage = location.pathname === '/' || location.pathname === '/login';
  const showSidebar = isAuthenticated && !isPublicPage;

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const getPageTitle = () => {
    const p = location.pathname;
    if (p === '/dashboard') return 'Dashboard';
    if (p === '/drives') return 'Placement Drives';
    if (p === '/companies') return 'Companies';
    if (p === '/manage-drives') return 'Manage Drives & Applicants';
    if (p === '/my-applications') return 'My Applications';
    if (p === '/profile') return 'Academic Profile';
    return 'Portal';
  };

  if (!showSidebar) {
    return (
      <div className="public-app-layout">
        <Navbar />
        <main className="public-content-container">
          {children}
        </main>
      </div>
    );
  }

  return (
    <div className="authenticated-app-layout">
      <Sidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />

      <div className="authenticated-main-wrapper">
        <header className="authenticated-topbar">
          <div className="topbar-left">
            <button
              className="topbar-menu-toggle"
              onClick={() => setSidebarOpen(true)}
              aria-label="Open sidebar"
            >
              ☰
            </button>
            <div className="breadcrumb-nav">
              <span className="breadcrumb-root">Portal</span>
              <span className="breadcrumb-separator">/</span>
              <span className="breadcrumb-current">{getPageTitle()}</span>
            </div>
          </div>

          <div className="topbar-right">
            <Link to="/" className="topbar-home-link" title="View Public Home Page">
              🌐 Public Site
            </Link>
            <div className="topbar-role-badge">
              {user?.role === 'ADMIN' ? 'Placement Admin' : user?.role === 'COMPANY' ? 'Recruiter' : 'Student Candidate'}
            </div>
            <div className="topbar-user-profile">
              <div className="topbar-avatar">
                {user?.fullName ? user.fullName.charAt(0).toUpperCase() : 'U'}
              </div>
              <span className="topbar-user-name">{user?.fullName}</span>
            </div>
            <button className="topbar-logout-btn" onClick={handleLogout} title="Log Out">
              Sign Out
            </button>
          </div>
        </header>

        <main className="authenticated-content-container">
          {children}
        </main>
      </div>
    </div>
  );
}

export default AppLayout;
