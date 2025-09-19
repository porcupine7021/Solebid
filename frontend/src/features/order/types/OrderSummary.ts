import type { Order } from "./Order";

export type OrderDetailSummaryProps = Pick<
    Order,
    "trackingNumber" | "date" | "status" | "statusColor"
> & {
    finalPrice: number;
};