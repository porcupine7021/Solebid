import { useQuery } from "@tanstack/react-query";
import { getProducts } from "../services/AuctionService";

const AUCTION_QUERY_KEY = 'auctions';

export const useAuctions = () => {
    return useQuery({
        queryKey: [AUCTION_QUERY_KEY],
        queryFn: () => getProducts(),
        select: (response) => response.data,
    });
};
