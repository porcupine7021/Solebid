import { useState, useEffect } from 'react';
import TemperatureCard from './TemperatureCard';
import type { UserProfile } from '../../user/types/AuthTypes';

const ProfileStats = () => {
    const [temperature, setTemperature] = useState<number>(36.5); // 기본값
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                setLoading(true);
                setError(null);
                
                const response = await fetch('/api/users/me', {
                    credentials: 'include' // 쿠키 포함
                });
                
                if (!response.ok) {
                    throw new Error('사용자 정보를 불러올 수 없습니다.');
                }
                
                const result = await response.json();
                
                if (result.success && result.data) {
                    const profile = result.data as UserProfile;
                    setTemperature(profile.temperature || 36.5);
                } else {
                    throw new Error('사용자 온도 정보를 찾을 수 없습니다.');
                }
            } catch (err) {
                console.error('Failed to fetch user profile:', err);
                setError(err instanceof Error ? err.message : '온도 정보를 불러올 수 없습니다.');
                // 에러 발생 시 기본값 유지
                setTemperature(36.5);
            } finally {
                setLoading(false);
            }
        };

        fetchUserProfile();
    }, []);

    if (loading) {
        return (
            <div className="mb-6">
                <div className="bg-white rounded-lg shadow-sm p-6 text-center">
                    <div className="flex items-center justify-center mb-2">
                        <i className="fas fa-spinner fa-spin text-orange-500 text-2xl mr-2" aria-hidden="true"></i>
                        <div className="text-2xl font-bold text-gray-400">--.-°C</div>
                    </div>
                    <div className="text-gray-600 text-sm">온도 정보 로딩 중...</div>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="mb-6">
                <div className="bg-white rounded-lg shadow-sm p-6 text-center">
                    <div className="flex items-center justify-center mb-2">
                        <i className="fas fa-thermometer-half text-orange-500 text-2xl mr-2" aria-hidden="true"></i>
                        <div className="text-2xl font-bold text-orange-600">
                            {temperature.toFixed(1)}°C
                        </div>
                    </div>
                    <div className="text-gray-600 text-sm">사용자 온도 (기본값)</div>
                    <div className="text-red-500 text-xs mt-1">{error}</div>
                </div>
            </div>
        );
    }

    return (
        <div className="mb-6">
            <TemperatureCard temperature={temperature} />
        </div>
    );
};

export default ProfileStats;