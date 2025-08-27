export interface SearchHeaderProps {
    searchQuery: string;
    totalResults: number;
    sortBy: string;
    setSortBy: (option: string) => void;
    sortOptions: string[];
    showFilters: boolean;
    setShowFilters: (show: boolean) => void;
}