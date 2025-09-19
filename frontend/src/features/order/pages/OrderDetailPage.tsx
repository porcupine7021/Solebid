import { useParams } from "react-router-dom";
import { OrderDetailItem, OrderDetailPayment, OrderDetailShipping, OrderDetailSummary, OrderDetailTimeline } from "../components";
import { useOrderDetails } from "../hooks/useOrders";

const OrderDetailPage = () => {
    const { orderId } = useParams<{ orderId: string }>();

    const { data: orderDetails, isLoading, isError, imageError } = useOrderDetails(orderId || "");

    if (isLoading) {
        return (
            <div className="fixed top-0 left-0 w-full h-full flex justify-center items-center">
                <i className="fas fa-spinner fa-spin fa-3x"></i>
            </div>
        );
    }

    if (isError || !orderDetails) {
        return (
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
                <div className="text-center">
                    <h2 className="text-2xl font-bold text-gray-800">주문 정보를 찾을 수 없습니다.</h2>
                    <p className="text-gray-600 mt-2">요청하신 주문에 해당하는 정보가 없습니다.</p>
                    {imageError && (
                        <p className="text-orange-600 mt-2">{imageError}</p>
                    )}
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <OrderDetailSummary
                    trackingNumber={orderDetails.trackingNumber}
                    date={orderDetails.date}
                    status={orderDetails.status}
                    statusColor={orderDetails.statusColor}
                    finalPrice={orderDetails.finalPrice}
                />
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                    <div className="lg:col-span-2 space-y-6">
                        <OrderDetailItem
                            items={orderDetails.items || []}
                        />
                        <OrderDetailPayment
                            payment={orderDetails.payment!}
                        />
                        <OrderDetailShipping
                            shipping={orderDetails.shipping!}
                        />
                    </div>
                    <div className="space-y-6">
                        <OrderDetailTimeline
                            timeline={orderDetails.timeline!}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OrderDetailPage;
