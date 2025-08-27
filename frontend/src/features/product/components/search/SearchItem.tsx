import type { SearchItemProps } from "../../types/search/SearchItemProps";

const SearchItem: React.FC<SearchItemProps> = ({ product }) => {
    return (
        <div className="bg-white rounded-lg shadow-sm hover:shadow-md transition-shadow cursor-pointer overflow-hidden">
            <img
                src={product.image}
                alt={product.name}
                onClick={() => { }}
                className="w-full h-64 object-cover object-top"
            />
            <div className="p-4">
                <div className="text-sm text-gray-500 mb-1">
                    {product.brand}
                </div>
                <h4 className="text-lg font-medium text-gray-900 mb-2 truncate">
                    {product.name}
                </h4>
                <div className="flex items-center justify-between">
                    <div>
                        <p className="text-sm text-gray-500">
                            현재가
                        </p>
                        <p className="text-lg font-semibold text-blue-600">
                            ₩{product.price}
                        </p>
                    </div>
                    <div className="text-right">
                        <p className="text-sm text-gray-500">
                            {product.bidCount}명 참여
                        </p>
                        <p className="text-sm font-medium text-red-500">
                            {product.timeLeft} 남음
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SearchItem;