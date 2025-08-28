import { useNavigate } from 'react-router-dom';
import type { BackPressProps } from '../types/BackPressProps';

const BackPress = ({ children, className }: BackPressProps) => {
    const navigate = useNavigate();
    const handleBackPressed = () => { navigate(-1); };
    return (
        <button
            onClick={handleBackPressed}
            className={className}
        >
            {children}
        </button>
    );
};

export default BackPress;