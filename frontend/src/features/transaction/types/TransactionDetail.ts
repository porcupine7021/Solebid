import type { TransactionBuyer } from "./TransactionBuyer";
import type { TransactionPayment } from "./TransactionPayment";
import type { TransactionShipping } from "./TransactionShipping";

export interface TransactionDetail {
    id: string;
    name: string;
    price: number;
    image: string;
    status: string;
    statusText: string;
    orderId: string;
    orderDate: string;
    saleDate: string;
    buyer: TransactionBuyer;
    shipping: TransactionShipping;
    payment: TransactionPayment;
}