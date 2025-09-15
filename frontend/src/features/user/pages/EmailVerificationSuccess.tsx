import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';

const EmailVerificationSuccess: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  
  const email = location.state?.email as string;

  // 이메일이 제공되지 않으면 리다이렉트
  useEffect(() => {
    if (!email) {
      navigate('/login', { replace: true });
    }
  }, [email, navigate]);

  const maskEmail = (email: string) => {
    if (!email) return '';
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
        <div className="text-center">
          {/* 성공 아이콘 */}
          <div className="mx-auto w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mb-6">
            <i className="fas fa-check text-green-600 text-3xl"></i>
          </div>

          {/* 성공 메시지 */}
          <h2 className="text-2xl font-bold text-gray-900 mb-4">
            이메일 인증 완료!
          </h2>
          
          <div className="space-y-3 mb-8">
            <p className="text-gray-600">
              <span className="font-medium">{email && maskEmail(email)}</span>
            </p>
            <p className="text-gray-600">
              이메일 인증이 성공적으로 완료되었습니다.<br />
              이제 SoleBid의 모든 서비스를 이용하실 수 있습니다.
            </p>
          </div>

          {/* 액션 버튼 */}
          <div className="space-y-3">
            <Link
              to="/login"
              className="w-full bg-blue-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-blue-700 transition-colors cursor-pointer inline-block text-center no-underline"
            >
              로그인하기
            </Link>
            
            <Link
              to="/"
              className="w-full bg-gray-100 text-gray-700 py-3 px-4 rounded-lg font-medium hover:bg-gray-200 transition-colors cursor-pointer inline-block text-center no-underline"
            >
              홈으로 가기
            </Link>
          </div>

          {/* 추가 정보 */}
          <div className="mt-8 pt-6 border-t border-gray-200">
            <div className="bg-blue-50 rounded-lg p-4">
              <h4 className="text-sm font-medium text-blue-900 mb-2">
                🎉 환영합니다!
              </h4>
              <p className="text-xs text-blue-700">
                SoleBid에서 원하는 신발을 경매로 만나보세요.<br />
                안전하고 투명한 거래를 약속드립니다.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EmailVerificationSuccess;