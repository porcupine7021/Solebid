import { apiFetch } from '../../../utils/apiFetch';
import type { ApiResponse } from '../types/AuthTypes';
import type { VerifyCodeRequest, ResendVerificationRequest, EmailVerificationResponse } from '../types/EmailVerificationTypes';

export async function verifyEmailCode(request: VerifyCodeRequest): Promise<ApiResponse<EmailVerificationResponse>> {
  return apiFetch<ApiResponse<EmailVerificationResponse>>('/api/auth/verify-code', {
    method: 'POST',
    json: request,
  });
}

export async function verifySignupEmailCode(request: VerifyCodeRequest): Promise<ApiResponse<EmailVerificationResponse>> {
  return apiFetch<ApiResponse<EmailVerificationResponse>>('/api/auth/verify-signup-code', {
    method: 'POST',
    json: request,
  });
}

export async function sendVerificationEmail(request: ResendVerificationRequest): Promise<ApiResponse<EmailVerificationResponse>> {
  return apiFetch<ApiResponse<EmailVerificationResponse>>('/api/auth/send-verification', {
    method: 'POST',
    json: request,
  });
}

export async function resendVerificationEmail(request: ResendVerificationRequest): Promise<ApiResponse<EmailVerificationResponse>> {
  return apiFetch<ApiResponse<EmailVerificationResponse>>('/api/auth/resend-verification', {
    method: 'POST',
    json: request,
  });
}