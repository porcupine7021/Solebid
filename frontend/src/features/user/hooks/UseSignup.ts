import { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { Agreements, SignupErrors, SignupFormData } from '../types/SignupTypes.ts';
import { signupUser } from '../services/UserService';
import { getOAuth2AuthUrl } from '../services/AuthService';
import { sendVerificationEmail, verifySignupEmailCode } from '../services/EmailVerificationService';

export function useSignup() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState<SignupFormData>({
    email: '', password: '', confirmPassword: '', nickname: '', name: '', phone: '', verificationCode: '',
  });
  const [agreements, setAgreements] = useState<Agreements>({ all: false, terms: false, privacy: false, marketing: false });
  const [errors, setErrors] = useState<SignupErrors>({
    email: '', password: '', confirmPassword: '', nickname: '', name: '', phone: '', agreeTerms: '', agreePrivacy: '', verificationCode: ''
  });
  const [showTermsModal, setShowTermsModal] = useState(false);
  const [showPrivacyModal, setShowPrivacyModal] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [sendingCode, setSendingCode] = useState(false);
  const [verificationSent, setVerificationSent] = useState(false);
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);
  const [emailVerified, setEmailVerified] = useState(false);
  const [verifyingCode, setVerifyingCode] = useState(false);

  const validateEmail = (email: string) => {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!email) return '이메일을 입력해주세요.';
    if (!regex.test(email)) return '올바른 이메일 형식이 아닙니다.';
    return '';
  };
  const validatePassword = (password: string) => {
    const regex = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,20}$/;
    if (!password) return '비밀번호를 입력해주세요.';
    if (!regex.test(password)) return '비밀번호는 8~20자 영문, 숫자, 특수문자를 포함해야 합니다.';
    return '';
  };
  const validateNickname = (nickname: string) => {
    if (!nickname) return '닉네임을 입력해주세요.';
    if (nickname.length < 2 || nickname.length > 10) return '닉네임은 2자 이상 10자 이하로 입력해주세요.';
    return '';
  };
  const validatePhone = (phone: string) => {
    const regex = /^01(?:0|1|[6-9])[0-9]{7,8}$/;
    if (!phone) return '휴대폰 번호를 입력해주세요.';
    if (!regex.test(phone)) return '올바른 휴대폰 번호 형식이 아닙니다.';
    return '';
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target as HTMLInputElement & { name: keyof SignupFormData };
    setFormData((prev) => ({ ...prev, [name]: value }));

    let error = '';
    if (name === 'email') error = validateEmail(value);
    if (name === 'password') error = validatePassword(value);
    if (name === 'confirmPassword') error = prevPasswordMatch(value) ? '' : '비밀번호가 일치하지 않습니다.';
    if (name === 'nickname') error = validateNickname(value);
    if (name === 'phone') error = validatePhone(value);
    if (name === 'name' && !value) error = '이름을 입력해주세요.';
    if (name === 'verificationCode') {
      // 인증번호가 6자리 완성되면 자동 검증
      if (value.length === 6) {
        handleVerifyCode(value);
      }
    }

    setErrors((prev) => ({ ...prev, [name]: error } as SignupErrors));
  };

  const prevPasswordMatch = (confirm: string) => formData.password === confirm;

  const handleAllAgreements = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { checked } = e.target;
    setAgreements({ all: checked, terms: checked, privacy: checked, marketing: checked });
  };

  const handleAgreementChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, checked } = e.target as HTMLInputElement & { name: keyof Agreements };
    setAgreements((prev) => ({ ...prev, [name]: checked }));
  };

  useEffect(() => {
    const { terms, privacy, marketing } = agreements;
    setAgreements((prev) => ({ ...prev, all: terms && privacy && marketing }));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [agreements.terms, agreements.privacy, agreements.marketing]);

  const handleVerifyCode = async (code: string) => {
    if (!code || code.length !== 6 || !formData.email) {
      setErrors(prev => ({ ...prev, verificationCode: '6자리 인증번호를 입력해주세요.' }));
      return;
    }

    setVerifyingCode(true);
    setErrors(prev => ({ ...prev, verificationCode: '' }));

    try {
      const response = await verifySignupEmailCode({
        email: formData.email,
        verificationCode: code,
      });

      if (response.success) {
        setEmailVerified(true);
        setErrors(prev => ({ ...prev, verificationCode: '' }));
      } else {
        setErrors(prev => ({ 
          ...prev, 
          verificationCode: response.message || '인증번호가 올바르지 않습니다.'
        }));
      }
    } catch (error) {
      console.error('Email verification error:', error);
      setErrors(prev => ({ 
        ...prev, 
        verificationCode: '서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.'
      }));
    } finally {
      setVerifyingCode(false);
    }
  };

  const isFormValid = useMemo(() => {
    if (!emailVerified) return false;
    
    // verificationCode 제외하고 검증
    const isFormDataFilled = Object.entries(formData).every(([k, v]) => (
      k === 'verificationCode' ? true : String(v).trim() !== ''
    ));

    // verificationCode 오류 제외하고 검증
    const noErrors = Object.entries(errors).every(([k, e]) => (
      k === 'verificationCode' ? true : e === ''
    ));

    const requiredAgreements = agreements.terms && agreements.privacy;
    return isFormDataFilled && noErrors && requiredAgreements;
  }, [formData, errors, agreements, emailVerified]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!emailVerified) {
      alert('이메일 인증을 먼저 완료해주세요.');
      return;
    }

    const newErrors: SignupErrors = {
      email: validateEmail(formData.email),
      password: validatePassword(formData.password),
      confirmPassword: formData.password !== formData.confirmPassword ? '비밀번호가 일치하지 않습니다.' : '',
      nickname: validateNickname(formData.nickname),
      name: !formData.name ? '이름을 입력해주세요.' : '',
      phone: validatePhone(formData.phone),
      agreeTerms: !agreements.terms ? '이용약관에 동의해주세요.' : '',
      agreePrivacy: !agreements.privacy ? '개인정보 처리방침에 동의해주세요.' : '',
      verificationCode: '', // 이미 검증 완료
    };
    setErrors(newErrors);

    // verificationCode 키를 제외하고 유효성 확인
    const valid = Object.entries(newErrors).every(([k, err]) => (
      k === 'verificationCode' ? true : err === ''
    ));
    if (!valid) return;

    try {
      setSubmitting(true);
      const res = await signupUser({
        email: formData.email,
        password: formData.password,
        nickname: formData.nickname,
        name: formData.name,
        phone: formData.phone,
        marketing: agreements.marketing,
      });
      if (res.success) {
        // 회원가입 완료 후 로그인 페이지로 리다이렉트
        alert('회원가입이 완료되었습니다! 로그인해주세요.');
        navigate('/login', { replace: true });
      } else {
        alert(res.message || '회원가입에 실패했습니다.');
      }
    } catch (err) {
      console.error('Signup error:', err);
      alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
    } finally {
      setSubmitting(false);
    }
  };

  const openTermsModal = (e: React.MouseEvent) => { e.preventDefault(); setShowTermsModal(true); };
  const openPrivacyModal = (e: React.MouseEvent) => { e.preventDefault(); setShowPrivacyModal(true); };
  const closeModal = () => { setShowTermsModal(false); setShowPrivacyModal(false); };

  const handleKakaoStart = async () => {
    try {
      const resp = await getOAuth2AuthUrl('kakao');
      if (resp.success && resp.data?.authUrl) {
        window.location.href = resp.data.authUrl;
      } else {
        alert(resp.message || '카카오 로그인 URL 생성에 실패했습니다.');
      }
    } catch {
      alert('카카오 로그인 준비 중 오류가 발생했습니다.');
    }
  };

  const handleGoogleStart = async () => {
    try {
      const resp = await getOAuth2AuthUrl('google');
      if (resp.success && resp.data?.authUrl) {
        window.location.href = resp.data.authUrl;
      } else {
        alert(resp.message || '구글 로그인 URL 생성에 실패했습니다.');
      }
    } catch {
      alert('구글 로그인 준비 중 오류가 발생했습니다.');
    }
  };

  const handleSendVerificationCode = async () => {
    if (!formData.email || errors.email) {
      return;
    }

    try {
      setSendingCode(true);
      setVerificationSent(false);
      
      const response = await sendVerificationEmail({
        email: formData.email,
      });

      if (response.success) {
        setVerificationSent(true);
        setShowSuccessMessage(true);
        // 3초 후 성공 메시지만 숨기기
        setTimeout(() => setShowSuccessMessage(false), 3000);
      } else {
        alert(response.message || '인증번호 전송에 실패했습니다.');
      }
    } catch (error) {
      console.error('Send verification code error:', error);
      alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
    } finally {
      setSendingCode(false);
    }
  };

  return {
    formData, agreements, errors,
    showTermsModal, showPrivacyModal, submitting,
    sendingCode, verificationSent, showSuccessMessage, emailVerified, verifyingCode,
    isFormValid,
    handleInputChange, handleAllAgreements, handleAgreementChange, handleSubmit,
    openTermsModal, openPrivacyModal, closeModal,
    handleKakaoStart, handleGoogleStart, handleSendVerificationCode, handleVerifyCode,
  };
}
