export interface MainProduct {
  id: number;
  image: string;
  name: string;
  price: number;
  bidCount: number;
  timeLeft: string;
}

export interface MainProductProps {
  product: MainProduct;
}
