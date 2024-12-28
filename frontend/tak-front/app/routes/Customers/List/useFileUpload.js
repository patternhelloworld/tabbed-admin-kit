import { useState } from 'react';
import agent from "shared/api/agent";

export const useFileUpload = () => {
    const [file, setFile] = useState(null);

    const onFileChange = (event) => {
        const selectedFile = event.target.files[0];
        if (selectedFile) {
            setFile(selectedFile);
        }
    };

    const uploadFile = async (file, customerIdx) => {
        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await agent.File.create({ url: `/files/customers/${customerIdx}`, body: formData });
            if (response.statusCode !== 200) {
                throw new Error('파일 업로드에 실패하였습니다.');
            }
            return response.data.imgSrcPath;
        } catch (error) {
            console.error(error);
            throw error;
        } finally {

        }
    };

    return {
        file,
        onFileChange,
        uploadFile,
    };
}
