import type { TransactionBuyerProps } from "../../types/TransactionBuyer";

const TransactionDetailBuyer = ({ buyer }: TransactionBuyerProps) => {
    return (
        <div className="bg-white rounded-lg shadow-sm mb-6">
            <div className="p-6">
                <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
                    <i className="fas fa-user mr-2 text-blue-600" />
                    구매자 정보
                </h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-500 mb-1">
                            구매자명
                        </label>
                        <p className="text-gray-900 font-medium">
                            {buyer.name}
                        </p>
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-500 mb-1">
                            연락처
                        </label>
                        <p className="text-gray-900 font-medium">
                            {buyer.phone}
                        </p>
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-500 mb-1">
                            이메일
                        </label>
                        <p className="text-gray-900 font-medium">
                            {buyer.email}
                        </p>
                    </div>
                    <div className="md:col-span-2">
                        <label className="block text-sm font-medium text-gray-500 mb-1">
                            배송지 주소
                        </label>
                        <p className="text-gray-900 font-medium">
                            {buyer.address}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default TransactionDetailBuyer;