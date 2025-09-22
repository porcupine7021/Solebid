import { useImageUrls } from "../../../hooks/useProductImageUrls";
import { useProfileBidSelling } from "../hooks/useProfileBidSelling";
import ProfileBidSection from "./ProfileSection";
import { ProfileBidEmpty, ProfileBidError, ProfileBidLoading } from "./ProfileStates";
import ProfileTransactionItem from "./ProfileTransactionItem";

const TITLE = "최근 판매 내역";
const MAX_DISPLAY_COUNT = 3;

const ProfileTransaction = () => {
    const { soldProducts, loading, error } = useProfileBidSelling();
    const { itemsWithImages: productsWithImages, isLoadingImages } = useImageUrls(
        soldProducts,
        (item) => item.productImageUrl
    );

    if (loading || isLoadingImages) {
        return <ProfileBidLoading title={TITLE} />;
    }

    if (error) {
        return <ProfileBidError title={TITLE} error={error} />;
    }

    return (
        <ProfileBidSection
            title={TITLE}
            linkTo="/transaction"
            linkText="전체 보기"
        >
            <div className="space-y-4">
                {productsWithImages.length === 0 ? (
                    <ProfileBidEmpty />
                ) : (
                    productsWithImages.slice(0, MAX_DISPLAY_COUNT).map(product => (
                        <ProfileTransactionItem
                            key={product.productId}
                            {...product}
                        />
                    ))
                )}
            </div>
        </ProfileBidSection>
    );
};

export default ProfileTransaction;