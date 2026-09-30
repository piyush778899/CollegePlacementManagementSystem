export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  role: 'STUDENT' | 'ADMIN' | 'COMPANY';
}

export interface AuthResponse {
  token: string | null;
  email: string;
  fullName: string;
  role: string;
}

export interface AuthUser {
  email: string;
  fullName: string;
  role: string;
}
