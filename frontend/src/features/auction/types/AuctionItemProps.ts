import type { Wish } from "../../wish/types/Wish";
import type { AuctionItem } from "./AuctionItem";

export interface AuctionItemProps {
    item: AuctionItem;
    onBidClick: (item: AuctionItem) => void;
    onSelect: (item: AuctionItem) => void;
    addWish: (item: Wish) => void;
    removeWish: (id: number) => void;
    pendingAddId: number | null;
    pendingRemoveId: number | null;
    canWish: boolean;
}
