export interface TransactionProductProps {
    data: {
        image: string;
        name: string;
        price: number;
        status: string;
        statusText: string;
        orderId: string;
        orderDate: string;
        saleDate: string;
    };
    getStatusColor: (status: string) => string;
}