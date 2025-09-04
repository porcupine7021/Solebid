import { useParams } from "react-router-dom";
import { getStatusColor } from "../../../utils/get-status-color";
import { transactionData } from "../../transaction/components/mockData";
import { TransactionDetailBuyer, TransactionDetailPayment, TransactionDetailProduct, TransactionDetailShipping } from "../components";

const TransactionDetailPage = () => {
    const { orderId } = useParams<{ orderId: string }>();

    const transactionDetailData = transactionData.find(
        (transaction) => transaction.id.toString() === orderId
    );

    if (!transactionDetailData) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <h2 className="text-2xl font-bold text-gray-800">
                        거래 내역을 찾을 수 없습니다.
                    </h2>
                    <p className="text-gray-600 mt-2">
                        요청하신 거래 내역이 존재하지 않거나, 잘못된 경로입니다.</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50">
            <main className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-6 pb-32">
                <TransactionDetailProduct
                    data={transactionDetailData}
                    getStatusColor={getStatusColor}
                />
                <TransactionDetailBuyer
                    buyer={transactionDetailData.buyer}
                />
                <TransactionDetailShipping
                    shipping={transactionDetailData.shipping}
                />
                <TransactionDetailPayment
                    payment={transactionDetailData.payment}
                />
            </main>
        </div>
    );
};

export default TransactionDetailPage;
