import { useQuery } from '@tanstack/react-query';
import { useProductImageUrls } from "../../../hooks/useProductImageUrls.ts";
import { getProducts } from '../services/AuctionService';

const AUCTION_QUERY_KEY = 'auctions';

export const useAuctions = () => {
    const { data: initialAuctions, isLoading: isInitialLoading, isError, error } = useQuery({
        queryKey: [AUCTION_QUERY_KEY],
        queryFn: () => getProducts(),
        select: response => response.data,
    });

    const { productsWithImages, isLoadingImages } = useProductImageUrls(
        initialAuctions || [],
    );

    return {
        auctions: productsWithImages,
        isLoading: isInitialLoading || isLoadingImages,
        isError,
        error,
    };
};

