import {useQuery} from '@tanstack/react-query';
import {getProducts} from '../../auction/services/AuctionService';

export const useMainProducts = () => {
    return useQuery({
        queryKey: ['mainProducts'],
        queryFn: () => getProducts(),
        select: (response) => response.data,
    });
};
