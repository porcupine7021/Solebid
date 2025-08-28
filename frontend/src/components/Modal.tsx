import { createPortal } from "react-dom";
import type { ModalProps } from "../types/ModalProps";

const Modal = ({ isOpen, onClose, children }: ModalProps) => {
    if (!isOpen) return null;
    return createPortal(
        <div
            onClick={onClose}
            className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div
                onClick={(e) => e.stopPropagation()}
                className="bg-white rounded-lg p-6 max-w-lg w-full mx-4">
                {children}
            </div>
        </div>,
        document.body
    );
};

export default Modal;