import type { MainBrandListProps } from "../types/MainBrandListProps";
import MainBrandItem from "./MainBrandItem";

const MainBrandList = ({ brands }: MainBrandListProps) => (
  <section className="max-w-[1440px] mx-auto px-6 pb-12">
    <h2 className="text-xl font-semibold text-gray-900 mb-6">
      인기 브랜드
    </h2>
    <div className="grid grid-cols-4 gap-6">
      {brands.map((brand) => (
        <MainBrandItem
          key={brand.id}
          brand={brand}
        />
      ))}
    </div>
  </section>
);

export default MainBrandList;
