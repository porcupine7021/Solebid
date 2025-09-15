export interface Wish {
    id: number;
    brand: string;
    name: string;
    image: string | null;
    imageUrl?: string;
    category: string;
    currentBid: number | null;
    timeLeft: string;
    bidders: number;
}