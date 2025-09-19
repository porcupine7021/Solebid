export interface OrderShipping {
    recipient: string;
    phone: string;
    address: string;
    request: string;
    trackingNumber: string;
    courier: string;
}

export interface OrderDetailShippingProps {
    shipping: OrderShipping;
}