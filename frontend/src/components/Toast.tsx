import type { ToastProps } from "../types/ToastProps";

const Toast = ({ message }: ToastProps) => (
    <div className="fixed top-4 right-4 bg-gray-800 text-white px-4 py-3 rounded-lg shadow-lg z-50 flex items-center animate-fade-in-out">
        <i className="fas fa-shopping-cart mr-2" />
        <span>{message}</span>
    </div>
);

export default Toast;