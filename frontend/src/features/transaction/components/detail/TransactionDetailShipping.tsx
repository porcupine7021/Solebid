import type { TransactionShippingProps } from "../../types/TransactionShipping";

const TransactionDetailShipping = ({ shipping }: TransactionShippingProps) => {
    return (
        <div className="bg-white rounded-lg shadow-sm mb-6">
            <div className="p-6">
                <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
                    <i className="fas fa-truck mr-2 text-green-600" />
                    배송 정보
                </h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-500 mb-1">
                            택배사
                        </label>
                        <p className="text-gray-900 font-medium">
                            {shipping.company}
                        </p>
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-500 mb-1">
                            운송장 번호
                        </label>
                        <div className="flex items-center">
                            <p className="text-gray-900 font-medium mr-2">
                                {shipping.trackingNumber}
                            </p>
                            <button
                                onClick={() => { }}
                                className="text-blue-600 hover:text-blue-800 cursor-pointer"
                            >
                                <i className="fas fa-external-link-alt text-sm" />
                            </button>
                        </div>
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-500 mb-1">
                            배송 상태
                        </label>
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                            {shipping.status}
                        </span>
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-500 mb-1">
                            배송 완료일
                        </label>
                        <p className="text-gray-900 font-medium">
                            {shipping.deliveredDate}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default TransactionDetailShipping;