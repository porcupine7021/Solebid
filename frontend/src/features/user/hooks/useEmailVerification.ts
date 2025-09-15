import { useState, useEffect, useCallback } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { verifyEmailCode, resendVerificationEmail } from '../services/EmailVerificationService';
import type { EmailVerificationState } from '../types/EmailVerificationTypes';

export function useEmailVerification() {
  const navigate = useNavigate();
  const location = useLocation();
  
  // 회원가입에서 전달된 이메일 가져오기
  const emailFromState = location.state?.email as string;
  
  const [state, setState] = useState<EmailVerificationState>({
    email: emailFromState || '',
    isVerifying: false,
    isResending: false,
    resendCooldown: 0,
    error: null,
    success: false,
  });

  const [verificationCode, setVerificationCode] = useState('');

  // 이메일이 제공되지 않으면 리다이렉트
  useEffect(() => {
    if (!emailFromState) {
      navigate('/signup', { replace: true });
    }
  }, [emailFromState, navigate]);

  // 재전송 대기 타이머
  useEffect(() => {
    if (state.resendCooldown > 0) {
      const timer = setTimeout(() => {
        setState(prev => ({ ...prev, resendCooldown: prev.resendCooldown - 1 }));
      }, 1000);
      return () => clearTimeout(timer);
    }
  }, [state.resendCooldown]);

  const verifyCode = useCallback(async () => {
    if (!verificationCode || verificationCode.length !== 6 || !state.email) {
      setState(prev => ({ ...prev, error: '6자리 인증번호를 입력해주세요.' }));
      return;
    }

    setState(prev => ({ ...prev, isVerifying: true, error: null }));

    try {
      const response = await verifyEmailCode({
        email: state.email,
        verificationCode: verificationCode,
      });

      if (response.success) {
        setState(prev => ({ ...prev, success: true, isVerifying: false }));
        // 잠시 후 성공 페이지로 리다이렉트
        setTimeout(() => {
          navigate('/email-verification-success', { 
            state: { email: state.email },
            replace: true 
          });
        }, 1000);
      } else {
        setState(prev => ({ 
          ...prev, 
          error: response.message || '인증번호가 올바르지 않습니다.',
          isVerifying: false 
        }));
      }
    } catch (error) {
      console.error('Email verification error:', error);
      setState(prev => ({ 
        ...prev, 
        error: '서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.',
        isVerifying: false 
      }));
    }
  }, [verificationCode, state.email, navigate]);

  const resendCode = useCallback(async () => {
    if (!state.email || state.resendCooldown > 0) {
      return;
    }

    setState(prev => ({ ...prev, isResending: true, error: null }));

    try {
      const response = await resendVerificationEmail({
        email: state.email,
      });

      if (response.success) {
        setState(prev => ({ 
          ...prev, 
          isResending: false,
          resendCooldown: 60, // 1분 대기시간
        }));
        setVerificationCode(''); // 현재 코드 지우기
      } else {
        setState(prev => ({ 
          ...prev, 
          error: response.message || '인증번호 재전송에 실패했습니다.',
          isResending: false 
        }));
      }
    } catch (error) {
      console.error('Resend verification error:', error);
      setState(prev => ({ 
        ...prev, 
        error: '서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.',
        isResending: false 
      }));
    }
  }, [state.email, state.resendCooldown]);

  // 코드가 완성되면 자동 인증
  useEffect(() => {
    if (verificationCode.length === 6 && !state.isVerifying) {
      verifyCode();
    }
  }, [verificationCode, verifyCode, state.isVerifying]);

  return {
    email: state.email,
    verificationCode,
    setVerificationCode,
    isVerifying: state.isVerifying,
    isResending: state.isResending,
    resendCooldown: state.resendCooldown,
    error: state.error,
    success: state.success,
    verifyCode,
    resendCode,
    clearError: () => setState(prev => ({ ...prev, error: null })),
  };
}