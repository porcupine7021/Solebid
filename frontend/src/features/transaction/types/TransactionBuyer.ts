export interface TransactionBuyer {
    name: string;
    phone: string;
    email: string;
    address: string;
}

export interface TransactionBuyerProps {
    buyer: TransactionBuyer;
}