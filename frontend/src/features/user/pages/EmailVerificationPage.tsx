import React from 'react';
import { Link } from 'react-router-dom';
import { useEmailVerification } from '../hooks/useEmailVerification';
import VerificationCodeInput from '../components/VerificationCodeInput';

const EmailVerificationPage: React.FC = () => {
  const {
    email,
    verificationCode,
    setVerificationCode,
    isVerifying,
    isResending,
    resendCooldown,
    error,
    success,
    resendCode,
    clearError,
  } = useEmailVerification();

  const handleCodeChange = (code: string) => {
    if (error) {
      clearError();
    }
    setVerificationCode(code);
  };

  const formatCooldownTime = (seconds: number) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
  };

  const maskEmail = (email: string) => {
    const [localPart, domain] = email.split('@');
    if (localPart.length <= 2) {
      return `${localPart[0]}***@${domain}`;
    }
    const maskedLocal = localPart[0] + '*'.repeat(localPart.length - 2) + localPart[localPart.length - 1];
    return `${maskedLocal}@${domain}`;
  };

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center py-12 px-6">
      <div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-md">
        <div className="text-center mb-8">
          <div className="mx-auto w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-4">
            <i className="fas fa-envelope text-blue-600 text-2xl"></i>
          </div>
          <h2 className="text-2xl font-bold text-gray-900 mb-2">
            이메일 인증
          </h2>
          <p className="text-gray-600 text-sm">
            {email && (
              <>
                <span className="font-medium">{maskEmail(email)}</span>로<br />
                전송된 6자리 인증번호를 입력해주세요
              </>
            )}
          </p>
        </div>

        <div className="space-y-6">
          {/* 인증번호 입력 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-4 text-center">
              인증번호 입력
            </label>
            <VerificationCodeInput
              value={verificationCode}
              onChange={handleCodeChange}
              disabled={isVerifying || success}
              error={!!error}
            />
            {error && (
              <p className="text-red-500 text-sm mt-2 text-center">{error}</p>
            )}
            {success && (
              <div className="flex items-center justify-center mt-2">
                <i className="fas fa-check-circle text-green-500 mr-2"></i>
                <p className="text-green-600 text-sm">인증이 완료되었습니다!</p>
              </div>
            )}
          </div>

          {/* 로딩 상태 */}
          {isVerifying && (
            <div className="text-center">
              <div className="inline-flex items-center">
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-blue-600 mr-2"></div>
                <span className="text-sm text-gray-600">인증 중...</span>
              </div>
            </div>
          )}

          {/* 재전송 섹션 */}
          <div className="text-center space-y-3">
            <p className="text-sm text-gray-600">
              인증번호를 받지 못하셨나요?
            </p>
            
            {resendCooldown > 0 ? (
              <p className="text-sm text-gray-500">
                재전송까지 {formatCooldownTime(resendCooldown)}
              </p>
            ) : (
              <button
                onClick={resendCode}
                disabled={isResending || success}
                className={`text-sm font-medium transition-colors ${
                  isResending || success
                    ? 'text-gray-400 cursor-not-allowed'
                    : 'text-blue-600 hover:text-blue-800 cursor-pointer'
                }`}
              >
                {isResending ? (
                  <span className="inline-flex items-center">
                    <div className="animate-spin rounded-full h-3 w-3 border-b-2 border-blue-600 mr-1"></div>
                    재전송 중...
                  </span>
                ) : (
                  '인증번호 재전송'
                )}
              </button>
            )}
          </div>

          {/* 도움말 텍스트 */}
          <div className="bg-gray-50 rounded-lg p-4">
            <h4 className="text-sm font-medium text-gray-900 mb-2">
              인증번호를 받지 못하셨나요?
            </h4>
            <ul className="text-xs text-gray-600 space-y-1">
              <li>• 스팸 메일함을 확인해주세요</li>
              <li>• 인증번호는 5분 후 만료됩니다</li>
              <li>• 재전송은 1분 간격으로 가능합니다</li>
            </ul>
          </div>

          {/* 회원가입으로 돌아가기 */}
          <div className="text-center pt-4 border-t border-gray-200">
            <span className="text-gray-600 text-sm">다른 이메일로 가입하시겠어요?</span>
            <Link 
              to="/signup" 
              className="ml-2 text-blue-600 hover:text-blue-800 text-sm font-medium cursor-pointer"
            >
              회원가입으로 돌아가기
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EmailVerificationPage;