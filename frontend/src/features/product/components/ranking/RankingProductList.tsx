import type { RankingProductListProps } from "../../types/ranking/RankingProductProps";
import RankingProductItem from "./RankingProductItem";

const RankingProductList = ({ items }: RankingProductListProps) => (
    <div className="space-y-4">
        {items.map((item) => (
            <RankingProductItem
                key={item.rank}
                {...item}
            />
        ))}
    </div>
);

export default RankingProductList;