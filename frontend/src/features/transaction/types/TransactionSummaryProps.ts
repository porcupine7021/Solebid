import type { ProfileBidSellingProps } from "../../profile/types/ProfileBidSellingProps";

export interface TransactionSummaryProps {
    data: (ProfileBidSellingProps & { imageUrl: string })[];
}
