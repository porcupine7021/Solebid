import { Link } from "react-router-dom";
import type { ProfileWishProps } from "../types/ProfileWishProps";
import { wishData } from "./mockData";

const WishItem = ({ name, price, imageUrl }: ProfileWishProps) => (
    <div className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow cursor-pointer">
        <img
            src={imageUrl}
            alt={name}
            className="w-full h-24 object-cover rounded-lg mb-2" />
        <h4 className="font-medium text-gray-900 text-sm">
            {name}
        </h4>
        <p className="text-blue-600 font-semibold text-sm">
            {price}
        </p>
    </div>
);

const ProfileWish = () => {
    return (
        <div className="bg-white rounded-lg shadow-sm p-6">
            <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-semibold text-gray-900">
                    찜한 상품
                </h3>
                <Link
                    to="/wish"
                    className="text-blue-600 text-sm hover:text-blue-800 cursor-pointer"
                >
                    전체 보기
                </Link>
            </div>
            <div className="grid grid-cols-3 gap-4">
                {wishData.map(item => <WishItem key={item.id} {...item} />)}
            </div>
        </div>
    );
};

export default ProfileWish;