import type { BrandProduct } from "./Brand";

export interface BrandItemProps {
    product: BrandProduct;
    onBidClick: (product: BrandProduct) => void;
}