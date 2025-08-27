export interface Brand {
    id: number;
    name: string;
    logo: string;
}

export interface BrandProduct {
    id: number;
    name: string;
    image: string;
    currentBid: string;
    timeLeft: string;
    bidders: number;
}

export interface BrandWithProducts {
    brand: string;
    description: string;
    logo: string;
    products: BrandProduct[];
}