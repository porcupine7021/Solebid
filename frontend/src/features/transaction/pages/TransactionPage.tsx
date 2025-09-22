import { useMemo, useState } from "react";
import { useImageUrls } from "../../../hooks/useProductImageUrls";
import { useProfileBidSelling } from "../../profile/hooks/useProfileBidSelling";
import type { ProfileBidSellingProps } from "../../profile/types/ProfileBidSellingProps";
import { TransactionList, TransactionSearch, TransactionSummary } from "../components";

const TransactionPage = () => {
    const [selectedDateFilter, setSelectedDateFilter] = useState("all");
    const [selectedStatusFilter, setSelectedStatusFilter] = useState("all");
    const [searchQuery, setSearchQuery] = useState("");
    const [showDateDropdown, setShowDateDropdown] = useState(false);
    const [showStatusDropdown, setShowStatusDropdown] = useState(false);

    // 실제 판매 내역 데이터 가져오기
    const { soldProducts, loading, error } = useProfileBidSelling();
    const { itemsWithImages: productsWithImages, isLoadingImages } = useImageUrls(
        soldProducts,
        (item) => item.productImageUrl
    );

    const filteredData = useMemo(() => {
        if (!productsWithImages.length) return [];

        const now = new Date();
        const oneWeekAgo = new Date(now.getFullYear(), now.getMonth(), now.getDate() - 7);
        const oneMonthAgo = new Date(now.getFullYear(), now.getMonth() - 1, now.getDate());
        const threeMonthsAgo = new Date(now.getFullYear(), now.getMonth() - 3, now.getDate());

        return productsWithImages.filter((item: ProfileBidSellingProps & { imageUrl: string }) => {
            const matchesSearch = item.productName
                .toLowerCase()
                .includes(searchQuery.toLowerCase());

            // 판매 완료된 상품이므로 상태 필터는 모두 "완료"로 처리
            const matchesStatus = selectedStatusFilter === "all" || selectedStatusFilter === "completed";

            const itemDate = new Date(item.soldDate);

            let matchesDate = true;

            switch (selectedDateFilter) {
                case "week":
                    matchesDate = itemDate >= oneWeekAgo;
                    break;
                case "month":
                    matchesDate = itemDate >= oneMonthAgo;
                    break;
                case "3months":
                    matchesDate = itemDate >= threeMonthsAgo;
                    break;
                default: // "all"
                    matchesDate = true;
                    break;
            }

            return matchesSearch && matchesStatus && matchesDate;
        });
    }, [productsWithImages, searchQuery, selectedStatusFilter, selectedDateFilter]);

    if (loading || isLoadingImages) {
        return (
            <div className="fixed top-0 left-0 w-full h-full flex justify-center items-center">
                <i className="fas fa-spinner fa-spin fa-3x"></i>
            </div>
        );
    }

    if (error) {
        return (
            <div className="min-h-screen bg-gray-50">
                <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <div className="text-center py-12">
                        <h2 className="text-2xl font-bold text-gray-900 mb-4">오류가 발생했습니다</h2>
                        <p className="text-gray-600">{error}</p>
                    </div>
                </main>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50">
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <TransactionSearch
                    searchQuery={searchQuery}
                    setSearchQuery={setSearchQuery}
                    showDateDropdown={showDateDropdown}
                    setShowDateDropdown={setShowDateDropdown}
                    setSelectedDateFilter={setSelectedDateFilter}
                    showStatusDropdown={showStatusDropdown}
                    setShowStatusDropdown={setShowStatusDropdown}
                    setSelectedStatusFilter={setSelectedStatusFilter}
                    selectedDateFilter={selectedDateFilter}
                    selectedStatusFilter={selectedStatusFilter}
                />
                <TransactionSummary
                    data={filteredData}
                />
                <TransactionList
                    data={filteredData}
                />
            </main>
        </div>
    );
};

export default TransactionPage;