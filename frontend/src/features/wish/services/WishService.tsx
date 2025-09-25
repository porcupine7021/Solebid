import {apiFetch} from "../../../utils/apiFetch";
import type {Wish} from '../types/Wish';
import type {ApiResponse} from "../../user/types/AuthTypes";

export const getWishes = async (): Promise<ApiResponse<Wish[]>> => {
    return apiFetch<ApiResponse<Wish[]>>('/api/wishes');
};

export const addWish = async (auctionEventId: number): Promise<ApiResponse<void>> => {
    return apiFetch<ApiResponse<void>>(`/api/wishes/${auctionEventId}`, {method: 'POST'});
};

export const removeWish = async (auctionEventId: number): Promise<ApiResponse<void>> => {
    return apiFetch<ApiResponse<void>>(`/api/wishes/${auctionEventId}`, {method: 'DELETE'});
};
