import { useState } from "react";
import type { NotificationSearchProps } from "../types/NotificationSearchProps";
import { sortOptions } from "./mockData";

const NotificationSearch = ({ sortBy, onSortChange, searchQuery, onSearchChange }: NotificationSearchProps) => {
    const [isSortDropdownOpen, setIsSortDropdownOpen] = useState(false);

    const selectedSortOption = sortOptions.find(option => option.value === sortBy);

    return (
        <div className="bg-white">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between space-y-4 sm:space-y-0">
                    <div className="relative">
                        <button
                            onClick={() => setIsSortDropdownOpen(!isSortDropdownOpen)}
                            className="appearance-none bg-white border border-gray-300 rounded-lg px-4 py-2 pr-8 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 cursor-pointer flex justify-between items-center w-32"
                        >
                            <span>
                                {selectedSortOption ? selectedSortOption.label : ''}
                            </span>
                            <i className="fas fa-chevron-down text-xs text-gray-400" />
                        </button>
                        {isSortDropdownOpen && (
                            <div className="absolute right-0 mt-2 w-32 bg-white border border-gray-200 rounded-lg shadow-lg z-20">
                                {sortOptions.map((option) => (
                                    <button
                                        key={option.value}
                                        onClick={() => {
                                            onSortChange(option.value);
                                            setIsSortDropdownOpen(false);
                                        }}
                                        className="block w-full text-left px-4 py-2 text-gray-700 hover:bg-gray-100 cursor-pointer"
                                    >
                                        {option.label}
                                    </button>
                                ))}
                            </div>
                        )}
                    </div>
                    <div className="relative">
                        <input
                            type="text"
                            placeholder="상품명으로 검색"
                            value={searchQuery}
                            onChange={(e) => onSearchChange(e.target.value)}
                            className="w-full sm:w-80 pl-10 pr-4 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        />
                        <i className="fas fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 text-sm" />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default NotificationSearch;