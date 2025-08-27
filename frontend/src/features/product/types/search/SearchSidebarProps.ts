export interface SearchSidebarProps {
    showFilters: boolean;
    priceRange: number[];
    setPriceRange: (value: number[]) => void;
    brands: string[];
    selectedBrands: string[];
    handleBrandChange: (brand: string) => void;
    sizes: string[];
    selectedSizes: string[];
    handleSizeChange: (size: string) => void;
    resetFilters: () => void;
}