import { Link } from "react-router-dom";
import type { MainBrandProps } from "../types/MainBrandProps";

const MainBrandItem = ({ brand }: MainBrandProps) => (
  <Link
    to={`/brand/${brand.id}`}
    className="bg-white rounded-xl p-6 flex flex-col items-center cursor-pointer hover:shadow-lg transition-shadow"
  >
    <img
      src={brand.logo}
      alt={brand.name}
      className="w-24 h-24 object-contain mb-4"
    />
    <h3 className="text-lg font-medium text-gray-900">
      {brand.name}
    </h3>
  </Link>
);

export default MainBrandItem;
