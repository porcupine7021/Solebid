import type {MainProductListProps} from "../types/MainProductListProps";
import type {MainProduct} from "../types/MainProductProps";
import MainProductItem from "./MainProductItem";

const MainProductList = ({products}: MainProductListProps) => {
    return (
        <div className="max-w-[1440px] mx-auto px-6 pb-12">
            <div className="flex items-center justify-between mb-6">
                <h3 className="text-xl font-semibold text-gray-900">
                    실시간 인기 경매
                </h3>
            </div>
            <div className="grid grid-cols-4 gap-6">
                {products.map((product: MainProduct) => (
                    <MainProductItem
                        key={product.id}
                        product={product}
                    />
                ))}
            </div>
        </div>
    );
};

export default MainProductList;
