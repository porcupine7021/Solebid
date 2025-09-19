import type { Order } from "../types/Order";
import type { OrderPayment } from "../types/OrderPayment";
import type { OrderShipping } from "../types/OrderShipping";
import type { OrderTimeline } from "../types/OrderTimeline";

export const periods = [
    "전체",
    "최근 1개월",
    "최근 3개월",
    "최근 6개월",
    "최근 1년",
];

export const statuses = [
    "전체",
    "결제대기",
    "배송준비중",
    "배송중",
    "배송완료",
    "취소·반품",
];

// API 응답 데이터를 기반으로 payment 정보 구성
export const createPaymentInfo = (order: Order): OrderPayment => {
    return {
        method: "신용카드", // 기본값, 실제로는 API에서 제공해야 함
        cardInfo: "****-****-****-****", // 기본값
        status: order.paymentStatus === "COMPLETED" ? "결제완료" :
            order.paymentStatus === "WAITING" ? "결제대기" : "결제실패",
        itemAmount: order.finalPrice,
        shippingFee: 0, // 기본값
        discount: 0, // 기본값
        finalAmount: order.finalPrice,
    };
};

// API 응답 데이터를 기반으로 shipping 정보 구성
export const createShippingInfo = (order: Order): OrderShipping => {
    return {
        recipient: "수령인", // 기본값, 실제로는 API에서 제공해야 함
        phone: "010-****-****", // 기본값
        address: order.deliveryAddress || "",
        request: "", // 기본값
        trackingNumber: order.trackingNumber || "",
        courier: "택배사", // 기본값
    };
};

// API 응답 데이터를 기반으로 timeline 정보 구성
export const createTimelineInfo = (order: Order): OrderTimeline[] => {
    const timeline: OrderTimeline[] = [
        { status: "주문접수", date: order.date, completed: true },
    ];

    // paymentStatus에 따른 결제 상태 추가
    if (order.paymentStatus === "COMPLETED") {
        timeline.push({ status: "결제완료", date: order.date, completed: true });
    } else if (order.paymentStatus === "WAITING") {
        timeline.push({ status: "결제대기", date: "", completed: false });
        return timeline;
    } else if (order.paymentStatus === "FAIL") {
        timeline.push({ status: "결제실패", date: order.date, completed: true });
        return timeline;
    }

    // deliveryStatus에 따른 배송 상태 추가
    switch (order.deliveryStatus) {
        case "PREPARING":
            timeline.push({ status: "상품준비중", date: order.date, completed: true });
            timeline.push({ status: "배송시작", date: "", completed: false });
            timeline.push({ status: "배송완료", date: "", completed: false });
            break;
        case "SHIPPED":
            timeline.push({ status: "상품준비중", date: order.date, completed: true });
            timeline.push({ status: "배송시작", date: order.date, completed: true });
            timeline.push({ status: "배송완료", date: "", completed: false });
            break;
        case "DELIVERED":
            timeline.push({ status: "상품준비중", date: order.date, completed: true });
            timeline.push({ status: "배송시작", date: order.date, completed: true });
            timeline.push({ status: "배송완료", date: order.date, completed: true });
            break;
        case "CANCELED":
            timeline.push({ status: "취소·반품", date: order.date, completed: true });
            break;
        default:
            timeline.push({ status: "상품준비중", date: "", completed: false });
            timeline.push({ status: "배송시작", date: "", completed: false });
            timeline.push({ status: "배송완료", date: "", completed: false });
    }

    return timeline;
};

// Order 객체에 추가 정보를 구성하여 반환
export const enrichOrderData = (order: Order): Order => {
    return {
        ...order,
        payment: createPaymentInfo(order),
        shipping: createShippingInfo(order),
        timeline: createTimelineInfo(order),
    };
};
