import type { CartSummaryProps } from "../types/CartSummaryProps";

const CartSummary = ({ totalAmount, shippingFee, finalAmount, formatPrice }: CartSummaryProps) => {
    return (
        <div className="bg-white rounded-lg p-4 shadow-sm border border-gray-100">
            <h3 className="text-base font-semibold text-gray-900 mb-4">
                주문 요약
            </h3>
            <div className="space-y-3">
                <div className="flex justify-between items-center">
                    <span className="text-gray-600">
                        총 상품 금액
                    </span>
                    <span className="text-gray-900 font-medium">
                        {formatPrice(totalAmount)}
                    </span>
                </div>
                <div className="flex justify-between items-center">
                    <span className="text-gray-600">
                        배송비
                    </span>
                    <span className="text-gray-900 font-medium">
                        {shippingFee === 0 ? "무료" : formatPrice(shippingFee)}
                    </span>
                </div>
                <div className="flex justify-between items-center">
                    <span className="text-lg font-semibold text-gray-900">
                        최종 결제 금액
                    </span>
                    <span className="text-xl font-bold text-blue-600">
                        {formatPrice(finalAmount)}
                    </span>
                </div>
            </div>
        </div>
    );
};

export default CartSummary;