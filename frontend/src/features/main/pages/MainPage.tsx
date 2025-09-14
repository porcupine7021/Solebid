import {MainBrandList, MainCategoryList, MainHeroSection, MainProductList} from '../components';
import {categories, heroImage, popularBrands} from '../components/mockData';
import {useMainProducts} from '../hooks/useMainProducts';
import {useMemo} from 'react';
import type {MainProduct} from '../types/MainProductProps';

const MainPage = () => {
    const {data: auctionProducts, isLoading, isError} = useMainProducts();

    const products = useMemo(() => {
        return (auctionProducts ?? []).map(
            (p): MainProduct => ({
                id: p.id,
                image: p.image || '',
                name: p.name || '',
                price: p.currentBid ?? 0,
                bidCount: p.bidders,
                timeLeft: p.timeLeft,
            }),
        );
    }, [auctionProducts]);

    if (isLoading) {
        return (
            <div className="fixed top-0 left-0 w-full h-full flex justify-center items-center">
                <i className="fas fa-spinner fa-spin fa-3x"></i>
            </div>
        )
    }

    if (isError) {
        return (
            <div>Error: 데이터를 불러오는 중 오류가 발생했습니다.</div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50">
            <MainHeroSection
                image={heroImage}
            />
            <MainCategoryList
                categories={categories}
            />
            <MainBrandList
                brands={popularBrands}
            />
            <MainProductList
                products={products}
                // filter={filter}
                // onFilterChange={setFilter}
            />
        </div>
    );
};

export default MainPage;
