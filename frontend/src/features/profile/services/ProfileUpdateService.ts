import { apiFetch } from '../../../utils/apiFetch';
import type {
  ProfileUpdateRequest,
  SensitiveProfileUpdateRequest,
  PasswordChangeRequest,
  ProfileUpdateApiResponse,
  PasswordChangeApiResponse,
} from '../types/ProfileUpdateTypes';

/**
 * 일반 프로필 정보 업데이트 API 함수
 * 닉네임, 이름 등 민감하지 않은 정보를 업데이트
 */
export const updateProfile = async (data: ProfileUpdateRequest): Promise<ProfileUpdateApiResponse> => {
  try {
    const response = await apiFetch<ProfileUpdateApiResponse>('/api/users/profile', {
      method: 'PUT',
      json: data,
    });

    if (!response.success) {
      throw new Error(response.message || '프로필 업데이트에 실패했습니다.');
    }

    return response;
  } catch (error) {
    if (error instanceof Error) {
      if (error.message.includes('HTTP 401')) {
        throw new Error('로그인이 필요합니다.');
      } else if (error.message.includes('HTTP 403')) {
        throw new Error('접근 권한이 없습니다.');
      } else if (error.message.includes('HTTP 409')) {
        throw new Error('이미 사용 중인 닉네임입니다.');
      } else if (error.message.includes('HTTP 400')) {
        throw new Error('입력한 정보가 올바르지 않습니다.');
      } else if (error.message.includes('HTTP 500')) {
        throw new Error('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
      }
      throw error;
    }
    throw new Error('프로필 업데이트 중 오류가 발생했습니다.');
  }
};

/**
 * 민감한 프로필 정보 업데이트 API 함수 (스텝업 인증)
 * 이메일, 전화번호 등 민감한 정보를 업데이트
 * 현재 비밀번호 확인이 필요
 */
export const updateSensitiveProfile = async (data: SensitiveProfileUpdateRequest): Promise<ProfileUpdateApiResponse> => {
  try {
    const response = await apiFetch<ProfileUpdateApiResponse>('/api/users/profile/sensitive', {
      method: 'PUT',
      json: data,
    });

    if (!response.success) {
      throw new Error(response.message || '민감한 정보 업데이트에 실패했습니다.');
    }

    return response;
  } catch (error) {
    if (error instanceof Error) {
      if (error.message.includes('HTTP 401')) {
        throw new Error('현재 비밀번호가 올바르지 않습니다.');
      } else if (error.message.includes('HTTP 403')) {
        throw new Error('접근 권한이 없습니다.');
      } else if (error.message.includes('HTTP 409')) {
        throw new Error('이미 사용 중인 이메일입니다.');
      } else if (error.message.includes('HTTP 400')) {
        throw new Error('입력한 정보가 올바르지 않습니다.');
      } else if (error.message.includes('HTTP 500')) {
        throw new Error('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
      }
      throw error;
    }
    throw new Error('민감한 정보 업데이트 중 오류가 발생했습니다.');
  }
};

/**
 * 비밀번호 변경 API 함수
 * 현재 비밀번호 확인 후 새 비밀번호로 변경
 * 성공 시 모든 세션이 무효화됨
 */
export const changePassword = async (data: PasswordChangeRequest): Promise<PasswordChangeApiResponse> => {
  try {
    // 비밀번호 확인 검증
    if (data.newPassword !== data.confirmPassword) {
      throw new Error('새 비밀번호와 비밀번호 확인이 일치하지 않습니다.');
    }

    // 현재 비밀번호와 새 비밀번호 동일성 검증
    if (data.currentPassword === data.newPassword) {
      throw new Error('새 비밀번호는 현재 비밀번호와 달라야 합니다.');
    }

    const response = await apiFetch<PasswordChangeApiResponse>('/api/users/password', {
      method: 'PUT',
      json: {
        currentPassword: data.currentPassword,
        newPassword: data.newPassword,
        confirmPassword: data.confirmPassword,
      },
    });

    if (!response.success) {
      throw new Error(response.message || '비밀번호 변경에 실패했습니다.');
    }

    return response;
  } catch (error) {
    if (error instanceof Error) {
      if (error.message.includes('HTTP 401')) {
        throw new Error('현재 비밀번호가 올바르지 않습니다.');
      } else if (error.message.includes('HTTP 400')) {
        throw new Error('비밀번호 형식이 올바르지 않습니다. 8~20자 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.');
      } else if (error.message.includes('HTTP 403')) {
        throw new Error('접근 권한이 없습니다.');
      } else if (error.message.includes('HTTP 500')) {
        throw new Error('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
      }
      throw error;
    }
    throw new Error('비밀번호 변경 중 오류가 발생했습니다.');
  }
};

/**
 * 비밀번호 강도 검증 함수
 * 8~20자 영문 대소문자, 숫자, 특수문자를 모두 포함해야 함
 */
export const validatePasswordStrength = (password: string): { isValid: boolean; message: string } => {
  const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;
  
  if (!password) {
    return { isValid: false, message: '비밀번호를 입력해주세요.' };
  }
  
  if (password.length < 8) {
    return { isValid: false, message: '비밀번호는 최소 8자 이상이어야 합니다.' };
  }
  
  if (password.length > 20) {
    return { isValid: false, message: '비밀번호는 최대 20자까지 가능합니다.' };
  }
  
  if (!passwordRegex.test(password)) {
    return { isValid: false, message: '영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.' };
  }
  
  return { isValid: true, message: '사용 가능한 비밀번호입니다.' };
};

/**
 * 닉네임 유효성 검증 함수
 */
export const validateNickname = (nickname: string): { isValid: boolean; message: string } => {
  if (!nickname) {
    return { isValid: false, message: '닉네임을 입력해주세요.' };
  }
  
  if (nickname.length < 2) {
    return { isValid: false, message: '닉네임은 최소 2자 이상이어야 합니다.' };
  }
  
  if (nickname.length > 50) {
    return { isValid: false, message: '닉네임은 최대 50자까지 가능합니다.' };
  }
  
  return { isValid: true, message: '사용 가능한 닉네임입니다.' };
};

/**
 * 이름 유효성 검증 함수
 */
export const validateName = (name: string): { isValid: boolean; message: string } => {
  if (!name) {
    return { isValid: false, message: '이름을 입력해주세요.' };
  }
  
  if (name.length < 2) {
    return { isValid: false, message: '이름은 최소 2자 이상이어야 합니다.' };
  }
  
  if (name.length > 50) {
    return { isValid: false, message: '이름은 최대 50자까지 가능합니다.' };
  }
  
  return { isValid: true, message: '사용 가능한 이름입니다.' };
};

/**
 * 전화번호 유효성 검증 함수
 */
export const validatePhone = (phone: string): { isValid: boolean; message: string } => {
  const phoneRegex = /^010-\d{4}-\d{4}$/;
  
  if (!phone) {
    return { isValid: false, message: '전화번호를 입력해주세요.' };
  }
  
  if (!phoneRegex.test(phone)) {
    return { isValid: false, message: '전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)' };
  }
  
  return { isValid: true, message: '사용 가능한 전화번호입니다.' };
};

/**
 * 이메일 유효성 검증 함수
 */
export const validateEmail = (email: string): { isValid: boolean; message: string } => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  
  if (!email) {
    return { isValid: false, message: '이메일을 입력해주세요.' };
  }
  
  if (!emailRegex.test(email)) {
    return { isValid: false, message: '올바른 이메일 형식이 아닙니다.' };
  }
  
  return { isValid: true, message: '사용 가능한 이메일입니다.' };
};
/**

 * 폼 데이터를 API 요청 형식으로 변환하는 유틸리티 함수
 */
export const transformProfileFormData = (formData: any): ProfileUpdateRequest => {
  const result: ProfileUpdateRequest = {};
  
  if (formData.nickname && formData.nickname.trim()) {
    result.nickname = formData.nickname.trim();
  }
  
  if (formData.name && formData.name.trim()) {
    result.name = formData.name.trim();
  }
  
  return result;
};

/**
 * 민감한 정보 폼 데이터를 API 요청 형식으로 변환하는 유틸리티 함수
 */
export const transformSensitiveProfileFormData = (formData: any): SensitiveProfileUpdateRequest => {
  const result: SensitiveProfileUpdateRequest = {
    currentPassword: formData.currentPassword,
  };
  
  if (formData.email && formData.email.trim()) {
    result.email = formData.email.trim();
  }
  
  if (formData.phone && formData.phone.trim()) {
    result.phone = formData.phone.trim();
  }
  
  return result;
};