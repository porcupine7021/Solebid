import React, { useEffect, useState, useCallback } from 'react';
import { getOtpStatus } from '../services/PasswordResetService';
import type { OtpStatusResponse } from '../types/AuthTypes';

interface OtpTimerProps {
  email: string;
  onExpired: () => void;
  onTimeUpdate: (remainingSeconds: number) => void;
  className?: string;
}

const OtpTimer: React.FC<OtpTimerProps> = ({
  email,
  onExpired,
  onTimeUpdate,
  className = ''
}) => {
  const [remainingSeconds, setRemainingSeconds] = useState<number>(0);
  const [isExpired, setIsExpired] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  const fetchOtpStatus = useCallback(async () => {
    try {
      const response = await getOtpStatus(email);
      
      if (response.success && response.data) {
        const otpStatus: OtpStatusResponse = response.data;
        
        if (!otpStatus.exists || otpStatus.expired) {
          setIsExpired(true);
          setRemainingSeconds(0);
          onExpired();
        } else {
          setRemainingSeconds(otpStatus.remainingTimeSeconds);
          setIsExpired(false);
          onTimeUpdate(otpStatus.remainingTimeSeconds);
          
          // 만료 시간에 도달했을 때 처리
          if (otpStatus.remainingTimeSeconds <= 0) {
            setIsExpired(true);
            onExpired();
          }
        }
      } else {
        // API 호출 실패 시 만료 처리
        setIsExpired(true);
        setRemainingSeconds(0);
        onExpired();
      }
    } catch (error) {
      console.error('OTP 상태 조회 실패:', error);
      // 에러 발생 시 만료 처리
      setIsExpired(true);
      setRemainingSeconds(0);
      onExpired();
    } finally {
      setIsLoading(false);
    }
  }, [email, onExpired, onTimeUpdate]);

  // 컴포넌트 마운트 시 초기 상태 조회
  useEffect(() => {
    fetchOtpStatus();
  }, [fetchOtpStatus]);

  // 1초마다 서버에서 상태 조회
  useEffect(() => {
    if (isExpired || isLoading) {
      return;
    }

    const interval = setInterval(() => {
      fetchOtpStatus();
    }, 1000);

    return () => clearInterval(interval);
  }, [fetchOtpStatus, isExpired, isLoading]);

  // 시간을 MM:SS 형식으로 포맷
  const formatTime = (seconds: number): string => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
  };

  // 남은 시간에 따른 스타일 결정
  const getTimerStyle = (): string => {
    if (isExpired || remainingSeconds <= 0) {
      return 'text-red-600 font-semibold';
    } else if (remainingSeconds < 60) {
      // 1분 미만: 빨간색 (긴급)
      return 'text-red-500 font-semibold animate-pulse';
    } else if (remainingSeconds < 180) {
      // 1-3분: 주황색 (주의)
      return 'text-orange-500 font-medium';
    } else {
      // 3분 이상: 기본 색상
      return 'text-gray-600';
    }
  };

  if (isLoading) {
    return (
      <div className={`text-sm text-gray-500 ${className}`}>
        타이머 로딩 중...
      </div>
    );
  }

  if (isExpired || remainingSeconds <= 0) {
    return (
      <div className={`text-sm text-red-600 font-semibold ${className}`}>
        인증번호가 만료되었습니다
      </div>
    );
  }

  return (
    <div className={`text-sm ${getTimerStyle()} ${className}`}>
      남은 시간: {formatTime(remainingSeconds)}
    </div>
  );
};

export default OtpTimer;