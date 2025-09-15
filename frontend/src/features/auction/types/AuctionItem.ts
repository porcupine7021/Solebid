export interface AuctionItem {
    id: number;
    brand: string;
    name: string;
    image: string | null;
    imageUrl?: string;
    currentBid: number | null;
    timeLeft: string;
    bidders: number;
    category: string;
    isWished?: boolean;
}
