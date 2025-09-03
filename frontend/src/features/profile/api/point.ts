import { apiFetch } from "../../../utils/apiFetch";
import type { PointSummaryResponse } from "../types/ProfilePointProps";

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "/api";

export async function fetchUserPoint(userId: number): Promise<PointSummaryResponse> {
    // ApiResponse 스키마가 아닌 순수 DTO를 반환하므로 제네릭은 unknown으로 두고 수동 캐스팅
    const data = await apiFetch<unknown>(`${BASE_URL}/users/${userId}/points`, {
        method: "GET",
    });
    return data as PointSummaryResponse;
}
