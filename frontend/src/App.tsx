import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import AppLayout from './components/AppLayout';
import ProtectedRoute from './components/ProtectedRoute';
import Home from './pages/Home';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Drives from './pages/Drives';
import Profile from './pages/Profile';
import MyApplications from './pages/MyApplications';
import ManageDrives from './pages/ManageDrives';
import Companies from './pages/Companies';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppLayout>
          <Routes>
            {/* Public Routes */}
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/drives" element={<Drives />} />
            <Route path="/companies" element={<Companies />} />

            {/* Authenticated Dashboard */}
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <Dashboard />
                </ProtectedRoute>
              }
            />

            {/* Student-only Routes */}
            <Route
              path="/profile"
              element={
                <ProtectedRoute roles={['STUDENT', 'ADMIN']}>
                  <Profile />
                </ProtectedRoute>
              }
            />
            <Route
              path="/my-applications"
              element={
                <ProtectedRoute roles={['STUDENT']}>
                  <MyApplications />
                </ProtectedRoute>
              }
            />

            {/* Admin & Company Routes */}
            <Route
              path="/manage-drives"
              element={
                <ProtectedRoute roles={['ADMIN', 'COMPANY']}>
                  <ManageDrives />
                </ProtectedRoute>
              }
            />
          </Routes>
        </AppLayout>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
