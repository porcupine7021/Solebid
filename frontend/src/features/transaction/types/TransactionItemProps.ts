import type { ProfileBidSellingProps } from "../../profile/types/ProfileBidSellingProps";

export interface TransactionItemProps {
    item: ProfileBidSellingProps & { imageUrl: string };
}
