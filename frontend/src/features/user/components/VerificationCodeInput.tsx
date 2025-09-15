import React, { useRef, useEffect, useState } from 'react';

interface VerificationCodeInputProps {
  value: string;
  onChange: (value: string) => void;
  disabled?: boolean;
  error?: boolean;
}

const VerificationCodeInput: React.FC<VerificationCodeInputProps> = ({
  value,
  onChange,
  disabled = false,
  error = false
}) => {
  const inputRefs = useRef<(HTMLInputElement | null)[]>([]);
  const [focusedIndex, setFocusedIndex] = useState<number | null>(null);

  // refs 배열 초기화
  useEffect(() => {
    inputRefs.current = inputRefs.current.slice(0, 6);
  }, []);

  const handleInputChange = (index: number, inputValue: string) => {
    // 숫자만 허용
    const digit = inputValue.replace(/\D/g, '').slice(-1);
    
    const newValue = value.split('');
    newValue[index] = digit;
    
    // 배열을 6개 요소로 채우기
    while (newValue.length < 6) {
      newValue.push('');
    }
    
    onChange(newValue.join(''));

    // 다음 입력 필드로 자동 포커스
    if (digit && index < 5) {
      inputRefs.current[index + 1]?.focus();
    }
  };

  const handleKeyDown = (index: number, e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Backspace') {
      if (!value[index] && index > 0) {
        // 현재 입력이 비어있으면 이전 입력으로 포커스
        inputRefs.current[index - 1]?.focus();
      } else {
        // 현재 입력 지우기
        const newValue = value.split('');
        newValue[index] = '';
        onChange(newValue.join(''));
      }
    } else if (e.key === 'ArrowLeft' && index > 0) {
      inputRefs.current[index - 1]?.focus();
    } else if (e.key === 'ArrowRight' && index < 5) {
      inputRefs.current[index + 1]?.focus();
    }
  };

  const handlePaste = (e: React.ClipboardEvent) => {
    e.preventDefault();
    const pastedData = e.clipboardData.getData('text').replace(/\D/g, '').slice(0, 6);
    onChange(pastedData.padEnd(6, ''));
    
    // 다음 빈 입력 또는 마지막 입력으로 포커스
    const nextIndex = Math.min(pastedData.length, 5);
    inputRefs.current[nextIndex]?.focus();
  };

  const handleFocus = (index: number) => {
    setFocusedIndex(index);
  };

  const handleBlur = () => {
    setFocusedIndex(null);
  };

  return (
    <div className="flex gap-2 justify-center">
      {Array.from({ length: 6 }, (_, index) => (
        <input
          key={index}
          ref={(el) => { inputRefs.current[index] = el; }}
          type="text"
          inputMode="numeric"
          maxLength={1}
          value={value[index] || ''}
          onChange={(e) => handleInputChange(index, e.target.value)}
          onKeyDown={(e) => handleKeyDown(index, e)}
          onPaste={handlePaste}
          onFocus={() => handleFocus(index)}
          onBlur={handleBlur}
          disabled={disabled}
          className={`
            w-12 h-12 text-center text-lg font-semibold border-2 rounded-lg
            focus:outline-none focus:ring-2 transition-colors
            ${error 
              ? 'border-red-500 focus:border-red-500 focus:ring-red-200' 
              : 'border-gray-300 focus:border-blue-500 focus:ring-blue-200'
            }
            ${disabled ? 'bg-gray-100 cursor-not-allowed' : 'bg-white'}
            ${focusedIndex === index ? 'ring-2' : ''}
          `}
        />
      ))}
    </div>
  );
};

export default VerificationCodeInput;