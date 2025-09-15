export interface VerifyCodeRequest {
  email: string;
  verificationCode: string;
}

export interface ResendVerificationRequest {
  email: string;
}

export interface EmailVerificationResponse {
  success: boolean;
  message: string;
}

export interface EmailVerificationState {
  email: string;
  isVerifying: boolean;
  isResending: boolean;
  resendCooldown: number;
  error: string | null;
  success: boolean;
}