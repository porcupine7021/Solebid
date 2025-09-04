export interface TransactionShipping {
    company: string;
    trackingNumber: string;
    status: string;
    deliveredDate: string | null;
}

export interface TransactionShippingProps {
    shipping: TransactionShipping;
}