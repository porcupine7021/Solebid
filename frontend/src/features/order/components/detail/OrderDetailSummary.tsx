import { getBadgeClass } from "../../../../utils/get-badge-class";
import type { OrderDetailSummaryProps } from "../../types/OrderSummary";

const OrderDetailSummary = ({
    trackingNumber,
    date,
    status,
    statusColor,
    finalPrice,
}: OrderDetailSummaryProps) => {
    return (
        <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
            <div className="flex justify-between items-start mb-4">
                <div>
                    <h2 className="text-xl font-bold text-gray-900 mb-2">
                        {trackingNumber}
                    </h2>
                    <p className="text-gray-600">
                        {date} 주문
                    </p>
                </div>
                <div className="text-right">
                    <span className={getBadgeClass(statusColor)}>
                        {status}
                    </span>
                    <div className="text-2xl font-bold text-gray-900 mt-2">
                        {finalPrice.toLocaleString()}원
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OrderDetailSummary;