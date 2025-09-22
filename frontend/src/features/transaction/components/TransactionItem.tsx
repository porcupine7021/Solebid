import { formatDate, formatPrice } from "../../../utils/bid-utils";
import type { TransactionItemProps } from "../types/TransactionItemProps";

const TransactionItem = ({ item }: TransactionItemProps) => {
    return (
        <div className="p-6 hover:bg-gray-50 transition-colors">
            <div className="flex items-center justify-between">
                <div className="flex items-center flex-1">
                    <img
                        src={item.imageUrl || '/placeholder-image.jpg'}
                        alt={item.productName}
                        className="w-20 h-20 rounded-lg object-cover mr-4"
                    />
                    <div className="flex-1">
                        <h3 className="font-medium text-gray-900 mb-1">
                            {item.productName}
                        </h3>
                        <p className="text-gray-600 text-sm mb-2">
                            판매일: {formatDate(item.soldDate)}
                        </p>
                        <div className="flex items-center space-x-4">
                            <span className="font-semibold text-gray-900">
                                {formatPrice(item.soldPrice)}
                            </span>
                            <span className="px-3 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800">
                                판매완료
                            </span>
                        </div>
                        <div className="flex items-center space-x-4 mt-2 text-sm text-gray-500">
                            <span>구매자: {item.buyerName}</span>
                            <span>브랜드: {item.productBrand}</span>
                            <span>사이즈: {item.productSize}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default TransactionItem;
