import type { BrandWithProducts, BrandProduct } from "./Brand";

export interface BrandListProps {
    brandData: BrandWithProducts;
    onBidClick: (product: BrandProduct) => void;
}