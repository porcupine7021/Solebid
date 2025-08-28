import type { RankingBidderListProps } from "../../types/ranking/RankingBidderProps";
import RankingBidderItem from "./RankingBidderItem";

const RankingBidderList = ({ items }: RankingBidderListProps) => (
    <div className="space-y-4">
        {items.map((item) => (
            <RankingBidderItem key={item.rank} {...item} />
        ))}
    </div>
);

export default RankingBidderList;