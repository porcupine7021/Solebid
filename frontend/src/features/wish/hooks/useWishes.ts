import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useProductImageUrls } from "../../../hooks/useProductImageUrls.ts";
import type { ApiResponse } from '../../user/types/AuthTypes';
import { addWish, getWishes, removeWish } from '../services/WishService.tsx';
import type { Wish } from '../types/Wish';

const WISHES_QUERY_KEY = 'wishes';

export const useWishes = () => {
    const queryClient = useQueryClient();

    const { data: initialWishes, isLoading: isInitialLoading, error } = useQuery<ApiResponse<Wish[]>, Error, Wish[]>({ // 1. useQuery의 반환값을 initialWishes로 받음
        queryKey: [WISHES_QUERY_KEY],
        queryFn: getWishes,
        select: (data) => data.data ?? [],
        staleTime: 1000 * 60 * 5,
    });

    const { productsWithImages: wishes, isLoadingImages } = useProductImageUrls(
        initialWishes || []
    );

    const addWishMutation = useMutation({
        mutationFn: (newItem: Wish) => addWish(newItem.id),

        onMutate: async (newItem: Wish) => {
            await queryClient.cancelQueries({ queryKey: [WISHES_QUERY_KEY] });

            const previousResponse = queryClient.getQueryData<ApiResponse<Wish[]>>([WISHES_QUERY_KEY]);

            queryClient.setQueryData<ApiResponse<Wish[]>>([WISHES_QUERY_KEY], (oldResponse) => {
                const oldWishes = oldResponse?.data ?? [];
                return {
                    success: true,
                    data: [...oldWishes, newItem],
                };
            });

            return { previousResponse };
        },

        onError: (_err, _newItem, context) => {
            if (context?.previousResponse) {
                queryClient.setQueryData([WISHES_QUERY_KEY], context.previousResponse);
            }
        },

        onSettled: () => {
            queryClient.invalidateQueries({ queryKey: [WISHES_QUERY_KEY] });
        },
    });

    const removeWishMutation = useMutation({
        mutationFn: removeWish,

        onMutate: async (removedId: number) => {
            await queryClient.cancelQueries({ queryKey: [WISHES_QUERY_KEY] });

            const previousResponse = queryClient.getQueryData<ApiResponse<Wish[]>>([WISHES_QUERY_KEY]);

            queryClient.setQueryData<ApiResponse<Wish[]>>([WISHES_QUERY_KEY], (oldResponse) => {
                if (!oldResponse?.data) {
                    return { success: true, data: [] };
                }
                return {
                    ...oldResponse,
                    data: oldResponse.data.filter((wish) => wish.id !== removedId),
                };
            });

            return { previousResponse };
        },

        onError: (_err, _removedId, context) => {
            if (context?.previousResponse) {
                queryClient.setQueryData([WISHES_QUERY_KEY], context.previousResponse);
            }
        },

        onSettled: () => {
            queryClient.invalidateQueries({ queryKey: [WISHES_QUERY_KEY] });
        },
    });

    const isWished = (productId: number) => {
        return wishes?.some(product => product.id === productId) ?? false;
    };

    return {
        wishes,
        isLoading: isInitialLoading || isLoadingImages,
        error,
        addWish: addWishMutation.mutate,
        removeWish: removeWishMutation.mutate,
        isWished,
        isAdding: addWishMutation.isPending,
        isRemoving: removeWishMutation.isPending,
    };
};
