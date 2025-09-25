import tailwindcss from '@tailwindcss/vite'
import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
    plugins: [react(), tailwindcss()],
    server: {
        host: '0.0.0.0', // Docker 환경에서 외부 접근 허용
        port: 3000,
        watch: {
            usePolling: true, // Docker 환경에서 파일 변경 감지 개선
        },
        proxy: {
            '/api': {
                target: process.env.VITE_API_BASE_URL || 'http://localhost:8080',
                changeOrigin: true,
            },
        },
    },
    build: {
        target: 'esnext',
        minify: 'esbuild',
        rollupOptions: {
            output: {
                manualChunks: undefined,
            },
        },
    },
    define: {
        global: 'globalThis',
    },
    // Docker 환경 최적화
    optimizeDeps: {
        include: ['react', 'react-dom'],
        force: true, // 의존성 재최적화 강제 실행
    },
})
