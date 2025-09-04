import type { TransactionProductProps } from "../../types/TransactionProduct";

const TransactionDetailProduct = ({ data, getStatusColor }: TransactionProductProps) => {
    return (
        <div className="bg-white rounded-lg shadow-sm mb-6 p-6">
            <div className="flex flex-col md:flex-row md:items-start space-y-4 md:space-y-0 md:space-x-6">
                <div className="flex-shrink-0">
                    <img
                        src={data.image}
                        alt={data.name}
                        className="w-full md:w-48 h-48 rounded-lg object-cover"
                    />
                </div>
                <div className="flex-1">
                    <div className="flex items-start justify-between mb-4">
                        <div>
                            <h2 className="text-xl font-bold text-gray-900 mb-2">
                                {data.name}
                            </h2>
                            <p className="text-xl font-bold text-gray-900 mb-3">
                                ₩{data.price.toLocaleString()}
                            </p>
                        </div>
                        <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(data.status)}`}>
                            {data.statusText}
                        </span>
                    </div>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-sm">
                        <div>
                            <span className="text-gray-500">
                                주문번호:
                            </span>
                            <span className="ml-2 font-medium text-gray-900">
                                {data.orderId}
                            </span>
                        </div>
                        <div>
                            <span className="text-gray-500">
                                주문일시:
                            </span>
                            <span className="ml-2 font-medium text-gray-900">
                                {data.orderDate}
                            </span>
                        </div>
                        <div>
                            <span className="text-gray-500">
                                판매일:
                            </span>
                            <span className="ml-2 font-medium text-gray-900">
                                {data.saleDate}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default TransactionDetailProduct;