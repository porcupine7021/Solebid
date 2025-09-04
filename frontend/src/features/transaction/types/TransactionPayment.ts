export interface TransactionPayment {
    method: string;
    cardInfo: string;
    paymentDate: string;
    productAmount: number;
    shippingFee: number;
    totalAmount: number;
}

export interface TransactionPaymentProps {
    payment: TransactionPayment;
}