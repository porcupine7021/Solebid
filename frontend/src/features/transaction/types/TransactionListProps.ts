import type { ProfileBidSellingProps } from "../../profile/types/ProfileBidSellingProps";

export interface TransactionListProps {
    data: (ProfileBidSellingProps & { imageUrl: string })[];
}
