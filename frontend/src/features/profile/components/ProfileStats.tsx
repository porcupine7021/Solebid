import type { ProfileStatProps } from "../types/ProfileStatProps";
import { statsData } from "./mockData";

const StatCard = ({ label, value, color }: ProfileStatProps) => (
    <div className="bg-white rounded-lg shadow-sm p-6 text-center">
        <div className={`text-2xl font-bold ${color} mb-1`}>{value}</div>
        <div className="text-gray-600 text-sm">{label}</div>
    </div>
);

const ProfileStats = () => {
    return (
        <div className="grid grid-cols-2 gap-4 mb-6">
            {statsData.map((stat) => (
                <StatCard key={stat.label} {...stat} />
            ))}
        </div>
    );
};

export default ProfileStats;