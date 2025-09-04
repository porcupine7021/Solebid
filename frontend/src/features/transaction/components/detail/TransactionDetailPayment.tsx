import type { TransactionPaymentProps } from "../../types/TransactionPayment";

const TransactionDetailPayment = ({ payment }: TransactionPaymentProps) => {
    return (
        <div className="bg-white rounded-lg shadow-sm mb-6">
            <div className="p-6">
                <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
                    <i className="fas fa-credit-card mr-2 text-purple-600" />
                    결제 정보
                </h3>
                <div className="space-y-4">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-500 mb-1">
                                결제 수단
                            </label>
                            <p className="text-gray-900 font-medium">
                                {payment.method}
                            </p>
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-500 mb-1">
                                카드 정보
                            </label>
                            <p className="text-gray-900 font-medium">
                                {payment.cardInfo}
                            </p>
                        </div>
                        <div className="md:col-span-2">
                            <label className="block text-sm font-medium text-gray-500 mb-1">
                                결제일시
                            </label>
                            <p className="text-gray-900 font-medium">
                                {payment.paymentDate}
                            </p>
                        </div>
                    </div>
                    <div className="border-t border-gray-200 pt-4">
                        <div className="space-y-2">
                            <div className="flex justify-between">
                                <span className="text-gray-600">
                                    상품 금액
                                </span>
                                <span className="text-gray-900">
                                    ₩{payment.productAmount.toLocaleString()}
                                </span>
                            </div>
                            <div className="flex justify-between">
                                <span className="text-gray-600">
                                    배송비
                                </span>
                                <span className="text-gray-900">
                                    ₩{payment.shippingFee.toLocaleString()}
                                </span>
                            </div>
                            <div className="border-t border-gray-200 pt-2 flex justify-between">
                                <span className="text-lg font-semibold text-gray-900">
                                    총 결제금액
                                </span>
                                <span className="text-lg font-bold text-blue-600">
                                    ₩{payment.totalAmount.toLocaleString()}
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default TransactionDetailPayment;