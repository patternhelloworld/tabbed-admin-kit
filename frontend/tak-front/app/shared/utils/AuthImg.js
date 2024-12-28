import React, {useState , useEffect} from 'react';
import agent from "shared/api/agent";

export const AuthImg = ({ src, alt, style }) => {
    const url = new URL(src);
    const pathname = url.pathname+"/blob";

    const [imgSrc, setImgSrc] = useState(pathname);
    const [downloadUrl, setDownloadUrl] = useState('');

    const isImage = (url) => {
        const imageExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.svg', '.webp'];
        return imageExtensions.some(ext => url.toLowerCase().endsWith(ext));
    };

    const fetchFile = async () => {
        try {
            const blob = await agent.File.imgFetch(pathname);
            const newImgUrl = URL.createObjectURL(blob);

            if(isImage(src)) {
                setImgSrc(newImgUrl);
            } else {
                setImgSrc(null);
            }
            setDownloadUrl(newImgUrl);
        } catch (error) {
            console.error('Error loading image:', error);
            setImgSrc(null);
        }
    };

    useEffect(() => {
        fetchFile();
        return () => {
            if (downloadUrl) {
                URL.revokeObjectURL(downloadUrl);
            }
        };
    }, [src]);

    return (
        <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
            {imgSrc ? (
                <img
                    src={imgSrc}
                    alt={alt}
                    style={{ ...style, maxWidth: '100px', maxHeight: '100px', display: 'block' }}
                />
            ) : (<p>미리 보기가 불가한 파일입니다.</p>)}
            {downloadUrl && (
                <a
                    href={downloadUrl}
                    download="image.jpg"
                    style={{
                        display: 'inline-flex',
                        alignItems: 'center',
                        padding: '8px 16px',
                        fontSize: '14px',
                        cursor: 'pointer',
                        backgroundColor: '#007bff',
                        color: '#fff',
                        border: 'none',
                        borderRadius: '4px',
                        textDecoration: 'none',
                        textAlign: 'center',
                        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
                        transition: 'background-color 0.3s ease',
                    }}
                    onMouseEnter={(e) => e.target.style.backgroundColor = '#0056b3'}
                    onMouseLeave={(e) => e.target.style.backgroundColor = '#007bff'}
                >
                    다운로드
                </a>
            )}
        </div>
    );
};